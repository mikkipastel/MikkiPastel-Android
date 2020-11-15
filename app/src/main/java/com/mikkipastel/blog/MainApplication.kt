package com.mikkipastel.blog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.preference.PreferenceManager
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
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initDarkMode()
    }

    private fun initKoin() {
        startKoin {
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

    private fun initDarkMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getBoolean("pref_dark_mode", false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}
