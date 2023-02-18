package com.guru.ktorgen.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.guru.ktorgen.data.remote.PlaceHolderService
import com.guru.ktorgen.viewmodel.mapper.toPostsUi
import com.guru.ktorgen.viewmodel.model.PostUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceHolderViewModel @Inject constructor(
    private val placeHolderService: PlaceHolderService
) : ViewModel() {

    val listOfPosts = mutableStateListOf<PostUiModel>()

    init {
        viewModelScope.launch {
            listOfPosts.addAll(placeHolderService.getPosts().toPostsUi())
        }
    }
}