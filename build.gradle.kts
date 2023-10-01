// Top-level build file where you can add configuration options common to all sub-projects/modules.
@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed

buildscript{
    dependencies{
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.8.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.46.1")
    }
}

plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.kotlinAndroid) apply false
}
true // Needed to make the Suppress annotation work for the plugins block