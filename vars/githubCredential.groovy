#!/usr/bin/groovy

def String call(body) {
  try {
    withCredentials([string(credentialsId: 'sonartech-github-token', variable: 'GITHUB_TOKEN')]) {
      body.call()
    }
  } catch(ex) {
    // Fallback on the testing environment (https://github.com/SonarSource/cix-pipelines) where GITHUB_TOKEN is not set
    body.call()
  }
}
