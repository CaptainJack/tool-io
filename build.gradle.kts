import org.jetbrains.kotlin.cli.common.arguments.K2JsArgumentConstants

plugins {
	kotlin("multiplatform") version "1.3.41"
	id("nebula.release") version "11.1.0"
	id("ru.capjack.bintray") version "0.19.0"
}

group = "ru.capjack.tool"

repositories {
	jcenter()
	maven("https://dl.bintray.com/capjack/public")
}

kotlin {
	jvm {
		compilations.all { kotlinOptions.jvmTarget = "1.8" }
	}
	js {
		browser()
		compilations["main"].kotlinOptions {
			sourceMap = true
			sourceMapEmbedSources = "always"
		}
	}
	
	sourceSets {
		get("commonMain").dependencies {
			implementation(kotlin("stdlib-common"))
			implementation("ru.capjack.tool:tool-lang:0.7.0-snapshot.20190808060334498+f435367")
		}
		
		get("jvmMain").dependencies {
			implementation(kotlin("stdlib-jdk8"))
		}
		
		get("jsMain").dependencies {
			implementation(kotlin("stdlib-js"))
		}
	}
}