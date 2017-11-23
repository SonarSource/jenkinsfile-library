#!/usr/bin/groovy

def call() {
  def currentVersion = mavenGetProjectVersion()
  def releaseVersion = currentVersion.minus('-SNAPSHOT')
  def digitSeparatorCount = releaseVersion.count('.')
  // Release are on 3 digits + build number
  while (digitSeparatorCount < 2) {
    releaseVersion += '.0'
    ++digitSeparatorCount
  }
  // Set the build number set up by the hook (if the build does not come from Jenkins (Travis, AppVeyor, ...) or from the Jenkins
  buildNumber = env.CI_BUILD_NUMBER ?: env.BUILD_NUMBER
  releaseVersion += ".${buildNumber}"
  echo "Release version is ${releaseVersion}"
  try {
    sh "mvn org.codehaus.mojo:versions-maven-plugin:2.5:set -DnewVersion=${releaseVersion}"
  } catch(ex) {
    error("Unable to set the verion of the project to ${releaseVersion}")
  }
}