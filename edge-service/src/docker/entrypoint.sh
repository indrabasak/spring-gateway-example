#!/bin/bash

cd /usr/local/example
[[ -f config/env ]] && . config/env

java $JAVA_OPTS -jar bin/app.jar