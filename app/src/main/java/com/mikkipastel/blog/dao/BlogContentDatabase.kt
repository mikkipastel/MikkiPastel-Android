package com.mikkipastel.blog.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mikkipastel.blog.model.PostBlog

const val blogContentTable = "blog_content_table"

//@Database(entities = [PostBlog::class], version = 1, exportSchema = false)
//abstract class BlogContentDatabase : RoomDatabase() {
//    abstract val blogContentDao: BlogContentDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: BlogContentDatabase? = null
//
//        fun getBlogContentDatabase(context: Context): BlogContentDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        BlogContentDatabase::class.java,
//                        blogContentTable
//                )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}