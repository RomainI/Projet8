package fr.ilardi.vitesse
import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.ilardi.vitesse.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class MyApplication : Application() {
}