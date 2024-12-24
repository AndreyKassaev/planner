plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
}
val mockitoAgent = configurations.create("mockitoAgent")
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-javaagent:${mockitoAgent.asPath}")
}

dependencies {
    //DI - Koin
    // https://mvnrepository.com/artifact/io.insert-koin/koin-core
    implementation(libs.koin.core)

    //Flow
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation(libs.kotlinx.coroutines.core)

    //Test
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-api
    testImplementation(libs.junit.jupiter.api)
    // https://mvnrepository.com/artifact/org.mockito/mockito-core
    testImplementation(libs.mockito.core)
    // https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-test-junit
    testImplementation(libs.kotlin.test.junit)
    // https://mvnrepository.com/artifact/org.junit.jupiter/junit-jupiter-engine
    testImplementation(libs.junit.jupiter.engine)
    mockitoAgent(libs.mockito.core) { isTransitive = false }

    //Coroutines-test
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-test
    implementation(libs.kotlinx.coroutines.test)
}