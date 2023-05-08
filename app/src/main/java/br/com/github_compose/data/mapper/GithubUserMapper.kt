package br.com.github_compose.data.mapper

import br.com.github_compose.data.dto.GithubListUserDTO
import br.com.github_compose.data.dto.GithubReposDTO
import br.com.github_compose.data.dto.GithubUserDTO
import br.com.github_compose.model.GithubRepo
import br.com.github_compose.model.GithubUser

fun GithubListUserDTO.toGithubUser() = GithubUser(
    id = id,
    name = null,
    avatar = avater,
    blog = null,
    company = null,
    followers = 0,
    following = 0,
    location = null,
    login = login,
    publicGists = 0,
    publicRepos = 0,
    repos = null
)

fun GithubUserDTO.toGithubUser(repos : List<GithubRepo>) = GithubUser(
    id = id,
    name = name,
    avatar = avater,
    blog = blog,
    company = company,
    followers = followers,
    following = following,
    location = location,
    login = login,
    publicGists = publicGists,
    publicRepos = publicRepos,
    repos = repos
)

fun GithubReposDTO.toGithubRepo(owner : String) = GithubRepo(
    name = name,
    description = description ?: "Nenhuma descrição informada",
    forks = forks,
    isOwner = this.owner.login == owner,
    issues = issues,
    language = language ?: "Não informada",
    stars = stars,
)