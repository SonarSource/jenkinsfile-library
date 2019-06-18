#!/usr/bin/groovy

def String call() {
  sh "git submodule update --init --recursive"
}
