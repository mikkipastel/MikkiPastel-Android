package com.mikkipastel.blogservice.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GhostBlogModelTest {
    var json: String? = null
    var body: GhostBlogModel? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("GhostBlogModel.json")
        body = Gson().fromJson(json, GhostBlogModel::class.java)
    }

    @Test
    fun parseJsonDataResultForNotNullPost() {
        Assert.assertNotNull(body?.posts)
    }

    @Test
    fun parseJsonDataResultForNotNullMeta() {
        Assert.assertNotNull(body?.meta)
    }

    @Test
    fun parseJsonDataResultForBlogLength() {
        Assert.assertEquals(15, body?.posts?.size)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}
