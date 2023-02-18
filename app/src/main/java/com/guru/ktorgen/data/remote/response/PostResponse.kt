package com.guru.ktorgen.data.remote.response

@kotlinx.serialization.Serializable
data class PostResponse(
    val userId: String,
    val id: String,
    val title: String,
    val body: String
)

@kotlinx.serialization.Serializable
data class PostRequest(
    val userId: String,
    val title: String,
    val body: String
)



