#!/usr/bin/groovy

def call() {
  burgrNotify("QA",
              "qa",
              "failed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}