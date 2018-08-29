#!/usr/bin/groovy

def call() {
  echo 'Simulate a notification to BURGR from GitHub (push, PRs, ...)'
  def data = computeGitData()
  def message = """
  {
    "ref": "refs/heads/${data['branch']}",
    "compare": "${data['url']}/commit/${data['commit']}/compare",
    "head_commit": {
      "id": "${data['commit']}",
      "message": "Fake commit message for ${data['commit']}",
      "timestamp": "${formatTimestamp(System.currentTimeMillis())}",
      "url": "${data['url']}/commit/${data['commit']}"
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
  httpRequest contentType: 'APPLICATION_JSON', httpMode: 'POST', requestBody: "${message}", responseHandle: 'NONE', url: "${env.BURGR_URL}/api/commit/github"
}
