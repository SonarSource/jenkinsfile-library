#!/usr/bin/groovy

def call(state, description, context) {
  def data = computeGitData()
  def owner = data['owner']
  def project = data['project']
  def commit = data['commit']

  echo "Send a step notification to GITHUB: [owner: ${owner}, project: ${project}, commit: ${commit}, state: ${state}, state: ${description}, state: ${context}]"
    
  def message = """ 
  { 
    "state": "${state}", 
    "target_url": "${env.BUILD_URL}", 
    "description": "${description}", 
    "context": "${context}" 
  }   
  """
  writeFile file:"step-github.tmp", text: message
  githubCredential() {
    sh "curl -X POST -d @step-github.tmp --header \"Content-Type:application/json\" https://api.github.com/repos/${owner}/${project}/statuses/${commit}?access_token=$GITHUB_TOKEN"  
  }
}
