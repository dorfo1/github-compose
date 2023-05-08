package br.com.github_compose.feature.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.github_compose.R
import br.com.github_compose.base.components.BaseCenteredMessage
import br.com.github_compose.base.components.BaseLoading
import br.com.github_compose.base.theme.GithubcomposeTheme
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubRepo
import br.com.github_compose.model.GithubUser
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    login: String,
    getUserData: (String) -> Unit,
    user: Resource<GithubUser>,
    onBackClicked: () -> Unit
) {
    LaunchedEffect(key1 = Unit) {
        getUserData.invoke(login)
    }

    val title = if (login.isNotEmpty()) stringResource(
        id = R.string.detail_title,
        login
    ) else stringResource(id = R.string.detail_title)

    GithubcomposeTheme {
        Column(modifier = Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = { onBackClicked.invoke() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = null
                        )
                    }
                }
            )
            when (user) {
                is Resource.Success -> UserDetailData(user.data)
                is Resource.Loading -> BaseLoading()
                is Resource.Error -> BaseCenteredMessage(
                    message = stringResource(id = R.string.fail_to_fetch_user_data)
                )

                else -> {}
            }
        }
    }

}

@Composable
fun UserDetailData(data: GithubUser?) {
    data?.let { user ->
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(user.avatar)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(90.dp)
                            .clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = user.login, style = MaterialTheme.typography.titleMedium)
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                UserOptionalData(user)
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
            }
            item {
                Text(
                    modifier = Modifier.padding(start = 16.dp),
                    text = stringResource(id = R.string.repositories, user.publicRepos),
                    style = MaterialTheme.typography.titleLarge
                )
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }

            items(items = user.repos ?: emptyList()) {
                ReposItem(it)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOptionalData(user: GithubUser) {
    Column(modifier = Modifier.fillMaxWidth()) {
        user.name?.let { OptionData(vector = R.drawable.baseline_person_24, title = it) }
        user.location?.let { OptionData(vector = R.drawable.baseline_location_on_24, title = it) }
        user.company?.let { OptionData(vector = R.drawable.baseline_work_24, title = it) }
    }
}

@ExperimentalMaterial3Api
@Composable
fun OptionData(@DrawableRes vector: Int, title: String) {
    ListItem(
        leadingContent = {
            Icon(
                imageVector = ImageVector.vectorResource(id = vector),
                contentDescription = null
            )
        },
        headlineText = { Text(text = title) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReposItem(repos: GithubRepo) {
    ListItem(
        headlineText = { Text(text = repos.name) },
        supportingText = { Text(text = repos.description) },
        trailingContent = { Text(text = repos.language) }
    )
}

@Preview
@Composable
fun UserDetailDataPreview() {
    UserDetailData(
        GithubUser(
            id = 1,
            login = "dorfo1",
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
}