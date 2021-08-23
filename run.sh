#!/bin/bash
command=$1
profile=$2

build_project(){
  ./mvnw clean package
}

up_docker_compose(){
  docker compose -f ./docker/docker-compose.yml --profile $profile up -d
}

down_docker_compose(){
  docker compose -f ./docker/docker-compose.yml --profile $profile down
}

clean_docker(){
  docker rm $(docker ps -a -q)
  docker rmi $(docker images -q)
  docker volume prune
}

case $command in
  "start")
    if [ "$profile" = "producao" ];
    then
      build_project
    fi
    up_docker_compose
  ;;
  "end")
    down_docker_compose
    clean_docker
  ;;
  *)
    echo "Comando nao existe!!"
  ;;
esac


