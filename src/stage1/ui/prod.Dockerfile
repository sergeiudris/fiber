FROM fiber.base2

WORKDIR /opt/app

RUN git clone https://github.com/seeris/fiber /opt/root && \
    cd /opt/root/ && \
    git checkout 8c942a6fb092330efa54dbd0ab1b0731b20bab01
COPY . .
RUN clojure -A:cache -Stree

EXPOSE 9500 7888 9630