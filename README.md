# 程序运行步骤

## 1.编译打包
将工程导入IDE（如IDEA）进行编译打包，确保ping-pong-service/ping-service、ping-pong-service/pong-service分别生成target目录，里面包含jar包、jacoco测试目录


## 1.执行运行脚本验证Ping-Pong服务

### 启动pong-service
在ping-pong-service根目录下，执行shell脚本，启动pong-service: ./start-pong-services.sh

### 启动ping-service
在ping-pong-service根目录下，执行shell脚本，启动ping-service: ./start-ping-services.sh

### 查看运行日志
进入ping-pong-service/logs目录，每个日志文件对应一个进程，打开文件查看各进程运行日志。