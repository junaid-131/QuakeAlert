plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.quakealert"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.quakealert"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.play.services.location)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // Android Core
    implementation(libs.androidx.core.ktx.v1131)
    implementation(libs.androidx.appcompat.v170)

    implementation(libs.material)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room

    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    kapt(libs.androidx.room.compiler)
   
// Add this line
    implementation(libs.osmdroid.android)

    implementation(libs.material.v1110)
    // OSMDroid

    implementation(libs.androidx.core.ktx.v1120)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.osmdroid.android.v6114)
    implementation(libs.osmdroid.android.v6118)

    implementation(libs.osmdroid.wms)


    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)

    implementation(libs.mpandroidchart)
}