package fr.ilardi.vitesse

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import fr.ilardi.vitesse.data.repository.CandidateRepository
import fr.ilardi.vitesse.model.Candidate
import fr.ilardi.vitesse.ui.AddCandidateFragmentViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddCandidateFragmentViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CandidateRepository
    private lateinit var addCandidateFragmentViewModel: AddCandidateFragmentViewModel
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
        addCandidateFragmentViewModel = AddCandidateFragmentViewModel(repository)
    }


    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `insertCandidate`() = runBlocking {
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

        addCandidateFragmentViewModel.insertCandidate(fakeCandidate)
        verify(repository, times(1)).insert(fakeCandidate)
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

        addCandidateFragmentViewModel.updateCandidate(fakeCandidate)
        verify(repository, times(1)).update(fakeCandidate)

    }

}