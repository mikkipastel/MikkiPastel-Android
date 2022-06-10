package com.mikkipastel.blogservice.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class GhostTagsModelTest {
    var json: String? = null
    var body: GhostTagsModel? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("GhostTagsModel.json")
        body = Gson().fromJson(json, GhostTagsModel::class.java)
    }

    @Test
    fun parseJsonDataResultForTags() {
        Assert.assertNotNull(body?.tags)
    }

    @Test
    fun parseJsonDataResultForTagsLength() {
        Assert.assertEquals(10, body?.tags?.size)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}
