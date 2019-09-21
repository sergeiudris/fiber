FROM ubuntu:18.04

RUN apt-get update && \
    apt-get install -y sudo && \
    apt-get install -y curl && \
    apt-get install -y wget && \
    apt-get install -y git-core && \
    apt-get install -y gpg && \
    apt-get install -y rlwrap && \
    apt-get install -y software-properties-common && \
    apt-get install -y unzip curl net-tools lsof

RUN apt-get update && \
    apt-get install -y openjdk-8-jdk

ENV CLOJURE_SCRIPT_VERSION=linux-install-1.10.1.466.sh

RUN curl -O https://download.clojure.org/install/$CLOJURE_SCRIPT_VERSION && \
    chmod +x $CLOJURE_SCRIPT_VERSION && \
    sudo ./$CLOJURE_SCRIPT_VERSION

WORKDIR /tmp

ENV LEIN_VERSION=2.9.1
ENV LEIN_INSTALL=/usr/local/bin/

# Download the whole repo as an archive
RUN mkdir -p $LEIN_INSTALL \
  && wget -q https://raw.githubusercontent.com/technomancy/leiningen/$LEIN_VERSION/bin/lein-pkg \
  # && echo "Comparing lein-pkg checksum ..." \
  # && echo "019faa5f91a463bf9742c3634ee32fb3db8c47f0 *lein-pkg" | sha1sum -c - \
  && mv lein-pkg $LEIN_INSTALL/lein \
  && chmod 0755 $LEIN_INSTALL/lein \
  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip \
  && wget -q https://github.com/technomancy/leiningen/releases/download/$LEIN_VERSION/leiningen-$LEIN_VERSION-standalone.zip.asc \
  && rm leiningen-$LEIN_VERSION-standalone.zip.asc \
  && mkdir -p /usr/share/java \
  && mv leiningen-$LEIN_VERSION-standalone.zip /usr/share/java/leiningen-$LEIN_VERSION-standalone.jar

ENV PATH=$PATH:$LEIN_INSTALL
ENV LEIN_ROOT 1

# ENV DATOMIC_VERSION 0.9.5786  no
# ENV DATOMIC_VERSION 0.9.5561 yes 
# ENV DATOMIC_VERSION 0.9.5783 no
# ENV DATOMIC_VERSION 0.9.5703 no
# ENV DATOMIC_VERSION 0.9.5697 no  https://forum.datomic.com/t/important-security-update-0-9-5697/379
# ENV DATOMIC_VERSION 0.9.5661

ENV DATOMIC_VERSION 0.9.5703.21



ENV DATOMIC_HOME /opt/datomic-free
# ENV DATOMIC_DATA $DATOMIC_HOME/data


WORKDIR /tmp

RUN curl -SL https://my.datomic.com/downloads/free/${DATOMIC_VERSION} -o /tmp/datomic.zip \
  && unzip /tmp/datomic.zip -d /opt \
  && mv /opt/datomic-free-${DATOMIC_VERSION} $DATOMIC_HOME \
  && rm -f /tmp/datomic.zip

# RUN mkdir -p /opt/datomic-pro
WORKDIR $DATOMIC_HOME
ADD config $DATOMIC_HOME/config

EXPOSE 4334 4335 4336 9000



