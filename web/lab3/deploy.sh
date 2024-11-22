#!/bin/bash

echo "Deploying to Helios"

## Remove existing deployment
ssh -p 2222 s408402@se.ifmo.ru "rm -rf wildfly26/standalone/deployments/lab3.war"
# add new deployment
scp -P 2222 ./target/lab3.war s408402@se.ifmo.ru:wildfly26/standalone/deployments