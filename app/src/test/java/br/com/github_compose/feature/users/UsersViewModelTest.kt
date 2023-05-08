package br.com.github_compose.feature.users

import app.cash.turbine.test
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import br.com.github_compose.rule.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class UsersViewModelTest {

    private lateinit var viewModel: UsersViewModel

    val mockedUser = GithubUser(
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
        viewModel = UsersViewModel(repo)
    }

    @Test
    fun `user list should be success when server returns success`() = runTest {
        coEvery { repo.searchUsers("teste") } returns flow { emit(Resource.Success(emptyList())) }

        viewModel.updateSearchText("teste")
        delay(2000L)
        viewModel.userList.test {
            val awaitItem = awaitItem()
            assert(awaitItem is Resource.Success)
        }
    }

    @Test
    fun `userState should be error when server returns error`() = runTest {
        coEvery { repo.searchUsers(any()) } returns flow { emit(Resource.Error(Exception())) }

        viewModel.updateSearchText("teste")
        delay(2000L)
        runBlocking {
            viewModel.userList.test {
                val awaitItem = awaitItem()
                assert(awaitItem is Resource.Error)
            }
        }
    }

}