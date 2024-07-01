package com.ivangarzab.webview.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.ivangarzab.webview.data.LoadingState
import com.ivangarzab.webview.data.rememberWebViewState
import com.ivangarzab.webview.sample.theme.MyTheme
import com.ivangarzab.webview.ui.WebView
import com.ivangarzab.webview.util.AccompanistWebChromeClient
import com.ivangarzab.webview.util.AccompanistWebViewClient

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
                        url = "https://medium.ivangarzab.com"
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

    val webClient = remember { object : AccompanistWebViewClient() { } }

    val chromeClient = remember { object : AccompanistWebChromeClient() { } }

    WebView(
        modifier = modifier,
        state = state,
        onCreated = { it.settings.javaScriptEnabled = true },
        client = webClient,
        chromeClient = chromeClient
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

@Composable
fun WebViewScreenToolbar(
    modifier: Modifier = Modifier
) {
    Row {

    }
}