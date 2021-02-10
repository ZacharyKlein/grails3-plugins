package com.artifact

import com.bintray.BintrayPackageSimple
import groovy.transform.CompileStatic
import groovy.transform.ToString

@ToString
@CompileStatic
class ArtifactPackageResponse {
    Integer total
    Integer start
    Integer end
    List<ArtifactPackageSimple> packageList = []
}
