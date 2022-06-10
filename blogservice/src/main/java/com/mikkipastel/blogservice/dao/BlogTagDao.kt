package com.mikkipastel.blogservice.dao

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