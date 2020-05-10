package com.mikkipastel.blog.service

import com.mikkipastel.blog.RestServiceTestHelper
import com.mikkipastel.blog.TestHelper
import com.mikkipastel.blog.manager.ApiService
import com.mikkipastel.blog.model.GhostBlogModel
import com.mikkipastel.blog.model.GhostTagsModel
import com.squareup.okhttp.mockwebserver.MockResponse
import com.squareup.okhttp.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import retrofit2.Call

class GetAllTagsGatewayTest  {
    var gateway: ApiService? = null
    var server: MockWebServer? = null

    @Before
    @Throws(Exception::class)
    fun setUp() {
        server = MockWebServer()
        server?.start()
        gateway = RestServiceTestHelper().createRetrofitService(server?.url("/").toString(), ApiService::class.java)
    }

    @Test
    @Throws(Exception::class)
    fun getUserInfoSuccessCode() {
        val json = TestHelper().getStringFromFile("GhostBlogModel.json")
        server?.enqueue(MockResponse().setResponseCode(200).setBody(json))

        val response = gateway?.getAllTags() as Call<GhostTagsModel>
        Assert.assertEquals(200, response.execute().code())
    }

    @Test
    @Throws(Exception::class)
    fun getUserInfoSuccessBody() {
        val json = TestHelper().getStringFromFile("GhostBlogModel.json")
        server?.enqueue(MockResponse().setResponseCode(200).setBody(json))

        val response = gateway?.getAllTags() as Call<GhostTagsModel>
        Assert.assertNotNull(response.execute().body())
    }

    @Test
    fun getUserInfoFailureCode() {
        server?.enqueue(MockResponse().setResponseCode(404).setBody(""))
        val response = gateway?.getAllTags() as Call<GhostTagsModel>
        Assert.assertEquals(404, response.execute().code())
    }

    @After
    fun tearDown() {
        gateway = null
        server = null
    }
}