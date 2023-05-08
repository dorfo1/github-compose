package br.com.github_compose.feature.detail

import app.cash.turbine.test
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import br.com.github_compose.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UserDetailViewModelTest {

    private lateinit var viewModel: UserDetailViewModel

    private val mockedUser = GithubUser(
        id = 1,
        login = "teste",
        name = "Teste nome",
        avatar = "https://avatars.githubusercontent.com/u/39884163?v=4",
        publicRepos = 0,
        publicGists = 0,
        location = "São Paulo",
        following = 0,
        followers = 0,
        company = "Compania teste",
        repos = null,
        blog = null
    )

    private val repo: GithubUserRepository = mockk()

    @OptIn(ExperimentalCoroutinesApi::class)
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        viewModel = UserDetailViewModel(repo)
    }

    @Test
    fun `userState should be success when server returns success`() {
        coEvery { repo.getUser(any()) } returns flow { emit(Resource.Success(mockedUser)) }

        viewModel.getUserInformation("teste")

        runBlocking {
            viewModel.userState.test {
                val awaitItem = awaitItem()
                assert(awaitItem is Resource.Success)
            }
        }
    }

    @Test
    fun `userState should be error when server returns error`() {
        coEvery { repo.getUser(any()) } returns flow { emit(Resource.Error(Exception())) }

        viewModel.getUserInformation("teste")

        runBlocking {
            viewModel.userState.test {
                val awaitItem = awaitItem()
                assert(awaitItem is Resource.Error)
            }
        }
    }
}