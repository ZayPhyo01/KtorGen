package com.guru.ktorgen.data.remote

import com.guru.annonation.Body
import com.guru.annonation.GET
import com.guru.annonation.Ktor
import com.guru.annonation.POST
import com.guru.annonation.Query
import com.guru.ktorgen.data.remote.response.PostRequest
import com.guru.ktorgen.data.remote.response.PostResponse
import io.ktor.util.*

@Ktor
interface PlaceHolderService {

    @GET(URL.GET_POSTS)
    suspend fun getPosts(): List<PostResponse>

    @GET(URL.GET_POSTS)
    suspend fun getPosts(
        @Query("id") id: String,
        @Query("name") name: String
    ): List<PostResponse>

    @POST(URL.GET_POSTS)
    suspend fun postPosts(
        @Body postRequest: PostRequest
    ): PostResponse
}