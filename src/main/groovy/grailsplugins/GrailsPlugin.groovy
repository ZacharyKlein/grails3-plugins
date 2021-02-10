package grailsplugins

import com.artifact.ArtifactPackage
import com.bintray.BintrayPackage
import com.github.GithubRepository
import groovy.transform.CompileStatic

@CompileStatic
class GrailsPlugin {
    ArtifactPackage bintrayPackage
    GithubRepository githubRepository
    String readme
    Date lastUpdated
}
