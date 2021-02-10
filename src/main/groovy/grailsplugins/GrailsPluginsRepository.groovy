package grailsplugins

import com.artifact.ArtifactPackage
import com.github.GithubRepository
import groovy.transform.CompileStatic

@CompileStatic
interface GrailsPluginsRepository {
    ArtifactKey save(ArtifactPackage bintrayPackage)
    ArtifactKey updateGithubRepository(ArtifactKey key, GithubRepository githubRepository)
    ArtifactKey updateGithubRepositoryReadme(ArtifactKey key, String readme)
    GrailsPlugin find(ArtifactKey key)
    int count()
    List<GrailsPlugin> topRatedPlugins()
    List<GrailsPlugin> latestPlugins()
    List<GrailsPlugin> findAll()
    List<GrailsPlugin> findByQuery(String query)
    GrailsPlugin findByPluginName(String name)
    String findPreviousLatestVersion(ArtifactKey key)
    void clearNotUpdatedSince(Date date)
}
