#!/usr/bin/groovy

def call(def stage) {
  def shortDir = "/${env.BUILD_TAG},${stage}".replaceAll('/', ',')
  def remoteDir = "${env.CIX_WLOGS_HOME}".plus(shortDir)
  sh "ssh -o \"StrictHostKeyChecking no\" ${env.CIX_WLOGS_HOST} \"mkdir -p ${remoteDir}\""
  sh "rsync -ra -e ssh --include '*/' --include='*.log' --include='*.txt' --include='*.conf' --include='*.xml' --include='*.properties' --include='*.html' --exclude='*' --prune-empty-dirs * ${env.CIX_WLOGS_HOST}:${remoteDir}"
  sh "ssh -o \"StrictHostKeyChecking no\" ${env.CIX_WLOGS_HOST} \"zip -qr ${remoteDir}.zip ${remoteDir}\""
  echo "TO INVESTIGATE TEST FAILURE: Go to https://cix-wlogs.sonarsource.com/$shortDir"
}
