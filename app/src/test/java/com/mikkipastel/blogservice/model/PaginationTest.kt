package com.mikkipastel.blogservice.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PaginationTest {
    var json: String? = null
    var body: Pagination? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("Pagination.json")
        body = Gson().fromJson(json, Pagination::class.java)
    }

    @Test
    fun parseJsonDataResultForPage() {
        Assert.assertEquals(1, body?.page)
    }

    @Test
    fun parseJsonDataResultForLimit() {
        Assert.assertEquals(15, body?.limit)
    }

    @Test
    fun parseJsonDataResultForPages() {
        Assert.assertEquals(19, body?.pages)
    }

    @Test
    fun parseJsonDataResultForTotal() {
        Assert.assertEquals(278, body?.total)
    }

    @Test
    fun parseJsonDataResultForNext() {
        Assert.assertEquals(2, body?.next)
    }

    @Test
    fun parseJsonDataResultForPrev() {
        Assert.assertEquals(null, body?.prev)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}
