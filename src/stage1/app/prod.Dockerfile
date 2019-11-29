FROM fiber.base2

WORKDIR /opt/app

# COPY ./.env /opt/
# ADD ./.m2/settings.xml /root/.m2/


# COPY .  /opt/code/app
# COPY project.clj  /opt/app
# RUN lein deps

# COPY .  .
# COPY c .
# COPY project.clj .
# RUN lein deps
# RUN bash c install_deps

RUN git clone https://github.com/seeris/fiber /opt/root && \
    cd /opt/root/ && \
    git checkout 8c942a6fb092330efa54dbd0ab1b0731b20bab01

COPY deps.edn .
RUN clojure -A:cache:git -Stree
COPY . .

EXPOSE 4001
EXPOSE 8080
EXPOSE 7878

