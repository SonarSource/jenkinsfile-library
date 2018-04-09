#!/usr/bin/groovy

def call() { 
  checkout scm   
  if (GITHUB_BRANCH.startsWith('PULLREQUEST-')){    
    def prNumber = GITHUB_BRANCH.minus('PULLREQUEST-')
    if (sh(returnStdout: true, script: "git branch -vv").contains("prmerge$prNumber")){
      try{      
        sh "git branch -D prmerge$prNumber"
        echo "removed already existing prmerge$prNumber branch, ready to fetch last merge commit"
      } catch(e) {
        echo "ready to checkout PR $prNumber merge commit"
      }
    }
    //fetch the PR merge commit
    sh "git pull origin pull/$prNumber/merge:prmerge$prNumber"
    //checkout the PR merge commit
    sh "git checkout prmerge$prNumber"
  }
}
