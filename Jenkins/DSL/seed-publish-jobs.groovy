def jobsWithoutManualParameters = ["DSL-Test - p1-deploy":"SSH-P1", "DSL-Test - p2-deploy":"SSH-P2"]
def jobsWithManualParameters = ["DSL-Test - p1-deploy (manuell)":"SSH-P1", "DSL-Test - p2-deploy (manuell)":"SSH-P2"]


def shellCommand = '''#Parameter Test
echo $ACTORS_GIT_ID
echo $MOVIES_GIT_ID
echo $SHOP_REST_GIT_ID
echo $SHOP_APP_GIT_ID
echo $NAVIGATION_GIT_ID
echo $MONITORING_GIT_ID

echo "Image Versionen setzen"
export ACTORS_VERSION=$ACTORS_GIT_ID
echo $ACTORS_VERSION
export MOVIES_VERSION=$MOVIES_GIT_ID
echo $MOVIES_VERSION
export SHOP_REST_VERSION=$SHOP_REST_GIT_ID
echo $SHOP_REST_VERSION
export SHOP_APP_VERSION=$SHOP_APP_GIT_ID
echo $SHOP_APP_VERSION
export NAVIGATION_VERSION=$NAVIGATION_GIT_ID
echo $NAVIGATION_VERSION
export MONITORING_VERSION=$MONITORING_GIT_ID
echo $MONITORING_VERSION

echo "Docker stoppen";
sudo /home/ec2-user/docker-latest stop $(sudo /home/ec2-user/docker-latest ps -a -q);
echo "Container entfernen";
sudo /home/ec2-user/docker-latest rm $(sudo /home/ec2-user/docker-latest ps -a -q);
echo "MaWi-CD update"
cd MaWi-CD;
git stash;
git pull;
echo "Docker compose up";
cd Jenkins;
cd Compose\\ ohne\\ Build/;
docker-compose up -d;'''

//Zuerst die normalen Publish-Jobs ohne manuelle Parameter erstellen
jobsWithoutManualParameters.each
{
  def jobName = it.key
  def targetServer = it.value
  
  job(jobName)
  {
    publishers {
        publishOverSsh {
            server(targetServer) {
                transferSet {
                    execCommand(shellCommand)
                }
            }
        }
    }
  }
}

//Jetzt die Publish-Jobs erstellen, die manuell mit Parametern befüllt werden müssen
jobsWithManualParameters.each
{
  def jobName = it.key
  def targetServer = it.value
  
  job(jobName)
  {
    parameters{
      stringParam("ACTORS_GIT_ID", defaultValue = null, description = null)
      stringParam("MOVIES_GIT_ID", defaultValue = null, description = null)
      stringParam("MONITORING_GIT_ID", defaultValue = null, description = null)
      stringParam("SHOP_APP_GIT_ID", defaultValue = null, description = null)
      stringParam("SHOP_REST_GIT_ID", defaultValue = null, description = null)
      stringParam("NAVIGATION_GIT_ID", defaultValue = null, description = null)
    }
      
    publishers {
        publishOverSsh {
            server(targetServer) {
                transferSet {
                    execCommand(shellCommand)
                }
            }
        }
    }
  }
}
