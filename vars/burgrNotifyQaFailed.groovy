#!/usr/bin/groovy

def call() {
  burgrNotify("qa",
              "qa",
              "failed",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}