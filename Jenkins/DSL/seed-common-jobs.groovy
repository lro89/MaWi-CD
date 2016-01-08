def gitAPI = new URL("https://api.github.com/users/CDhoch2/repos")
def repos = new groovy.json.JsonSlurper().parse(gitAPI.newReader())
def artifactRepoURL = "http://52.29.31.102:8081/content/repositories/snapshots/"
def artifactRepoID = "nexus"

//Namen der jeweiligen Repo- bzw. Projekte
def commonsRepoName = "movie-database-commons"
def securityRepoName = "movie-database-security"


repos.each
{
  def repoName = it.name
  def gitCloneUrl = it.clone_url	//Die Git-URL, die zum Klonen verwendet wird (d.h. mit *.git am Ende)
  def gitProjectUrl = it.html_url	//Die normale Web-URL des Projekts/Repos
  
  if(repoName == commonsRepoName | repoName == securityRepoName) 
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
      
      
      publishers
      {
        deployArtifacts
        {
          evenIfUnstable(evenIfUnstable = false)
          
          repositoryId(artifactRepoID)
          repositoryUrl(artifactRepoURL)
          
          // If set, assigns timestamp-based unique version number to the deployed artifacts, when their versions end with -SNAPSHOT.
          uniqueVersion(uniqueVersion = false)
        }
      }
    }
  }
}
