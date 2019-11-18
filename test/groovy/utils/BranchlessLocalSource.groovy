package utils

import com.lesfurets.jenkins.unit.global.lib.SourceRetriever
import groovy.transform.CompileStatic
import groovy.transform.Immutable

@Immutable
@CompileStatic
class BranchlessLocalSource implements SourceRetriever {

    String sourceURL

    @Override
    List<URL> retrieve(String repository, String branch, String targetPath) {
        //def path = branch.trim().length() == 0 ? "$repository" : "$repository@$branch"
        String path = branch.trim().length() == 0 ? "." : "$repository@$branch"
        def sourceDir = new File(sourceURL).toPath()
            .resolve(path).toFile()
        if (sourceDir.exists()) {
            return [sourceDir.toURI().toURL()]
        }
        throw new IllegalStateException("Directory $sourceDir.path does not exists")
    }

    static SourceRetriever branchlessLocalSource(String source) {
        new BranchlessLocalSource(source)
    }

    @Override
    String toString() {
        return "LocalSource{" +
            "sourceURL='" + sourceURL + '\'' +
            '}'
    }
}
