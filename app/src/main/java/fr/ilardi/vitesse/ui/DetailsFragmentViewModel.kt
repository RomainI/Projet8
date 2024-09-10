package fr.ilardi.vitesse.ui

import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import fr.ilardi.vitesse.utils.CurrencyApiService
import fr.ilardi.vitesse.utils.RetrofitInstance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel  @Inject constructor(
    private val repository: CandidateRepository,
    private val apiService: CurrencyApiService = RetrofitInstance.api
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

    suspend fun getGbpFromEur(euro : Double): Double? {
        return try {
            val response = withContext(Dispatchers.IO) {
                apiService.getEuroRates()
            }
            response.eur.gbp * euro
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

}
