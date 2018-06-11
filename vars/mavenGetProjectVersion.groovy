#!/usr/bin/groovy

def String call() {
  def version
  try {
    def mvnCommand = isUnix() ? 'mvn' : 'mvn.cmd'
    def result = sh returnStdout: true, script: "${mvnCommand} help:evaluate -Dexpression=project.version | grep -v \'^\\[\\|Download\\w\\+\\:\' | grep -v \'\\[WARNING\\]\'"
    // Need to get the last line of the result of the above maven command as the Maven wrapper of the Maven Pipeline plugin add information in the log
    version = result.readLines().last()
  } catch(ex) {
    error("Unable to get the version of the project")
  }
  echo "The version of the project is ${version}"
  return version
}
