def String call(gid, aid, repo) {
  def response = httpRequest authentication: 'repox-api', httpMode: 'GET', url: "${env.ARTIFACTORY_URL}/api/search/latestVersion?g=${gid}&a=${aid}&repos=${repo}", validResponseCodes: '200'
  return response.content
}
