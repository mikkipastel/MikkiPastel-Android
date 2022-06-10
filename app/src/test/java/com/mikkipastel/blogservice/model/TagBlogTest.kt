package com.mikkipastel.blogservice.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class TagBlogTest {
    var json: String? = null
    var body: TagBlog? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("TagBlog.json")
        body = Gson().fromJson(json, TagBlog::class.java)
    }

    @Test
    fun parseJsonDataResultForId() {
        Assert.assertEquals("5e4a503b1876cb04e896eea3", body?.id)
    }

    @Test
    fun parseJsonDataResultForName() {
        Assert.assertEquals("android", body?.name)
    }

    @Test
    fun parseJsonDataResultForSlug() {
        Assert.assertEquals("android", body?.slug)
    }

    @Test
    fun parseJsonDataResultForDescription() {
        Assert.assertEquals("เราเป็นคนใช้แอนดรอยด์มาหลายปีดีดัก ตั้งแต่สมัยฝึกงานเลยนะเออ และก็เป็น Android Development จากตอนนั้นมาถึงตอนนี้", body?.description)
    }

    @Test
    fun parseJsonDataResultForFeatureImage() {
        Assert.assertEquals("https://images.unsplash.com/photo-1541345023926-55d6e0853f4b?ixlib=rb-1.2.1&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=2000&fit=max&ixid=eyJhcHBfaWQiOjExNzczfQ", body?.feature_image)
    }

    @Test
    fun parseJsonDataResultForVisibility() {
        Assert.assertEquals("public", body?.visibility)
    }

    @Test
    fun parseJsonDataResultForUrl() {
        Assert.assertEquals("https://www.mikkipastel.com/tag/android/", body?.url)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}
