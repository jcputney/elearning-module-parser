#!/bin/bash

# Function to fix a class by removing builder and adding setters
fix_class() {
    local file="$1"
    echo "Fixing $file..."
    
    # Create a temporary file
    temp_file=$(mktemp)
    
    # Flag to track if we're inside a builder class
    in_builder=0
    builder_depth=0
    
    while IFS= read -r line; do
        # Check if we're entering a builder class
        if [[ $line =~ "static class".*"Builder" ]] || [[ $line =~ "public static class".*"Builder" ]] || [[ $line =~ "public static final class".*"Builder" ]]; then
            in_builder=1
            builder_depth=1
            continue
        fi
        
        # If we're in a builder, track braces to know when it ends
        if [ $in_builder -eq 1 ]; then
            # Count opening braces
            open_braces=$(echo "$line" | grep -o '{' | wc -l)
            close_braces=$(echo "$line" | grep -o '}' | wc -l)
            builder_depth=$((builder_depth + open_braces - close_braces))
            
            # If we've closed all braces for the builder, we're done with it
            if [ $builder_depth -le 0 ]; then
                in_builder=0
            fi
            continue
        fi
        
        # If not in builder, write the line
        echo "$line" >> "$temp_file"
    done < "$file"
    
    # Replace the original file
    mv "$temp_file" "$file"
}

# Find all Java files that likely have issues
echo "Finding and fixing Java files with builder issues..."

# Fix files with syntax errors from builders
files_to_fix=(
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/LangString.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/Identifier.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/UnboundLangString.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/CatalogEntry.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/SingleLangString.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/Taxon.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/Resource.java"
    "src/main/java/dev/jcputney/elearning/parser/input/lom/types/TaxonPath.java"
)

for file in "${files_to_fix[@]}"; do
    if [ -f "$file" ]; then
        fix_class "$file"
    fi
done

# Find all files that have orphaned builder classes
find src/main/java -name "*.java" -exec grep -l "Builder()" {} \; | while read file; do
    # Check if this has an orphaned builder (no class declaration)
    if grep -q "^[[:space:]]*[A-Z][a-zA-Z]*Builder()" "$file"; then
        echo "Found orphaned builder in $file"
        fix_class "$file"
    fi
done

echo "Done fixing classes"