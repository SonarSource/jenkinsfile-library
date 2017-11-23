#!/usr/bin/groovy

def String call(timestamp) {
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
  return sdf.format(timestamp)
}
