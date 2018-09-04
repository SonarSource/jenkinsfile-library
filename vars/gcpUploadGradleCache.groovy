#!/usr/bin/groovy

def call(String name) {
  sh 'rm -f  $HOME/.gradle/caches/modules-2/modules-2.lock \
    && rm -fr $HOME/.gradle/caches/*/plugin-resolution/ \
    && rm -fr $HOME/.gradle/caches/*/fileHashes/ \
    && rm -fr $HOME/.gradle/caches/*/scripts/ \
    && cd $HOME/.gradle/caches/ \
    && tar czf $HOME/.jenkins-slave/workspace/sonar-enterprise-build/gradleCache-'+name+'.tar.gz .'
  googleStorageUpload credentialsId:'cix-94919', bucket:'gs://slave-build-cache/'+CI_BUILD_NAME+'/'+GITHUB_BRANCH, pattern:'gradleCache-'+name+'.tar.gz'
}