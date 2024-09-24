package com.cyber.restory.data.api

import com.cyber.restory.data.model.CityFilterResponse
import com.cyber.restory.data.model.Post
import com.cyber.restory.data.model.PostResponse
import com.cyber.restory.data.model.postType.FilterTypeResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ApiClient {
    @GET("filters/type")
    suspend fun getFilterTypes(): List<FilterTypeResponse>

    @GET("posts")
    suspend fun getPosts(
        @Query("city") city: String?,
        @Query("type") type: String?,
        @Query("size") size: Int? = 100,
        @Query("page") page: Int? = 1
    ): PostResponse

    /*
    * 재생공간 상세
    * */
    @GET("posts/{id}")
    suspend fun getPostDetail(@Path("id") id: Int): Post


    /*
    * 지역 필터 조회
    * */
    @GET("filters/city")
    suspend fun getCityFilters(): List<CityFilterResponse>
}