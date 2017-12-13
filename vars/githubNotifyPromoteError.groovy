#!/usr/bin/groovy

def call() {
  githubNotify('error',
              'Promoted on cix',
              'cix-promotion')
}
