def String call(project, buildNumber, property) {
  def response = httpRequest authentication: 'repox-api', httpMode: 'GET', url: "${env.ARTIFACTORY_URL}/api/build/${project}/${buildNumber}", validResponseCodes: '200'
  def buildInfo = readJSON text: response.content
  return buildInfo.buildInfo.properties[property]
}
