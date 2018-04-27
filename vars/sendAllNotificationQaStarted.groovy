#!/usr/bin/groovy

def call(metadata) { 
  burgrNotifyQaStarted(metadata)
  githubNotifyQaPending(metadata)    
}
