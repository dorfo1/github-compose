package br.com.github_compose.feature.detail

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithText
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.github_compose.base.theme.GithubcomposeTheme
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.koin.core.component.inject
import org.koin.test.KoinTest

fun UserDetailScreenTest.executeTest(
    testRule: ComposeContentTestRule, func: UserDetailScreenRobot.() -> Unit
) = UserDetailScreenRobot(testRule).apply(func)

class UserDetailScreenRobot(private val testRule: ComposeContentTestRule) : KoinTest {

    private val repository: GithubUserRepository by inject()

    private val viewModel: UserDetailViewModel by inject()

    fun withUserDetailError() {
        coEvery { repository.getUser(any()) } returns flow { emit(Resource.Error(Exception())) }
    }

    fun withUserDetailSuccess() {
        coEvery { repository.getUser(any()) } returns flow {
            emit(
                Resource.Success(
                    GithubUser(
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
                )
            )
        }
    }

    infix fun launch(
        func: UserDetailScreenRobot.() -> Unit
    ): UserDetailScreenRobot {
        testRule.setContent {
            GithubcomposeTheme {
                val state = viewModel.userState.collectAsStateWithLifecycle()
                UserDetailScreen(
                    login = "teste",
                    getUserData = { viewModel.getUserInformation("teste") },
                    user = state.value,
                    onBackClicked = {}
                )
            }
        }
        return this.apply(func)
    }

    infix fun check(func: Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkTopAppBarIsDisplayed() =
            testRule.onNodeWithText("Informações de teste").assertIsDisplayed()

        fun checkNameIsDisplayed() = testRule.onNodeWithText("Teste nome").assertIsDisplayed()

        fun checkCompanyIsDisplayed() =
            testRule.onNodeWithText("Compania teste").assertIsDisplayed()

        fun checkLocationIsDisplayed() = testRule.onNodeWithText("São Paulo").assertIsDisplayed()

        fun checkLoginIsDisplayed() = testRule.onNodeWithText("teste").assertIsDisplayed()

        fun checkNumberOfReposIsDisplayed() =
            testRule.onNodeWithText("0 Repositórios públicos").assertIsDisplayed()

        fun checkErrorMessageIsDisplayed() =
            testRule.onNodeWithText("Falha ao buscar informações do usuário").assertIsDisplayed()
    }
}

