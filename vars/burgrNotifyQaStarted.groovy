#!/usr/bin/groovy

def call() {
  burgrNotify("qa",
              "qa",
              "started",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
