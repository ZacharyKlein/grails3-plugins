package grailsplugins

import com.artifact.ArtifactPackage
import com.artifact.ArtifactPackageResponse
import com.artifact.ArtifactPackageSimple
import com.artifact.ArtifactService
import com.github.GithubReadmeService
import com.github.GithubRepository
import com.github.GithubService
import com.twitter.TwitterService
import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import groovy.time.TimeCategory
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

import static grails.async.Promises.task

@Slf4j
@CompileStatic
class GrailsPluginsService implements GrailsConfigurationAware {

    GrailsPluginsRepository grailsPluginsRepository
    ArtifactService artifactService
    GithubService githubService
    GithubReadmeService githubReadmeService
    AsciidocRenderService asciidocRenderService
    MarkdownRenderService markdownRenderService
    TwitterService twitterService

    List<String> blacklist

    @Override
    void setConfiguration(Config co) {
        blacklist = co.getProperty('grails.plugins.blacklist', List, [])
    }

    @CompileDynamic
    Date oneDayAgo() {
        Date now = new Date()
        use(TimeCategory) {
            now -= 1.day
        }
        now
    }

    void refresh() {
        grailsPluginsRepository.clearNotUpdatedSince(oneDayAgo())
        Integer startAt = 0
        ArtifactPackageResponse rsp = artifactService.fetchPackagesByStartPosition(startAt)
        if ( rsp != null ) {
            log.trace 'ArtifactPackageResponse start: {} end: {} total: {}', rsp.start, rsp.end, rsp.total
            fetch(rsp)
            List<Integer> positions = artifactService.expectedExtraStarPositions(rsp.total, rsp.start, rsp.end) //TODO: Is this needed for our own API?
            log.trace("positions {}", positions)
            positions.each { Integer start ->
                task {
                    log.trace("fetching bintray packages with start {}", start)
                    artifactService.fetchPackagesByStartPosition(start)
                }.onComplete { ArtifactPackageResponse bintrayPackageResponse ->
                    log.trace 'ArtifactPackageResponse start: {} end: {} total: {}', bintrayPackageResponse.start, bintrayPackageResponse.end, bintrayPackageResponse.total
                    if ( bintrayPackageResponse != null ) {
                        fetch(bintrayPackageResponse)
                    } else {
                        log.warn("no bintray package response for {}", start)
                    }
                }
            }
        }
    }

    void fetch(ArtifactPackageResponse rsp) {
        for (ArtifactPackage artifactPackage : rsp.packageList ) {
            log.debug("fetching bintray package {}", artifactPackage.name)
            fetch(artifactPackage)
        }
    }

    void fetch(ArtifactPackage artifactPackage) {
        if ( !blacklist.contains(artifactPackage.name) ) {
                if ( artifactPackage ) {
                    ArtifactKey key = ArtifactKey.of(artifactPackage)
                    String previousVersion = grailsPluginsRepository.findPreviousLatestVersion(key)

                    if ( previousVersion &&
                         isThereANewVersion(artifactPackage, previousVersion) ) {
                        tweetAboutNewVersion(artifactPackage)
                    }
                    log.trace("saving {}", artifactPackage.name)
                    key = grailsPluginsRepository.save(artifactPackage)
                    fetchGithubRepository(key)
                    fetchGithubReadme(key)
                } else {
                    log.warn("could not fetch bintray package {}", artifactPackage.name)
                }

        }
    }

    void tweetAboutNewVersion(ArtifactPackage bintrayPackage) {
        twitterService.tweet "${bintrayPackage.name} ${bintrayPackage.latestVersion} released: http://plugins.grails.org/plugin/${bintrayPackage.owner}/${bintrayPackage.name}"
    }

    boolean isThereANewVersion(ArtifactPackage bintrayPackage, String previousVersion) {
        if ( !previousVersion ) {
            return false
        }
        try {
            SoftwareVersion previousSoftwareVersion = SoftwareVersion.build(previousVersion)
            SoftwareVersion softwareVersion = SoftwareVersion.build(bintrayPackage.latestVersion)
            if ( !softwareVersion || !previousSoftwareVersion || softwareVersion.isSnapshot() ) {
                return false
            }
            return softwareVersion.compareTo(previousSoftwareVersion) as boolean

        } catch(NumberFormatException e) {

            if ( previousVersion && bintrayPackage.latestVersion && previousVersion != bintrayPackage.latestVersion ) {
                return true
            }

        }
        false
    }

    void fetchGithubReadme(ArtifactKey key) {
        String vcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        if ( vcsUrl ) {
            task {
                githubReadmeService.fetchMarkdown(vcsUrl)
            }.onComplete { String markdown ->
                if ( markdown ) {
                    task {
                        markdownRenderService.renderMarkdown(markdown)
                    }.onComplete { String html ->
                        if ( html ) {
                            grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                        }
                    }
                } else {
                    task {
                        githubReadmeService.fetchAsciidoc(vcsUrl)
                    }.onComplete { String asciidoc ->
                        if ( asciidoc ) {
                            task {
                                asciidocRenderService.renderAsciidoc(asciidoc)
                            }.onComplete { String html ->
                                if ( html ) {
                                    grailsPluginsRepository.updateGithubRepositoryReadme(key, html)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    void fetchGithubRepository(ArtifactKey key) {
        String vcsUrl = grailsPluginsRepository.find(key)?.bintrayPackage?.vcsUrl
        if ( vcsUrl ) {
            task {
                githubService.fetchGithubRepository(vcsUrl)
            }.onComplete { GithubRepository githubRepository ->
                if ( githubRepository ) {
                    grailsPluginsRepository.updateGithubRepository(key, githubRepository)
                }
            }
        }
    }

}
