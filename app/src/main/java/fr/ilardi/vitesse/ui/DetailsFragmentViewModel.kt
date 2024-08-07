package fr.ilardi.vitesse.ui

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.vitesse.data.repository.CandidateRepository
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel  @Inject constructor(
    private val repository: CandidateRepository
) : ViewModel() {
}
