package br.com.github_compose.feature.users

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import br.com.github_compose.R
import br.com.github_compose.base.components.BaseCenteredMessage
import br.com.github_compose.base.components.BaseLoading
import br.com.github_compose.base.theme.GithubcomposeTheme
import br.com.github_compose.base.utils.ApiLimitException
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.feature.LocalOnUserClicked
import br.com.github_compose.model.GithubUser
import coil.compose.AsyncImage
import coil.request.ImageRequest

const val TEXT_FIELD_TEST_TAG = "TEST_TEXT_FIELD"
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsersScreen(userList: Resource<List<GithubUser>>, search: String, onSearch: (String) -> Unit) {
    GithubcomposeTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            TopAppBar(title = { Text(text = stringResource(id = R.string.find_user)) })
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp).testTag(TEXT_FIELD_TEST_TAG),
                value = search,
                onValueChange = { onSearch.invoke(it) },
                label = { Text(text = stringResource(id = R.string.type_to_find_user)) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
            when (userList) {
                is Resource.Success -> UserListData(userList.data)
                is Resource.Loading -> BaseLoading()
                is Resource.Error -> UserListError(userList.exception)
                else -> {}
            }
        }
    }
}

@Composable
fun UserListError(error: Exception?) {
    if (error is ApiLimitException) {
        BaseCenteredMessage(stringResource(id = R.string.api_limit_error))
    } else {
        BaseCenteredMessage(stringResource(id = R.string.generic_erroMessage))
    }
}

@Composable
fun UserListData(data: List<GithubUser>?) {
    data?.let { users ->
        if (users.isNotEmpty()) {
            LazyColumn {
                items(items = users) {
                    UserItemView(user = it)
                }
            }
        } else {
            BaseCenteredMessage(stringResource(id = R.string.empty_user_list_message))
        }
    } ?: kotlin.run {
        BaseCenteredMessage(stringResource(id = R.string.generic_erroMessage))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserItemView(user: GithubUser) {
    val onUserClicked = LocalOnUserClicked.current
    ListItem(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onUserClicked.invoke(user) },
        leadingContent = {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(user.avatar)
                    .crossfade(true)
                    .build(),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
            )
        },
        headlineText = { Text(text = user.login) },
        supportingText = { Text(text = stringResource(id = R.string.click_more_details)) }
    )
}

@Preview
@Composable
fun UserItemViewPreview() {
    CompositionLocalProvider(LocalOnUserClicked provides {}) {
        UserItemView(
            GithubUser(
                id = 1,
                login = "dorfo1",
                name = null,
                avatar = "https://avatars.githubusercontent.com/u/39884163?v=4",
                publicRepos = 0,
                publicGists = 0,
                location = null,
                following = 0,
                followers = 0,
                company = null,
                repos = null,
                blog = null
            )
        )
    }
}

@Preview
@Composable
fun UserListMessagPreview() {
    BaseCenteredMessage("Mensagem genérica")
}


@Preview
@Composable
fun UserScreenPreview() {
    UsersScreen(search = "", userList = Resource.Initial()) {}
}