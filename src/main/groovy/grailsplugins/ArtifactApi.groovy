package grailsplugins

import com.artifact.ArtifactPackage
import com.artifact.ArtifactPackageResponse

interface ArtifactApi {

    ArtifactPackage fetchPackage(String name, String organization, String repository) throws IOException

    ArtifactPackageResponse fetchPackagesByStartPosition(Integer startPos) throws NumberFormatException, IOException

    List<Integer> expectedExtraStarPositions(Integer total, Integer startPosition, Integer endPosition)
}
