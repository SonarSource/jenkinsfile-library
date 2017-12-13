#!/usr/bin/groovy

def call() {
  githubNotify('pending',
              'QA on cix',
              'cix-qa')
}
