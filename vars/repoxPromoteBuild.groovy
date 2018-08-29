#!/usr/bin/groovy

def call() {
  def data = computeGitData()
  def project = data['project']
  def buildNumber = data['buildNumber']
  def branch = data['branch']
  def commit = data['commit']

  def boolean doPromote = false
  def targetRepo = ''
  def status = 'it-passed-pr'

  def repo = repoxGetPropertyFromBuildInfo(project, buildNumber, 'buildInfo.env.ARTIFACTORY_DEPLOY_REPO')

  if ("master".equals(branch) || branch.startsWith("branch-")) {
    targetRepo = repo.replace('qa', 'builds')
    status = 'it-passed'
    doPromote = true
  }
  if (branch.startsWith("dogfood-on-")) {
    targetRepo = "sonarsource-dogfood-builds"
    status = 'it-passed'
    doPromote = true
  }
  if (branch.startsWith("PULLREQUEST-")) {
    targetRepo = repo.replace('qa', 'dev')
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
    httpRequest authentication: 'repox-api', contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: "${message}", responseHandle: 'NONE', url: "${env.ARTIFACTORY_URL}/api/build/promote/${project}/${buildNumber}", validResponseCodes: '200'
    echo "Build ${project}#${buildNumber} promoted to ${targetRepo}"
    return
  }
  echo "No promotion for builds coming from a development branch"
}
