package fr.ilardi.vitesse.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CandidateListViewModel @Inject constructor(
    private val repository: CandidateRepository
) : ViewModel() {

    suspend fun getAllCandidates(): Flow<List<Candidate>> {
        return repository.getAllCandidates()
    }

    suspend fun searchCandidatesByName(name: String): Flow<List<Candidate>> {
        return searchCandidates(getAllCandidates(), name)
    }

    fun insertCandidate(candidate: Candidate) {
        viewModelScope.launch {
            repository.insert(candidate)
        }
    }

    private fun searchCandidates(
        candidatesFlow: Flow<List<Candidate>>,
        query: String
    ): Flow<List<Candidate>> {
        val searchQuery = query.trim().lowercase()

        return candidatesFlow.map { candidatesList ->
            candidatesList.filter { candidate ->
                candidate.firstName.lowercase().contains(searchQuery) ||
                        candidate.lastName.lowercase().contains(searchQuery)
            }
        }
    }

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

    suspend fun getCandidateById(id: Int): Candidate? {
        return repository.getCandidateById(id)
    }
}