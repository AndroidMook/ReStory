package com.cyber.restory.data.api

import com.cyber.restory.data.model.FilterTypeResponse
import com.cyber.restory.data.model.Post
import com.cyber.restory.data.model.PostRequest
import com.cyber.restory.data.model.PostResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiClient {
    @GET("filters/type")
    suspend fun getFilterTypes(): List<FilterTypeResponse>

    @GET("posts")
    suspend fun getPosts(@Query("postRequest") postRequest: PostRequest): PostResponse

    @GET("posts/{id}")
    suspend fun getPostDetail(@Path("id") id: Int): Post
}