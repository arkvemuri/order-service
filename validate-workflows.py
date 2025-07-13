#!/usr/bin/env python3
"""
Simple YAML validation script for GitHub Actions workflows
"""
import yaml
import sys
import os

def validate_yaml_file(file_path):
    """Validate a YAML file"""
    try:
        with open(file_path, 'r', encoding='utf-8') as file:
            yaml.safe_load(file)
        print(f"✅ {file_path} - Valid YAML")
        return True
    except yaml.YAMLError as e:
        print(f"❌ {file_path} - YAML Error: {e}")
        return False
    except Exception as e:
        print(f"❌ {file_path} - Error: {e}")
        return False

def main():
    """Main validation function"""
    workflows_dir = ".github/workflows"
    
    if not os.path.exists(workflows_dir):
        print(f"❌ Directory {workflows_dir} not found")
        return False
    
    yaml_files = [f for f in os.listdir(workflows_dir) if f.endswith(('.yml', '.yaml'))]
    
    if not yaml_files:
        print(f"❌ No YAML files found in {workflows_dir}")
        return False
    
    all_valid = True
    for yaml_file in yaml_files:
        file_path = os.path.join(workflows_dir, yaml_file)
        if not validate_yaml_file(file_path):
            all_valid = False
    
    if all_valid:
        print("\n🎉 All workflow files are valid!")
        return True
    else:
        print("\n💥 Some workflow files have errors!")
        return False

if __name__ == "__main__":
    success = main()
    sys.exit(0 if success else 1)