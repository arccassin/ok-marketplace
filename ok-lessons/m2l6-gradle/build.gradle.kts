import org.jetbrains.kotlin.gradle.internal.ensureParentDirsCreated

plugins {
    alias(libs.plugins.jvm)
}

repositories {
    mavenCentral()
}

tasks {
    // Таска для создания файла
    val myCustomTask by creating {
        group = "my group"
        val dir = layout.buildDirectory.dir("my-in")
        outputs.dir(dir)

        doFirst {
            val fileContent = """
                package my.x
                
                const val MY_VERSION: String = "${project.version}"
            """.trimIndent()
            dir
                .get()
                .file("my-version.kt")
                .asFile
                .apply {
                    ensureParentDirsCreated()
                    writeText(fileContent)
                }
        }
    }

    val myCopyTask by creating(Copy::class) {
        dependsOn(myCustomTask)

        group = "my group"
        from(myCustomTask.outputs)
        into(layout.buildDirectory.dir("tmp"))
    }

    compileKotlin {
        println(layout.projectDirectory.dir("src/jvmMain/kotlin"))
        source(layout.buildDirectory.dir("my-in"), layout.projectDirectory.dir("src/jvmMain/kotlin"))
        dependsOn(myCopyTask)
    }
}

tasks {
    create("myTask") {
        println("Configuration stage")
        doFirst { println("At task starts") }
        doLast { println("At task ends") }
    }
}

afterEvaluate {
    tasks {
        create("myOtherTask") {
            println("After other tasks initialized")
        }

        forEach { println("TASK $it") }
    }
}

