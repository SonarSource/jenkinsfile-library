#!/usr/bin/groovy

def String call(body) {
  try {
    withCredentials([usernamePassword(credentialsId: 'repox-api', passwordVariable: 'ARTIFACTORY_API_PASSWORD', usernameVariable: 'ARTIFACTORY_API_USER')]) {
      body.call()
    }
  } catch(ex) {
    // Fallback on the testing environment (https://github.com/SonarSource/cix-pipelines) where ARTIFACTORY_API_USER and ARTIFACTORY_API_PASSWORD are set
    body.call()
  }
}
