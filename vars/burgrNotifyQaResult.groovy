#!/usr/bin/groovy

def call() {
  def STATUS_MAP = ['SUCCESS': 'passed', 'FAILURE': 'failed', 'UNSTABLE': 'failed', 'ABORTED': 'cancelled']
  echo "currentBuild.currentResult $currentBuild.currentResult"
  burgrNotify("qa",
              "qa",
              "${STATUS_MAP[currentBuild.currentResult]}",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
