plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "ba.aadil.namaz"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 3
        versionName = "1.2"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packagingOptions {
        resources.excludes.add("META-INF/*.kotlin_module")
    }
    buildTypes {
        getByName("release") {
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
        isCoreLibraryDesugaringEnabled = true
    }

    lint {
        isWarningsAsErrors = true
        isAbortOnError = true
    }


    kotlinOptions {
        jvmTarget = "1.8"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(project(":vaktijasdk"))
    val room_version = "2.3.0"
    val koin_version = "3.1.4"
    val ktx_version = "2.4.0"
    val navigation_version = "2.4.0-rc01"
    val emoji2_version = "1.1.0-alpha01"
    val work_version = "2.7.1"

    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.4.1")

    implementation("io.insert-koin:koin-android:$koin_version")

    implementation("com.google.android.material:material:1.5.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.3")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$ktx_version")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:$ktx_version")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$ktx_version")
    implementation("androidx.lifecycle:lifecycle-service:$ktx_version")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navigation_version")
    implementation("androidx.navigation:navigation-ui-ktx:$navigation_version")

    // room
    // fix for m1 macs
    kapt("org.xerial:sqlite-jdbc:3.34.0")
    implementation("androidx.room:room-runtime:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("androidx.room:room-ktx:$room_version")

    // LocalTime
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:1.1.5")

    // lists
    implementation("com.github.vivchar:RendererRecyclerViewAdapter:3.0.1")

    //firebase
    implementation(platform("com.google.firebase:firebase-bom:29.1.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth-ktx")

    //charts
    implementation("androidx.emoji2:emoji2:$emoji2_version")
    implementation("androidx.emoji2:emoji2-views:$emoji2_version")
    implementation("androidx.emoji2:emoji2-views-helper:$emoji2_version")

    //work manager
    implementation("androidx.work:work-runtime-ktx:$work_version")

    // testing
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
    testImplementation("io.mockk:mockk:1.12.1")
    testImplementation("io.mockk:mockk-agent-jvm:1.12.1")
}
