FROM alpine

LABEL br.com.alpine.proposta.authors="Thiago Hayaci Zup"

RUN apk update && apk upgrade

RUN apk add openjdk11

RUN adduser -D proposta

USER proposta

WORKDIR /home/proposta

COPY target/*.jar proposta.jar

CMD ["java", "-jar", "proposta.jar"]