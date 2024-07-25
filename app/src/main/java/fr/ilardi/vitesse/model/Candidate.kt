package fr.ilardi.vitesse.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "candidates")
data class Candidate(
    val firstName: String,
    val lastName: String,
    val phoneNumber: Long,
    val email: String,
    val dateOfBirth: Date,
    val pictureURI: String,
    val salary: Long,
    val notes: String,
    val isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
)