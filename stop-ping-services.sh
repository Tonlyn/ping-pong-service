#!/bin/bash

# Function to check and kill processes on a specific port
check_and_kill() {
    local port=$1
    # Find the process ID (PID) using the specified port
    pid=$(lsof -t -i:$port)

    if [ -n "$pid" ]; then
        echo "Port $port is occupied by Java process with PID $pid. Killing it..."
        kill -9 $pid
        if [ $? -eq 0 ]; then
            echo "Process $pid on port $port has been killed."
        else
            echo "Failed to kill process $pid on port $port."
        fi
    else
        echo "No Java process found on port $port."
    fi
}

# Check and kill processes on ports 8081 and 8082
check_and_kill 8081
check_and_kill 8082
check_and_kill 8083
