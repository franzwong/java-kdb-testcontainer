FROM alpine:3.20.3

WORKDIR /app

ENV QHOME=/app/q
ENV PATH=${PATH}:${QHOME}/l64/

ENTRYPOINT ["rlwrap", "-r", "/app/q/l64/q"]

RUN apk update \
    && apk add --no-cache rlwrap gcompat

COPY q /app/q

COPY kc.lic /app/q
