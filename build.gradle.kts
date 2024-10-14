plugins {
    application
    id("org.jetbrains.kotlin.jvm") version "2.0.20"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "2.24.4"
}

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

javafx {
    version = "21"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.graphics")
}

application {
    mainClass.set("purrinha.Main")
    mainModule.set("purrinha")
}

dependencies {
    implementation("org.openjfx:javafx-controls:21")
    implementation("org.openjfx:javafx-fxml:21")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
}

tasks.register("createWindows") {
    group = "packaging"
    dependsOn("jpackage")
    doFirst {
        if (!org.gradle.internal.os.OperatingSystem.current().isWindows) {
            throw GradleException("This task can only be run on Windows")
        }
    }
}

tasks.register("createMac") {
    group = "packaging"
    dependsOn("jpackage")
    doFirst {
        if (!org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            throw GradleException("This task can only be run on macOS")
        }
    }
}

tasks.register("cleanSource") {
    doLast {
        delete(fileTree("build/image") {
            include("**/*.kt")
            include("**/*.java")
            include("**/*.kotlin_metadata")
        })
    }
}

jlink {
    imageDir.set(file("${buildDir}/image/purrinha"))
    options.set(listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "purrinha"
        jvmArgs = listOf()
    }

    jpackage {
        imageName = "purrinha"
        skipInstaller = false
        installerName = "purrinha-Installer"

        // Define icon paths relative to project root
        val iconPath = project.projectDir.resolve("src/main/resources")
        val macIconPath = iconPath.resolve("purrinha.icns").absolutePath
        val winIconPath = iconPath.resolve("purrinha.ico").absolutePath

        if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            installerType = "dmg"
            imageOptions = listOf(
                "--mac-package-name", "purrinha",
                "--icon", macIconPath,
                "--verbose"
            )
            installerOptions = listOf(
                "--mac-package-identifier", "purrinha",
                "--mac-package-name", "purrinha",
                "--vendor", "SistersProject",
                "--copyright", "Copyright 2024 by Sheila Vitor de Souza and Aline Sugisaki Silva",
                "--dest", "build/installer",
                "--icon", macIconPath
            )
        } else if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            installerType = "msi"
            imageOptions = listOf(
                "--win-console",
                "--icon", winIconPath
            )
            installerOptions = listOf(
                "--win-dir-chooser",
                "--win-menu",
                "--win-shortcut",
                "--win-per-user-install",
                "--win-menu-group", "purrinha",
                "--vendor", "SistersProject",
                "--copyright", "Copyright 2024 by Sheila Vitor de Souza and Aline Sugisaki Silva",
                "--dest", "build/installer",
                "--icon", winIconPath
            )
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "17"
    }
}

tasks.register("cleanupAfterPackage") {
    dependsOn("jpackage")
    doLast {
        delete(fileTree("build") {
            include("**/*.kt")
            include("**/*.java")
            include("**/*.kotlin_metadata")
            include("**/META-INF/*.kotlin_module")
        })
    }
}