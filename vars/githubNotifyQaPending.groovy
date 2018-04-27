#!/usr/bin/groovy

def call(metadata) {
  def defaultDescription = 'QA on cix'
  def defaultContext = 'cix-qa'
  githubNotify('pending',
              metadata ? "$metadata $defaultDescription" : defaultDescription,
              metadata ? "$defaultContext-$metadata" : defaultContext)
}
