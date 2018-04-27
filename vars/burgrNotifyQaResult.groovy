#!/usr/bin/groovy

def call(metadata) {
  def STATUS_MAP = ['SUCCESS': 'passed', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'cancelled']
  echo "currentBuild.currentResult $currentBuild.currentResult"
  def defaultStep = 'qa'
  burgrNotify(metadata ? "$metadata $defaultStep" : defaultStep,
              metadata ? "$defaultStep-$metadata" : defaultStep,
              "${STATUS_MAP[currentBuild.currentResult]}",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
