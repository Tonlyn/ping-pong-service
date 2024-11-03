#!/bin/bash

# 启动第1个Ping服务实例
java -jar ping-service/target/ping-service-1.0.0-SNAPSHOT.jar --spring.application.name=ping-service-1 --server.port=8081 &

# 启动第2个Ping服务实例
java -jar ping-service/target/ping-service-1.0.0-SNAPSHOT.jar --spring.application.name=ping-service-2 --server.port=8082 &

# 启动第3个Ping服务实例
#java -jar ping-service/target/ping-service-1.0.0-SNAPSHOT.jar --spring.application.name=ping-service-3 --server.port=8083 &

# 启动第4个Ping服务实例
#java -jar ping-service/target/ping-service-1.0.0-SNAPSHOT.jar --spring.application.name=ping-service-4 --server.port=8084 &


echo "All Ping services started."
