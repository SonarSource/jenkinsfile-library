#!/usr/bin/groovy

package com.sonarsource.burgr

def fakeCommit(url, branch, commit) {
  echo 'Simulate a notification to BURGR from GitHub (push, PRs, ...)'
  def data = extractDataForBurgrFromURL(url)
  def currentDir = pwd
  echo currentDir
  def bc = readFile(this.getClass().getResource('commit-burgr.json'))
  def result = bc.toString().replaceAll("@@BRANCH@@", branch)
                              .replaceAll("@@COMMIT@@", commit)
                              .replaceAll("@@REPO_URL@@", data['url'])
                              .replaceAll("@@OWNER@@", data['owner'])
                              .replaceAll("@@PROJECT@@", data['project'])
                              .replaceAll("@@TIMESTAMP@@", data['timestamp'])
  writeFile file:"commit-burgr.tmp", text: result
  sh "curl -X POST -d @commit-burgr.tmp --header \"Content-Type:application/json\" ${env.BURGR_URL}/api/commit/github"
}

def notifyBurgr(url, branch, commit, step, type, status){
  def data = extractDataForBurgrFromURL(url)
  def bc = readFile(this.getClass().getResource('src/com/sonastep-burgr.json'))
  def result = bc.toString().replaceAll("@@BRANCH@@", branch)
                            .replaceAll("@@COMMIT@@", commit)
                            .replaceAll("@@OWNER@@", data['owner'])
                            .replaceAll("@@PROJECT@@", data['project'])
                            .replaceAll("@@TIMESTAMP@@", data['timestamp'])
                            .replaceAll("@@BUILD_NUMBER@@", env.BUILD_NUMBER)
                            .replaceAll("@@BURGR_STEP_NAME@@", step)
                            .replaceAll("@@BURGR_STEP_TYPE@@", type)
                            .replaceAll("@@BURGR_STEP_STATUS@@", status)
                            .replaceAll("@@BUILD_URL@@", env.BUILD_URL)
  writeFile file:"step-burgr.tmp", text: result
  sh "curl -X POST -d @step-burgr.tmp --header \"Content-Type:application/json\" ${env.BURGR_URL}/api/stage"
}

def Map extractDataForBurgrFromURL(url) {
  java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
  def trimedUrl = url.reverse().drop(4).reverse()
  def splitUrl = trimedUrl.drop(8).split("/")
  return [timestamp:sdf.format(currentBuild.getTimeInMillis()),
          url:trimedUrl,
          owner:splitUrl[1],
          project:splitUrl[2]]
}
