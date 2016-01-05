def gitAPI = new URL("https://api.github.com/users/CDhoch2/repos")
def repos = new groovy.json.JsonSlurper().parse(gitAPI.newReader())
def artifactRepoURL = "http://52.29.31.102:8081/content/repositories/snapshots/"
def artifactRepoID = "nexus"

//Namen der jeweiligen Repo- bzw. Projekte
def moviesRepoName = "movie-database-movies"
def actorsRepoName = "movie-database-actors"
def shopAppRepoName = "movie-database-shop-app"
def shopRestRepoName = "movie-database-shop-rest"
def monitoringRepoName = "movie-database-monitoring"
def navigationRepoName = "movie-database-navigation"


/*
  PROJEKTSPEZIFISCHE SHELL-BEFEHLE
*/

//Befehl zum Auslesen der Commit-IDs (speziell für den movies-Job !)
def moviesCmd_PostBuild_ReadCommitIDs = '''cd /var/lib/jenkins/jobs/movie-database-actors/workspace/
# Nur ein ">" damit die Datei zunächst überschrieben wird
echo "GIT_ACTORS=$(git log | head -1 | sed s/'commit '//)" > $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-app/workspace/
sudo echo "GIT_SHOP_APP=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-rest/workspace/
sudo echo "GIT_SHOP_REST=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-monitoring/workspace/
sudo echo "GIT_MONITORING=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-navigation/workspace/
sudo echo "GIT_NAVIGATION=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties'''

//Befehl zum Auslesen der Commit-IDs (speziell für den actors-Job !)
def actorsCmd_PostBuild_ReadCommitIDs = '''cd /var/lib/jenkins/jobs/movie-database-movies/workspace/
# Nur ein ">" damit die Datei zunächst überschrieben wird
echo "GIT_MOVIES=$(git log | head -1 | sed s/'commit '//)" > $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-app/workspace/
sudo echo "GIT_SHOP_APP=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-rest/workspace/
sudo echo "GIT_SHOP_REST=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-monitoring/workspace/
sudo echo "GIT_MONITORING=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-navigation/workspace/
sudo echo "GIT_NAVIGATION=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties'''

//Befehl zum Auslesen der Commits-IDs (speziell für den navigation-Job!)
def navigationCmd_PostBuild_ReadCommitIDs = '''cd /var/lib/jenkins/jobs/movie-database-movies/workspace/
# Nur ein ">" damit die Datei zunächst überschrieben wird
echo "GIT_MOVIES=$(git log | head -1 | sed s/'commit '//)" > $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-app/workspace/
sudo echo "GIT_SHOP_APP=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-shop-rest/workspace/
sudo echo "GIT_SHOP_REST=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-monitoring/workspace/
sudo echo "GIT_MONITORING=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties
cd /var/lib/jenkins/jobs/movie-database-actors/workspace/
sudo echo "GIT_ACTORS=$(git log | head -1 | sed s/'commit '//)" >> $WORKSPACE/env.properties'''

repos.each
{
  def name = it.name
  def gitCloneUrl = it.clone_url	//Die Git-URL, die zum Klonen verwendet wird (d.h. mit *.git am Ende)
  def gitProjectUrl = it.html_url	//Die normale Web-URL des Projekts/Repos
  
  if(name == moviesRepoName | name == actorsRepoName | name == navigationRepoName) 
  {
    println "Erstelle Job: ${name}"
    println "Git-Clone-URL: ${gitCloneUrl}"
    println "Artefakt-Repo-URL: ${artifactRepoURL}"
    println "Artefakt-Repo-ID: ${artifactRepoID}"
    
    mavenJob("DSL-Test - ${name}")
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
        githubPush()    
      }
      
      rootPOM("${name}/pom.xml")
      
      wrappers
      {
        credentialsBinding
        {
          usernamePassword("dockerhub_user", "dockerhub_pwd", "fuejenkins/****** (Dockerhub Jenkins User)")
          string("dockerhub_mail", "some text (DockerHub Jenkins Mail)")
        }
      }
      
      postBuildSteps('SUCCESS')
      {
        def shellCmd_GitCommits
        
        switch(name)
        {
          case moviesRepoName:
            shellCmd_GitCommits = moviesCmd_PostBuild_ReadCommitIDs
            break
          case actorsRepoName:
            shellCmd_GitCommits = actorsCmd_PostBuild_ReadCommitIDs
            break
          case navigationRepoName:
            shellCmd_GitCommits = navigationCmd_PostBuild_ReadCommitIDs
            break
        }
        
        shell(shellCmd_GitCommits)
        
        environmentVariables
        {
          propertiesFile("env.properties")
        }
        
        def shellCmd_DockerBuild = '''cd Jenkins
sudo sh docker-build.sh'''
        
        shell(shellCmd_DockerBuild)
        
        /* TODO: Noch zu klären, ob die Echos wichtig sind oder nicht ?!
        
        def shellCmd_DockerPush = '''echo "GIT_ACTORS"
echo $GIT_ACTORS
echo "GIT_SHOP_APP"
echo $GIT_SHOP_APP
echo "GIT_SHOP_REST"
echo $GIT_SHOP_REST
echo "GIT_MONITORING"
echo $GIT_MONITORING
echo "GIT_NAVIGATION"
echo $GIT_NAVIGATION
cd Jenkins
sudo sh dockerhub-push.sh'''
*/
        def shellCmd_DockerPush = '''cd Jenkins
sudo sh dockerhub-push.sh'''
        
        shell(shellCmd_DockerPush)
      }
      
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
        
        downstreamParameterized {
            trigger("tstage") {
                condition('SUCCESS')
                parameters { 
                  predefinedProp("ACTORS_GIT_ID", name == actorsRepoName ? "\$GIT_COMMIT":"\$GIT_ACTORS")
                  predefinedProp("MOVIES_GIT_ID", name == moviesRepoName ? "\$GIT_COMMIT":"\$GIT_MOVIES")
                  predefinedProp("SHOP_REST_GIT_ID", "\$GIT_SHOP_REST")
                  predefinedProp("SHOP_APP_GIT_ID", "\$GIT_SHOP_APP")
                  predefinedProp("NAVIGATION_GIT_ID", name == navigationRepoName ? "\$GIT_COMMIT":"\$GIT_NAVIGATION")
                  predefinedProp("MONITORING_GIT_ID", "\$GIT_MONITORING")
                }
            }
        }
      }
    }
  }
}
