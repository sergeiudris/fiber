FROM fiber.base2

WORKDIR /opt/app

RUN git clone https://github.com/seeris/fiber /opt/root && \
    cd /opt/root/ && \
    git checkout 3173267a01ff300d48e5e49c24fc8b4ff9033652
COPY . .
RUN clojure -A:cache -Stree

EXPOSE 9500 7888 9630