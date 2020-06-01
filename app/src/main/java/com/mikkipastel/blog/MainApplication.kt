package com.mikkipastel.blog

import android.app.Application
import com.mikkipastel.blog.manager.HttpManager
import com.mikkipastel.blog.repository.BlogRepository
import com.mikkipastel.blog.repository.BlogRepositoryImpl
import com.mikkipastel.blog.viewmodel.BlogViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MainApplication : Application() {
    override fun onCreate(){
        super.onCreate()
        initKoin()
    }

    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(this@MainApplication)
            val networkModule = module {
                single { HttpManager().getApiService() }
            }
            val blogModule = module {
                single<BlogRepository> { BlogRepositoryImpl(get()) }
                viewModel { BlogViewModel(get()) }
            }
            modules(listOf(networkModule, blogModule))
        }
    }
}