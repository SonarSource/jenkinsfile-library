#!/usr/bin/groovy

def call(step, type, status, started_at, finished_at) {
  def data = computeGitData()
  def owner = data['owner']
  def project = data['project']
  def buildNumber = data['buildNumber']
  def branch = data['branch']
  def commit = data['commit']

  echo "Send a step notification to BURGR: [owner: ${owner}, project: ${project}, buildNumber: ${buildNumber}, branch: ${branch}, commit: ${commit}, step: ${step}, type: ${type}, status: ${status}, started_at: ${formatTimestamp(started_at)}, finished_at: ${formatTimestamp(finished_at)} ]"
  
  //cleans pullrequest branch name
  def branchLabel='branch'
  if (branch.startsWith('PULLREQUEST-')){
    branch = branch.minus('PULLREQUEST-')
    branchLabel='pr_number'
  }
  
  def message = """  
  {
    "repository": "${owner}/${project}",
    "pipeline": "${buildNumber}",
    "name": "${step}",
    "system": "cix",
    "type": "${type}",
    "number": "${env.BUILD_NUMBER}",
    "$branchLabel": "${branch}",
    "sha1": "${commit}",
    "url": "${env.BUILD_URL}",
    "status": "${status}",
    "metadata": "{}",
    "started_at": "${formatTimestamp(started_at)}",
    "finished_at": "${formatTimestamp(finished_at)}"
  }
  """
  httpRequest contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: "${message}", consoleLogResponseBody: true, responseHandle: 'NONE', url: "${env.BURGR_URL}/api/stage", validResponseCodes: '100:599'
}
