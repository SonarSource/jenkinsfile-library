#!/usr/bin/groovy

def call() {
  burgrNotify("Build",
              "build",
              "cancelled",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
