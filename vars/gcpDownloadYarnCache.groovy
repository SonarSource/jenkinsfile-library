#!/usr/bin/groovy

def call(String name, String branch){
  try {
    googleStorageDownload credentialsId:'cix-94919', bucketUri:'gs://slave-build-cache/'+ CI_BUILD_NAME+'/'+branch+'/yarnCache-'+name+'.tar.gz', localDirectory:'.', pathPrefix: CI_BUILD_NAME+'/'+branch
    sh 'mkdir -p $HOME/.cache \
      && tar --warning=no-timestamp -xzf yarnCache-'+name+'.tar.gz -C $HOME/.cache/ \
      && rm yarnCache-'+name+'.tar.gz'      
  } catch (e) {
    if (!branch.equals('master')){
      gcpDownloadGradleCache(name,'master')
    }else{
      echo 'no cache available'
    }
  }
}