#!/usr/bin/groovy

def call() {
    burgrNotify(env.GITHUB_REPOSITORY_OWNER, env.CI_BUILD_NAME, env.CI_BUILD_NUMBER, env.GITHUB_BRANCH, env.GIT_SHA1, "QA", "qa", "passed", currentBuild.getStartTimeInMillis(),System.currentTimeMillis())    
}