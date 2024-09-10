#bin/bash

# This script is used to run all the scripts in the project
javac src/*.java -d bin/
cd bin/
java App
cd ..