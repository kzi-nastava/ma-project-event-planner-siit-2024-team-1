import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
}

fun getIpAddress(): String {
    val properties = Properties()
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        file.inputStream().use { properties.load(it) }
    }
    return properties.getProperty("ip_addr", "default_ip") // Replace "default_ip" with a fallback value if needed
}

android {
    namespace = "com.example.EventPlanner"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.EventPlanner"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        buildConfigField("String", "IP_ADDR", "\"${getIpAddress()}\"")
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        multiDexEnabled = true
    }

    buildFeatures{
        viewBinding = true
        buildFeatures.dataBinding = true
        buildConfig = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            buildConfigField("String", "IP_ADDR", "\"${getIpAddress()}\"")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.recyclerview)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation ("com.squareup:seismic:1.0.3")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:3.12.1")
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation ("com.fasterxml.jackson.core:jackson-databind:2.15.2")
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("com.itextpdf:itext7-core:7.2.3")
    // StompProtocolAndroid for WebSocket
    implementation ("com.github.NaikSoftware:StompProtocolAndroid:1.6.6")

    // RxJava dependencies
    implementation ("io.reactivex.rxjava2:rxjava:2.2.21")
    implementation ("io.reactivex.rxjava2:rxandroid:2.1.1")

    // OkHttp for network connections
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")
    implementation ("org.osmdroid:osmdroid-android:6.1.20")
}