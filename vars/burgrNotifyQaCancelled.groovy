#!/usr/bin/groovy

def call() {
  burgrNotify("qa",
              "qa",
              "cancelled",
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
