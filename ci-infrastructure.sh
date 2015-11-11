# Installation und Konfiguration des CI-Servers
# Vorher docker-infrastructure.sh ausführen
# Jenkins Installation
sudo yum update
sudo wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat-stable/jenkins.repo
sudo rpm --import http://pkg.jenkins-ci.org/redhat-stable/jenkins-ci.org.key
sudo yum install jenkins
sudo service jenkins start
sudo chkconfig jenkins on

# Weitere Abhängigkeiten
sudo yum install nodejs npm --enablerepo=epel 
sudo npm install -g npm 
sudo npm install -g grunt-cli  
sudo npm install -g bower 
sudo npm install grunt --save-dev 
sudo yum install -y git 
sudo wget http://repos.fedorapeople.org/repos/dchen/apache-maven/epel-apache-maven.repo -O /etc/yum.repos.d/epel-apache-maven.repo 
sudo sed -i s/\$releasever/6/g /etc/yum.repos.d/epel-apache-maven.repo 
sudo yum install -y apache-maven 
