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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.webview.data.LoadingState
import com.ivangarzab.webview.data.rememberWebViewState
import com.ivangarzab.webview.sample.theme.MyTheme
import com.ivangarzab.webview.ui.WebView
import com.ivangarzab.webview.util.rememberWebViewNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyTheme { WebViewScreen(url = DEFAULT_URL) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(
    modifier: Modifier = Modifier,
    url: String
) {
    val state = rememberWebViewState(url)

    val navigator = rememberWebViewNavigator()

    val loadingState = state.loadingState

    val pullToRefreshState = rememberPullToRefreshState()

    val coroutineScope = rememberCoroutineScope()

    val onRefresh: () -> Unit = {
        coroutineScope.launch {
            delay(1500)
            navigator.reload()
            pullToRefreshState.endRefresh()
        }
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) { onRefresh() }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.app_name) + " Sample") },
                actions = {
                    IconButton(onClick = { pullToRefreshState.startRefresh() }) {
                        Icon(
                            imageVector = Icons.Filled.Refresh,
                            contentDescription = "Trigger Refresh"
                        )
                    }
                }
            )
        }
    ) {
        Box(
            Modifier
                .padding(it)
                .nestedScroll(pullToRefreshState.nestedScrollConnection)
        ) {
            LazyColumn(Modifier.fillMaxSize()) {
                if (!pullToRefreshState.isRefreshing) {
                    item {
                        WebView(
                            modifier = Modifier
                                .fillMaxSize(),
                            state = state,
                            navigator = navigator
//                      onCreated = { it.settings.javaScriptEnabled = true },
                        )
                    }
                }
            }
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = pullToRefreshState,
            )

            if (loadingState is LoadingState.Loading) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth(),
                    progress = { loadingState.progress },
                    color = MaterialTheme.colorScheme.onSurface,
                    trackColor = MaterialTheme.colorScheme.surface
                )
            }
        }
    }
}

@Preview
@Composable
fun WebViewScreenPreview() { MyTheme { WebViewScreen(url = DEFAULT_URL) } }

private const val DEFAULT_URL = "https://medium.ivangarzab.com"