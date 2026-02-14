/*
 * Class: CMSC203
 * Instructor: Ashique Tanveer
 * Description: Grade Calculator
 * Due: 02/14/2026
 * Platform/compiler: iMac M3/ Java 24
 * I pledge that I have completed the programming assignment
 * independently. I have not copied the code from a student or
 * any source. I have not given my code to any student.
 * Print your Name here: ROBERT GRAVATT
 */

import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.IOException;

public class GradeCalculator {

    public static void main(String[] args) throws IOException {

        // Header
        System.out.println("========================================");
        System.out.println("CMSC203 Project 1 - Grade Calculator");
        System.out.println("========================================");

        // -------------------------------
        // 1. LOAD CONFIGURATION FILE
        // -------------------------------
        String configFileName = "gradeconfig.txt";
        String courseName = "CMSC203 Computer Science I";
        int numCategories = 3;
        boolean usingDefaultConfig = false;

        // Explicit category variables (No arrays)
        String cat1Name = "Projects";
        int cat1Weight = 40;
        String cat2Name = "Quizzes";
        int cat2Weight = 30;
        String cat3Name = "Exams";
        int cat3Weight = 30;

        File configFile = new File(configFileName);
        System.out.println("Loading configuration from " + configFileName + " ...");

        if (configFile.exists()) {
            Scanner configScanner = new Scanner(configFile);

            // Read course name and number of categories
            courseName = configScanner.nextLine();
            numCategories = configScanner.nextInt();

            int totalWeight = 0;

            // Explicitly read Category 1
            cat1Name = configScanner.next();
            cat1Weight = configScanner.nextInt();
            totalWeight += cat1Weight;

            // Explicitly read Category 2
            if (numCategories >= 2) {
                cat2Name = configScanner.next();
                cat2Weight = configScanner.nextInt();
                totalWeight += cat2Weight;
            }

            // Explicitly read Category 3
            if (numCategories == 3) {
                cat3Name = configScanner.next();
                cat3Weight = configScanner.nextInt();
                totalWeight += cat3Weight;
            }
            configScanner.close();

            // Validate the weights
            if (totalWeight != 100) {
                System.out.println("Invalid configuration. Using default configuration.");
                courseName = "CMSC203 Computer Science I";
                numCategories = 3;
                cat1Name = "Projects";  cat1Weight = 40;
                cat2Name = "Quizzes";   cat2Weight = 30;
                cat3Name = "Exams";     cat3Weight = 30;
                usingDefaultConfig = true;
            } else {
                System.out.println("Configuration loaded successfully.");
                usingDefaultConfig = false;
            }
        } else {
            System.out.println("Configuration file missing. Using default configuration.");
            usingDefaultConfig = true;
        }

        // -------------------------------
        // 2. PROCESS COMMAND-LINE ARGUMENTS
        // -------------------------------
        String inputFileName = "grades_input.txt";
        String outputFileName = "grades_report.txt";

        if (args.length == 1) {
            inputFileName = args[0];
        } else if (args.length >= 2) {
            inputFileName = args[0];
            outputFileName = args[1];
        }

        System.out.println("Using input file: " + inputFileName);
        System.out.println("Using output file: " + outputFileName);

        // -------------------------------
        // 3. READ STUDENT SCORES FILE
        // -------------------------------
        File studentFile = new File(inputFileName);
        if (!studentFile.exists()) {
            System.out.println("Error: cannot open input file.");
            return;
        }

        Scanner scoreScanner = new Scanner(studentFile);
        String firstName = scoreScanner.next();
        String lastName = scoreScanner.next();
        System.out.println("Reading student scores...");

        double overallWeightedTotal = 0;
        String categoryReport = "";

        // Loop through categories to process scores
        for (int i = 1; i <= numCategories; i++) {
            if (!scoreScanner.hasNext()) break;

            String categoryFromFile = scoreScanner.next();
            String currentName = "";
            int currentWeight = 0;

            // Explicitly determine which category we are working on
            if (i == 1) {
                currentName = cat1Name;
                currentWeight = cat1Weight;
            } else if (i == 2) {
                currentName = cat2Name;
                currentWeight = cat2Weight;
            } else if (i == 3) {
                currentName = cat3Name;
                currentWeight = cat3Weight;
            }

            // Check if the file matches our expected category
            if (!categoryFromFile.equalsIgnoreCase(currentName)) {
                System.out.println("Category mismatch for " + currentName + ". Skipping.");
                int skipCount = scoreScanner.nextInt();
                for (int s = 0; s < skipCount; s++) {
                    scoreScanner.nextDouble();
                }
                continue;
            }

            // Read the scores for this category
            int numScores = scoreScanner.nextInt();
            double sum = 0;
            for (int s = 0; s < numScores; s++) {
                sum += scoreScanner.nextDouble();
            }

            double average = sum / numScores;
            double weightedContribution = average * (currentWeight / 100.0);
            overallWeightedTotal = overallWeightedTotal + weightedContribution;

            // Build the reporting line
            categoryReport = categoryReport + currentName + " (" + currentWeight + "%): average = "
                    + String.format("%.2f", average) + "\n";
        }
        scoreScanner.close();

        // -------------------------------
        // 4. KEYBOARD INPUT: +/- GRADING
        // -------------------------------
        Scanner keyboard = new Scanner(System.in);
        System.out.print("Apply +/- grading? (Y/N): ");
        String userInput = keyboard.next();
        char firstChar = Character.toLowerCase(userInput.charAt(0));

        while (firstChar != 'y' && firstChar != 'n') {
            System.out.print("Invalid input. Please enter Y or N: ");
            userInput = keyboard.next();
            firstChar = Character.toLowerCase(userInput.charAt(0));
        }
        boolean plusMinusEnabled = (firstChar == 'y');

        // -------------------------------
        // 5. DETERMINE LETTER GRADE
        // -------------------------------
        double numeric = overallWeightedTotal;
        String letterGrade = "";

        if (numeric >= 90) {
            letterGrade = "A";
        } else if (numeric >= 80) {
            letterGrade = "B";
        } else if (numeric >= 70) {
            letterGrade = "C";
        } else if (numeric >= 60) {
            letterGrade = "D";
        } else {
            letterGrade = "F";
        }

        String baseLetter = letterGrade;

        if (plusMinusEnabled && !letterGrade.equals("F")) {
            // Ones-digit modulus logic (e.g., 88.63 becomes 88, 88 % 10 = 8)
            int onesDigit = ((int)numeric) % 10;

            if (onesDigit >= 7 || numeric >= 97) {
                letterGrade = letterGrade + "+";
            } else if (onesDigit <= 2) {
                letterGrade = letterGrade + "-";
            }
        }

        // -------------------------------
        // 6. OUTPUT SUMMARY
        // -------------------------------
        String finalSummary = "Course: " + courseName +
                "\nStudent: " + firstName + " " + lastName +
                "\nCategory Results:\n" + categoryReport +
                "Overall numeric average: " + String.format("%.2f", numeric) +
                "\nBase letter grade: " + baseLetter +
                "\nFinal letter grade: " + letterGrade +
                "\nDefault config used: " + usingDefaultConfig;

        // Print to console
        System.out.println(finalSummary);

        // Write to file
        PrintWriter writer = new PrintWriter(outputFileName);
        writer.println(finalSummary);
        writer.close();

        System.out.println("Summary written to " + outputFileName);
        System.out.println("Program complete. Goodbye!");
    }
}