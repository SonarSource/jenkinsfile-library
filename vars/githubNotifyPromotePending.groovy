#!/usr/bin/groovy

def call() {
  githubNotify('pending',
              'Promoted on cix',
              'cix-promotion')
}
