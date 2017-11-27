#!/usr/bin/groovy

def String call(project, buildNumber) {
  def artifact = repoxGetDataFromBuildInfo(project, buildNumber, ".buildInfo.modules[0].id")
  return artifact.tokenize(':').last().trim()
}
