package com.mikkipastel.blog.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.mikkipastel.blog.model.PostBlog

@Dao
interface BlogContentDao {
    @Query("SELECT * FROM $blogContentTable ORDER BY published_at DESC")
    fun getContentBlog() : MutableList<PostBlog>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertContent(list: MutableList<PostBlog>)

    @Query("DELETE FROM $blogContentTable")
    fun deleteAllContent()
}