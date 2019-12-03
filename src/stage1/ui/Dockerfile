FROM fiber.base2

WORKDIR /opt/app

COPY deps.edn .
RUN clojure -A:cache -Stree

EXPOSE 9500 8899 9630