package com.artifact

import groovy.transform.CompileStatic

@CompileStatic
class ArtifactPackage {
    String name
    String repo
    String owner
    String desc
    List<String> labels
    List<String> attribute_names
    List<String> licenses
    List<String> custom_licenses
    Integer followers_count
    String created
    String website_url
    String issue_tracker_url
    List<String> linked_to_repos
    List<String> permissions
    List<String> versions
    String latest_version
    String updated
    Integer rating_count
    List<String> system_ids
    String vcs_url
    String maturity

    List<String> getAttributeNames() {
        attribute_names
    }

    List<String> getCustomLicenses() {
        custom_licenses
    }

    Integer getFollowersCount() {
        followers_count
    }

    String getWebsiteUrl() {
        website_url
    }

    String getIssueTrackerUrl() {
        issue_tracker_url
    }

    List<String> getLinkedToRepos() {
        linked_to_repos
    }

    String getLatestVersion() {
        latest_version
    }

    Integer getRatingCount() {
        rating_count
    }

    List<String> getSystemIds() {
        system_ids
    }

    String getVcsUrl() {
        vcs_url
    }


    @Override
    public String toString() {
        return "ArtifactPackage{" +
                "name='" + name + '\'' +
                ", repo='" + repo + '\'' +
                ", owner='" + owner + '\'' +
                ", desc='" + desc + '\'' +
                ", labels=" + labels +
                ", attribute_names=" + attribute_names +
                ", licenses=" + licenses +
                ", custom_licenses=" + custom_licenses +
                ", followers_count=" + followers_count +
                ", created='" + created + '\'' +
                ", website_url='" + website_url + '\'' +
                ", issue_tracker_url='" + issue_tracker_url + '\'' +
                ", linked_to_repos=" + linked_to_repos +
                ", permissions=" + permissions +
                ", versions=" + versions +
                ", latest_version='" + latest_version + '\'' +
                ", updated='" + updated + '\'' +
                ", rating_count=" + rating_count +
                ", system_ids=" + system_ids +
                ", vcs_url='" + vcs_url + '\'' +
                ", maturity='" + maturity + '\'' +
                '}';
    }
}
