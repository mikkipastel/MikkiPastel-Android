package com.mikkipastel.blog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.preference.PreferenceManager
import com.mikkipastel.blog.viewmodel.BlogViewModel
import com.mikkipastel.blogservice.di.blogServiceModule
import com.mikkipastel.readcontent.di.readContentModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.dsl.module

const val preferenceDarkMode = "pref_dark_mode"

class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin()
        initDarkMode()
    }

    private fun initKoin() {
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            val blogModule = module {
                viewModel { BlogViewModel(get(), get(), get()) }
            }
            modules(listOf(
                blogServiceModule(),
                readContentModule(),
                blogModule
            ))
        }
    }

    private fun initDarkMode() {
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        when (sharedPreferences.getBoolean(preferenceDarkMode, false)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        }
    }
}
