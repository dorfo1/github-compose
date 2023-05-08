package br.com.github_compose.feature

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.com.github_compose.base.theme.GithubcomposeTheme
import br.com.github_compose.feature.detail.UserDetailScreen
import br.com.github_compose.feature.detail.UserDetailViewModel
import br.com.github_compose.feature.users.UsersScreen
import br.com.github_compose.feature.users.UsersViewModel
import br.com.github_compose.model.GithubUser
import org.koin.androidx.compose.koinViewModel

val LocalOnUserClicked = compositionLocalOf<(GithubUser) -> Unit> { {} }


class MainActivity : ComponentActivity() {

    companion object {
        const val USER_LIST_ROUTE = "user_list"
        const val USER_DETAIL_ROUTE = "user_detail"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GithubcomposeTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = USER_LIST_ROUTE) {
                    composable(USER_LIST_ROUTE) {
                        val viewModel: UsersViewModel = koinViewModel()
                        val userList = viewModel.userList.collectAsStateWithLifecycle()
                        val searchText = viewModel.searchText.collectAsStateWithLifecycle()

                        fun onUserClicked(user: GithubUser) {
                            navController.navigate("$USER_DETAIL_ROUTE/${user.login}")
                        }

                        CompositionLocalProvider(LocalOnUserClicked provides ::onUserClicked) {
                            UsersScreen(
                                userList = userList.value,
                                search = searchText.value,
                                onSearch = viewModel::updateSearchText
                            )
                        }
                    }
                    composable(
                        "$USER_DETAIL_ROUTE/{login}",
                        arguments = listOf(navArgument("login") { type = NavType.StringType })
                    ) { entry ->
                        val login = entry.arguments?.getString("login")
                        val viewModel: UserDetailViewModel = koinViewModel()
                        val user = viewModel.userState.collectAsStateWithLifecycle()

                        UserDetailScreen(
                            user = user.value,
                            login = login ?: "",
                            getUserData = { viewModel.getUserInformation(it) },
                            onBackClicked = { navController.popBackStack() }
                        )
                    }
                }
            }
        }
    }
}

