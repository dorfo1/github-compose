package br.com.github_compose.repository

import br.com.github_compose.base.utils.ApiLimitException
import br.com.github_compose.base.utils.Resource
import br.com.github_compose.data.mapper.toGithubRepo
import br.com.github_compose.data.mapper.toGithubUser
import br.com.github_compose.data.remote.GithubApi
import br.com.github_compose.model.GithubUser
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.runBlocking
import retrofit2.HttpException

const val API_LIMIT_ERROR_CODE = 403

class GithubUserRepository(
    private val githubApi: GithubApi,
) {
    suspend fun searchUsers(search: String): Flow<Resource<List<GithubUser>>> = flow {
        emit(Resource.Loading())
        val response = githubApi.fetchGithubUsers(search)
        emit(Resource.Success(response.users.map { it.toGithubUser() }))
    }.catch {
        if (it is HttpException) {
            if (it.code() == API_LIMIT_ERROR_CODE) {
                emit(Resource.Error(ApiLimitException()))
            } else {
                emit(Resource.Error(Exception()))
            }
        }
        emit(Resource.Error(Exception()))
    }

    suspend fun getUser(username: String): Flow<Resource<GithubUser>> = flow {
        emit(Resource.Loading())
        coroutineScope {
            val deferredUser = async { githubApi.fetchUserInformation(username) }
            val deferredRepos = async { githubApi.fetchUserRepos(username) }

            val user = deferredUser.await()
            val repos = deferredRepos.await()
            emit(Resource.Success(user.toGithubUser(repos.map { it.toGithubRepo(username) })))
        }
    }.catch {
        it.printStackTrace()
        emit(Resource.Error(Exception()))
    }

}