#!/usr/bin/groovy

def call(url, branch, commit, timestamp) {
  echo 'Simulate a notification to BURGR from GitHub (push, PRs, ...)'
  def data = burgrExtractDataFromURL(url, timestamp)
  def message = """
  {
    "ref": "refs/heads/${branch}",
    "compare": "${data['url']}/commit/${commit}/compare",
    "head_commit": {
      "id": "${commit}",
      "message": "Fake commit message for ${commit}",
      "timestamp": "${data['timestamp']}",
      "url": "${data['url']}/commit/${commit}"
    },
    "repository": {
      "owner": {
        "name": "${data['owner']}"
      },
      "name": "${data['project']}",
      "url": "${data['url']}"
    },
    "sender": {
      "login": "Builders",
      "avatar_url": "https://www.google.com/a/sonarsource.com/images/logo.gif"
    }
  }
  """
  writeFile file:"commit-burgr.tmp", text: message
  sh "curl -X POST -d @commit-burgr.tmp --header \"Content-Type:application/json\" ${env.BURGR_URL}/api/commit/github"
}