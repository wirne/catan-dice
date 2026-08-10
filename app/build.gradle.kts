plugins {
    id("com.android.application")
    id("com.google.dagger.hilt.android")
    id("com.google.protobuf")
    id("org.jetbrains.kotlin.plugin.compose")
    id("com.google.devtools.ksp")
    kotlin("android")
    kotlin("plugin.serialization") version "2.0.20"
}

// Removed ktlint plugin - use IDE formatting instead

android {
    compileSdk = 35

    defaultConfig {
        applicationId = "com.wirne.catandice"
        minSdk = 26
        targetSdk = 35
        versionCode = 14
        versionName = "1.3.4"

        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }
    kotlinOptions {
        jvmTarget = "21"
        freeCompilerArgs += listOf("-Xcontext-receivers")
    }
    buildFeatures {
        compose = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    lint {
        abortOnError = true
        checkAllWarnings = true
    }
    namespace = "com.wirne.catandice"
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.2:osx-x86_64"
    }
    generateProtoTasks {
        all().forEach {
            it.builtins {
                create("java") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    val composeBom = platform("androidx.compose:compose-bom:2024.12.01")
    implementation(composeBom)

    implementation("androidx.core:core-ktx:1.15.0")
    implementation("androidx.datastore:datastore-preferences:1.1.1")
    implementation("androidx.datastore:datastore:1.1.1")
    implementation("androidx.navigation:navigation-compose:2.9.1")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.1")
    implementation("com.google.dagger:hilt-android:2.51.1")
    ksp("com.google.dagger:hilt-android-compiler:2.51.1")
    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3:1.3.1")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.activity:activity-compose:1.10.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.google.protobuf:protobuf-javalite:3.25.5")
    debugImplementation("androidx.compose.ui:ui-tooling:1.8.1")
}
