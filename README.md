# Steps to run Ping Service And Pong Service

## 1. compile and package

import ping-pong-service project into IDE(such as IDEA), then click to Maven Package, ensure there target folder generated under ping-pong-service/ping-service and ping-pong-service/pong-service, which contains jar and jacoco reports.


## 2. run Ping-Pong-Service by shell

### 2.1 start pong-service

in root directory of ping-pong-service, to run the shell to start pong-service:

./start-pong-services.sh


### 2.2 start ping-service

in root directory of ping-pong-service, to run the shell to start ping-service:

./start-ping-services.sh

### 2.3 view running logs

open the directory of ping-pong-service/logs, there are log files and each log file belongs to an instance.
Open the log file to view running state of relative instance.