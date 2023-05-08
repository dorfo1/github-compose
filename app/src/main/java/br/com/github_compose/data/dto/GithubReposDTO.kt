package br.com.github_compose.data.dto

import com.google.gson.annotations.SerializedName

data class GithubReposDTO(
    val name: String,
    val description: String?,
    val language: String?,
    @SerializedName("forks_count")
    val forks : Int,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("open_issues")
    val issues : Int,
    val owner : RepoOwnerDTO,
)

data class RepoOwnerDTO(
    val login : String,
)