package com.guru.ktorgen.viewmodel.mapper

import com.guru.ktorgen.data.remote.response.PostResponse
import com.guru.ktorgen.viewmodel.model.PostUiModel

fun List<PostResponse>?.toPostsUi() = this?.map {
    PostUiModel(
        title = it.title,
        body = it.body
    )
}.orEmpty()