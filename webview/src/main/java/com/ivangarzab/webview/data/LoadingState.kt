package com.ivangarzab.webview.data

import android.webkit.WebView

/**
 * Sealed class for constraining possible loading states.
 *
 * @see [Loading] and [Finished].
 */
sealed class LoadingState {
    /**
     * Describes a [WebView] that has not yet loaded for the first time.
     */
    object Initializing : LoadingState()

    /**
     * Describes a [WebView] between `onPageStarted` and `onPageFinished` events, contains a
     * [progress] property which is updated by the [WebView].
     */
    data class Loading(val progress: Float) : LoadingState()

    /**
     * Describes a [WebView] that has finished loading content.
     */
    object Finished : LoadingState()
}
