#!/usr/bin/groovy

def String call(body) {
  withCredentials([string(credentialsId: 'ARTIFACTORY_PRIVATE_API_KEY', variable: 'ARTIFACTORY_API_KEY')]) {
    body.call()
  }  
}
