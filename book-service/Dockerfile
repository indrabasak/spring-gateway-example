FROM openjdk:12-jdk

MAINTAINER Indra Basak <indra.basak1@gmail.comr>

ARG JAR_FILE
ADD target/${JAR_FILE} /usr/local/example/bin/app.jar
RUN sh -c 'touch /usr/local/example/bin/app.jar'
ADD src/docker/entrypoint.sh /usr/local/example/bin
RUN chmod 755 /usr/local/example/bin/entrypoint.sh

CMD /usr/local/example/bin/entrypoint.sh

