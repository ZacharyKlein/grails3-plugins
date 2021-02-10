package com.artifact

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grailsplugins.ArtifactApi
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired

@CompileStatic
@Slf4j
class ArtifactService implements GrailsConfigurationAware, ArtifactApi {

    String organization
    String repository


    @Autowired
    ArtifactApiClient artifactApiClient

    @Override
    void setConfiguration(Config co) {
        organization = co.getProperty('bintray.organization', String, 'grails')
        repository = co.getProperty('bintray.repository', String, 'plugins')
//        this.username = co.getProperty('bintray.username', String)
//        this.token = co.getProperty('bintray.token', String)
    }
    @Override
    ArtifactPackage fetchPackage(String name, String organization = this.organization, String repository = this.repository) throws IOException {
        return new ArtifactPackage(name: name)
    }

    @Override
    ArtifactPackageResponse fetchPackagesByStartPosition(Integer startPos) throws NumberFormatException, IOException {

        List<ArtifactPackage> result = artifactApiClient.artifactPackageList(startPos)

        return new ArtifactPackageResponse(total: result.size(), start: 0, end: result.size(), packageList: result)
    }

    @Override
    List<Integer> expectedExtraStarPositions(Integer total, Integer startPosition, Integer endPosition) {
        return [1]
    }
}
