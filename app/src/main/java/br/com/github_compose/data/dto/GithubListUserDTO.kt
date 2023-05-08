package br.com.github_compose.data.dto

import com.google.gson.annotations.SerializedName

data class GithubListUserDTO(
    val id: Int,
    val login: String,
    @SerializedName("avatar_url")
    val avater: String
)

data class GithubListReponse(
    @SerializedName("items")
    val users : List<GithubListUserDTO>
)