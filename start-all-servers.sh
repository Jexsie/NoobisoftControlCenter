#!/bin/bash

# Define protocols and ports for the servers
PROTOCOL=https

# Start game1-server
echo "Starting game1-server"
cd needfortoken
./mvnw clean verify
./mvnw spring-boot:run &
GAME1_SERVER_PID=$!
cd ..

# Start game1-frontend
echo "Starting game1-frontend"
cd needfortoken-frontend
npm install
npm run dev &
GAME1_FRONTEND_PID=$!
cd ..

# Start game2-server
echo "Starting game2-server"
cd tokemon
./mvnw clean verify
./mvnw spring-boot:run &
GAME2_SERVER_PID=$!
cd ..

# Start game2-frontend
echo "Starting game2-frontend"
cd tokemon-frontend
npm install
npm run dev &
GAME2_FRONTEND_PID=$!
cd ..

# Start general-server
echo "Starting general-server"
cd Noobisoft-server
./mvnw clean verify
./mvnw spring-boot:run &
GENERAL_SERVER_PID=$!
cd ..

# Start general-frontend
echo "Starting general-frontend"
cd noobisoft-frontend
npm install
npm run dev &
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
