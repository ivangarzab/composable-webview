// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    extra["kotlin_version"] = libs.versions.kotlin
    repositories {
        google()
        mavenCentral()
        mavenLocal()
    }
    dependencies {
        classpath(libs.android.gradle.plugin)
        classpath(libs.kotlin.plugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.jetbrains.kotlin.android) apply false
    alias(libs.plugins.compose.compiler) apply false
}
