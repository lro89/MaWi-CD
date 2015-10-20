#!/usr/bin/env bash

# DockerHub Login
docker login -e ${dockerhub_mail} -u ${dockerhub_user} -p ${dockerhub_pwd}

#Tag images
docker tag -f moviedatabase_shopgui cdzwei/mvdb_shopgui:latest 
docker tag -f moviedatabase_shop cdzwei/mvdb_shop:latest
docker tag -f moviedatabase_movies cdzwei/mvdb_movies:latest 
docker tag -f moviedatabase_actors cdzwei/mvdb_actors:latest 
docker tag -f moviedatabase_navigation cdzwei/mvdb_navigation:latest 
docker tag -f moviedatabase_monitoring cdzwei/mvdb_monitoring:latest 

# Push to DockerHub
docker push docker.io/cdzwei/mvdb_shopgui 
docker push docker.io/cdzwei/mvdb_shop 
docker push docker.io/cdzwei/mvdb_movies 
docker push docker.io/cdzwei/mvdb_actors 
docker push docker.io/cdzwei/mvdb_navigation 
docker push docker.io/cdzwei/mvdb_monitoring
