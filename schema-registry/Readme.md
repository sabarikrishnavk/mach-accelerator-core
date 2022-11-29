
abstract class GenerateFilesTask : DefaultTask() {

    private val LOGGER = org.slf4j.LoggerFactory.getLogger("DgsCodegenPlugin")

    @Input
    var generatedSourcesDir: String = project.buildDir.absolutePath

    @InputFiles
    var schemaPaths = mutableListOf<Any>("${project.projectDir}/src/main/resources/schema")

    @Input
    var rootFolder:String = "${project.projectDir}/schema/"

    @Input
    var packageName = "com.galaxy.codegen"

//    @Input
//    var packageNames = mutableListOf<Any>("com.galaxy.codegen")

    @Input
    var subPackageNameClient = "client"

    @Input
    var subPackageNameDatafetchers = "datafetchers"

    @Input
    var subPackageNameTypes = "types"

    private val hasKotlinPluginWrapperClass = try {
        this.javaClass.classLoader.loadClass("org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper")
        true
    } catch (ex: Exception) {
        false
    }

    @Input
    var language =
        if (hasKotlinPluginWrapperClass && project.plugins.hasPlugin(org.jetbrains.kotlin.gradle.plugin.KotlinPluginWrapper::class.java)) "KOTLIN" else "JAVA"

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
    fun getOutputDir(): File {
        return file("$generatedSourcesDir/generated")
    }

    @OutputDirectory
    fun getExampleOutputDir(): File {
        return file("$generatedSourcesDir/generated-examples")
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


        var index = 0
        for (schemafile in schemaPaths) {

            LOGGER.info("Codegen schemafile: {}", schemafile)
            var config = com.netflix.graphql.dgs.codegen.CodeGenConfig(
                schemaFiles = setOf(file(rootFolder + schemafile)),
                packageName = packageNames.get(index).toString(),
                schemas = emptySet(),
                outputDir = getOutputDir().toPath(),
                examplesOutputDir = getExampleOutputDir().toPath(),
                writeToFiles = true,
                subPackageNameClient = subPackageNameClient,
                subPackageNameDatafetchers = subPackageNameDatafetchers,
                subPackageNameTypes = subPackageNameTypes,
                language = com.netflix.graphql.dgs.codegen.Language.valueOf(language.toUpperCase()),
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

            LOGGER.info("Codegen config: {}", config)

            val codegen = com.netflix.graphql.dgs.codegen.CodeGen(config)
            codegen.generate()
            index += 1
        }
    }
}