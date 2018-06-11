#!/usr/bin/groovy

def call() {
  def snaptshot = '-SNAPSHOT'
  def currentVersion = mavenGetProjectVersion()
  if (!currentVersion.contains(snaptshot)) {
    return
  }
  def releaseVersion = currentVersion.minus(snaptshot)
  def digitSeparatorCount = releaseVersion.count('.')
  // Release are on 3 digits + build number
  while (digitSeparatorCount < 2) {
    releaseVersion += '.0'
    ++digitSeparatorCount
  }
  // Set the build number set up by the hook (if the build does not come from Jenkins (Travis, AppVeyor, ...) or from the Jenkins
  buildNumber = env.CI_BUILD_NUMBER ?: env.BUILD_NUMBER
  releaseVersion += ".${buildNumber}"
  echo "Build version is ${releaseVersion}"
  def mvnCommand = isUnix() ? 'mvn' : 'mvn.cmd'
  try {
    sh "${mvnCommand} org.codehaus.mojo:versions-maven-plugin:2.5:set -DnewVersion=${releaseVersion}"
  } catch(ex) {
    error("Unable to set the verion of the project to ${releaseVersion}")
  }
}
