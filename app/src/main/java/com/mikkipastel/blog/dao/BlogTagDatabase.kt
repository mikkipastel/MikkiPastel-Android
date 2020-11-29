package com.mikkipastel.blog.dao

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mikkipastel.blog.model.TagBlog

const val blogTagTable = "blog_tag_table"

@Database(entities = [TagBlog::class], version = 1)
abstract class BlogTagDatabase : RoomDatabase() {
    abstract val blogTagDao: BlogDao

    companion object {
        @Volatile
        private var INSTANCE: BlogTagDatabase? = null

        fun getBlogTagDatabase(context: Context): BlogTagDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        BlogTagDatabase::class.java,
                        blogTagTable
                )
                        .fallbackToDestructiveMigration()
                        .build()
                INSTANCE = instance
                instance
            }
        }
    }
}