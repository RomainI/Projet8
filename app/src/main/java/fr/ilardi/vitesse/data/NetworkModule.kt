package fr.ilardi.vitesse.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import fr.ilardi.vitesse.utils.CurrencyApiService
import fr.ilardi.vitesse.utils.RetrofitInstance
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)  // Le module est lié au cycle de vie de l'application
object NetworkModule {

    @Provides
    @Singleton
    fun provideCurrencyApiService(): CurrencyApiService {
        return RetrofitInstance.api  // Utilise RetrofitInstance pour fournir CurrencyApiService
    }
}