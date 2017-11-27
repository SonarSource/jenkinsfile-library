def String call(project, buildNumber, expression) {
  def result
  repoxCredential() {
    result = sh returnStdout: true, script: "curl -s -u${env.ARTIFACTORY_API_USER}:${env.ARTIFACTORY_API_PASSWORD} ${env.ARTIFACTORY_URL}/api/build/${project}/${buildNumber} | jq -r ${expression}"
  }
  return result.trim()
}
