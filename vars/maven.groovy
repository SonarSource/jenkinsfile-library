#!/usr/bin/groovy

def call(args) {
    withJava {
      sh "${tool 'Maven 3.3.9'}/bin/mvn ${args}"
    }
}