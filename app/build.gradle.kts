plugins {
    id("com.android.application")
}

android {
    compileSdk = 34

    namespace = "com.example.appfood"

    defaultConfig {
        applicationId = "com.example.appfood"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        named("release") {
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
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.activity:activity:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation ("com.google.android.gms:play-services-auth:20.1.0")
    implementation ("com.github.bumptech.glide:glide:4.15.1")
    implementation ("org.json:json:20210307")

    implementation ("com.google.android.material:material:1.10.0")
    // Thêm để gắn token vào header
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation ("com.squareup.okhttp3:okhttp:4.10.0" )


    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    // Gson converter for Retrofit
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for networking
    implementation ("com.squareup.okhttp3:okhttp:4.10.0")

    annotationProcessor ("com.github.bumptech.glide:compiler:4.15.1")

}
