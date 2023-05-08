package br.com.github_compose.feature.users

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.model.GithubUser
import br.com.github_compose.repository.GithubUserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class UsersViewModel(
    private val repository: GithubUserRepository
) : ViewModel() {

    private val _userList = MutableStateFlow<Resource<List<GithubUser>>>(Resource.Initial())
    val userList: StateFlow<Resource<List<GithubUser>>> = _userList.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private var job: Job? = null

    init {
        viewModelScope.launch {
            _searchText.collect { search ->
                job?.cancel()
                job = viewModelScope.launch {
                    if (search.isNotEmpty()) {
                        delay(2.seconds)
                        repository.searchUsers(search).collect {
                            _userList.value = it
                        }
                    }
                }
            }
        }
    }

    fun updateSearchText(value: String) {
        _searchText.value = value
    }
}