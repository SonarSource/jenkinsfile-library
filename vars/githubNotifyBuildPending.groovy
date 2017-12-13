#!/usr/bin/groovy

def call() {
  githubNotify('pending',
              'Building on cix',
              'cix-build')
}
