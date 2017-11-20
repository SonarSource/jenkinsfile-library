#!/usr/bin/groovy

def call(url, branch, commit, step, type, status, timestamp) {
  echo "Send a step notification to BURGR: [repo: ${url}, branch: ${branch}, commit: ${commit}, step: ${step}, type: ${type}, status: ${status}]"
  def data = burgrExtractDataFromURL(url, timestamp)
  def message = """
  {
    "repository": "${data['owner']}/${data['project']}",
    "pipeline": "${env.BUILD_NUMBER}",
    "name": "${step}",
    "system": "cix",
    "type": "${type}",
    "number": "${env.BUILD_NUMBER}",
    "branch": "${branch}",
    "sha1": "${commit}",
    "url": "${env.BUILD_URL}",
    "status": "${status}",
    "metadata": "{}",
    "started_at": "${data['timestamp']}",
    "finished_at": "${data['timestamp']}"
  }
  """
  writeFile file:"step-burgr.tmp", text: message
  sh "curl -X POST -d @step-burgr.tmp --header \"Content-Type:application/json\" ${env.BURGR_URL}/api/stage"
}