#!/bin/bash

# Define protocols and ports for the servers
PROTOCOL=https
GAME1_SERVER_PORT=8443
GAME1_FRONTEND_PORT=3001
GAME2_SERVER_PORT=8444
GAME2_FRONTEND_PORT=3002
GENERAL_SERVER_PORT=8445
GENERAL_FRONTEND_PORT=3003

# Start game1-server
echo "Starting game1-server on port $GAME1_SERVER_PORT"
cd needfortoken
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$GAME1_SERVER_PORT &
GAME1_SERVER_PID=$!
cd ..

# Start game1-frontend
echo "Starting game1-frontend on port $GAME1_FRONTEND_PORT"
cd needfortoken-frontend
npm install
npm run dev -- --port $GAME1_FRONTEND_PORT &
GAME1_FRONTEND_PID=$!
cd ..

# Start game2-server
echo "Starting game2-server on port $GAME2_SERVER_PORT"
cd tokemon
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$GAME2_SERVER_PORT &
GAME2_SERVER_PID=$!
cd ..

# Start game2-frontend
echo "Starting game2-frontend on port $GAME2_FRONTEND_PORT"
cd tokemon-frontend
npm install
npm run dev -- --port $GAME2_FRONTEND_PORT &
GAME2_FRONTEND_PID=$!
cd ..

# Start general-server
echo "Starting general-server on port $GENERAL_SERVER_PORT"
cd Noobisoft-server
mvn clean install
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=$GENERAL_SERVER_PORT &
GENERAL_SERVER_PID=$!
cd ..

# Start general-frontend
echo "Starting general-frontend on port $GENERAL_FRONTEND_PORT"
cd noobisoft-frontend
npm install
npm run dev -- --port $GENERAL_FRONTEND_PORT &
GENERAL_FRONTEND_PID=$!
cd ..

# Function to terminate all running servers
terminate_servers() {
  echo "Terminating all servers..."
  kill $GAME1_SERVER_PID $GAME1_FRONTEND_PID $GAME2_SERVER_PID $GAME2_FRONTEND_PID $GENERAL_SERVER_PID $GENERAL_FRONTEND_PID
}

# Trap SIGINT and SIGTERM to terminate all servers gracefully
trap terminate_servers SIGINT SIGTERM

# Wait for all background processes to finish
wait $GAME1_SERVER_PID $GAME1_FRONTEND_PID $GAME2_SERVER_PID $GAME2_FRONTEND_PID $GENERAL_SERVER_PID $GENERAL_FRONTEND_PID
