#!/usr/bin/groovy

def call() {
  burgrNotify("Build",
              "build",
              "failed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}