// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    id("com.google.gms.google-services") version "4.4.2" apply false // Keep this to apply the google services plugin in your app module
}

buildscript {
    dependencies {
        // Ensure this is present in the buildscript dependencies
        classpath("com.google.gms:google-services:4.4.2")
    }
}
