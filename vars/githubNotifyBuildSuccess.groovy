#!/usr/bin/groovy

def call() {
  githubNotify('success',
              'Building on cix',
              'cix-build')
}
