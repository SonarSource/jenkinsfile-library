#!/usr/bin/groovy

def call() { 
  burgrNotifyBuildStarted()
  githubNotifyBuildPending()    
}