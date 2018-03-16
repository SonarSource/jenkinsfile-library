#!/usr/bin/groovy

def call() {
  def data = computeGitData()
  def project = data['project']
  def buildNumber = data['buildNumber']
  def branch = data['branch']
  def commit = data['commit']
 
  def boolean doPromote = false
  def srcRepo1='sonarsource-private-qa'
  def srcRepo2='sonarsource-public-qa'
  def targetRepo1 = 'sonarsource-private-builds'
  def targetRepo2 = 'sonarsource-public-builds'
  def status = 'it-passed-pr'

  def repo = repoxGetDataFromBuildInfo(project, buildNumber, ".buildInfo.properties[\\\"buildInfo.env.ARTIFACTORY_DEPLOY_REPO\\\"]")

  if ("master".equals(branch) || branch.startsWith("branch-")) {
    status = 'it-passed'
    doPromote = true
  }
  if (branch.startsWith("dogfood-on-")) {
    targetRepo1 = "sonarsource-dogfood-builds"
    targetRepo2 = "sonarsource-dogfood-builds"
    status = 'it-passed'
    doPromote = true
  }
  if (branch.startsWith("PULLREQUEST-")) {
    targetRepo1 = 'sonarsource-private-dev'
    targetRepo2 = 'sonarsource-public-dev'
    doPromote = true
  }
  if (doPromote) {
    echo "Promoting build ${project}#${buildNumber}"
    def httpCode
    repoxCredential() {
      httpCode = sh returnStdout: true, script:  "curl --write-out %{http_code} -X GET -o /dev/null -u${env.ARTIFACTORY_API_USER}:${env.ARTIFACTORY_API_PASSWORD} ${env.ARTIFACTORY_URL}/api/plugins/execute/multiRepoPromote?params=buildName=$project;buildNumber=$buildNumber;src1=$srcRepo1;target1=$targetRepo1;src2=$srcRepo1;target2=$targetRepo2;status=$STATUS"
    }
    if ('200'.equals(httpCode)) {
      echo "Build ${project}#${buildNumber} promoted to ${targetRepo}"
      return
    }
    error "Build ${project}#${buildNumber} promotion to ${targetRepo} failed with code ${httpCode}"
  }
  echo "No promotion for builds coming from a development branch"
}
