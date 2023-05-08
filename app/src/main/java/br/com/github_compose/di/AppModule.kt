package br.com.github_compose.di

import br.com.github_compose.data.remote.GithubApi
import br.com.github_compose.feature.detail.UserDetailViewModel
import br.com.github_compose.feature.users.UsersViewModel
import br.com.github_compose.repository.GithubUserRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object AppModule {

    val moduleDependencies = module {
        single<Retrofit> { provideRetrofit(get()) }
        single<OkHttpClient> { provideOkhttpClient() }

        single<GithubUserRepository> { GithubUserRepository(get()) }
        single<GithubApi> { get<Retrofit>().create(GithubApi::class.java) }

        viewModel<UserDetailViewModel> { UserDetailViewModel(get()) }
        viewModel<UsersViewModel> { UsersViewModel(get()) }
    }

    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideOkhttpClient(): OkHttpClient {
        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addNetworkInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        return okHttpClient.build()
    }

}