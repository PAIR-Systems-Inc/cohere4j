plugins {
    `java-library`
    `maven-publish`
    id("org.openapi.generator") version "7.19.0"
}

group = "com.github.PAIR-Systems-Inc"
version = "v0.2.0"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
    withSourcesJar()
    // withJavadocJar() // Disabled due to invalid javadoc tags in generated code
}

repositories {
    mavenCentral()
}

dependencyLocking {
    lockAllConfigurations()
}

dependencies {
    // OkHttp and Gson
    implementation("com.squareup.okhttp3:okhttp:5.3.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.3.2")
    implementation("com.google.code.gson:gson:2.13.2")
    implementation("io.gsonfire:gson-fire:1.9.0")
    
    // Jakarta WS-RS (for generated JSON class)
    implementation("jakarta.ws.rs:jakarta.ws.rs-api:4.0.0")
    
    // misc
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("jakarta.annotation:jakarta.annotation-api:3.0.0")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.14.2")
    testImplementation("org.mockito:mockito-core:5.21.0")
    testImplementation("org.mockito:mockito-junit-jupiter:5.21.0")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.3.2")
}

tasks.test {
    useJUnitPlatform()
}

openApiGenerate {
    generatorName.set("java")
    library.set("okhttp-gson")
    inputSpec.set("$projectDir/openapi/cohere-openapi.yaml")
    outputDir.set("$buildDir/generated")
    packageName.set("ai.pairsys.cohere4j.client")
    apiPackage.set("ai.pairsys.cohere4j.client.api")
    modelPackage.set("ai.pairsys.cohere4j.client.model")
    configOptions.set(mapOf(
        "dateLibrary" to "java8",
        "useJakartaEe" to "true",
        "hideGenerationTimestamp" to "true",
        "generatePom" to "false",
        "openApiNullable" to "false",
        "legacyDiscriminatorBehavior" to "false",
        "disallowAdditionalPropertiesIfNotPresent" to "false"
    ))
}

sourceSets {
    val main by getting {
        java {
            srcDir("$buildDir/generated/src/main/java")
            exclude("**/auth/OAuth*")
        }
    }
}

val exampleClassesDir = layout.buildDirectory.dir("examples/classes")

val compileExamples by tasks.registering(JavaCompile::class) {
    dependsOn("classes")
    source = fileTree("examples") { include("**/*.java") }
    destinationDirectory.set(exampleClassesDir)
    classpath = files(sourceSets["main"].compileClasspath, sourceSets["main"].output)
    options.release.set(11)
}

fun registerExampleRunTask(taskName: String, mainClassName: String, descriptionText: String) {
    tasks.register<JavaExec>(taskName) {
        group = "examples"
        description = descriptionText
        dependsOn(compileExamples)
        mainClass.set(mainClassName)
        classpath = files(exampleClassesDir) + sourceSets["main"].runtimeClasspath
    }
}

registerExampleRunTask(
    taskName = "runEmbeddingExample",
    mainClassName = "EmbeddingExample",
    descriptionText = "Run the embeddings example using the Cohere client"
)

registerExampleRunTask(
    taskName = "runRerankExample",
    mainClassName = "RerankExample",
    descriptionText = "Run the rerank example using the Cohere client"
)

registerExampleRunTask(
    taskName = "runChatExample",
    mainClassName = "ChatExample",
    descriptionText = "Run the chat example using the Cohere client"
)

tasks.named("sourcesJar") {
    dependsOn("openApiGenerate")
}

tasks.compileJava {
    dependsOn("openApiGenerate")
}

publishing {
    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/PAIR-Systems-Inc/cohere4j")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("GITHUB_ACTOR")
                password = project.findProperty("gpr.key") as String? ?: System.getenv("GITHUB_TOKEN")
            }
        }
    }
    
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
            
            pom {
                name.set("Cohere4J")
                description.set("Java client library for Cohere APIs (embeddings, rerank, chat)")
                url.set("https://github.com/PAIR-Systems-Inc/cohere4j")
                
                licenses {
                    license {
                        name.set("MIT License")
                        url.set("https://opensource.org/licenses/MIT")
                    }
                }
                
                developers {
                    developer {
                        id.set("pair-systems")
                        name.set("PAIR Systems Inc")
                    }
                }
                
                scm {
                    connection.set("scm:git:git://github.com/PAIR-Systems-Inc/cohere4j.git")
                    developerConnection.set("scm:git:ssh://github.com/PAIR-Systems-Inc/cohere4j.git")
                    url.set("https://github.com/PAIR-Systems-Inc/cohere4j")
                }
            }
        }
    }
}
