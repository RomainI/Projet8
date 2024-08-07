package fr.ilardi.vitesse.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "candidates")
data class Candidate(
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val email: String,
    val dateOfBirth: String,
    val pictureURI: String,
    val salary: String,
    val notes: String,
    val isFavorite: Boolean,
    @PrimaryKey(autoGenerate = true) val id: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readByte() != 0.toByte(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(dateOfBirth)
        parcel.writeString(pictureURI)
        parcel.writeString(salary)
        parcel.writeString(notes)
        parcel.writeByte(if (isFavorite) 1 else 0)
        parcel.writeInt(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Candidate> {
        override fun createFromParcel(parcel: Parcel): Candidate {
            return Candidate(parcel)
        }

        override fun newArray(size: Int): Array<Candidate?> {
            return arrayOfNulls(size)
        }
    }
}