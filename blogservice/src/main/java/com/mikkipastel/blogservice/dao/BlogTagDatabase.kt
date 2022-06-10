package com.mikkipastel.blogservice.dao

const val blogTagTable = "blog_tag_table"

//@Database(entities = [TagBlog::class], version = 1, exportSchema = false)
//abstract class BlogTagDatabase : RoomDatabase() {
//    abstract val blogTagDao: BlogTagDao
//
//    companion object {
//        @Volatile
//        private var INSTANCE: BlogTagDatabase? = null
//
//        fun getBlogTagDatabase(context: Context): BlogTagDatabase {
//            return INSTANCE ?: synchronized(this) {
//                val instance = Room.databaseBuilder(
//                        context.applicationContext,
//                        BlogTagDatabase::class.java,
//                        blogTagTable
//                )
//                        .fallbackToDestructiveMigration()
//                        .build()
//                INSTANCE = instance
//                instance
//            }
//        }
//    }
//}