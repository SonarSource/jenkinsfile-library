#!/usr/bin/groovy

def call(metadata) {
  def defaultStep = 'qa'
  burgrNotify(metadata ? "$metadata $defaultStep" : defaultStep,
              metadata ? "$defaultStep-$metadata" : defaultStep,
              'started',
              currentBuild.getStartTimeInMillis(),
              System.currentTimeMillis())
}
