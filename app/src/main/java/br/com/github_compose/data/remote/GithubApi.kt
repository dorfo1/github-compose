package br.com.github_compose.data.remote

import br.com.github_compose.data.dto.GithubListReponse
import br.com.github_compose.data.dto.GithubReposDTO
import br.com.github_compose.data.dto.GithubUserDTO
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users")
    suspend fun fetchGithubUsers(@Query("q") search: String): GithubListReponse

    @GET("users/{username}")
    suspend fun fetchUserInformation(@Path("username") username: String): GithubUserDTO

    @GET("users/{username}/repos")
    suspend fun fetchUserRepos(@Path("username") username: String): List<GithubReposDTO>
}