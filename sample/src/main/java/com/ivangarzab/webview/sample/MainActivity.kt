package com.ivangarzab.webview.sample

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.ivangarzab.webview.data.rememberWebViewState
import com.ivangarzab.webview.sample.theme.MyTheme
import com.ivangarzab.webview.ui.WebViewLayout

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

    val pullToRefreshState = rememberPullToRefreshState()

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
        WebViewLayout(
            modifier = Modifier.padding(it),
            state = state,
            loadingIndicatorEnabled = true,
            pullToRefreshState = pullToRefreshState,
        )
    }
}

@Preview
@Composable
fun WebViewScreenPreview() { MyTheme { WebViewScreen(url = DEFAULT_URL) } }

private const val DEFAULT_URL = "https://medium.ivangarzab.com"