package br.com.github_compose.base.utils

sealed class Resource<T>(
    var data: T? = null,
    var exception: Exception? = null
) {

    class Initial<T> : Resource<T>()
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    class Error<T>(exception: Exception) : Resource<T>(null, exception)
}