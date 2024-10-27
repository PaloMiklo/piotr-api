#!/bin/bash

PROFILES="dev"

cd "$(dirname "$0")"

if [[ "$1" =~ ^(sentry|with-sentyr|s|monitoring)$ ]]; then ./mvnw clean compile -DrunSentry=true && ./mvnw spring-boot:run -Dspring-boot.run.profiles=${PROFILES}
else ./mvnw clean compile && ./mvnw spring-boot:run -Dspring-boot.run.profiles=${PROFILES}
fi