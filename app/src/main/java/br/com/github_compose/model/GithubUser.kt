package br.com.github_compose.model

import com.google.gson.annotations.SerializedName

data class GithubUser(
    val id: Int,
    val login: String,
    val name: String?,
    @SerializedName("avatar_url")
    val avatar: String,
    val company: String?,
    val blog: String?,
    val location: String?,
    @SerializedName("public_repos")
    val publicRepos: Int,
    @SerializedName("public_gists")
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val repos : List<GithubRepo>?
)