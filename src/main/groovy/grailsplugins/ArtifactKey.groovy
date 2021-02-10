package grailsplugins

import com.artifact.ArtifactPackage
import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode

@EqualsAndHashCode
@CompileStatic
class ArtifactKey {
    String owner
    String name

    static ArtifactKey of(ArtifactPackage artifactPackage) {
        new ArtifactKey(owner: artifactPackage.owner, name: artifactPackage.name)
    }
}
