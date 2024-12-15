
buildscript {
    dependencies {
        classpath(libs.realm.kotlin.gradle.plugin)
        classpath(libs.gradle.plugin.v210)
    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.dagger.hilt) apply false
    alias(libs.plugins.google.services) apply false
    alias(libs.plugins.google.ksp) apply false
    alias(libs.plugins.realm) apply false
    alias(libs.plugins.jetbrains.kotlin.compose) apply false
}