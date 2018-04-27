#!/usr/bin/groovy

def call(metadata) {
  def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failure', 'UNSTABLE': 'failure', 'ABORTED': 'error']
  def defaultDescription = 'QA on cix'
  def defaultContext = 'cix-qa'
  githubNotify("${STATUS_MAP[currentBuild.currentResult]}",
              metadata ? "$metadata $defaultDescription" : defaultDescription,
              metadata ? "$defaultContext-$metadata" : defaultContext)
}
