package com.mikkipastel.blog.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class MetaTest {
    var json: String? = null
    var body: Meta? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("Meta.json")
        body = Gson().fromJson(json, Meta::class.java)
    }

    @Test
    fun parseJsonDataResultForPagination() {
        Assert.assertNotNull(body?.pagination!!)
    }

    @Test
    fun parseJsonDataResultForPage() {
        Assert.assertEquals(1, body?.pagination?.page)
    }

    @Test
    fun parseJsonDataResultForLimit() {
        Assert.assertEquals(15, body?.pagination?.limit)
    }

    @Test
    fun parseJsonDataResultForPages() {
        Assert.assertEquals(19, body?.pagination?.pages)
    }

    @Test
    fun parseJsonDataResultForTotal() {
        Assert.assertEquals(278, body?.pagination?.total)
    }

    @Test
    fun parseJsonDataResultForNext() {
        Assert.assertEquals(2, body?.pagination?.next)
    }

    @Test
    fun parseJsonDataResultForPrev() {
        Assert.assertEquals(null, body?.pagination?.prev)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}
