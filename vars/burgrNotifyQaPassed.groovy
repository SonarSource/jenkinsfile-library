#!/usr/bin/groovy

def call() {
  burgrNotify("QA",
              "qa",
              "passed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
