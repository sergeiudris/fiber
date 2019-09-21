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
    mkdir -p spaces/data
    ln -s ../../../fiber.data spaces/data/fiber.data
}

link_space_srv() {
    SPACE=srv
    mkdir -p spaces/$SPACE
    ln -s ../../src/stage1/app/src spaces/$SPACE/app
    ln -s ../../src/stage1/fiber/src spaces/$SPACE/fiber
    ln -s ../../.vscode spaces/$SPACE/.vscode
    ln -s ../../src/stage1/app/deps.edn space/$SPACE/deps.edn
}
link_space_cln() {
    SPACE=cln
    mkdir -p spaces/$SPACE
    ln -s ../../src/stage1/ui/src spaces/$SPACE/ui
    ln -s ../../src/stage1/ui/shadow-cljs.edn spaces/$SPACE/shadow-cljs.edn
    ln -s ../../src/stage1/ui/resources/public/css spaces/cln/css
    ln -s ../../src/stage1/fiber/src spaces/$SPACE/fiber
    ln -s ../../.vscode spaces/$SPACE/.vscode
}

reset_spaces(){
    rm -rf spaces
    link_data
    link_space_srv
    link_space_cln
}

"$@"