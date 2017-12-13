#!/usr/bin/groovy

def call() {
  githubNotify('failed',
              'QA on cix',
              'cix-qa')
}
