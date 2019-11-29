#!/bin/bash


dc(){

    docker-compose --compatibility \
        -f docker/base.yml \
        -f docker/db.yml \
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

app-logs(){
    bash c dc logs -f app    
}

remove-db(){
    docker volume rm fiber.stage1.db
}

link-data(){
    mkdir -p spaces/data
    ln -s ../../../fiber.data spaces/data/fiber.data
}

link-space-srv() {
    SPACE=srv
    mkdir -p spaces/$SPACE
    ln -s ../../src/stage1/app/src spaces/$SPACE/app
    ln -s ../../src/stage1/fiber/src spaces/$SPACE/fiber
    ln -s ../../.vscode spaces/$SPACE/.vscode
    ln -s ../../src/stage1/app/deps.edn spaces/$SPACE/deps.edn
    ln -s ../../../fiber.data spaces/$SPACE/data
}
link-space-cln() {
    SPACE=cln
    mkdir -p spaces/$SPACE
    ln -s ../../src/stage1/ui/src spaces/$SPACE/ui
    ln -s ../../src/stage1/ui/shadow-cljs.edn spaces/$SPACE/shadow-cljs.edn
    ln -s ../../src/stage1/ui/resources/public/css spaces/cln/css
    ln -s ../../src/stage1/fiber/src spaces/$SPACE/fiber
    ln -s ../../.vscode spaces/$SPACE/.vscode
}

reset-spaces(){
    rm -rf spaces
    link-data
    link-space-srv
    link-space-cln
}

prod(){

    docker-compose --compatibility \
        -f docker/base.yml \
        -f docker/prod.yml \
        "$@"
}

prod-up(){
    prod up -d --build
}

prod-down(){
    prod down 
}

prod-remove-db(){
    docker volume rm fiber.db
}

prod-term(){
   prod exec $1 bash -c "bash;"
}

prod-app-logs(){
    bash c prod logs -f app    
}

prod-push(){

    docker tag seeris/fiber.db:dev seeris/fiber.db
    docker tag seeris/fiber.app:dev seeris/fiber.app
    docker tag seeris/fiber.ui:dev seeris/fiber.ui

    docker push seeris/fiber.db:dev
    docker push seeris/fiber.app:dev
    docker push seeris/fiber.ui:dev

    docker push seeris/fiber.db
    docker push seeris/fiber.app
    docker push seeris/fiber.ui
}


"$@"