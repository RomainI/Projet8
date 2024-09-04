package fr.ilardi.vitesse.model

// Modèle pour EUR contenant toutes les devises
data class CurrencyResponse(
    val date: String,
    val eur: CurrencyRates
)

// Modèle pour les taux de devises
data class CurrencyRates(
    val gbp: Double
)