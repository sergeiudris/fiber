#!/bin/bash


dc(){

    docker-compose --compatibility \
        -f docker/base.yml \
        -f docker/datomic.yml \
        -f docker/stage1.yml \
        "$@"
}

up(){
    dc up -d --build
}

down(){
    dc down 
}

term(){
   dc exec $1 bash -c "bash;"
}

link_data(){
    ln -s ../../fiber.data data/fiber.data
}

link_space_srv() {
    SPACE=srv
    mkdir -p space/$SPACE
    ln -s ../../stage1/app/src space/$SPACE/app
    ln -s ../../stage1/fiber/src space/$SPACE/fiber
    ln -s ../../.vscode space/$SPACE/.vscode
    ln -s ../../stage1/app/deps.edn space/$SPACE/deps.edn
}
link_space_cln() {
    SPACE=cln
    mkdir -p space/$SPACE
    ln -s ../../stage1/ui/src space/$SPACE/ui
    ln -s ../../stage1/ui/shadow-cljs.edn space/$SPACE/shadow-cljs.edn
    ln -s ../../stage1/ui/resources/public/css space/cln/css
    ln -s ../../stage1/fiber/src space/$SPACE/fiber
    ln -s ../../.vscode space/$SPACE/.vscode
}

reset_spaces(){
    rm -rf space
    link_space_srv
    link_space_cln
}

"$@"