package utils

import com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration
import groovy.json.JsonSlurper
import groovy.transform.CompileStatic
import groovy.transform.SelfType
import testSupport.PipelineSpockTestBase

import static BranchlessLocalSource.branchlessLocalSource
import static com.lesfurets.jenkins.unit.global.lib.LibraryConfiguration.library
import static com.lesfurets.jenkins.unit.global.lib.LocalSource.localSource
import static com.lesfurets.jenkins.unit.global.lib.GitSource.gitSource


@SelfType(PipelineSpockTestBase)
@CompileStatic
trait SharedLibraryHelpers {

    private String sharedLibs = new File("./")
    private String testSharedLibs = new File("./test/resources/libs/")

    def setup() {
        setupSharedLibrary()
        setupStringParameterWorkaround()
    }

    LibraryConfiguration buildAndRegisterTestSharedLibrary(String name, String version) {
        def allowOverride = false
        def implicit = false
        def library = library()
            .name(name)
            .defaultVersion(version)
            .allowOverride(allowOverride)
            .implicit(implicit)
            .targetPath(testSharedLibs)
            .retriever(localSource(testSharedLibs))
            .build()
        helper.registerSharedLibrary(library)
        return library
    }

    LibraryConfiguration buildAndRegisterSharedLibrary(String name, String version) {
        def allowOverride = false
        def implicit = false
        def library = library()
            .name(name)
            .defaultVersion(version)
            .allowOverride(allowOverride)
            .implicit(implicit)
            .targetPath(sharedLibs)
            .retriever(branchlessLocalSource(sharedLibs))
            .build()
        helper.registerSharedLibrary(library)
        return library
    }

    LibraryConfiguration buildAndRegisterGitSharedLibrary(String name, String version) {
        def allowOverride = false
        def implicit = false
        if(version == null || version.trim().length() == 0) {
            Process pngquantCmd = "git rev-parse --abbrev-ref HEAD".execute();
            version = pngquantCmd .in.text.trim();
            println("Version : " + version);
        }
        def library = library()
            .name(name)
            .defaultVersion(version)
            .allowOverride(allowOverride)
            .implicit(implicit)
            .targetPath(sharedLibs)
            .retriever(gitSource(sharedLibs))
            .build()
        helper.registerSharedLibrary(library)
        return library
    }

    def setupStringParameterWorkaround() {
        //        helper.registerAllowedMethod('string', [Map.class], { Map stringParam ->
        //            // Add the param default for a string
        //            //addParam(stringParam.name, stringParam.defaultValue)
        //        })

        // TODO find out why it doues not work w/ Map.class
        // FIXME registerAllowedMethod 'string'
        helper.registerAllowedMethod('string', [LinkedHashMap.class], {it})
    }

    def setupSharedLibrary() {

        helper.registerAllowedMethod('libraries', [Closure.class], { libraryBody ->
            //helper.registerAllowedMethod('lib', [String.class], { libName ->
            //    null
            //})

            //def paramsResult = libraryBody()
            //paramsResult
            //helper.unRegisterAllowedMethod('lib', [String.class])
            null
        })

        helper.registerAllowedMethod('quietPeriod', [Integer.class], null)

        helper.registerAllowedMethod('httpRequest', [Map.class], null)
        helper.registerAllowedMethod("readJSON", [Map.class], {new JsonSlurper().parseText((it as Map).text as String) })
        helper.registerAllowedMethod('sendCIMessage', [Map.class], null)
        helper.registerAllowedMethod('sendCamelMessage', [Map.class], null)
        helper.registerAllowedMethod('camelTrigger', [Map.class], null)
        helper.registerAllowedMethod('GenericTrigger', [Map.class], null)


        //helper.registerAllowedMethod("configFileProvider", [List.class, Closure.class], withConfigFileProviderInterceptor) // FIXME
        helper.registerAllowedMethod("configFileProvider", [List.class, Closure.class], null)
        helper.registerAllowedMethod("configFile", [Map.class], null)

        helper.registerAllowedMethod("rtp", [Map.class], null)


        // TODO process it.file also
        //        helper.registerAllowedMethod("readCSV", [Map.class], {
        //            def _csvFormat = CSVFormat.POSTGRESQL_CSV
        //                .withFirstRecordAsHeader()
        //            if (it.containsKey('text')) {
        //                return _csvFormat
        //                    .parse(IOUtils.toInputStream(it['text'], "UTF-8").newReader())
        //            } else if (it.containsKey('file')) {
        //                return _csvFormat
        //                 .parse(FileUtils.openInputStream(it['file'], "UTF-8").newReader())
        //            } else {
        //                throw new IllegalArgumentException("'file' or 'text' should be passed to readCSV")
        //            }
        //        })

        //buildAndRegisterGitSharedLibrary('tfs-commons', " ")
        buildAndRegisterSharedLibrary('tfs-commons', " ")
        buildAndRegisterSharedLibrary('ctl', " ")
    }


}
