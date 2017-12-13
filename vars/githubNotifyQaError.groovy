#!/usr/bin/groovy

def call() {
  githubNotify('error',
              'QA on cix',
              'cix-qa')
}
