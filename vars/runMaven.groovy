#!/usr/bin/groovy

def String call(JDK, mvnArgs) {
  withJava(JDK) {      
    def mvnCommand = isUnix() ? 'mvn' : 'mvn.cmd'
    try {
      sh "${mvnCommand} ${mvnArgs}"
    } catch(ex) {
      error("Unable to run ${mvnCommand} with ${mvnArgs}")
    }    
  }  
}
