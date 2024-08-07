package fr.ilardi.vitesse.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CandidateListFragment : Fragment() {

    private val candidateViewModel: CandidateViewModel by viewModels()
    private lateinit var candidateAdapter: CandidateAdapter
    private var showFavorites: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            showFavorites = it.getBoolean(ARG_SHOW_FAVORITES, false)
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

        // Observer les données
        viewLifecycleOwner.lifecycleScope.launch {
            candidateViewModel.getAllCandidates().collect { candidates ->
                val filteredCandidates = if (showFavorites) {
                    candidates.filter { it.isFavorite }
                } else {
                    candidates
                }
                candidateAdapter.setCandidates(filteredCandidates)
            }
        }
    }

    companion object {
        private const val ARG_SHOW_FAVORITES = "show_favorites"

        @JvmStatic
        fun newInstance(showFavorites: Boolean) =
            CandidateListFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(ARG_SHOW_FAVORITES, showFavorites)
                }
            }
    }
}