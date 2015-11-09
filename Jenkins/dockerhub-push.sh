#!/usr/bin/env bash

# DockerHub Login
/home/ec2-user/docker-latest login -e ${dockerhub_mail} -u ${dockerhub_user} -p ${dockerhub_pwd}

#Tag images
/home/ec2-user/docker-latest tag -f moviedatabase_shopgui cdzwei/mvdb_shopgui:latest 
/home/ec2-user/docker-latest tag -f moviedatabase_shop cdzwei/mvdb_shop:latest
/home/ec2-user/docker-latest tag -f moviedatabase_movies cdzwei/mvdb_movies:latest 
/home/ec2-user/docker-latest tag -f moviedatabase_actors cdzwei/mvdb_actors:latest 
/home/ec2-user/docker-latest tag -f moviedatabase_navigation cdzwei/mvdb_navigation:latest 
/home/ec2-user/docker-latest tag -f moviedatabase_monitoring cdzwei/mvdb_monitoring:latest 

# Push to DockerHub
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_shopgui 
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_shop 
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_movies 
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_actors 
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_navigation 
/home/ec2-user/docker-latest push docker.io/cdzwei/mvdb_monitoring
