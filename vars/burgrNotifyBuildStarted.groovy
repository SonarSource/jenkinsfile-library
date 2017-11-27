#!/usr/bin/groovy

def call() {
  burgrNotify("Build",
              "build",
              "started",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
