#!/usr/bin/groovy

def String call(JDK, mvnArgs, optArgs = '-Dmaven.test.redirectTestOutputToFile=true') {
  withOrchEnv {
    withJava(JDK) {      
      def mvnCommand = isUnix() ? 'mvn' : 'mvn.cmd'
      try {
        sh "${mvnCommand} ${mvnArgs} ${optArgs} -B -e -V"
      } catch(ex) {
        error("Unable to run ${mvnCommand} with ${mvnArgs}")
      }    
    }
  }  
}
