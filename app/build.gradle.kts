import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.1.21"
    alias(libs.plugins.google.gms.google.services)
    id("kotlin-parcelize")
}

android {
    namespace = "com.jk.cashcontrol"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.jk.cashcontrol"
        minSdk = 26
        targetSdk = 35
        versionCode = 10
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            val geminiApiKey = gradleLocalProperties(rootDir,providers).getProperty("GEMINI_API_KEY"," ")
            buildConfigField(type = "String", name = "GEMINI_API_KEY", value = """$geminiApiKey""")
            signingConfig = signingConfigs.getByName("debug")
            ndk.debugSymbolLevel = "FULL"
        }

        debug {
            val geminiApiKey = gradleLocalProperties(rootDir,providers).getProperty("GEMINI_API_KEY"," ")
            buildConfigField(type = "String", name = "GEMINI_API_KEY", value = """$geminiApiKey""")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    ndkVersion = "29.0.13599879 rc2"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.ycharts)

    implementation(libs.androidx.navigation.compose)


    implementation(libs.kotlinx.serialization.json)

    implementation(libs.bundles.koin)

    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.runtime.livedata)
    implementation(platform(libs.firebase.bom))
    implementation (libs.play.services.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.androidx.datastore.preferences)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.meticha:permissions_compose:0.0.1+4")
    implementation("androidx.compose.material3:material3-adaptive-navigation-suite")
    implementation(libs.accompanist.permissions)




}