package com.guru.ktorgen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.guru.ktorgen.ui.theme.KtorGenTheme
import com.guru.ktorgen.viewmodel.PlaceHolderViewModel
import com.guru.ktorgen.viewmodel.model.PostUiModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: PlaceHolderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            KtorGenTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    if (viewModel.listOfPosts.isEmpty()) {
                        Text(text = "please wait")
                    }
                    LazyColumn {
                        items(viewModel.listOfPosts) {
                            Post(postUiModel = it)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Post(postUiModel: PostUiModel) {
    Column {
        Text(text = postUiModel.title)
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = postUiModel.body)
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    KtorGenTheme {
        Greeting("Android")
    }
}