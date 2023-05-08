package br.com.github_compose.model

import com.google.gson.annotations.SerializedName

data class GithubRepo(
    val name: String,
    val description: String,
    val language: String,
    val forks: Int,
    @SerializedName("stargazers_count")
    val stars: Int,
    @SerializedName("open_issues")
    val issues: Int,
    val isOwner: Boolean,
)
