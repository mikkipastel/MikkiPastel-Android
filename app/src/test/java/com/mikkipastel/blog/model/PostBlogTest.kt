package com.mikkipastel.blog.model

import com.google.gson.Gson
import com.mikkipastel.blog.TestHelper
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class PostBlogTest {
    var json: String? = null
    var body: PostBlog? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        json = TestHelper().getStringFromFile("PostBlog.json")
        body = Gson().fromJson(json, PostBlog::class.java)
    }

    @Test
    fun parseJsonDataResultForTitle() {
        Assert.assertEquals("มาทบทวนเรื่อง Margin และ Padding กันอีกสักนิด", body?.title)
    }

    @Test
    fun parseJsonDataResultForFeatureImage() {
        Assert.assertEquals("https://www.mikkipastel.com/content/images/2020/04/IMG_0166.PNG", body?.feature_image)
    }

    @Test
    fun parseJsonDataResultForCustomExcerpt() {
        Assert.assertEquals("แรกๆที่เราเขียน Android มักจะแยก margin และ padding ไม่ค่อยถูก พอเป็น Android Developer ไปเรื่อยๆก็จะแยกออกหล่ะ แล้วเห็นบล็อกพี่เอกเขียนหัวข้อนี้ แต่ยังไม่ได้อ่าน ก็เลยลองวาดๆเขียนๆดู", body?.custom_excerpt)
    }

    @Test
    fun parseJsonDataResultForPublishedAt() {
        Assert.assertEquals("2020-05-08T12:10:00.000+07:00", body?.published_at)
    }

    @Test
    fun parseJsonDataResultForTags() {
        Assert.assertNotNull(body?.tags)
    }

    @Test
    fun parseJsonDataResultForUrl() {
        Assert.assertEquals("https://www.mikkipastel.com/android-margin-and-padding/", body?.url)
    }

    @After
    fun tearDown() {
        json = null
        body = null
    }
}