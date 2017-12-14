#!/usr/bin/groovy

def call() {
  def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failure', 'UNSTABLE': 'failure', 'ABORTED': 'error']
  githubNotify("${STATUS_MAP[currentBuild.currentResult]}",
              'Promoted on cix',
              'cix-promotion')
}
