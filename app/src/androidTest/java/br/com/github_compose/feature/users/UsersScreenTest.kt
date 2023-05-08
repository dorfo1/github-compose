package br.com.github_compose.feature.users

import androidx.compose.ui.test.junit4.createComposeRule
import br.com.github_compose.KoinTestRule
import br.com.github_compose.repository.GithubUserRepository
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class UsersScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = mockk<GithubUserRepository>(relaxed = true)
    private val viewModel = UsersViewModel(repository)
    private val module = module {
        single { repository }
        single { viewModel }
    }

    @get:Rule
    val koinTest = KoinTestRule(listOf(module))

    @Test
    fun whenOpenScreens_withServerSuccess_CheckEmptyList() {
        executeTest(composeTestRule) {
            withSearchWithEmptyList()
        } launch {
            insertTextToSearchField()
        } check  {
            checkTextFieldValue()
            checkEmptyListMessage()
        }
    }

    @Test
    fun whenOpenScreens_withServerSuccess_CheckUsers() {
        executeTest(composeTestRule) {
            withSearchSuccess()
        } launch {
            insertTextToSearchField()
        } check  {
            checkTextFieldValue()
            checkItemsOnList("teste1")
            checkItemsOnList("teste2")
            checkItemsOnList("teste3")
        }
    }

    @Test
    fun whenOpenScreens_withServerSuccess_CheckGenericError() {
        executeTest(composeTestRule) {
            withSearchError()
        } launch {
            insertTextToSearchField()
        } check  {
            checkTextFieldValue()
            checkGenericErrorMessage()
        }
    }

    @Test
    fun whenOpenScreens_withServerSuccess_CheckApiError() {
        executeTest(composeTestRule) {
            withSearchApiError()
        } launch {
            insertTextToSearchField()
        } check  {
            checkTextFieldValue()
            checkApiLimitErrorMessage()
        }
    }
}