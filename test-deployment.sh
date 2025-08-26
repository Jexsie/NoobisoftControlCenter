#!/bin/bash

# Test script for Coolify deployment
# This script will help you verify that your backend services are working correctly

echo "=== Coolify Deployment Test Script ==="
echo ""

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# Function to test endpoint
test_endpoint() {
    local url=$1
    local description=$2
    
    echo -e "${YELLOW}Testing: $description${NC}"
    echo "URL: $url"
    
    # Test basic connectivity
    if curl -s --connect-timeout 10 "$url" > /dev/null; then
        echo -e "${GREEN}✓ Basic connectivity: OK${NC}"
    else
        echo -e "${RED}✗ Basic connectivity: FAILED${NC}"
        return 1
    fi
    
    # Test health endpoint
    if curl -s --connect-timeout 10 "$url/health-check" > /dev/null; then
        echo -e "${GREEN}✓ Health check: OK${NC}"
    else
        echo -e "${RED}✗ Health check: FAILED${NC}"
    fi
    
    # Test actuator health
    if curl -s --connect-timeout 10 "$url/actuator/health" > /dev/null; then
        echo -e "${GREEN}✓ Actuator health: OK${NC}"
    else
        echo -e "${RED}✗ Actuator health: FAILED${NC}"
    fi
    
    # Test CORS headers
    echo -e "${YELLOW}Testing CORS headers...${NC}"
    cors_response=$(curl -s -I -H "Origin: https://example.com" \
        -H "Access-Control-Request-Method: GET" \
        -H "Access-Control-Request-Headers: X-Requested-With" \
        -X OPTIONS "$url/health-check" 2>/dev/null)
    
    if echo "$cors_response" | grep -q "Access-Control-Allow-Origin"; then
        echo -e "${GREEN}✓ CORS headers: OK${NC}"
    else
        echo -e "${RED}✗ CORS headers: FAILED${NC}"
    fi
    
    echo ""
}

# Test Tokemon service
echo "=== Testing Tokemon Service ==="
test_endpoint "https://tokemon.open-elements.cloud" "Tokemon Backend"

# Test Needfortoken service
echo "=== Testing Needfortoken Service ==="
test_endpoint "https://needfortoken.open-elements.cloud" "Needfortoken Backend"

# Test specific endpoints
echo "=== Testing Specific Endpoints ==="

# Test createNewAccount endpoint
echo -e "${YELLOW}Testing createNewAccount endpoint...${NC}"
if curl -s --connect-timeout 10 "https://tokemon.open-elements.cloud/createNewAccount" > /dev/null; then
    echo -e "${GREEN}✓ createNewAccount endpoint: OK${NC}"
else
    echo -e "${RED}✗ createNewAccount endpoint: FAILED${NC}"
fi

echo ""

# Network connectivity test
echo "=== Network Connectivity Test ==="
echo -e "${YELLOW}Testing DNS resolution...${NC}"
if nslookup tokemon.open-elements.cloud > /dev/null 2>&1; then
    echo -e "${GREEN}✓ DNS resolution: OK${NC}"
else
    echo -e "${RED}✗ DNS resolution: FAILED${NC}"
fi

echo ""

# SSL/TLS test
echo "=== SSL/TLS Test ==="
echo -e "${YELLOW}Testing SSL certificate...${NC}"
if openssl s_client -connect tokemon.open-elements.cloud:443 -servername tokemon.open-elements.cloud < /dev/null 2>/dev/null | grep -q "Verify return code: 0"; then
    echo -e "${GREEN}✓ SSL certificate: OK${NC}"
else
    echo -e "${RED}✗ SSL certificate: FAILED${NC}"
fi

echo ""
echo "=== Test Summary ==="
echo "If you see any FAILED tests above, please check:"
echo "1. Environment variables in Coolify"
echo "2. Port configuration"
echo "3. CORS settings"
echo "4. Network connectivity"
echo "5. SSL certificate configuration"
echo ""
echo "For detailed troubleshooting, refer to COOLIFY_DEPLOYMENT.md" 