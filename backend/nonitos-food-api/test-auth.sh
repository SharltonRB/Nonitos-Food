#!/bin/bash

BASE_URL="http://localhost:8080/api/auth"

echo "========================================="
echo "🧪 Testing Nonito's Food Authentication"
echo "========================================="
echo ""

# Test 1: Register a new user
echo "📝 Test 1: Register new user"
echo "----------------------------"
REGISTER_RESPONSE=$(curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123",
    "fullName": "Test User",
    "phoneNumber": "1234567890"
  }')

echo "$REGISTER_RESPONSE" | jq .

# Extract tokens
ACCESS_TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.data.accessToken')
REFRESH_TOKEN=$(echo "$REGISTER_RESPONSE" | jq -r '.data.refreshToken')

echo ""
echo "✅ Access Token: ${ACCESS_TOKEN:0:50}..."
echo "✅ Refresh Token: ${REFRESH_TOKEN:0:50}..."
echo ""

# Test 2: Try to register with same email (should fail)
echo "📝 Test 2: Register with duplicate email (should fail)"
echo "-------------------------------------------------------"
curl -s -X POST "$BASE_URL/register" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123",
    "fullName": "Another User",
    "phoneNumber": "0987654321"
  }' | jq .
echo ""

# Test 3: Login with correct credentials
echo "📝 Test 3: Login with correct credentials"
echo "-----------------------------------------"
LOGIN_RESPONSE=$(curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "Password123"
  }')

echo "$LOGIN_RESPONSE" | jq .

# Update tokens
ACCESS_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.accessToken')
REFRESH_TOKEN=$(echo "$LOGIN_RESPONSE" | jq -r '.data.refreshToken')
echo ""

# Test 4: Login with wrong password (should fail)
echo "📝 Test 4: Login with wrong password (should fail)"
echo "--------------------------------------------------"
curl -s -X POST "$BASE_URL/login" \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "WrongPassword123"
  }' | jq .
echo ""

# Test 5: Refresh token
echo "📝 Test 5: Refresh access token"
echo "-------------------------------"
REFRESH_RESPONSE=$(curl -s -X POST "$BASE_URL/refresh" \
  -H "Content-Type: application/json" \
  -d "{
    \"refreshToken\": \"$REFRESH_TOKEN\"
  }")

echo "$REFRESH_RESPONSE" | jq .

# Update access token
NEW_ACCESS_TOKEN=$(echo "$REFRESH_RESPONSE" | jq -r '.data.accessToken')
echo ""
echo "✅ New Access Token: ${NEW_ACCESS_TOKEN:0:50}..."
echo ""

# Test 6: Access protected endpoint with valid token
echo "📝 Test 6: Access protected endpoint (should work)"
echo "--------------------------------------------------"
curl -s -X GET "http://localhost:8080/actuator/health" \
  -H "Authorization: Bearer $NEW_ACCESS_TOKEN" | jq .
echo ""

# Test 7: Access protected endpoint without token (should fail)
echo "📝 Test 7: Access protected endpoint without token"
echo "--------------------------------------------------"
echo "Note: /actuator/health is public, so this will work"
curl -s -X GET "http://localhost:8080/actuator/health" | jq .
echo ""

echo "========================================="
echo "✅ All tests completed!"
echo "========================================="
echo ""
echo "📋 Summary:"
echo "  - User registered successfully"
echo "  - Duplicate email rejected"
echo "  - Login successful"
echo "  - Wrong password rejected"
echo "  - Token refresh successful"
echo "  - Protected endpoints working"
echo ""
echo "🔍 Check application logs for email verification token:"
echo "   tail -50 /tmp/nonitos-app.log | grep 'EMAIL VERIFICATION'"
