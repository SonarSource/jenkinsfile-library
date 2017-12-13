#!/usr/bin/groovy

def call() {
  githubNotify('error',
              'Building on cix',
              'cix-build')
}
