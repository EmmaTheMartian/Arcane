@file:Suppress("UnstableApiUsage")

import java.time.format.DateTimeFormatter

plugins {
    id("maven-publish")
    id("dev.architectury.loom") version "1.4-SNAPSHOT"
}

object Properties {
    const val MC_VERSION = "1.20.1"
    const val FORGE_VERSION = "47.2.20"
    const val JEI_VERSION = "15.2.0.27"
    const val PARCHMENT_VERSION = "1.20.1:2023.09.03"
    const val EMI_VERSION = "1.1.0+1.20.1"

    const val MOD_VERSION = "1.0.0"
    const val MOD_LICENSE = "MIT"
    const val MOD_ID = "arcane"
    const val MOD_AUTHORS = "EmmaTheMartian"
    const val MOD_NAME = "Arcane"
    const val MOD_DESC = "Harness aura for magical automation!"

    const val MAVEN_GROUP = "martian.arcane"
    const val ARCHIVES_BASE_NAME = "arcane"
}

base.archivesName = Properties.ARCHIVES_BASE_NAME

version = Properties.MOD_VERSION
group = Properties.MAVEN_GROUP

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
    withSourcesJar()
}

loom {
    runs {
        register("data") {
            data()
            programArgs("--all", "--mod", Properties.MOD_ID,
                    "--output", file("src/generated/resources/").absolutePath,
                    "--existing", file("src/main/resources/").absolutePath)
        }
    }
}

sourceSets {
    main {
        resources {
            srcDir("src/generated/resources")
            exclude("*.cache/")
        }
    }
}

repositories {
    // Parchment
    maven("https://maven.parchmentmc.org") { name = "ParchmentMC" }
    // JEI
    maven("https://maven.blamejared.com/") { name = "Jared's maven" }
    // JEI Mirror (backup)
    maven("https://modmaven.dev") { name = "ModMaven" }
    // EMI
    maven("https://maven.terraformersmc.com") { name = "TerraformersMC" }
}

dependencies {
    minecraft("com.mojang:minecraft:${Properties.MC_VERSION}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${Properties.PARCHMENT_VERSION}@zip")
    })

    forge("net.minecraftforge:forge:${Properties.MC_VERSION}-${Properties.FORGE_VERSION}")

    // JEI
    modCompileOnlyApi("mezz.jei:jei-${Properties.MC_VERSION}-common-api:${Properties.JEI_VERSION}") { isTransitive = false }
    modCompileOnlyApi("mezz.jei:jei-${Properties.MC_VERSION}-forge-api:${Properties.JEI_VERSION}") { isTransitive = false }
    modRuntimeOnly("mezz.jei:jei-${Properties.MC_VERSION}-forge:${Properties.JEI_VERSION}") { isTransitive = false }

    // EMI
    modCompileOnly("dev.emi:emi-forge:${Properties.EMI_VERSION}:api")
    modRuntimeOnly("dev.emi:emi-forge:${Properties.EMI_VERSION}")
}

tasks.processResources {
    inputs.property("mod_version", Properties.MOD_VERSION)
    inputs.property("mod_license", Properties.MOD_LICENSE)
    inputs.property("mod_name", Properties.MOD_NAME)
    inputs.property("mod_authors", Properties.MOD_AUTHORS)
    inputs.property("mod_description", Properties.MOD_DESC)
    inputs.property("mod_id", Properties.MOD_ID)

    filesMatching("META-INF/mods.toml") {
        expand("mod_version" to Properties.MOD_VERSION,
                "mod_license" to Properties.MOD_LICENSE,
                "mod_name" to Properties.MOD_NAME,
                "mod_authors" to Properties.MOD_AUTHORS,
                "mod_description" to Properties.MOD_DESC,
                "mod_id" to Properties.MOD_ID)
    }

    filesMatching("pack.mcmeta") {
        expand("mod_id" to Properties.MOD_ID)
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            "Specification-Title" to Properties.MOD_ID,
            "Specification-Vendor" to Properties.MOD_AUTHORS,
            "Specification-Version" to "1",
            "Implementation-Title" to Properties.MOD_NAME,
            "Implementation-Version" to Properties.MOD_VERSION,
            "Implementation-Vendor" to Properties.MOD_AUTHORS,
            "Implementation-Timestamp" to DateTimeFormatter.ISO_DATE_TIME
        )
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])
        }
    }

    repositories {
    }
}

tasks.register("idePostSync")
