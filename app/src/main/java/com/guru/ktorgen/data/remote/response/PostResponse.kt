package com.guru.ktorgen.data.remote.response

@kotlinx.serialization.Serializable
data class PostResponse(
    val userId: String,
    val id: String,
    val title: String,
    val body: String
)