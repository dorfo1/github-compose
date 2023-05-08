package br.com.github_compose.feature.users

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import br.com.github_compose.base.theme.GithubcomposeTheme
import br.com.github_compose.base.utils.ApiLimitException
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import io.mockk.coEvery
import kotlinx.coroutines.flow.flow
import org.koin.core.component.inject
import org.koin.test.KoinTest

fun UsersScreenTest.executeTest(
    testRule: ComposeContentTestRule, func: UsersRobot.() -> Unit
) = UsersRobot(testRule).apply(func)

class UsersRobot(private val testRule: ComposeContentTestRule) : KoinTest {

    private val repository: GithubUserRepository by inject()

    private val viewModel: UsersViewModel by inject()

    fun withSearchError() {
        coEvery { repository.searchUsers(any()) } returns flow { emit(Resource.Error(Exception())) }
    }

    fun withSearchApiError() {
        coEvery { repository.searchUsers(any()) } returns flow {
            emit(
                Resource.Error(
                    ApiLimitException()
                )
            )
        }
    }

    fun withSearchSuccess() {
        coEvery { repository.searchUsers(any()) } returns flow {
            emit(
                Resource.Success(
                    listOf(
                        GithubUser(
                            id = 1,
                            login = "teste1",
                            name = null,
                            avatar = "https://avatars.githubusercontent.com/u/39884163?v=4",
                            publicRepos = 0,
                            publicGists = 0,
                            location = "São Paulo",
                            following = 0,
                            followers = 0,
                            company = "Compania teste",
                            repos = null,
                            blog = null
                        ),
                        GithubUser(
                            id = 1,
                            login = "teste2",
                            name = null,
                            avatar = "https://avatars.githubusercontent.com/u/39884163?v=4",
                            publicRepos = 0,
                            publicGists = 0,
                            location = "São Paulo",
                            following = 0,
                            followers = 0,
                            company = "Compania teste",
                            repos = null,
                            blog = null
                        ),
                        GithubUser(
                            id = 1,
                            login = "teste3",
                            name = null,
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
            )
        }
    }

    fun withSearchWithEmptyList() {
        coEvery { repository.searchUsers(any()) } returns flow {
            emit(
                Resource.Success(emptyList())
            )
        }
    }

    infix fun launch(
        func: UsersRobot.() -> Unit
    ): UsersRobot {
        testRule.setContent {
            GithubcomposeTheme {
                val state = viewModel.userList.collectAsStateWithLifecycle()
                val search = viewModel.searchText.collectAsStateWithLifecycle()
                UsersScreen(
                    userList = state.value,
                    search = search.value,
                    onSearch = { viewModel.updateSearchText(it) }
                )
            }
        }
        return this.apply(func)
    }

    fun insertTextToSearchField() =
        testRule.onNodeWithTag(TEXT_FIELD_TEST_TAG).performTextInput("teste")

    infix fun check(func: Result.() -> Unit) = Result().apply(func)

    inner class Result {
        fun checkTextFieldValue() =
            testRule.onNodeWithTag(TEXT_FIELD_TEST_TAG).assert(hasText("teste"))

        @OptIn(ExperimentalTestApi::class)
        fun checkItemsOnList(text: String) = testRule.waitUntilAtLeastOneExists(hasText(text),2000L)

        @OptIn(ExperimentalTestApi::class)
        fun checkEmptyListMessage() =
            testRule.waitUntilAtLeastOneExists(hasText("Nenhum usuário encontrado, tente novamente."),2000L)

        @OptIn(ExperimentalTestApi::class)
        fun checkGenericErrorMessage() =
            testRule.waitUntilAtLeastOneExists(
                hasText("Falha de conexão, tente novamente mais tarde"),
                2000L
            )

        @OptIn(ExperimentalTestApi::class)
        fun checkApiLimitErrorMessage() =
            testRule.waitUntilAtLeastOneExists(
                hasText("Limite de requisições atingido, tente novamente mais tarde"),
                2000L
            )
    }
}