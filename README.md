# fiber

food metrics app</br>
status: early stage, dev only

<div align="center">
  <a href="./doc/resources/app-1.gif?raw=true">
  <img width="64%" src="./doc/resources/app-1.gif"></img>
  </a>
</div>


- [concept](./doc/concept/concept.md)
- [

## run 

- using [docker-compose.yml](./docker-compose.yml)

```bash
git clone https://github.com/seeris/fiber
cd fiber
docker-compose up -d # start

# follow logs (the first time it will take 5-10 mins to populate db)
docker-compose logs -f app

# stop 
docker-compose down

# remove db 
docker volume rm fiber.db

```

## development




