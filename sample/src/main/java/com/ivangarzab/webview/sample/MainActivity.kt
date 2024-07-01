package com.ivangarzab.webview.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.webview.data.LoadingState
import com.ivangarzab.webview.data.rememberWebViewState
import com.ivangarzab.webview.sample.theme.MyTheme
import com.ivangarzab.webview.ui.WebView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    WebViewScreen(
                        modifier = Modifier.padding(innerPadding),
                        url = DEFAULT_URL
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String
) {
    val state = rememberWebViewState(url)

    val loadingState = state.loadingState

    WebView(
        modifier = modifier,
        state = state,
//        onCreated = { it.settings.javaScriptEnabled = true },
    )

    if (loadingState is LoadingState.Loading) {
        LinearProgressIndicator(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding(),
            progress = { loadingState.progress },
            color = MaterialTheme.colorScheme.onSurface,
            trackColor = MaterialTheme.colorScheme.surface
        )
    }
}

@Preview
@Composable
fun WebViewScreenPreview() {
    MyTheme {
        Box {
            WebViewScreen(
                modifier = Modifier.fillMaxSize(),
                url = DEFAULT_URL
            )
        }
    }
}

val DEFAULT_URL = "https://medium.ivangarzab.com"