#!/usr/bin/groovy

def Map call() {
  def owner = env.GITHUB_REPOSITORY_OWNER ?: env.GITHUB_REPOSITORY_OWNER_NAME
  def project = env.CI_BUILD_NAME
  def url = "https://github.com/${owner}/${project}"
  def buildNumber = env.CI_BUILD_NUMBER  ?: env.BUILD_NUMBER
  def branch = env.GITHUB_BRANCH
  def commit = env.GIT_SHA1
  if (!env.CI_BUILD_NAME) {
    def trimedUrl = env.GIT_URL.reverse().drop(4).reverse()
    def splitUrl = trimedUrl.drop(8).split("/")
    url = trimedUrl
    owner = splitUrl[1]
    project = splitUrl[2]
    buildNumber = env.BUILD_NUMBER
    branch = env.GIT_BRANCH
    commit = env.GIT_COMMIT
  }
  return [url:url,
          owner:owner,
          project:project,
          buildNumber:buildNumber,
          branch:branch,
          commit:commit]
}