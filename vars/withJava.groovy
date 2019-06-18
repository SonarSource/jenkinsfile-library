#!/usr/bin/groovy

def String call(jdk, body) {
  def javaHome = tool name: jdk, type: 'hudson.model.JDK'
  withEnv(["JAVA_HOME=${javaHome}"]) {
    body.call()
  }  
}
