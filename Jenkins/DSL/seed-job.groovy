def gitAPI = new URL("https://api.github.com/users/CDhoch2/repos")
def repos = new groovy.json.JsonSlurper().parse(gitAPI.newReader())

repos.each
{
  def name = it.name
  def gitCloneUrl = it.clone_url	//Die Git-URL, die zum Klonen verwendet wird (d.h. mit *.git am Ende)
  def gitProjectUrl = it.html_url	//Die normale Web-URL des Projekts/Repos
  
  def artifactRepoURL = "http://52.29.31.102:8081/content/repositories/snapshots/"
  def artifactRepoID = "nexus"
  
  if(name == "movie-database-movies") //if-Beschränkung gilt nur fürs Testen der DSL (muss später weg!)
  {
    println "Erstelle Job: ${name}"
    println "Git-Clone-URL: ${gitCloneUrl}"
    
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
        def shellCmd_GitCommits = '''cd /var/lib/jenkins/jobs/movie-database-actors/workspace/
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
        
        shell(shellCmd_GitCommits)
        
        environmentVariables
        {
          propertiesFile("env.properties")
        }
        
        def shellCmd_DockerBuild = '''cd Jenkins
sudo sh docker-build.sh'''
        
        shell(shellCmd_DockerBuild)
        
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
                  predefinedProp("ACTORS_GIT_ID", "\$GIT_ACTORS")
                  predefinedProp("MOVIES_GIT_ID", "\$GIT_COMMIT")
                  predefinedProp("SHOP_REST_GIT_ID", "\$GIT_SHOP_REST")
                  predefinedProp("SHOP_APP_GIT_ID", "\$GIT_SHOP_APP")
                  predefinedProp("NAVIGATION_GIT_ID", "\$GIT_NAVIGATION")
                  predefinedProp("MONITORING_GIT_ID", "\$GIT_MONITORING")
                }
            }
        }
      }
    }
  }
}
