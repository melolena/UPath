plugins {
    alias(libs.plugins.androidApplication)
}

android {
    namespace = "com.example.upath"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.upath"
        minSdk = 25
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

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {

    // ANDROIDX
    implementation(libs.appcompatLib)
    implementation(libs.materialLib)
    implementation(libs.activityLib)
    implementation(libs.constraintlayoutLib)

    // BLUR
    implementation("jp.wasabeef:blurry:4.0.0")

    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OKHTTP
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // GLIDE (Java = annotationProcessor)
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // TESTES
    testImplementation(libs.junit)
    androidTestImplementation(libs.extJunit)
    androidTestImplementation(libs.espressoCoreLib)
}
