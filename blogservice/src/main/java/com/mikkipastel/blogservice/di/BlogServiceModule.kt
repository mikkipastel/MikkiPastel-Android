package com.mikkipastel.blogservice.di

import com.mikkipastel.blogservice.domain.GetBlogContentUseCase
import com.mikkipastel.blogservice.domain.GetBlogPostUseCase
import com.mikkipastel.blogservice.domain.GetBlogTagUseCase
import com.mikkipastel.blogservice.manager.HttpManager
import com.mikkipastel.blogservice.repository.BlogRepository
import com.mikkipastel.blogservice.repository.BlogRepositoryImpl
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

fun blogServiceModule() = module {
    single { HttpManager().getApiService() }

    //single<BlogRepository> { BlogRepositoryImpl(get(), get(), get()) }
    single<BlogRepository> { BlogRepositoryImpl(get()) }
    single(named("io")) { Dispatchers.IO }

    factory { GetBlogPostUseCase(get(), get(named("io"))) }
    factory { GetBlogTagUseCase(get(), get(named("io"))) }
    factory { GetBlogContentUseCase(get(), get(named("io"))) }

//    single { Room.databaseBuilder(androidContext(), BlogTagDatabase::class.java, blogTagTable).build() }
//    single { BlogTagDatabase.getBlogTagDatabase(get()).blogTagDao }
//
//    single { Room.databaseBuilder(androidContext(), BlogContentDatabase::class.java, blogContentTable).build() }
//    single { BlogContentDatabase.getBlogContentDatabase(get()).blogContentDao }
}