#!/usr/bin/groovy

package com.sonarsource.burgr

def fakeCommit(url, branch, commit) {
  echo 'Simulate a notification to BURGR from GitHub (push, PRs, ...)'
  def data = extractDataForBurgrFromURL(url)
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

def notifyBurgr(url, branch, commit, step, type, status) {
  echo "Send a step notification to BURGR: [repo: ${url}, branch: ${branch}, commit: ${commit}, step: ${step}, type: ${type}, status: ${status}]"
  def data = extractDataForBurgrFromURL(url)
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

def Map extractDataForBurgrFromURL(url) {
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
  def trimedUrl = url.reverse().drop(4).reverse()
  def splitUrl = trimedUrl.drop(8).split("/")
  return [timestamp:sdf.format(currentBuild.getTimeInMillis()),
          url:trimedUrl,
          owner:splitUrl[1],
          project:splitUrl[2]]
}
