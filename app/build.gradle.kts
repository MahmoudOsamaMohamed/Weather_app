plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.kapt")
}

android {
    namespace = "com.mahmoud.weatherapp"
    compileSdk = 34
    //viewBinding = true
    buildFeatures {
        viewBinding = true
    }


    defaultConfig {
        applicationId = "com.mahmoud.weatherapp"
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
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    implementation ("androidx.work:work-runtime-ktx:2.7.1")
    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    //gson
    implementation("com.google.code.gson:gson:2.10.1")
    //mvvm
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")
    //room
    implementation ("androidx.room:room-ktx:2.6.1")
    implementation ("androidx.room:room-runtime:2.6.1")
    kapt ( "androidx.room:room-compiler:2.6.1" )
    //glide
    implementation("com.github.bumptech.glide:glide:4.14.2")
    kapt("com.github.bumptech.glide:compiler:4.14.2")
    //navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    //lottie
    implementation("com.airbnb.android:lottie:6.4.0")
    //data store
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    // google play servives

    implementation("com.google.android.gms:play-services-location:21.1.0")


    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // Dependencies for local unit tests
    val junitVersion = "4.13.2"
    testImplementation ("junit:junit:$junitVersion")
    val hamcrestVersion= "1.3"
    testImplementation ("org.hamcrest:hamcrest-all:$hamcrestVersion")
    val archTestingVersion= "2.1.0"
    testImplementation ("androidx.arch.core:core-testing:$archTestingVersion")
    val robolectricVersion = "4.5.1"
    testImplementation ("org.robolectric:robolectric:$robolectricVersion")

    // AndroidX Test - JVM testing
    val androidXTestCoreVersion = "1.4.0"
    testImplementation ("androidx.test:core-ktx:$androidXTestCoreVersion")
    //testImplementation "androidx.test.ext:junit:$androidXTestExtKotlinRunnerVersion"

    // AndroidX Test - Instrumented testing
    val androidXTestExtKotlinRunnerVersion = "1.1.3"

    val espressoVersion = "3.4.0"
    val androidXTestRulesVersion = "1.2.0"
    androidTestImplementation ("androidx.test:runner:$androidXTestExtKotlinRunnerVersion")
    androidTestImplementation ("androidx.test:rules:$androidXTestRulesVersion")
    androidTestImplementation ("androidx.test.espresso:espresso-core:$espressoVersion")

    //Timber
    implementation( "com.jakewharton.timber:timber:5.0.1")

    // hamcrest
    testImplementation ("org.hamcrest:hamcrest:2.2")
    testImplementation( "org.hamcrest:hamcrest-library:2.2")
    androidTestImplementation( "org.hamcrest:hamcrest:2.2")
    androidTestImplementation ("org.hamcrest:hamcrest-library:2.2")


    // AndroidX and Robolectric
    testImplementation ("androidx.test.ext:junit-ktx:$androidXTestExtKotlinRunnerVersion")
    testImplementation ("androidx.test:core-ktx:$androidXTestCoreVersion")
    testImplementation ("org.robolectric:robolectric:4.8")

    // InstantTaskExecutorRule
    testImplementation ("androidx.arch.core:core-testing:2.1.0")
    androidTestImplementation ("androidx.arch.core:core-testing:2.1.0")
    //kotlinx-coroutines
    val coroutinesVersion = "1.6.4"
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion")
    testImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")
    androidTestImplementation ("org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion")


}
//safe args
apply(plugin = "androidx.navigation.safeargs.kotlin")