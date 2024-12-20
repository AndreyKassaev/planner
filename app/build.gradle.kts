plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("androidx.navigation.safeargs.kotlin")
    kotlin("plugin.serialization") version "2.1.0"
    id("com.google.devtools.ksp")
    id("androidx.room")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.kassaev.planner"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.kassaev.planner"
        minSdk = 23
        targetSdk = 35
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        viewBinding = true
    }
    room {
        schemaDirectory("$projectDir/schemas")
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    //Navigation Fragments
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Navigation Compose
    // https://mvnrepository.com/artifact/androidx.navigation/navigation-compose
    implementation(libs.androidx.navigation.compose)

    //Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //Serialization
    implementation(libs.kotlinx.serialization.json)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Koin DI
    // https://mvnrepository.com/artifact/io.insert-koin/koin-android
    implementation(libs.koin.android)
    // https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-compose
    implementation(libs.koin.androidx.compose)
    // https://mvnrepository.com/artifact/io.insert-koin/koin-androidx-navigation
    implementation(libs.koin.androidx.navigation)


    // https://mvnrepository.com/artifact/androidx.viewpager2/viewpager2
    implementation(libs.androidx.viewpager2)

    //Compose
    // https://mvnrepository.com/artifact/androidx.compose.ui/ui
    implementation(libs.androidx.ui)
    // https://mvnrepository.com/artifact/androidx.compose.ui/ui-tooling-preview
    implementation(libs.androidx.ui.tooling.preview)
    // https://mvnrepository.com/artifact/androidx.compose.material3/material3
    implementation(libs.androidx.material3)
    // https://mvnrepository.com/artifact/androidx.activity/activity-compose
    implementation(libs.androidx.activity.compose)
}