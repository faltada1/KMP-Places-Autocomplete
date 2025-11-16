plugins {
    id("com.android.library")
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompilerPlugin)
    alias(libs.plugins.kotlinSerialization)
    id("com.github.gmazzo.buildconfig") version "5.4.0"
}

// ---- KOTLIN MULTIPLATFORM ----
kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_1_8)
        }
    }
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "places"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.ui)
            implementation(compose.material3)

            implementation(libs.kotlinx.serialization)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

            implementation("io.ktor:ktor-client-core:${libs.versions.ktor.get()}")
            implementation("io.ktor:ktor-client-serialization:${libs.versions.ktor.get()}")
            implementation("io.ktor:ktor-serialization-kotlinx-json:${libs.versions.ktor.get()}")
            implementation("io.ktor:ktor-client-content-negotiation:${libs.versions.ktor.get()}")
            implementation("io.ktor:ktor-client-logging:${libs.versions.ktor.get()}")
            implementation(libs.androidx.lifecycle.viewmodel.compose)
        }

        androidMain.dependencies {
            implementation("io.ktor:ktor-client-okhttp:${libs.versions.ktor.get()}")
        }

        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:${libs.versions.ktor.get()}")
        }

        commonTest.dependencies {
            implementation(libs.koin.test)
            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.0")
            implementation("io.ktor:ktor-client-mock:${libs.versions.ktor.get()}")
        }
    }
}

// ---- ANDROID ----
android {
    namespace = "com.ngallazzi.places"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
