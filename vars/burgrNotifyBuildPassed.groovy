#!/usr/bin/groovy

def call() {
  burgrNotify("Build",
              "build",
              "passed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
