Grade Calculator (CMSC 203)
A Java command-line tool that processes student scores and configuration files to generate detailed grade reports. This project was developed to demonstrate mastery of file I/O, nested loops, and defensive programming without the use of arrays.

## Key Features
File-Driven Configuration: Dynamically loads course weights from gradeconfig.txt.

Linear Data Processing: Calculates weighted averages using running totals to satisfy "no-array" constraints.

Defensive Programming: Validates user input and gracefully handles missing files or invalid data.

Command-Line Support: Supports custom file paths via String[] args.

Plus/Minus Logic: Uses modulus-based logic to determine grade brackets (e.g., 88.63 = B+).

## Usage
### Execution
Bash
# Compile
javac GradeCalculator.java

# Run with defaults
java GradeCalculator

# Run with custom files
java GradeCalculator input.txt output.txt
### Required Files
gradeconfig.txt: Contains course name, category count, and weights (totaling 100).

grades_input.txt: Contains student name followed by category names and raw scores.

## Sample Results
Overall Numeric Average: 88.63

Base Grade: B

Final Grade: B+ (via ones-digit logic)
