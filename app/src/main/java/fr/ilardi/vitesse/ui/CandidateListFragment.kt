package fr.ilardi.vitesse.ui

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateListFragment : Fragment() {

    private val candidateListViewModel: CandidateListViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var showFavorites: Boolean = false
    private var showSearchContent: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showFavorites = it.getBoolean(ARG_SHOW_FAVORITES, false)
            showSearchContent = it.getString(ARG_SHOW_SEARCH_CONTENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_candidates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        candidateAdapter = CandidateAdapter { candidate ->
            val fragment = DetailsFragment.newInstance(candidate)
            (activity as MainActivity).replaceFragment(fragment)
        }
        recyclerView.adapter = candidateAdapter

        updateRecyclerView()
    }

    private fun searchCandidates(query: String) {
        // Utilisez le viewLifecycleOwner pour lancer une coroutine dans la portée du Fragment
        viewLifecycleOwner.lifecycleScope.launch {
            // Collecter les résultats du Flow et les transmettre à l'adapter du RecyclerView
            candidateListViewModel.searchCandidatesByName(query).collectLatest { candidateList ->
                candidateAdapter.setCandidates(candidateList)
            }
        }
    }

    private fun resetList() {
        candidateAdapter.setCandidates(emptyList()) // Par exemple, vider la liste
    }

    private fun updateRecyclerView() {
        viewLifecycleOwner.lifecycleScope.launch {

            if (showSearchContent == null) {

                candidateListViewModel.getAllCandidates().collect { candidates ->
                    val filteredCandidates = if (showFavorites) {
                        candidates.filter { it.isFavorite }
                    } else {
                        candidates
                    }
                    candidateAdapter.setCandidates(filteredCandidates)
                }
            } else {
                candidateListViewModel.searchCandidatesByName(showSearchContent!!).collect { candidates ->
                    val filteredCandidates = if (showFavorites) {
                        candidates.filter { it.isFavorite }
                    } else {
                        candidates
                    }
                    candidateAdapter.setCandidates(filteredCandidates)
                }
            }
        }
    }

    companion object {
        private const val ARG_SHOW_FAVORITES = "show_favorites"
        private const val ARG_SHOW_SEARCH_CONTENT = "show_search_content"

        @JvmStatic
        fun newInstance(showFavorites: Boolean, showSearchContent: String?) =
            CandidateListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_FAVORITES, showFavorites)
                    putString(ARG_SHOW_SEARCH_CONTENT, showSearchContent)
                }
            }
    }
}