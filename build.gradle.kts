@file:Suppress("UnstableApiUsage")

import java.time.format.DateTimeFormatter

plugins {
    id("maven-publish")
    id("dev.architectury.loom") version "1.4-SNAPSHOT"
}

object Properties {
    const val MC_VERSION = "1.20.1"
    const val FORGE_VERSION = "47.2.20"
    const val PARCHMENT_VERSION = "1.20.1:2023.09.03"
    const val JEI_VERSION = "15.2.0.27"
    const val EMI_VERSION = "1.1.0"
    const val JADE_VERSION = "5072729" // 1.20.1-forge-11.8.0
    const val LDLIB_VERSION = "1.0.24.a"
    const val PHOTON_VERSION = "1.0.7.a"
    const val CURIOS_VERSION = "5.7.0"
    const val MODONOMICON_VERSION = "1.59.0"
    const val CREATE_VERSION = "0.5.1.e-22"
    const val FLYWHEEL_VERSION = "0.6.10-7"
    const val REGISTRATE_VERSION = "MC1.20-1.3.3"
    const val KUBEJS_VERSION = "2001.6.5-build.2"
    const val PROBEJS_VERSION = "5054324" // 5.9.3-forge

    const val ENABLE_PHOTON = true
    const val ENABLE_CURIOS = true
    const val ENABLE_CREATE = true
    const val ENABLE_PROBEJS = true

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
        all {
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }

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

    // Jade and ProbeJS
    maven("https://www.cursemaven.com") {
        mavenContent {
            includeGroup("curse.maven")
        }
    }

    // LDLib and Photon
    maven("https://maven.firstdarkdev.xyz/snapshots")

    // Curios
    maven("https://maven.theillusivec4.top/") { name = "Illusive Soulworks maven" }

    // Modonomicon
    maven("https://dl.cloudsmith.io/public/klikli-dev/mods/maven/") {
        mavenContent {
            includeGroup("com.klikli_dev")
        }
    }

    // Create
    maven("https://maven.tterrag.com/") { name = "tterrag maven" }

    // KubeJS and Rhino
    maven("https://maven.saps.dev/minecraft") {
        mavenContent {
            includeGroup("dev.latvian.mods")
        }
    }
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
    modCompileOnly("dev.emi:emi-forge:${Properties.EMI_VERSION}+${Properties.MC_VERSION}:api")
    modRuntimeOnly("dev.emi:emi-forge:${Properties.EMI_VERSION}+${Properties.MC_VERSION}")

    // Jade
    modCompileOnly("curse.maven:jade-324717:${Properties.JADE_VERSION}")
    modRuntimeOnly("curse.maven:jade-324717:${Properties.JADE_VERSION}")

    // LDLib and Photon
    val ldlib = "com.lowdragmc.ldlib:ldlib-forge-${Properties.MC_VERSION}:${Properties.LDLIB_VERSION}"
    val photon = "com.lowdragmc.photon:photon-forge-${Properties.MC_VERSION}:${Properties.PHOTON_VERSION}"
    modCompileOnly(ldlib) { isTransitive = false }
    modCompileOnly(photon) { isTransitive = false }
    if (Properties.ENABLE_PHOTON) {
        modRuntimeOnly(ldlib) { isTransitive = false }
        modRuntimeOnly(photon) { isTransitive = false }
    }

    // Curios
    modCompileOnly("top.theillusivec4.curios:curios-forge:${Properties.CURIOS_VERSION}+${Properties.MC_VERSION}:api")
    if (Properties.ENABLE_CURIOS) {
        modRuntimeOnly("top.theillusivec4.curios:curios-forge:${Properties.CURIOS_VERSION}+${Properties.MC_VERSION}")
    }

    // Modonomicon
    forgeRuntimeLibrary("org.commonmark:commonmark:0.21.0")
    forgeRuntimeLibrary("org.commonmark:commonmark-ext-gfm-strikethrough:0.21.0")
    forgeRuntimeLibrary("org.commonmark:commonmark-ext-ins:0.21.0")
    modCompileOnly("com.klikli_dev:modonomicon-${Properties.MC_VERSION}-common:${Properties.MODONOMICON_VERSION}")
    modImplementation("com.klikli_dev:modonomicon-${Properties.MC_VERSION}-forge:${Properties.MODONOMICON_VERSION}") { isTransitive = false }

    // Create
    if (Properties.ENABLE_CREATE) {
        modRuntimeOnly("com.simibubi.create:create-${Properties.MC_VERSION}:${Properties.CREATE_VERSION}:slim") {
            isTransitive = false
        }
        modRuntimeOnly("com.jozufozu.flywheel:flywheel-forge-${Properties.MC_VERSION}:${Properties.FLYWHEEL_VERSION}")
        modRuntimeOnly("com.tterrag.registrate:Registrate:${Properties.REGISTRATE_VERSION}")
    }

    // KubeJS
    modImplementation("dev.latvian.mods:kubejs-forge:${Properties.KUBEJS_VERSION}")
    localRuntime("io.github.llamalad7:mixinextras-forge:0.3.5")
    if (Properties.ENABLE_PROBEJS) {
        modRuntimeOnly("curse.maven:probejs-585406:${Properties.PROBEJS_VERSION}")
    }
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
