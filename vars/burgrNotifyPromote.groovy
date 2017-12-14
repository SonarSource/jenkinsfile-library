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
  def promoteOnBranches = [ 'master', 'branch-', 'PULLREQUEST-' ]
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

  def version = repoxGetProjectVersion(project, buildNumber)
  def metadata = """{\\"version\\":\\"${version}\\"}"""

  echo "Send a promote notification to BURGR: [owner: ${owner}, project: ${project}, buildNumber: ${buildNumber}, branch: ${branch}, commit: ${commit}, status: ${STATUS_MAP[currentBuild.currentResult]}]"

  if ('passed'.equals(STATUS_MAP[currentBuild.currentResult])) {
    def artifactsToDownload = repoxGetArtifactsToPublish(project, buildNumber)
    if ('null'.equals(artifactsToDownload)) {
      artifactsToDownload = repoxGetArtifactsToDownload(project, buildNumber)
    }
    if (!'null'.equals(artifactsToDownload)) {
      def artifacts = artifactsToDownload.tokenize(',')
      def promotedRepo = repoxGetDataFromBuildInfo(project, buildNumber, """'.buildInfo.statuses[] | select(.status | contains("it-passed")).repository'""")
      def List urls = []
      artifacts.each() {
        def url = "${env.ARTIFACTORY_URL}/${promotedRepo}/"
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
  writeFile file:"step-burgr.tmp", text: message
  sh "curl -X POST -d @step-burgr.tmp --header \"Content-Type:application/json\" ${env.BURGR_URL}/api/stage"
}
