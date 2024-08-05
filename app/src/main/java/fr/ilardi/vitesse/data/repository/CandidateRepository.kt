package fr.ilardi.vitesse.data.repository

import fr.ilardi.vitesse.database.CandidateDao
import fr.ilardi.vitesse.model.Candidate
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CandidateRepository @Inject constructor(private val candidateDao: CandidateDao) {

    suspend fun insert(candidate: Candidate) {
        candidateDao.insert(candidate)
    }

    suspend fun update(candidate: Candidate) {
        candidateDao.update(candidate)
    }

    suspend fun delete(candidate: Candidate) {
        candidateDao.delete(candidate)
    }

    suspend fun getCandidateById(id: Int): Candidate? {
        return candidateDao.getCandidateById(id)
    }

    suspend fun getAllCandidates(): Flow<List<Candidate>> {
        return candidateDao.getAllCandidates()
    }
}