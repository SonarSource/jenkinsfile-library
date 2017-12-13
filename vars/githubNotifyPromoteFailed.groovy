#!/usr/bin/groovy

def call() {
  githubNotify('failed',
              'Promoted on cix',
              'cix-promotion')
}
