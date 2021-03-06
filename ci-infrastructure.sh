#**********************************************************************************************
# Die folgenden Schritte müssen nach der Ausführung dieses Skriptes manuell ausgeführt werden
# *.pem auf Server hochladen mit SFTP
#
# Key in Jenkins-Ordner kopieren
# sudo cp *.pem /var/lib/jenkins/
# 
# Jenkins Zugriff auf die Datei erlauben
# sudo chown jenkins:jenkins *.pem
#+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# Um bei Jenkins die sudo Passwortnachfrage abzuschalten müssen folgede Schritte ausgeführt werden: 
# sudo visudo 
# An das Ende der Datei gehen und Leerzeile einfügen, dann in neuer Zeile folgendes einfügen: 
# jenkins ALL=(ALL) NOPASSWD: ALL 
# Strg + O (Speichern) 
# Strg + X (Exit) 
# Speziell für Linux AMI noch folgede Zeile bearbeiten: 
# Defaults !requiretty
#**********************************************************************************************
#**********************************************************************************************
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

# Git Repositories clonen
# Metadatenrepository
cd /home/ec2-user/
git clone https://github.com/lro89/MaWi-CD.git
# Jenkins Konfiguation
cd /var/lib/jenkins
git clone https://github.com/CDhoch2/jenkins-config.git
