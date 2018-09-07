#!/usr/bin/groovy

def call(String name, String branch){
  try {
    googleStorageDownload credentialsId:'cix-94919', bucketUri:'gs://slave-build-cache/'+ CI_BUILD_NAME+'/'+branch+'/gradleCache-'+name+'.tar.gz', localDirectory:'.', pathPrefix: CI_BUILD_NAME+'/'+branch
    sh 'mkdir -p $HOME/.gradle/caches/.eslintcache/ \
      && mkdir -p $HOME/.gradle/caches/.jestcache/ \
      && tar --warning=no-timestamp -xzf gradleCache-'+name+'.tar.gz -C $HOME/.gradle/caches/ \
      && rm gradleCache-'+name+'.tar.gz'      
  } catch (e) {
    if (!branch.equals('master')){
      gcpDownloadGradleCache(name,'master')
    }else{
      echo 'no cache available'
    }
  }
}