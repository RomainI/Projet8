package fr.ilardi.vitesse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import fr.ilardi.vitesse.model.CurrencyRates
import fr.ilardi.vitesse.model.CurrencyResponse
import fr.ilardi.vitesse.ui.DetailsFragmentViewModel
import fr.ilardi.vitesse.utils.CurrencyApiService
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify



@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class DetailsFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CandidateRepository
    private lateinit var detailsFragmentViewModel: DetailsFragmentViewModel
    private val testDispatcher = UnconfinedTestDispatcher()
    @Mock
    private lateinit var apiService: CurrencyApiService


    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        detailsFragmentViewModel = DetailsFragmentViewModel(repository, apiService)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun`updateCandidate`() = runBlocking {
        val fakeCandidate = Candidate(
            id = 1,
            firstName = "zinedine",
            lastName = "Zidane",
            phoneNumber = "0",
            email = "zinedine@zidane.com",
            dateOfBirth = "01/01/2020",
            pictureURI = "aaa",
            salary = "10",
            notes = "notes",
            isFavorite = true
        )

        detailsFragmentViewModel.updateCandidate(fakeCandidate)
        verify(repository, Mockito.times(1)).update(fakeCandidate)

    }



    @Test
    fun`deleteCandidate`() = runBlocking {
        val fakeCandidate = Candidate(
            id = 1,
            firstName = "zinedine",
            lastName = "Zidane",
            phoneNumber = "0",
            email = "zinedine@zidane.com",
            dateOfBirth = "01/01/2020",
            pictureURI = "aaa",
            salary = "10",
            notes = "notes",
            isFavorite = true
        )

        detailsFragmentViewModel.deleteCandidate(fakeCandidate)
        verify(repository, Mockito.times(1)).delete(fakeCandidate)

    }

    @Test
    fun `getGbpFromEur returns correct conversion`() = runBlocking {
        val mockCurrencyResponse = CurrencyResponse(
            date = "2024-01-01",
            eur = CurrencyRates(gbp = 0.85)
        )
        `when`(apiService.getEuroRates()).thenReturn(mockCurrencyResponse)

        val result = detailsFragmentViewModel.getGbpFromEur(100.0)

        assertEquals(85.0, result)
    }

    @Test
    fun `getGbpFromEur handles API failure`() = runBlocking {
        `when`(apiService.getEuroRates()).thenThrow(RuntimeException("API error"))

        val result = detailsFragmentViewModel.getGbpFromEur(100.0)

        assertNull(result)
    }

}