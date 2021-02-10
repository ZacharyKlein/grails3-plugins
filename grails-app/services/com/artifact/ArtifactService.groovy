package com.artifact

import grails.config.Config
import grails.core.support.GrailsConfigurationAware
import grails.gorm.transactions.Transactional
import grailsplugins.ArtifactApi

@Transactional
class ArtifactService implements GrailsConfigurationAware, ArtifactApi {

    @Override
    void setConfiguration(Config co) {

    }

    @Override
    ArtifactPackage fetchPackage(String name, String organization, String repository) throws IOException {
        return null
    }

    @Override
    ArtifactPackageResponse fetchPackagesByStartPosition(Integer startPos) throws NumberFormatException, IOException {
        return null
    }

    @Override
    List<Integer> expectedExtraStarPositions(Integer total, Integer startPosition, Integer endPosition) {
        return null
    }
}
