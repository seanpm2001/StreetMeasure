import java.io.FileInputStream
import java.util.Properties

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    namespace = "de.westnordost.streetmeasure"
    compileSdk = 33

    signingConfigs {
        create("release") {
        }
    }

    defaultConfig {
        applicationId = "de.westnordost.streetmeasure"
        minSdk = 24
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    val kotlinxCoroutinesVersion = "1.6.4"

    // core android stuff
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
    // coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$kotlinxCoroutinesVersion")

    // measuring distance with AR
    implementation("com.google.ar:core:1.35.0")
    implementation("com.google.ar.sceneform:core:1.17.1")
}

val keystorePropertiesFile = rootProject.file("keystore.properties")
if (keystorePropertiesFile.exists()) {
    val props = Properties()
    props.load(FileInputStream(keystorePropertiesFile))
    val releaseSigningConfig = android.signingConfigs.getByName("release")
    releaseSigningConfig.storeFile = file(props.getProperty("storeFile"))
    releaseSigningConfig.storePassword = props.getProperty("storePassword")
    releaseSigningConfig.keyAlias = props.getProperty("keyAlias")
    releaseSigningConfig.keyPassword = props.getProperty("keyPassword")
}

tasks.register<UpdateAppTranslationsTask>("updateTranslations") {
    group = "streetmeasure"
    apiToken = properties["POEditorAPIToken"] as String
    projectId = "97843"
    targetFiles = { "$projectDir/src/main/res/values${if (it.isNotEmpty()) "-$it" else ""}/strings.xml" }
    strings = setOf(
        "ar_core_error_sdk_too_old",
        "ar_core_tracking_error_bad_state",
        "ar_core_tracking_error_insufficient_light",
        "ar_core_tracking_error_excessive_motion",
        "ar_core_tracking_error_insufficient_features",
        "ar_core_tracking_error_camera_unavailable",
        "ar_core_tracking_error_too_steep_angle",
        "ar_core_tracking_error_no_plane_hit",
        "ar_core_tracking_hint_tap_to_measure",
        "no_camera_permission_warning_title",
        "no_camera_permission_warning",
        "no_camera_permission_toast",
        "privacy_html_arcore",
        "ar_measure_horizontal",
        "ar_measure_vertical",
        "ar_measure_meters",
        "ar_measure_ft_in",
    )
}