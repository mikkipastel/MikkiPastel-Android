package com.mikkipastel.blog.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkipastel.blog.model.TagBlog

@Dao
interface BlogDao {
    @Query("SELECT * FROM $blogTagTable ORDER BY name ASC")
    fun getTagBlog() : LiveData<MutableList<TagBlog>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertTag(list: MutableList<TagBlog>)

    @Query("DELETE FROM $blogTagTable")
    fun deleteAllTag()
}