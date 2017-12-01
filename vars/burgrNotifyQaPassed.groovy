#!/usr/bin/groovy

def call() {
  burgrNotify("qa",
              "qa",
              "passed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
