// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
//    // 添加 KSP 版本（示例：KSP 1.0.16 + Kotlin 1.9.20）
//    id("com.google.devtools.ksp") version "1.0.16-1.9.20"
//    id("com.google.dagger.hilt.android")
}