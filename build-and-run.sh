#!/bin/bash

# Noobisoft Control Center - Docker Build and Run Script
# This script builds and runs the backend services using Docker

set -e  # Exit on any error

echo "🚀 Noobisoft Control Center - Docker Setup"
echo "=========================================="

# Check if Docker is running
if ! docker info > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

# Check if docker compose is available
if ! docker compose version &> /dev/null; then
    echo "❌ docker compose is not available. Please install Docker Compose and try again."
    exit 1
fi

# Function to display usage
show_usage() {
    echo "Usage: $0 [OPTIONS]"
    echo ""
    echo "Options:"
    echo "  -b, --build     Build and start services"
    echo "  -s, --start     Start services (without rebuilding)"
    echo "  -d, --down      Stop and remove services"
    echo "  -l, --logs      Show logs"
    echo "  -c, --clean     Clean up Docker resources"
    echo "  -h, --help      Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 --build      # Build and start all services"
    echo "  $0 --start      # Start existing services"
    echo "  $0 --down       # Stop all services"
    echo "  $0 --logs       # Show service logs"
}

# Function to build and start services
build_and_start() {
    echo "🔨 Building and starting services..."
    
    # Check if .env file exists, if not create from template
    if [ ! -f .env ]; then
        if [ -f env.example ]; then
            echo "📝 Creating .env file from template..."
            cp env.example .env
            echo "⚠️  Please review and update .env file with your configuration"
        fi
    fi
    
    # Build and start services
    docker compose up --build -d
    
    echo "✅ Services started successfully!"
    echo ""
    echo "🌐 Service URLs:"
    echo "  NeedForToken Backend: http://localhost:8070"
    echo "  Tokemon Backend:      http://localhost:8000"
    echo ""
    echo "📊 Check service status:"
    echo "  docker compose ps"
    echo ""
    echo "📋 View logs:"
    echo "  docker compose logs -f"
}

# Function to start services
start_services() {
    echo "▶️  Starting services..."
    docker compose up -d
    echo "✅ Services started!"
}

# Function to stop services
stop_services() {
    echo "⏹️  Stopping services..."
    docker compose down
    echo "✅ Services stopped!"
}

# Function to show logs
show_logs() {
    echo "📋 Service logs:"
    docker compose logs -f
}

# Function to clean up
cleanup() {
    echo "🧹 Cleaning up Docker resources..."
    
    # Stop services
    docker compose down -v
    
    # Remove unused containers, networks, and images
    docker system prune -f
    
    echo "✅ Cleanup completed!"
}

# Parse command line arguments
case "${1:-}" in
    -b|--build)
        build_and_start
        ;;
    -s|--start)
        start_services
        ;;
    -d|--down)
        stop_services
        ;;
    -l|--logs)
        show_logs
        ;;
    -c|--clean)
        cleanup
        ;;
    -h|--help|"")
        show_usage
        ;;
    *)
        echo "❌ Unknown option: $1"
        show_usage
        exit 1
        ;;
esac 