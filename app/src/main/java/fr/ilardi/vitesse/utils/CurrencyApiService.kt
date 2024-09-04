package fr.ilardi.vitesse.utils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.ilardi.vitesse.model.CurrencyResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface CurrencyApiService {

    // Déclaration de l'appel GET pour récupérer les devises
    @GET("v1/currencies/eur.json")
    suspend fun getEuroRates(): CurrencyResponse
}

// Fonction pour créer l'instance Retrofit
object RetrofitInstance {

    // Créez une instance de Moshi avec KotlinJsonAdapterFactory
    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory()) // Ajoutez cette ligne
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)) // Utilisez l'instance Moshi personnalisée
            .build()
    }

    val api: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }
}