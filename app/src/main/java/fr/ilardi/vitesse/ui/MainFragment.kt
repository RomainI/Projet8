package fr.ilardi.vitesse.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.databinding.MainFragmentBinding

@AndroidEntryPoint
class MainFragment : Fragment() {
    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: CandidateViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val addButton = binding.addCandidateButton
        addButton.setOnClickListener {
            (activity as MainActivity).replaceFragment(AddCandidateFragment())
        }


        val fragments = listOf(
            CandidateListFragment.newInstance(false),  // All candidates, tagged false in the database (isFavorite)
            CandidateListFragment.newInstance(true)    // Favorites candidates, tagged true in the database
        )

        //Creates Tabs to sort favorite candidates
        val tabLayout = binding.tabLayout

        //Allow the possibility to swipe between the two lists of candidates
        val viewPager = binding.viewPager
        val adapter = ViewPagerAdapter(requireActivity(), fragments)
        viewPager.adapter = adapter

        //Sync the viewPager and tabLayout to sort favorite candidates
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> getString(R.string.tab_all_candidates)
                1 -> getString(R.string.tab_favorites)
                else -> null
            }
        }.attach()
    }
}