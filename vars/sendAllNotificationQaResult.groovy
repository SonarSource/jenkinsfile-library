#!/usr/bin/groovy

def call(metadata) { 
  burgrNotifyQaResult(metadata)
  githubNotifyQaResult(metadata)    
}
