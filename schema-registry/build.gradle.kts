
 

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    id("com.netflix.dgs.codegen")
    kotlin("plugin.spring")
    java
    id("com.bmuschko.docker-spring-boot-application") version "6.7.0"
}

group = "com.pranetr"
version = "0.0.1"
java.sourceCompatibility = JavaVersion.VERSION_18
val javaversion = "18"

repositories {
    mavenCentral()
}

configurations {
    all {
        exclude(group = "org.springframework.boot", module = "spring-boot-starter-logging")
    }
}
dependencies {
    api(project(":foundation"))

    api("com.netflix.graphql.dgs.codegen:graphql-dgs-codegen-gradle:5.6.2")
    api("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}


tasks.bootJar{
    enabled = false
}
tasks.jar{
    enabled = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "$javaversion"
    }
}
tasks.withType<Test> {
    useJUnitPlatform()
}

val projectList = mutableListOf<String>("auth","store","catalog","inventory","price","promotion","order")

tasks.withType<GenerateFilesTask> {
//    schemaFiles= mutableListOf<String>(
//        "catalog-schema.graphqls",
//        "inventory-schema.graphqls",
//        "price-schema.graphqls")
//    packageNames = mutableListOf<String>(
//        "com.pranetr.catalog.codegen",
//        "com.pranetr.inventory.codegen",
//        "com.pranetr.price.codegen")

    val catalog = arrayListOf<String>("attributes")
    val order = arrayListOf<String>("cart","discounts")
    val promotion = arrayListOf<String>("cart","discounts")

    dependencies=mapOf(Pair("catalog",catalog),Pair("order",order),Pair("promotion",promotion))

    projects = projectList
    generateClient = true
    generateDataTypes = true
    generateInterfaces = false
}
tasks.register<GenerateFilesTask>("generateSchemas")
tasks.generateJava{
    enabled = false
}
sourceSets {
    main {
        java {
            projectList.forEach {
                srcDir("build/$it")
            }
        }
    }
}

open class GenerateFilesTask  @javax.inject.Inject constructor()  : DefaultTask() {


    private val LOGGER = org.slf4j.LoggerFactory.getLogger("DgsCodegenPlugin")

    @Input
    var generatedSourcesDir: String = project.buildDir.absolutePath

    @Input
    var  dependencies = mapOf<String,ArrayList<String>>()
    @Input
    var schemaFiles = mutableListOf<String>()


    @Input
    var packageNames = mutableListOf<String>()

    @Input
    var projects = mutableListOf<String>("project")


    @Input
    var subPackageNameClient = "client"

    @Input
    var subPackageNameDatafetchers = "datafetchers"

    @Input
    var subPackageNameTypes = "types"


    @Input
    var typeMapping = mutableMapOf<String, String>()

    @Input
    var generateBoxedTypes = false

    @Input
    var generateClient = false

    @Input
    var generateDataTypes = true

    @Input
    var generateInterfaces = false

    @OutputDirectory
    fun getExampleOutputDir(): File {
        return File("$generatedSourcesDir/generated-examples")
    }

    @Input
    var includeQueries = mutableListOf<String>()

    @Input
    var includeMutations = mutableListOf<String>()

    @Input
    var skipEntityQueries = false

    @Input
    var shortProjectionNames = false

    @Input
    var omitNullInputFields = false

    @Input
    var maxProjectionDepth = 10

    @TaskAction
    fun generate() {

        for (projectName in projects){

            val packageName = "com.pranetr.$projectName.codegen"
            var schemaFilePath ="${project.projectDir}/src/main/resources/schema/$projectName/"

            var schemaFileset = HashSet<File>()
            schemaFileset.add(File("$schemaFilePath"))

            if(dependencies.get(projectName) !=null){
                dependencies.get(projectName)?.forEach {
                    val commonSchemaFilePath ="${project.projectDir}/src/main/resources/schema/common/$it-schema.graphqls"
                    schemaFileset.add(File("$commonSchemaFilePath"))
                }
            }

            println("Generate ---$schemaFileset")


            val config = com.netflix.graphql.dgs.codegen.CodeGenConfig(
                schemas = emptySet(),
                schemaFiles = schemaFileset,
                packageName = packageName,
                outputDir = File("$generatedSourcesDir/$projectName").toPath(),
                examplesOutputDir = getExampleOutputDir().toPath(),
                writeToFiles = true,
                subPackageNameClient = subPackageNameClient,
                subPackageNameDatafetchers = subPackageNameDatafetchers,
                subPackageNameTypes = subPackageNameTypes,
                language = com.netflix.graphql.dgs.codegen.Language.KOTLIN,
                generateBoxedTypes = generateBoxedTypes,
                generateClientApi = generateClient,
                generateInterfaces = generateInterfaces,
                typeMapping = typeMapping,
                includeQueries = includeQueries.toSet(),
                includeMutations = includeMutations.toSet(),
                skipEntityQueries = skipEntityQueries,
                shortProjectionNames = shortProjectionNames,
                generateDataTypes = generateDataTypes,
                omitNullInputFields = omitNullInputFields,
                maxProjectionDepth = maxProjectionDepth,
            )
//            println("Codegen config:"+ config)
            val codegen = com.netflix.graphql.dgs.codegen.CodeGen(config)
            codegen.generate()




        }
    }
}