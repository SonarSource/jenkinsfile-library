#!/usr/bin/groovy

def call() {
  burgrNotify("QA",
              "qa",
              "started",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
