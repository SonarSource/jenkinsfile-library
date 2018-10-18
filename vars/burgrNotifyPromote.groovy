#!/usr/bin/groovy

def call() {
  def STATUS_MAP = ['SUCCESS': 'passed', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'cancelled']
  def data = computeGitData()
  def owner = data['owner']
  def project = data['project']
  def buildNumber = data['buildNumber']
  def branch = data['branch']
  def commit = data['commit']

  def doPromote = false
  def promoteOnBranches = [ 'master', 'branch-', 'PULLREQUEST-', 'dogfood-on-' ]
  for (it in promoteOnBranches) {
    if (branch.startsWith(it)) {
      doPromote = true
      break
    }
  }
  if (!doPromote) {
    echo "Do not send a promote notification to BURGR for builds coming from a development branch"
    return
  }

  def branchLabel='branch'
  if (branch.startsWith('PULLREQUEST-')){
    branch = branch.minus('PULLREQUEST-')
    branchLabel='pr_number'
  }
  
  def response = httpRequest authentication: 'repox-api', httpMode: 'GET', url: "${env.ARTIFACTORY_URL}/api/build/${project}/${buildNumber}", validResponseCodes: '200'
  def buildInfo = readJSON text: response.content
  def version = buildInfo.buildInfo.properties['buildInfo.env.PROJECT_VERSION']
  def metadata = """{\\"version\\":\\"${version}\\"}"""

  echo "Send a promote notification to BURGR: [owner: ${owner}, project: ${project}, buildNumber: ${buildNumber}, branch: ${branch}, commit: ${commit}, status: ${STATUS_MAP[currentBuild.currentResult]}]"

  if ('passed'.equals(STATUS_MAP[currentBuild.currentResult])) {
        
    def artifactsToPublish = buildInfo.buildInfo.properties['buildInfo.env.ARTIFACTS_TO_PUBLISH']
    if (artifactsToPublish == null) {
      artifactsToPublish = getModuleProperty(buildInfo.buildInfo.modules[0], 'artifactsToPublish')
    }
    def artifactsToDownload = buildInfo.buildInfo.properties['buildInfo.env.ARTIFACTS_TO_DOWNLOAD']
    if (artifactsToDownload == null) {
      artifactsToDownload = getModuleProperty(buildInfo.buildInfo.modules[0], 'artifactsToDownload')
    }

    def artifactsToDisplay = null
    if (artifactsToPublish != null) {
      artifactsToDisplay = artifactsToPublish
      if (artifactsToDownload != null) {
        artifactsToDisplay += ",${artifactsToDownload}"
      }
    } else if (artifactsToDownload != null) {
      artifactsToDisplay = artifactsToDownload
    }
    if (artifactsToDisplay != null) {
      def artifacts = artifactsToDisplay.tokenize(',')
      def List urls = []
      artifacts.each() {
        def url = "${env.ARTIFACTORY_URL}/sonarsource/"
        def tokens = it.tokenize(':')
        url += tokens[0].replace('.', '/') + '/'
        url += tokens[1] + '/'
        url += version + '/'
        url += tokens[1] + '-' + version
        classifier = tokens[3]
        if (classifier) {
          url += '-' + classifier
        }
        url += '.' + tokens[2]
        urls.add(url)
      }
      def metadataUrl = urls.join(',')
      metadata = """{\\"version\\":\\"${version}\\",\\"url\\":\\"${metadataUrl}\\"}"""
    }
  }
  def url = "${env.ARTIFACTORY_URL}/webapp/builds/${project}/${buildNumber}"
  def message = """
  {
    "repository": "${owner}/${project}",
    "pipeline": "${buildNumber}",
    "name": "artifacts",
    "system": "cix",
    "type": "promotion",
    "number": "${buildNumber}",
    "${branchLabel}": "${branch}",
    "sha1": "${commit}",
    "url": "${url}",
    "status": "${STATUS_MAP[currentBuild.currentResult]}",
    "metadata": "${metadata}",
    "finished_at": "${formatTimestamp(System.currentTimeMillis())}"
  }
  """
  httpRequest contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: "${message}", responseHandle: 'NONE', url: "${env.BURGR_URL}/api/stage", validResponseCodes: '100:599'
}

def getModuleProperty(module, property) {
  def ret = null 
  def properties = module.properties
  if (properties != null) {
    ret = properties[property]
  }
  return ret
}
