package com.guru.ktorgen.data.remote

import com.guru.ktorgen.data.remote.response.PostResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import javax.inject.Inject

class PlaceHolderServiceImpl @Inject constructor(
    private val client: HttpClient
) : PlaceHolderService {
    override suspend fun getPosts(): List<PostResponse> {
        return client.get {
            url(URL.GET_POSTS)
        }.body()
    }
}