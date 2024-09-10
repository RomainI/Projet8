package fr.ilardi.vitesse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import fr.ilardi.vitesse.ui.AddCandidateFragmentViewModel
import fr.ilardi.vitesse.ui.CandidateListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify
@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class CandidateListViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CandidateRepository
    private lateinit var candidateListViewModel: CandidateListViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        candidateListViewModel = CandidateListViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getAllCandidates`() = runTest {
        val fakeCandidate1 = Candidate(
            id = 1,
            firstName = "Zinedine",
            lastName = "Zidane",
            phoneNumber = "0",
            email = "zinedine@zidane.com",
            dateOfBirth = "01/01/2020",
            pictureURI = "aaa",
            salary = "10",
            notes = "notes",
            isFavorite = true
        )
        val fakeCandidate2 = Candidate(
            id = 2,
            firstName = "Zinedine2",
            lastName = "Zidane2",
            phoneNumber = "0",
            email = "zinedine2@zidane.com",
            dateOfBirth = "01/01/2020",
            pictureURI = "aaa",
            salary = "10",
            notes = "notes",
            isFavorite = false
        )

        `when`(repository.getAllCandidates()).thenReturn(flowOf(listOf(fakeCandidate1, fakeCandidate2)))

        candidateListViewModel.getAllCandidates().test {
            val result = awaitItem()
            assertEquals(2, result.size)
            assertEquals("Zinedine", result[0].firstName)
            assertEquals("Zinedine2", result[1].firstName)
            awaitComplete()
        }

        verify(repository, times(1)).getAllCandidates()
    }

    @Test
    fun `searchCandidatesByName returns filtered candidates`() = runTest {
        val fakeCandidate1 = Candidate(
            id = 1,
            firstName = "Zinedine",
            lastName = "Zidane",
            phoneNumber = "0",
            email = "zinedine@zidane.com",
            dateOfBirth = "01/01/2020",
            pictureURI = "aaa",
            salary = "10",
            notes = "notes",
            isFavorite = true
        )
        val fakeCandidate2 = Candidate(
            id = 2,
            firstName = "Cristiano",
            lastName = "Ronaldo",
            phoneNumber = "1",
            email = "cristiano@ronaldo.com",
            dateOfBirth = "05/02/1985",
            pictureURI = "bbb",
            salary = "20",
            notes = "goal",
            isFavorite = false
        )

        `when`(repository.getAllCandidates()).thenReturn(flowOf(listOf(fakeCandidate1, fakeCandidate2)))

        candidateListViewModel.searchCandidatesByName("Zidane").test {
            val result = awaitItem()
            assertEquals(1, result.size)
            assertEquals("Zinedine", result[0].firstName)
            awaitComplete()
        }

        candidateListViewModel.searchCandidatesByName("Messi").test {
            val result = awaitItem()
            assertEquals(0, result.size)
            awaitComplete()
        }

        verify(repository, times(2)).getAllCandidates()
    }
}