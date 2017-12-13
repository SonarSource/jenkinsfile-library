#!/usr/bin/groovy

def String call(body) {
  try {
    withCredentials([usernamePassword(credentialsId: '911c81fa-2ac1-4d0f-8223-6439a027e237', passwordVariable: 'GITHUB_TOKEN', usernameVariable: 'NOT_USED_1')]) {
      body.call()
    }
  } catch(ex) {
    // Fallback on the testing environment (https://github.com/SonarSource/cix-pipelines) where GITHUB_TOKEN is not set
    body.call()
  }
}
