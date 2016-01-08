def gitAPI = new URL("https://api.github.com/users/CDhoch2/repos")
def repos = new groovy.json.JsonSlurper().parse(gitAPI.newReader())
def artifactRepoURL = "http://52.29.31.102:8081/content/repositories/snapshots/"
def artifactRepoID = "nexus"

def mavenGoal = "test -Dmovie-database-url=http://ec2-52-29-100-175.eu-central-1.compute.amazonaws.com"

def postProjects = ["p1-deploy","p2-deploy"]

//Namen der jeweiligen Repo- bzw. Projekte
def testRepoName = "movie-database-test"

repos.each
{
  def repoName = it.name
  def gitCloneUrl = it.clone_url	//Die Git-URL, die zum Klonen verwendet wird (d.h. mit *.git am Ende)
  def gitProjectUrl = it.html_url	//Die normale Web-URL des Projekts/Repos
  
  if(repoName == testRepoName) 
  {
    println "Erstelle Job: ${repoName}"
    println "Git-Clone-URL: ${gitCloneUrl}"
    
    mavenJob("DSL-Test - ${repoName}")
    {
      properties 
      {
        githubProjectUrl(gitProjectUrl)
      }
      
      description("Dieser Job wurde automatisch via Job-DSL erstellt!")
      
      scm 
      {
        git(gitCloneUrl, "*/master")
      }
      
      triggers
      {
        scm('H/5 * * * *') 
      }
      
      rootPOM("pom.xml")
      
      goals(mavenGoal)
      
      postBuildSteps
      {
        downStreamParameterized
        {
          trigger(postProjects)
        }
      }
    }
  }
}
