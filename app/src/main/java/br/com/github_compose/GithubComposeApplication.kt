package br.com.github_compose

import android.app.Application
import br.com.github_compose.di.AppModule
import org.koin.core.context.startKoin

class GithubComposeApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            modules(AppModule.moduleDependencies)
        }
    }
}