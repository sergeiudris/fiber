FROM fiber.base2

WORKDIR /opt/app

RUN git clone https://github.com/seeris/fiber /opt/root && \
    cd /opt/root/ && \
    git checkout 8c942a6fb092330efa54dbd0ab1b0731b20bab01

COPY deps.edn .
RUN clojure -A:cache:git -Stree
COPY . .
RUN bash f prod

EXPOSE 9500 8899 9630