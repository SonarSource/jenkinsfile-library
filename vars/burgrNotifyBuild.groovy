#!/usr/bin/groovy

def call(status) {
  burgrNotify("Build",
              "build",
              "${status}",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
