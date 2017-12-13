#!/usr/bin/groovy

def call() {
  githubNotify('failure',
              'Building on cix',
              'cix-build')
}
