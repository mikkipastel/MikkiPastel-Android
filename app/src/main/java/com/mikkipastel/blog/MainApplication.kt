package com.mikkipastel.blog

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.preference.PreferenceManager
import androidx.room.Room
import com.mikkipastel.blog.dao.BlogContentDatabase
import com.mikkipastel.blog.dao.BlogTagDatabase
import com.mikkipastel.blog.dao.blogContentTable
import com.mikkipastel.blog.dao.blogTagTable
import com.mikkipastel.blog.manager.HttpManager
import com.mikkipastel.blog.repository.BlogRepository
import com.mikkipastel.blog.repository.BlogRepositoryImpl
import com.mikkipastel.blog.viewmodel.BlogViewModel
import org.koin.android.ext.koin.androidContext
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
                single<BlogRepository> { BlogRepositoryImpl(get(), get(), get()) }
                viewModel { BlogViewModel(get()) }
            }
            val databaseModule = module {
                single { Room.databaseBuilder(androidContext(), BlogTagDatabase::class.java, blogTagTable).build() }
                single { BlogTagDatabase.getBlogTagDatabase(get()).blogTagTagDao }

                single { Room.databaseBuilder(androidContext(), BlogContentDatabase::class.java, blogContentTable).build() }
                single { BlogContentDatabase.getBlogContentDatabase(get()).blogContentDao }
            }
            modules(listOf(networkModule, blogModule, databaseModule))
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
