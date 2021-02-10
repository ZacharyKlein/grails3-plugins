package com.artifact

import io.micronaut.http.annotation.Get
import io.micronaut.http.client.annotation.Client

@Client("http://localhost:8888")
interface ArtifactApiClient {

    @Get("/{?start}")
    List<ArtifactPackage> artifactPackageList(Integer start)

}
