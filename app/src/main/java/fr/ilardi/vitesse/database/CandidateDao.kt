package fr.ilardi.vitesse.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import fr.ilardi.vitesse.model.Candidate

@Dao
interface CandidateDao {
    @Insert
    suspend fun insert(candidate: Candidate)

    @Update
    suspend fun update(candidate: Candidate)

    @Delete
    suspend fun delete(candidate: Candidate)

    @Query("DELETE FROM candidates")
    suspend fun deleteAll(): Int

    @Query("SELECT * FROM candidates WHERE id = :id")
    suspend fun getCandidateById(id: Int): Candidate?

    @Query("SELECT * FROM candidates")
    suspend fun getAllCandidates(): List<Candidate>
}