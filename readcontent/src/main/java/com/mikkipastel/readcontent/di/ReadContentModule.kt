package com.mikkipastel.readcontent.di

import com.mikkipastel.readcontent.viewmodel.ContentViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

fun readContentModule() = module {
    viewModel { ContentViewModel(get()) }
}