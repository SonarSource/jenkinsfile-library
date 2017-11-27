#!/usr/bin/groovy

def call() {
  burgrNotify("QA",
              "qa",
              "cancelled",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
