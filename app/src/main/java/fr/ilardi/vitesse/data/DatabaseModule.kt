package fr.ilardi.vitesse.data
import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import fr.ilardi.vitesse.database.AppDatabase
import fr.ilardi.vitesse.database.CandidateDao
import fr.ilardi.vitesse.model.Candidate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import javax.inject.Singleton
import kotlin.reflect.KParameter

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext appContext: Context
    ): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "VitesseDB"
        )
            .fallbackToDestructiveMigration()
            .addCallback(object : RoomDatabase.Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    super.onCreate(db)

                    //populateDatabase is only used to test with pre populate DB
                    /**
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(provideDatabase(appContext).candidateDao())
                    }*/
                }
            })
            .build()
    }

    @Provides
    fun provideCandidateDao(database: AppDatabase): CandidateDao {
        return database.candidateDao()
    }

    suspend fun populateDatabase(candidateDao: CandidateDao) {
        // Clear the database
        candidateDao.deleteAll()

        // Add sample candidates
        val candidate1 = Candidate(
            firstName = "John",
            lastName = "Doe",
            phoneNumber = "1234567890",
            email = "john.doe@example.com",
            dateOfBirth = "30/05/1991",
            pictureURI = "",
            salary = "50000",
            notes = "Notes about John",
            isFavorite = true
        )
        val candidate2 = Candidate(
            firstName = "Jane",
            lastName = "Smith",
            phoneNumber = "2345678901",
            email = "jane.smith@example.com",
            dateOfBirth = "30/05/1991",
            pictureURI = "",
            salary = "60000",
            notes = "Notes about Jane",
            isFavorite = false
        )
        val candidate3 = Candidate(
            firstName = "Michael",
            lastName = "Brown",
            phoneNumber = "3456789012",
            email = "michael.brown@example.com",
            dateOfBirth = "30/05/1991",
            pictureURI = "",
            salary = "70000",
            notes = "Notes about Michael",
            isFavorite = true
        )

        candidateDao.insert(candidate1)
        candidateDao.insert(candidate2)
        candidateDao.insert(candidate3)
    }
}