#!/usr/bin/groovy

def call() { 
  burgrNotifyBuildResult()
  githubNotifyBuildResult()    
}
