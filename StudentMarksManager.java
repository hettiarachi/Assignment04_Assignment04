import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

// Class to represent a student
class Student {
    String name;
    String studentID;
    int assignment1;
    int assignment2;
    int assignment3;
    int totalMarks;

    public Student(String name, String studentID, int assignment1, int assignment2, int assignment3) {
        this.name = name;
        this.studentID = studentID;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.assignment3 = assignment3;
        this.totalMarks = assignment1 + assignment2 + assignment3;
    }

    @Override
    public String toString() {
        return String.format("%s (ID: %s) - A1: %d, A2: %d, A3: %d, Total: %d", name, studentID, assignment1, assignment2, assignment3, totalMarks);
    }
}

public class StudentMarksManager {

    private static List<Student> students = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("Menu:");
            System.out.println("1. Load data from file");
            System.out.println("2. Print student marks");
            System.out.println("3. Filter students by marks");
            System.out.println("4. Sort and print top/bottom students");
            System.out.println("5. Exit");

            int choice = scanner.nextInt();
            scanner.nextLine();  // Consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter the filename: ");
                    String filename = scanner.nextLine();
                    loadData(filename);
                    break;
                case 2:
                    printStudentMarks();
                    break;
                case 3:
                    System.out.print("Enter the threshold for filtering: ");
                    int threshold = scanner.nextInt();
                    scanner.nextLine();  // Consume newline
                    filterStudents(threshold);
                    break;
                case 4:
                    sortAndPrintStudents();
                    break;
                case 5:
                    running = false;
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
                    break;
            }
        }
        scanner.close();
    }

    // Load student data from file
    private static void loadData(String filename) {
        students.clear();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            boolean hasData = false;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("#") || line.trim().isEmpty()) {
                    continue;  // Skip comments and empty lines
                }
                hasData = true;
                String[] parts = line.split(",");
                if (parts.length != 5) {
                    System.out.println("Error reading file: Incorrect data format in line " + line);
                    continue;  // Skip incorrectly formatted lines
                }
                try {
                    String name = parts[0].trim();
                    String studentID = parts[1].trim();
                    int assignment1 = Integer.parseInt(parts[2].trim());
                    int assignment2 = Integer.parseInt(parts[3].trim());
                    int assignment3 = Integer.parseInt(parts[4].trim());
                    students.add(new Student(name, studentID, assignment1, assignment2, assignment3));
                } catch (NumberFormatException e) {
                    System.out.println("Error reading file: Invalid number format in line " + line);
                }
            }
            if (!hasData) {
                System.out.println("Error: No data loaded. The file is empty.");
            } else {
                System.out.println("Data loaded successfully.");
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
    }

    // Print student marks
    private static void printStudentMarks() {
        if (students.isEmpty()) {
            System.out.println("No data available. Please load data first.");
            return;
        }
        for (Student student : students) {
            System.out.println(student);
        }
    }

    // Filter students by total marks
    private static void filterStudents(int threshold) {
        if (students.isEmpty()) {
            System.out.println("No data available. Please load data first.");
            return;
        }
        System.out.println("Students with total marks less than " + threshold + ":");
        boolean found = false;
        for (Student student : students) {
            if (student.totalMarks < threshold) {
                System.out.println(student);
                found = true;
            }
        }
        if (!found) {
            System.out.println("No students found with total marks less than " + threshold + ".");
        }
    }

    // Sort and print top/bottom students
    private static void sortAndPrintStudents() {
        if (students.isEmpty()) {
            System.out.println("No data available. Please load data first.");
            return;
        }

        // Sort by total marks
        List<Student> sortedStudents = new ArrayList<>(students);
        sortedStudents.sort(Comparator.comparingInt(student -> student.totalMarks));

        System.out.println("Top 5 students with highest total marks:");
        for (int i = sortedStudents.size() - 1; i >= Math.max(sortedStudents.size() - 5, 0); i--) {
            System.out.println(sortedStudents.get(i));
        }

        System.out.println("\nBottom 5 students with lowest total marks:");
        for (int i = 0; i < Math.min(5, sortedStudents.size()); i++) {
            System.out.println(sortedStudents.get(i));
        }
    }
}
