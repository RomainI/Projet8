package fr.ilardi.vitesse.model

data class CurrencyResponse(
    val date: String,
    val eur: CurrencyRates
)

data class CurrencyRates(
    val gbp: Double
)