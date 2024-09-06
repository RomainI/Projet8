package fr.ilardi.vitesse.utils
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import fr.ilardi.vitesse.model.CurrencyResponse
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET

interface CurrencyApiService {

    @GET("v1/currencies/eur.json")
    suspend fun getEuroRates(): CurrencyResponse
}

object RetrofitInstance {

    private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/")
            .addConverterFactory(MoshiConverterFactory.create(moshi)) 
            .build()
    }

    val api: CurrencyApiService by lazy {
        retrofit.create(CurrencyApiService::class.java)
    }
}