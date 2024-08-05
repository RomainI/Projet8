package fr.ilardi.vitesse.ui

import android.content.Context
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.room.Room
import dagger.hilt.android.AndroidEntryPoint
import fr.ilardi.vitesse.R
import fr.ilardi.vitesse.database.AppDatabase
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        Thread.sleep(3000)

        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment())
                .commit()
        }
    }

    fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }
}

object DatabaseBuilder {
    private var instance: AppDatabase? = null

    fun getInstance(context: Context): AppDatabase {
        if (instance == null) {
            synchronized(AppDatabase::class) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "candidate_database"
                ).build()
            }
        }
        return instance!!
    }
}