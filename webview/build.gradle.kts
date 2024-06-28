plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.compose.compiler)
    `maven-publish`
}

android {
    namespace = "com.ivangarzab.webview"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    publishing {
        multipleVariants("production") {
            includeBuildTypeValues("release")
            withJavadocJar()
            withSourcesJar()
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}

publishing {
    publications {
        register<MavenPublication>("production") {
            groupId = "com.ivangarzab"
            artifactId = "composable-webview"
            version = "0.1"

            afterEvaluate {
                from(components["production"])
            }
        }
    }

    repositories {
        maven {
            name = "localRepo"
            url = uri("${project.buildDir}/repo")
        }
    }
}

tasks.register<Zip>("zipRepo") {
    group = "publishing"
    description = "A simple task that zip the publication."
    val publishTask = tasks.named(
        "publishProductionPublicationToLocalRepoRepository",
        PublishToMavenRepository::class.java)
    from(publishTask.map { it.repository.url })
    into("myLibrary")
    archiveFileName.set("myLibrary.zip")
}
