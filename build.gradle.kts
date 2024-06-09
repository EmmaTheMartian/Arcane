@file:Suppress("UnstableApiUsage")

import java.time.format.DateTimeFormatter

plugins {
    id("maven-publish")
    id("dev.architectury.loom") version "1.6-SNAPSHOT"
}

fun prop(id: String): String = property(id) as String

base.archivesName = prop("archives_base_name")
version = prop("mod_version")
group = prop("maven_group")

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
    withSourcesJar()
}

loom {
    runs {
        all {
            vmArg("-XX:+AllowEnhancedClassRedefinition")
        }

        register("data") {
            data()
            programArgs("--all", "--mod", prop("mod_id"),
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
    maven("https://maven.neoforged.net")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.blamejared.com/") // JEI
    maven("https://modmaven.dev") // JEI Mirror
    maven("https://maven.terraformersmc.com") // EMI
    maven("https://maven.tterrag.com/") // Create
    maven("https://maven.firstdarkdev.xyz/snapshots") // LDLib and Photon
    maven("https://maven.theillusivec4.top/") // Curios

    // Jade and ProbeJS
    maven("https://www.cursemaven.com") {
        mavenContent {
            includeGroup("curse.maven")
        }
    }

    // Modonomicon
    maven("https://dl.cloudsmith.io/public/klikli-dev/mods/maven/") {
        mavenContent {
            includeGroup("com.klikli_dev")
        }
    }

    // KubeJS and Rhino
    maven("https://maven.saps.dev/minecraft") {
        mavenContent {
            includeGroup("dev.latvian.mods")
        }
    }

    // GeckoLib
//    maven("https://dl.cloudsmith.io/public/geckolib3/geckolib/maven/") {
//        mavenContent {
//            includeGroupByRegex("software\\.bernie.*")
//            includeGroup("com.eliotlash.mclib")
//        }
//    }
}

dependencies {
    minecraft("com.mojang:minecraft:${prop("mc_version")}")

    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${prop("parchment_version")}@zip")
    })

    neoForge("net.neoforged:neoforge:${prop("neoforge_version")}")

    // JEI
    /*
    modCompileOnlyApi("mezz.jei:jei-${prop("mc_version")}-common-api:${prop("jei_version")}") { isTransitive = false }
    modCompileOnlyApi("mezz.jei:jei-${prop("mc_version")}-forge-api:${prop("jei_version")}") { isTransitive = false }
    modRuntimeOnly("mezz.jei:jei-${prop("mc_version")}-forge:${prop("jei_version")}") { isTransitive = false }
     */

    // EMI
    modCompileOnly("dev.emi:emi-neoforge:${prop("emi_version")}+${prop("mc_version")}:api")
    modRuntimeOnly("dev.emi:emi-neoforge:${prop("emi_version")}+${prop("mc_version")}")

    // Jade
    modCompileOnly("curse.maven:jade-324717:${prop("jade_version")}")
    modRuntimeOnly("curse.maven:jade-324717:${prop("jade_version")}")

    // LDLib and Photon
    /*
    val ldlib = "com.lowdragmc.ldlib:ldlib-forge-${prop("mc_version")}:${prop("ldlib_version")}"
    val photon = "com.lowdragmc.photon:photon-forge-${prop("mc_version")}:${prop("photon_version")}"
    modCompileOnly(ldlib) { isTransitive = false }
    modCompileOnly(photon) { isTransitive = false }
    if (prop("enable_photon").toBoolean()) {
        modRuntimeOnly(ldlib) { isTransitive = false }
        modRuntimeOnly(photon) { isTransitive = false }
    }
    */

    // Curios
    modCompileOnly("top.theillusivec4.curios:curios-neoforge:${prop("curios_version")}+${prop("mc_version")}:api")
    if (prop("enable_curios").toBoolean()) {
        modRuntimeOnly("top.theillusivec4.curios:curios-neoforge:${prop("curios_version")}+${prop("mc_version")}")
    }

    // Modonomicon
    forgeRuntimeLibrary("org.commonmark:commonmark:0.22.0")
    forgeRuntimeLibrary("org.commonmark:commonmark-ext-gfm-strikethrough:0.22.0")
    forgeRuntimeLibrary("org.commonmark:commonmark-ext-ins:0.22.0")
    modCompileOnly("com.klikli_dev:modonomicon-${prop("mc_version")}-common:${prop("modonomicon_version")}")
    modImplementation("com.klikli_dev:modonomicon-${prop("mc_version")}-neoforge:${prop("modonomicon_version")}") { isTransitive = false }

    // Create
    /*
    if (prop("enable_create").toBoolean()) {
        modRuntimeOnly("com.simibubi.create:create-${prop("mc_version")}:${prop("create_version")}:slim") {
            isTransitive = false
        }
        modRuntimeOnly("com.jozufozu.flywheel:flywheel-forge-${prop("mc_version")}:${prop("flywheel_version")}")
        modRuntimeOnly("com.tterrag.registrate:Registrate:${prop("registrate_version")}")
    }

    // KubeJS
    modImplementation("dev.latvian.mods:kubejs-forge:${prop("kubejs_version")}")
    localRuntime("io.github.llamalad7:mixinextras-forge:0.3.5")
    if (prop("enable_probejs").toBoolean()) {
        modRuntimeOnly("curse.maven:probejs-585406:${prop("probejs_version")}")
    }
     */

    // GeckoLib
//    modImplementation("software.bernie.geckolib:geckolib-forge-${prop("mc_version")}:${prop("geckolib_version")}")
}

tasks.withType<ProcessResources>().configureEach {
    val props = mutableMapOf(
        "mod_id" to prop("mod_id"),
        "mod_license" to prop("mod_license"),
        "mod_version" to prop("mod_version"),
        "mod_name" to prop("mod_name"),
        "mod_authors" to prop("mod_authors"),
        "neoforge_version" to prop("neoforge_version"),
        "minecraft_version" to prop("mc_version"),
    )

    println("processResources properties:")
    println(props)

    inputs.properties(props)

    filesMatching(listOf("META-INF/neoforge.mods.toml", "pack.mcmeta")) {
        expand(props)
    }
}

tasks.withType(JavaCompile::class) {
    options.encoding = "UTF-8"
}

tasks.jar {
    manifest {
        attributes(
            "Specification-Title" to prop("mod_id"),
            "Specification-Vendor" to prop("mod_authors"),
            "Specification-Version" to "1",
            "Implementation-Title" to prop("mod_name"),
            "Implementation-Version" to prop("mod_version"),
            "Implementation-Vendor" to prop("mod_authors"),
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
