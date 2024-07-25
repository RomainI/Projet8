package fr.ilardi.vitesse.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class CandidateViewModel @Inject constructor(
    private val repository: CandidateRepository
) : ViewModel() {

    suspend fun insertCandidate(candidate: Candidate) {
        withContext(Dispatchers.IO) {
            repository.insert(candidate)
        }
    }

    suspend fun updateCandidate(candidate: Candidate) {
        withContext(Dispatchers.IO) {
            repository.update(candidate)
        }
    }

    suspend fun deleteCandidate(candidate: Candidate) {
        withContext(Dispatchers.IO) {
            repository.delete(candidate)
        }
    }

    suspend fun getCandidateById(id: Int): Candidate? {
        return withContext(Dispatchers.IO) {
            repository.getCandidateById(id)
        }
    }

    suspend fun getAllCandidates(): List<Candidate> {
        return withContext(Dispatchers.IO) {
            repository.getAllCandidates()
        }
    }
}