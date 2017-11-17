#!/usr/bin/groovy

def call(def body) {
  def javaHome = tool name: 'Java 8', type: 'jdk'
  withEnv(["JAVA_HOME=${javaHome}"]) {
    body.call()
  }
}