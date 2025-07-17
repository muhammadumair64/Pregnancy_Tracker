plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
    id("com.google.dagger.hilt.android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.firebase-perf")
}

android {
    namespace = "com.example.pregnancytrackerignite"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.babytracker.periodtracker.babygenderpredictor.pregnancyapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 7
        versionName = "1.6"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        debug {
//            isMinifyEnabled = true
//            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            // Applovin Mediation
            resValue("string", "APP_LOVIN_MEDIUM_NATIVE", "YOUR_AD_UNIT_ID")
            resValue("string", "APP_LOVIN_SMALL_NATIVE", "YOUR_AD_UNIT_ID")
            resValue("string", "APP_LOVIN_INTERSTITIAL", "YOUR_AD_UNIT_ID")
            resValue("string", "APP_LOVIN_BANNER", "YOUR_AD_UNIT_ID")

            //admob app id
            resValue("string", "admob_app_id", "ca-app-pub-3940256099942544~3347511713")

            resValue("string", "ADMOB_BANNER_V2", "ca-app-pub-3940256099942544/6300978111")
            resValue("string", "ADMOB_OPEN_AD", "ca-app-pub-3940256099942544/9257395921")
            resValue("string", "ADMOB_INTERSTITIAL_V2", "ca-app-pub-3940256099942544/1033173712")
            resValue(
                "string",
                "ADMOB_NATIVE_WITHOUT_MEDIA_V2",
                "ca-app-pub-3940256099942544/2247696110"
            )
            resValue(
                "string",
                "ADMOB_NATIVE_WITH_MEDIA_V2",
                "ca-app-pub-3940256099942544/2247696110"
            )
            resValue("string", "ADMOB_REWARD_VIDEO", "ca-app-pub-3940256099942544/5224354917")
            resValue("string", "ADMOB_REWARD_INTER", "ca-app-pub-3940256099942544/5354046379")
             resValue("string", "ADMOB_BANNER_COLLAPSIBLE", "ca-app-pub-3940256099942544/2014213617")
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            // Applovin Mediation
            resValue("string", "APP_LOVIN_MEDIUM_NATIVE", "YOUR_AD_UNIT_ID")
            resValue("string", "APP_LOVIN_SMALL_NATIVE", "YOUR_AD_UNIT_ID")
            resValue("string", "APP_LOVIN_INTERSTITIAL", "")
            resValue("string", "APP_LOVIN_BANNER", "YOUR_AD_UNIT_ID")

            //admob app id
            resValue("string", "admob_app_id", "")

            resValue("string", "ADMOB_BANNER_V2", "")
            resValue("string", "ADMOB_OPEN_AD", "")
            resValue("string", "ADMOB_INTERSTITIAL_V2", "")
            resValue("string", "ADMOB_NATIVE_WITHOUT_MEDIA_V2", "")
            resValue("string", "ADMOB_NATIVE_WITH_MEDIA_V2", "")
            resValue("string", "ADMOB_REWARD_VIDEO", "")
            resValue("string", "ADMOB_REWARD_INTER", "")
            resValue("string", "ADMOB_BANNER_COLLAPSIBLE", "")
        }
    }
    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    kapt {
        generateStubs = true
        correctErrorTypes = true
    }
    buildFeatures{
        viewBinding=true
        dataBinding=true
        buildConfig = true
    }
}


dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.video)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    testImplementation(libs.junit.jupiter)

    // modules implementation
    implementation(project(":sonictimer"))

    //  Firebase
    //   Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.play.services.auth)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-crashlytics")
    implementation("com.google.firebase:firebase-auth")
    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")
    implementation("com.google.firebase:firebase-messaging")
    implementation("com.google.firebase:firebase-storage-ktx")
    implementation("com.google.firebase:firebase-config-ktx")
    implementation("com.google.firebase:firebase-perf")
    implementation("com.google.firebase:firebase-messaging-ktx")

    val roomVersion = "2.6.1"
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")

//    for minify issue
    implementation(libs.infer.annotation)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    //shimmer effect
    implementation(libs.shimmer)

    //shimmer effect
    implementation(libs.shimmer)

    //Hilt Dependency Injection
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    kapt(libs.hilt.android.compiler)

    // ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Lifecycles only (without ViewModel or LiveData)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    kapt(libs.androidx.hilt.compiler)

    // viewModelScope:
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.sdp.android)

    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.androidx.hilt.navigation.fragment)

    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation(libs.androidx.lifecycle.common.java8)
    // Saved state module for ViewModel
    implementation(libs.androidx.lifecycle.viewmodel.savedstate)

    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Kotlin
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.fragment.ktx)
    // Testing Navigation
    androidTestImplementation(libs.androidx.navigation.testing)

    //Glide
    implementation(libs.glide)

    implementation(libs.androidx.recyclerview)

    // BILLING LIBRARY
    implementation(libs.billing)

    // Applovin
//    implementation(libs.applovin.sdk)
    implementation(libs.google.adapter)
    implementation(libs.facebook.adapter)

    implementation(libs.play.services.ads.identifier)
    implementation(libs.play.services.base)
    implementation(libs.picasso)

    // admob ads
    implementation(libs.play.services.ads)
    // ad consent
    implementation(libs.user.messaging.platform)

    implementation(libs.androidx.lifecycle.process)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    annotationProcessor(libs.androidx.lifecycle.compiler)

    implementation(libs.lottie)

    // google common library
    implementation(libs.listenablefuture)
    implementation(libs.guava)
    implementation(libs.drag.drop.swipe.recyclerview)

    //GSON of Retrofit
    implementation(libs.converter.gson)

    implementation (libs.circularprogressbar)

    implementation (libs.circleimageview)

    // for bidding
    implementation  (libs.applovin)
    implementation  (libs.facebook)
    implementation  (libs.mintegral)
    implementation  (libs.vungle)
    implementation  (libs.vungle.ads)
    implementation ("com.github.sampullman:android-mgraphicslib:-SNAPSHOT")
    implementation ("com.jakewharton.timber:timber:4.7.1")
    implementation (libs.androidx.camera.camera2)
    implementation (libs.androidx.camera.lifecycle.v120alpha02)
    implementation (libs.androidx.camera.view)

    //viewPager Indecator
    implementation(libs.dotsindicator)

    implementation (libs.anrwatchdog)

    implementation (libs.simpleratingbar)
    //for dot indicator
    implementation(libs.dotsindicator)

    implementation (libs.infer.annotation)
    implementation (libs.number.picker)
    implementation("com.google.ai.client.generativeai:generativeai:0.9.0")
    implementation ("io.noties.markwon:core:4.6.2")

    implementation (libs.horizontalcalendarview)
    implementation(libs.circularprogressbar)
    //prime date and calender picker
    implementation(libs.primedatepicker)
    implementation(libs.primecalendar)
    //Desugring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Graph
    implementation (libs.mpandroidchart)

    // moshi for parsing the JSON format
    val moshiVersion = "1.12.0"
    implementation("com.squareup.moshi:moshi:$moshiVersion")
    implementation("com.squareup.moshi:moshi-kotlin:$moshiVersion")
    kapt("com.squareup.moshi:moshi-kotlin-codegen:$moshiVersion")
    implementation ("com.squareup.moshi:moshi-adapters:1.10.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")

    implementation ("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    // flex box
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    //numberpicker
    implementation ("io.github.ShawnLin013:number-picker:2.4.13")

    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")

    implementation ("androidx.hilt:hilt-work:1.2.0")
    implementation ("androidx.work:work-runtime-ktx:2.9.0")

    implementation ("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    implementation ("androidx.media3:media3-exoplayer:1.4.1")
    implementation ("androidx.media3:media3-exoplayer-dash:1.4.1")
    implementation ("androidx.media3:media3-ui:1.4.1")
}