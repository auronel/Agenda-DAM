plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    kotlin("kapt") version "2.0.21"
}

android {
    namespace = "com.dam.agendadam"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.dam.agendadam"
        minSdk = 21
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
    kotlinOptions {
        jvmTarget = "11"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    var VERSION_ROOM = "2.6.1"
    // Dependencias de Room
    implementation("androidx.room:room-runtime:$VERSION_ROOM")
    // Para usar Room con Kotlin Kapt
    kapt("androidx.room:room-compiler:$VERSION_ROOM")
    //kapt("groupId:artifactId:$VERSION_ROOM")
    // Para usar Coroutines con Room (opcional, pero muy recomendado para operaciones asíncronas)
    implementation("androidx.room:room-ktx:$VERSION_ROOM")
    // Soporte para Coroutines en la Activity/Fragment
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:VERSION_LIFECYCLE")
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}