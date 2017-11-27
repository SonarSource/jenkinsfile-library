#!/usr/bin/groovy

def call() {
  def data = computeGitData()
  def project = data['project']
  def buildNumber = data['buildNumber']
  def branch = data['branch']
  def commit = data['commit']

  def boolean doPromote = false
  def targetRepo = 'sonarsource-dev'
  def status = 'it-passed-pr'

  if ("master".equals(branch) || branch.startsWith("branch-")) {
    def repo = repoxGetDataFromBuildInfo(project, buildNumber, ".buildInfo.properties[\\\"buildInfo.env.ARTIFACTORY_DEPLOY_REPO\\\"]")
    targetRepo = repo.replace('qa', 'builds')
    status = 'it-passed'
    doPromote = true
  }
  if (branch.startsWith("PULLREQUEST-")) {
    doPromote = true
  }
  if (doPromote) {
    echo "Promoting build ${project}#${buildNumber} to ${targetRepo}"
    def message = """
    { 
      "status": "${status}",
      "properties": { 
        "it" : [ "${formatTimestamp(System.currentTimeMillis())}" ]
      },
      "targetRepo": "${targetRepo}"
    }
    """
    writeFile file:"promote.tmp", text: message
    def httpCode
    repoxCredential() {
      httpCode = sh returnStdout: true, script:  "curl --write-out %{http_code} -X POST -o /dev/null -d @promote.tmp -H \"Content-Type: application/json\" -u${env.ARTIFACTORY_API_USER}:${env.ARTIFACTORY_API_PASSWORD} ${env.ARTIFACTORY_URL}/api/build/promote/${project}/${buildNumber}"
    }
    if ('200'.equals(httpCode)) {
      echo "Build ${project}#${buildNumber} promoted to ${targetRepo}"
      return
    }
    error "Build ${project}#${buildNumber} promotion to ${targetRepo} failed with code ${httpCode}"
  }
  echo "No promotion for builds coming from a development branch"
}
