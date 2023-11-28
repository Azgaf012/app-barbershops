plugins {
    id("com.android.application")
    id("com.google.gms.google-services")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}



android {
    namespace = "com.dapm.barbershop"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.dapm.barbershop"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
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

}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.fragment:fragment-ktx:1.6.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.9.10")
    implementation("org.ow2.asm:asm:9.3")

    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation(platform("com.google.firebase:firebase-bom:32.3.1"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx:24.9.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")
    implementation ("com.google.dagger:hilt-android:2.48")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")

    implementation("com.github.prolificinteractive:material-calendarview:2.0.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")

    kapt ("com.google.dagger:hilt-compiler:2.48")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

kapt {
    correctErrorTypes = true
}