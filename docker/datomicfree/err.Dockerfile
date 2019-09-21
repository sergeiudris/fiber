FROM fiber.base2

# ENV DATOMIC_VERSION 0.9.5786  no
# ENV DATOMIC_VERSION 0.9.5561 yes 
# ENV DATOMIC_VERSION 0.9.5783 no
# ENV DATOMIC_VERSION 0.9.5703 no
# ENV DATOMIC_VERSION 0.9.5697 no  https://forum.datomic.com/t/important-security-update-0-9-5697/379
ENV DATOMIC_VERSION 0.9.5661


ENV DATOMIC_HOME /opt/datomic-free
# ENV DATOMIC_DATA $DATOMIC_HOME/data


WORKDIR /tmp

RUN curl -SL https://my.datomic.com/downloads/free/${DATOMIC_VERSION} -o /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && mv /opt/datomic-free-${DATOMIC_VERSION} $DATOMIC_HOME \
  && rm -f /tmp/datomic.zip

# RUN mkdir -p /opt/datomic-pro
ADD config $DATOMIC_HOME/config

WORKDIR $DATOMIC_HOME

EXPOSE 4334 4335 4336 9000



