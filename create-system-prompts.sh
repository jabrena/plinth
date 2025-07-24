#!/bin/bash

# Script to copy .cursor/rules content to system-prompts folder

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Check if .cursor/rules directory exists
if [ ! -d ".cursor/rules" ]; then
    print_error ".cursor/rules directory not found in current directory"
    print_info "Please run this script from the root of your project where .cursor/rules exists"
    exit 1
fi

# Check if system-prompts directory already exists
if [ -d "system-prompts" ]; then
    print_warning "system-prompts directory already exists"
    echo -n "Do you want to overwrite it? (y/N): "
    read -r overwrite_response
    case $overwrite_response in
        [Yy]* )
            print_info "Removing existing system-prompts directory..."
            rm -rf system-prompts
            ;;
        * )
            print_info "Operation cancelled"
            exit 0
            ;;
    esac
fi

# Ask user if they want to proceed
echo ""
print_info "This script will:"
print_info "  1. Create a new 'system-prompts' directory"
print_info "  2. Copy all files from '.cursor/rules' to 'system-prompts'"
echo ""

# Count files to be copied
file_count=$(find .cursor/rules -type f | wc -l)
print_info "Found $file_count files to copy"

echo ""
echo -n "Do you want to proceed? (y/N): "
read -r response

case $response in
    [Yy]* )
        print_info "Creating system-prompts directory..."
        
        # Create the directory
        mkdir -p system-prompts
        
        if [ $? -eq 0 ]; then
            print_success "system-prompts directory created"
        else
            print_error "Failed to create system-prompts directory"
            exit 1
        fi
        
        # Copy files
        print_info "Copying files from .cursor/rules to system-prompts..."
        
        # Use cp with recursive flag to copy all files and subdirectories
        cp -r .cursor/rules/* system-prompts/
        
        if [ $? -eq 0 ]; then
            # Count copied files
            copied_count=$(find system-prompts -type f | wc -l)
            print_success "Successfully copied $copied_count files to system-prompts/"
            
            # List the copied files
            echo ""
            print_info "Copied files:"
            find system-prompts -type f -exec basename {} \; | sort
            
        else
            print_error "Failed to copy files"
            exit 1
        fi
        ;;
    * )
        print_info "Operation cancelled"
        exit 0
        ;;
esac

echo ""
print_success "Script completed successfully!"
print_info "You can now use files from system-prompts/ directory"