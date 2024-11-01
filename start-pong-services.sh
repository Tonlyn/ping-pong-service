#!/bin/bash

port=8080
# 启动一个Pong服务实例
java -jar pong-service/target/pong-service-1.0.0-SNAPSHOT.jar --spring.application.name=pong-service --server.port=$port &


echo "Pong services started."
