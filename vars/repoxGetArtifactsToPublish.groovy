#!/usr/bin/groovy

def call(project, buildNumber) {
  def artifacts = repoxGetDataFromBuildInfo(project, buildNumber, ".buildInfo.properties[\\\"buildInfo.env.ARTIFACTS_TO_PUBLISH\\\"]")
  if ('null'.equals(artifacts)) {
    artifacts = repoxGetDataFromBuildInfo(project, buildNumber, ".buildInfo.modules[0].properties[\\\"artifactsToPublish\\\"]")
  }
  return artifacts
}
