plugins {
    id("java")
    id("io.freefair.lombok") version "6.6.1"
    id("maven-publish")
    id("signing")
}

group = "dev.jcputney.elearning"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.commons:commons-configuration2:2.11.0")
    implementation("org.json:json:20240303")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.18.1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.18.1")
    implementation("org.projectlombok:lombok:1.18.36")
    implementation("jakarta.xml.bind:jakarta.xml.bind-api:4.0.2")
    implementation("org.glassfish.jaxb:jaxb-runtime:4.0.5")

    // Optional AWS SDK v1
    compileOnly("com.amazonaws:aws-java-sdk-s3:1.12.778")

    // Optional AWS SDK v2
    compileOnly("software.amazon.awssdk:s3:2.29.15")
    compileOnly("software.amazon.awssdk:auth:2.29.15")
    compileOnly("software.amazon.awssdk:core:2.29.15")
    compileOnly("software.amazon.awssdk:aws-core:2.29.15")

    annotationProcessor("org.projectlombok:lombok:1.18.34")

    testImplementation("org.projectlombok:lombok:1.18.34")
    testAnnotationProcessor("org.projectlombok:lombok:1.18.34")
    testImplementation(platform("org.junit:junit-bom:5.11.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.mockito:mockito-core:5.14.2")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.annotationProcessorPath = configurations.annotationProcessor.get()
}

tasks.named<Javadoc>("javadoc") {
    // Include all sources
    source = sourceSets["main"].allJava

    // Add the classpath with Lombok annotation processing
    classpath = sourceSets["main"].compileClasspath

    options {
        encoding = "UTF-8"
        (this as StandardJavadocDocletOptions).apply {
            addStringOption("Xdoclint:none", "-quiet") // Suppress warnings
            links("https://docs.oracle.com/en/java/javase/17/docs/api/")
        }
    }
}

tasks.register<Jar>("javadocJar") {
    dependsOn(tasks.named("javadoc"))
    archiveClassifier.set("javadoc")
    from(tasks.named<Javadoc>("javadoc").get().destinationDir)
}

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
            artifact(tasks.named<Jar>("javadocJar").get())
            groupId = project.group.toString()
            version = project.version.toString()
            artifactId = "elearning-module-parser"

            pom {
                name.set("elearning-module-parser")
                description.set("A library for parsing eLearning module configuration files and packages")
                url.set("https://github.com/jcputney/elearning-module-parser")

                developers {
                    developer {
                        id.set("jcputney")
                        name.set("Jonathan Putney")
                        email.set("jonathan@putney.io")
                        organization.set("dev.jcputney")
                        organizationUrl.set("https://github.com/jcputney")
                    }
                }

                scm {
                    connection.set("scm:git:git://github.com/jcputney/elearning-module-parser.git")
                    developerConnection.set("scm:git:git://github.com/jcputney/elearning-module-parser.git")
                    url.set("https://github.com/jcputney/elearning-module-parser")
                }
            }
        }
    }

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/jcputney/elearning-module-parser")
            credentials {
                username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
                password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
            }
        }
        maven {
            name = "MavenCentral"
            url = uri("https://oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = project.findProperty("ossrhUsername") as String?
                        ?: System.getenv("OSSRH_USERNAME")
                password = project.findProperty("ossrhPassword") as String?
                        ?: System.getenv("OSSRH_PASSWORD")
            }
        }
    }
}

signing {
    sign(publishing.publications["mavenJava"])
}
