package br.com.github_compose.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class UserDetailViewModel(
    private val repository: GithubUserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow<Resource<GithubUser>>(Resource.Initial())
    val userState: StateFlow<Resource<GithubUser>> = _userState.asStateFlow()

    fun getUserInformation(username: String) {
        viewModelScope.launch {
            repository.getUser(username).collect {
                _userState.value = it
            }
        }
    }
}