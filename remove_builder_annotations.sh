#!/bin/bash

# Remove @JsonDeserialize(builder = ...) annotations
echo "Removing @JsonDeserialize(builder=...) annotations..."
find src/main/java -name "*.java" -exec sed -i '' '/@JsonDeserialize.*builder.*)/d' {} \;

# Remove @JsonPOJOBuilder annotations and the following lines
echo "Removing @JsonPOJOBuilder annotations..."
find src/main/java -name "*.java" -exec sed -i '' '/@JsonPOJOBuilder/,/^[[:space:]]*$/d' {} \;

echo "Done removing builder annotations"