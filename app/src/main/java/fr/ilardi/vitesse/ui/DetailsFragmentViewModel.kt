package fr.ilardi.vitesse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel  @Inject constructor(
    private val repository: CandidateRepository
) : ViewModel() {

    fun updateCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.update(candidate)
        }
    }
    fun deleteCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.delete(candidate)
        }
    }
}
