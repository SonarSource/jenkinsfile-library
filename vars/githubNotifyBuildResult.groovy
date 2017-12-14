#!/usr/bin/groovy

def call() {
  def STATUS_MAP = ['SUCCESS': 'success', 'FAILURE': 'failure', 'UNSTABLE': 'failure', 'ABORTED': 'error']
  githubNotify("${STATUS_MAP[currentBuild.currentResult]}",
              'Building on cix',
              'cix-build')
}
