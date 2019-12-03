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

COPY deps.edn .
RUN clojure -A:dev:cache -Stree

EXPOSE 4001
EXPOSE 8080
EXPOSE 7788

