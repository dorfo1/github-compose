package br.com.github_compose.feature.detail

import androidx.compose.ui.test.junit4.createComposeRule
import br.com.github_compose.KoinTestRule
import br.com.github_compose.repository.GithubUserRepository
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import org.koin.dsl.module

class UserDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val repository = mockk<GithubUserRepository>(relaxed = true)
    private val viewModel = UserDetailViewModel(repository)
    private val module = module {
        single { repository }
        single { viewModel }
    }

    @get:Rule
    val koinTest = KoinTestRule(listOf(module))

    @Test
    fun whenOpenScreens_withServerSuccess_CheckContent() {
        executeTest(composeTestRule) {
            withUserDetailSuccess()
        } launch {} check {
            checkTopAppBarIsDisplayed()
            checkNameIsDisplayed()
            checkNumberOfReposIsDisplayed()
            checkCompanyIsDisplayed()
            checkLocationIsDisplayed()
            checkLoginIsDisplayed()
        }
    }

    @Test
    fun whenOpenScreens_withServerError_CheckContent() {
        executeTest(composeTestRule) {
            withUserDetailError()
        } launch {} check {
            checkTopAppBarIsDisplayed()
            checkErrorMessageIsDisplayed()
        }
    }

}
