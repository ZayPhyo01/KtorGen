package com.guru.ktorgen.data.remote

import com.guru.ktorgen.data.remote.response.PostResponse

interface PlaceHolderService {
    suspend fun getPosts(): List<PostResponse>
}