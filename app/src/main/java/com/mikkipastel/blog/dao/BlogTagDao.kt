package com.mikkipastel.blog.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkipastel.blog.model.TagBlog

//@Dao
//interface BlogTagDao {
//    @Query("SELECT * FROM $blogTagTable ORDER BY name ASC")
//    fun getTagBlog() : MutableList<TagBlog>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertTag(list: MutableList<TagBlog>)
//
//    @Query("DELETE FROM $blogTagTable")
//    fun deleteAllTag()
//}