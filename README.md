![badge-android](http://img.shields.io/badge/platform-android-6EDB8D.svg?style=flat)
[![badge-release](https://jitpack.io/v/ivangarzab/composable-webview.svg)](https://jitpack.io/#ivangarzab/composable-webview)

![banner-image](./assets/banner.png)

A WebView implementation in Compose as forked by [Google's Accompanist](https://github.com/google/accompanist/) repository.

With the Google's Accompanist repository deprecating it's [WebView implementation](https://github.com/google/accompanist/tree/main/web) starting on August 2023,
I decided to take their advice and fork that piece of the library in order to continue maintaining their implementation myself,
as well as making it public so that other people may leverage it too.

For more information, please check out their [official documentation](https://google.github.io/accompanist/web/).

## As Seen On
_TBD..._

## Setup
Import this library as a Gradle dependency in two simple steps:
1.  Add Jitpack as a repository inside your Gradle build file

``` groovy
// build.gradle
repositories {
    ...
    maven { url 'https://jitpack.io' }
}
```

``` kotlin
// build.gradle.kts
repositories {
    ...    
    maven { url = uri("https://jitpack.io") }
}
```

2.  Bring in dependency inside your module-level Gradle build file

``` groovy
// build.gradle
dependencies {
    ...
    implementation "com.ivangarzab:composable-webview:<latest_version>"
}
```

``` kotlin
// build.gradle.kts
dependencies {
    ...
    implementation("com.ivangarzab:composable-webview:<latest_version>")
}
```

## Basic Usage
The simplest way to implement this wrapper is two use two main APIs from this library: 
- The [`WebView`](https://github.com/ivangarzab/composable-webview/blob/master/webview/src/main/java/com/ivangarzab/webview/ui/WebView.kt) Composable
- The [`rememberWebViewState(url: String)`](https://github.com/ivangarzab/composable-webview/blob/master/webview/src/main/java/com/ivangarzab/webview/WebViewState.kt) function to take care of its state

``` kotlin
// Composable function
val state = rememberWebViewState("https://example.com")
WebView(state = state)
```

### Enabling JavaScript
JavaScript is disabled by default in the `WebView` implementation.

In order to enable this feature -- or any other settings -- you can do so inside the `onCreated` callback:

``` kotlin
WebView(
    state = state
    onCreated = { it.settings.javaScriptEnabled = true }
)
```

### Capturing Back Actions
Back Actions -- such as back button presses or back gestures --  are captured by default in the `WebView` implementation.

This behavior can be disabled via a parameter in the Composable:

``` kotlin
WebView(
    ...
    captureBackPresses = false
)
```

### Subclassing a `WebView` Implementation
Subclassing of the default `WebView` implementation is possible through the `factory` Composable:

``` kotlin
WebView(
    ...
    factory = { context -> CustomWebView(context) }
)
```


## Upcoming Features
- [ ] Pull to refresh ♻️
- [ ] Website loading spinner ↻
- [ ] Distribution through the Maven package 🚀

## License
The `composable-webview` library is distributed under the terms and conditions of the Apache License (Version 2).  

Please refer to the [License](https://github.com/ivangarzab/composable-webview/blob/dev/LICENSE) page for more information.
