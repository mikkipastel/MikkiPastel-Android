package com.mikkipastel.blogservice.dao

//@Dao
//interface BlogContentDao {
//    @Query("SELECT * FROM $blogContentTable ORDER BY published_at DESC")
//    fun getContentBlog() : MutableList<PostBlog>
//
//    @Insert(onConflict = OnConflictStrategy.IGNORE)
//    fun insertContent(list: MutableList<PostBlog>)
//
//    @Query("DELETE FROM $blogContentTable")
//    fun deleteAllContent()
//}