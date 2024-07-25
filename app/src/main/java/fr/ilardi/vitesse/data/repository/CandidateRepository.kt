package fr.ilardi.vitesse.data.repository

import fr.ilardi.vitesse.database.CandidateDao
import fr.ilardi.vitesse.model.Candidate

class CandidateRepository(private val candidateDao: CandidateDao) {

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

    suspend fun getAllCandidates(): List<Candidate> {
        return candidateDao.getAllCandidates()
    }
}