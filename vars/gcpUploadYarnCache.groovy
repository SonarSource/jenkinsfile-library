#!/usr/bin/groovy

def call(String name) {
  sh 'cd $HOME/.cache/yarn \
    && tar czf $HOME/.jenkins-slave/workspace/sonar-enterprise-build/yarnCache-'+name+'.tar.gz .'
  googleStorageUpload credentialsId:'cix-94919', bucket:'gs://slave-build-cache/'+CI_BUILD_NAME+'/'+GITHUB_BRANCH, pattern:'yarnCache-'+name+'.tar.gz'
}