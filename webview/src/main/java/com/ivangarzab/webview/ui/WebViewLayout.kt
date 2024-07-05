/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ivangarzab.webview.ui

import android.content.Context
import android.webkit.WebView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.PullToRefreshState
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.ivangarzab.webview.data.LoadingState
import com.ivangarzab.webview.data.WebViewState
import com.ivangarzab.webview.util.AccompanistWebChromeClient
import com.ivangarzab.webview.util.AccompanistWebViewClient
import com.ivangarzab.webview.util.WebViewNavigator
import com.ivangarzab.webview.util.rememberWebViewNavigator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A wrapper around the Android View [WebView] to provide a basic WebView composable,
 * plus the added functionalities of pull-to-refresh and loading indicators.
 *
 * If you require more customisation you are most likely better rolling your own and using this
 * wrapper as an example.
 *
 * The WebView attempts to set the layoutParams based on the Compose modifier passed in. If it
 * is incorrectly sizing, use the layoutParams composable function instead.
 *
 * @param state The webview state holder where the Uri to load is defined.
 * @param modifier A compose modifier
 * @param captureBackPresses Set to true to have this Composable capture back presses and navigate
 * the WebView back.
 * @param loadingIndicatorEnabled Set to true to show a loading indicator when applicable.
 * @param pullToRefreshState An optional PullToRefreshState object that can be used to control the
 * pull-to-refresh from outside the composable.
 * @param navigator An optional navigator object that can be used to control the WebView's
 * navigation from outside the composable.
 * @param onCreated Called when the WebView is first created, this can be used to set additional
 * settings on the WebView. WebChromeClient and WebViewClient should not be set here as they will be
 * subsequently overwritten after this lambda is called.
 * @param onDispose Called when the WebView is destroyed. Provides a bundle which can be saved
 * if you need to save and restore state in this WebView.
 * @param client Provides access to [android.webkit.WebViewClient] via subclassing
 * @param chromeClient Provides access to [android.webkit.WebChromeClient] via subclassing
 * @param factory An optional WebView factory for using a custom subclass of WebView
 * @sample com.ivangarzab.webview.sample.MainActivity
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WebViewLayout(
    state: WebViewState,
    modifier: Modifier = Modifier,
    captureBackPresses: Boolean = true,
    loadingIndicatorEnabled: Boolean = true,
    pullToRefreshState: PullToRefreshState = rememberPullToRefreshState(),
    navigator: WebViewNavigator = rememberWebViewNavigator(),
    onCreated: (WebView) -> Unit = {},
    onDispose: (WebView) -> Unit = {},
    client: AccompanistWebViewClient = remember { AccompanistWebViewClient() },
    chromeClient: AccompanistWebChromeClient = remember { AccompanistWebChromeClient() },
    factory: ((Context) -> WebView)? = null,
) {
    val loadingState = state.loadingState

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

    Box(modifier.nestedScroll(pullToRefreshState.nestedScrollConnection)) {
        LazyColumn(Modifier.fillMaxSize()) {
            if (!pullToRefreshState.isRefreshing) {
                item {
                    WebView(
                        modifier = Modifier.fillMaxSize(),
                        state = state,
                        navigator = navigator,
                        captureBackPresses = captureBackPresses,
                        onCreated = onCreated,
                        onDispose = onDispose,
                        client = client,
                        chromeClient = chromeClient,
                        factory = factory,
                    )
                }
            }
        }
        PullToRefreshContainer(
            modifier = Modifier.align(Alignment.TopCenter),
            state = pullToRefreshState,
        )

        if (loadingIndicatorEnabled) {
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