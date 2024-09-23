plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android.plugin)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.yk.mobile.advanced3dfilemanager"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.yk.mobile.advanced3dfilemanager"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //------------------ neumorphism------------------
    api(libs.neumorphism.lib)
    implementation(libs.ssp.lib)
    implementation(libs.sdp.lib)
    implementation(libs.dagger.hilt.android)
    kapt(libs.dagger.compiler)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.android.spinkit)
    implementation(libs.mp.android.chart)

    implementation(libs.glide)
    kapt(libs.glide.compiler)

    implementation("androidx.preference:preference:1.1.1")
    implementation("androidx.security:security-crypto:1.1.0-alpha03")


}