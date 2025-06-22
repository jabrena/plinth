#!/bin/bash

echo "Building AWS Lambda with GraalVM Native Image..."

# Check if GraalVM is installed
if ! command -v native-image &> /dev/null; then
    echo "Error: GraalVM native-image is not installed or not in PATH"
    echo "Please install GraalVM and make sure native-image is available"
    exit 1
fi

# Clean previous builds
echo "Cleaning previous builds..."
mvn clean

# Compile with native profile
echo "Compiling with GraalVM native profile..."
mvn -Pnative package

# Check if native image was created successfully
if [ -f "target/aws-lambda-hello-world" ]; then
    echo "Native image built successfully: target/aws-lambda-hello-world"
    echo "File size: $(du -h target/aws-lambda-hello-world | cut -f1)"
    echo ""
    echo "To test locally, run:"
    echo "./target/aws-lambda-hello-world"
    echo ""
    echo "To package for AWS Lambda deployment, create a bootstrap file:"
    echo "cp target/aws-lambda-hello-world bootstrap"
    echo "zip aws-lambda-native.zip bootstrap"
else
    echo "Error: Native image compilation failed"
    exit 1
fi 