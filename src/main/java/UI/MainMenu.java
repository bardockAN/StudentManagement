package UI;

import Menu.StudentMenu;
import Menu.TeacherMenu;
import Model.*;
import Service.StudentCourseManagementImpl;
import Service.TeacherCourseManagementImpl;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class MainMenu {
    private static final Scanner scanner = new Scanner(System.in);
    private static boolean isLoggedIn = false;
    private static String loggedInRole = null;
    private static final String STUDENT_REGISTER_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\studentRegister.csv";
    private static final String TEACHER_REGISTER_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\teacherRegister.csv";

    public static void main(String[] args) {
        System.out.println("=====================================");
        System.out.println("   WELCOME TO THE MANAGEMENT SYSTEM  ");
        System.out.println("=====================================");

        while (true) {
            System.out.println("\n+-----------------------------------+");
            System.out.println("|               MAIN MENU           |");
            System.out.println("+-----------------------------------+");
            System.out.println("|  1. Log in                        |");
            System.out.println("|  2. Register                      |");
            System.out.println("|  3. Log out                       |");
            System.out.println("|  4. Exit                          |");
            System.out.println("+-----------------------------------+");
            System.out.print("\nPlease enter your choice (1-4): ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1" -> login();
                case "2" -> register();
                case "3" -> logout();
                case "4" -> {
                    System.out.println("\n=====================================");
                    System.out.println("    THANK YOU FOR USING OUR SYSTEM   ");
                    System.out.println("=====================================");
                    return;
                }
                default -> System.out.println("Invalid choice! Please enter a valid option (1-4).\n");
            }
        }
    }

    public static void login() {
        if (isLoggedIn) {
            System.out.println("\nYou are already logged in.");
            return;
        }

        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("\nSelect your role:");
        System.out.println("  1. Student");
        System.out.println("  2. Teacher");
        System.out.print("Enter your choice (1-2): ");
        String roleChoice = scanner.nextLine();

        String filePath = roleChoice.equals("1") ? STUDENT_REGISTER_FILE_PATH : TEACHER_REGISTER_FILE_PATH;
        if (validateLogin(filePath, username, password)) {
            isLoggedIn = true;
            loggedInRole = roleChoice.equals("1") ? "student" : "teacher";

            System.out.println("\n+-----------------------------------+");
            System.out.println("|         LOGIN SUCCESSFUL          |");
            System.out.println("+-----------------------------------+");

            if (loggedInRole.equals("student")) {
                StudentCourseManagementImpl studentCourseManagement = new StudentCourseManagementImpl();
                Person loggedInStudent = studentCourseManagement.findStudentByUsername(username);
                if (loggedInStudent == null) {
                    System.out.println("Error: No student found for this account. Please contact admin.");
                    return;
                }
                new StudentMenu(studentCourseManagement, loggedInStudent).displayMenu();
            } else {
                TeacherCourseManagementImpl teacherCourseManagement = new TeacherCourseManagementImpl();
                Teacher loggedInTeacher = teacherCourseManagement.findTeacherByUsername(username, password);
                if (loggedInTeacher == null) {
                    System.out.println("Error: No teacher found for this account. Please contact admin.");
                    return;
                }
                new TeacherMenu(teacherCourseManagement).displayMenu();
            }
        } else {
            System.out.println("\nInvalid credentials. Please try again.");
        }
    }

    public static void logout() {
        if (!isLoggedIn) {
            System.out.println("\nYou are not logged in.");
        } else {
            isLoggedIn = false;
            loggedInRole = null;
            System.out.println("\n+-----------------------------------+");
            System.out.println("|         LOGOUT SUCCESSFUL         |");
            System.out.println("+-----------------------------------+");
        }
    }

    private static boolean validateLogin(String filePath, String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(username)) {
                    if (data[1].equals(password)) {
                        return true;
                    } else {
                        System.out.println("Invalid password.");
                        return false;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        System.out.println("Username not found.");
        return false;
    }

    private static void register() {
        System.out.println("\nSelect your role for registration:");
        System.out.println("  1. Student");
        System.out.println("  2. Teacher");
        System.out.print("Enter your choice (1-2): ");
        String roleChoice = scanner.nextLine();

        if (roleChoice.equals("1")) {
            registerStudent();
        } else if (roleChoice.equals("2")) {
            registerTeacher();
        } else {
            System.out.println("Invalid role choice. Registration aborted.");
        }
    }

    private static void registerStudent() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (isUsernameExists(STUDENT_REGISTER_FILE_PATH, username)) {
            System.out.println("Username already exists. Please try a different username.");
            return;
        }

        System.out.println("Select student type:");
        System.out.println("  1. Undergraduate Student");
        System.out.println("  2. Graduate Student");
        System.out.print("Enter your choice (1-2): ");
        String studentTypeChoice = scanner.nextLine();

        String studentType;
        if (studentTypeChoice.equals("1")) {
            studentType = "undergrad";
        } else if (studentTypeChoice.equals("2")) {
            studentType = "grad";
        } else {
            System.out.println("Invalid choice. Registration aborted.");
            return;
        }

        System.out.print("Enter student ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter date of birth (dd/MM/yyyy): ");
        String dobStr = scanner.nextLine();
        Date dOb = null;
        try {
            dOb = new SimpleDateFormat("dd/MM/yyyy").parse(dobStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        if (studentType.equals("undergrad")) {
            System.out.print("Enter undergraduate major: ");
            String major = scanner.nextLine();
            UndergraduateStudent student = new UndergraduateStudent(name, id, dOb, major);
            new StudentCourseManagementImpl().saveStudentToCSV(student);
        } else {
            System.out.print("Enter graduate major: ");
            String major = scanner.nextLine();
            GraduateStudent student = new GraduateStudent(name, id, dOb, major);
            new StudentCourseManagementImpl().saveStudentToCSV(student);
        }

        saveToFileForTeacher(STUDENT_REGISTER_FILE_PATH, username, password, id, studentType);
        System.out.println("Student registration successful!");
    }

    private static void registerTeacher() {
        System.out.print("\nEnter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        if (isUsernameExists(TEACHER_REGISTER_FILE_PATH, username)) {
            System.out.println("Username already exists. Please try a different username.");
            return;
        }

        System.out.print("Enter teacher ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter teacher name: ");
        String name = scanner.nextLine();
        System.out.print("Enter date of birth (dd/MM/yyyy): ");
        String dobStr = scanner.nextLine();
        Date dob = null;
        try {
            dob = new SimpleDateFormat("dd/MM/yyyy").parse(dobStr);
        } catch (ParseException e) {
            System.out.println("Invalid date format.");
            return;
        }

        Teacher teacher = new Teacher(name, id, dob);
        new TeacherCourseManagementImpl().saveTeacherToCSV(username, password, teacher);
        saveToFileForStudent(TEACHER_REGISTER_FILE_PATH, username, password);
        System.out.println("Teacher registration successful!");
    }

    private static boolean isUsernameExists(String filePath, String username) {
        try (var reader = new java.io.BufferedReader(new java.io.FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.split(",")[0].equals(username)) {
                    return true;
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }
        return false;
    }

    private static void saveToFileForStudent(String filePath, String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

    private static void saveToFileForTeacher(String filePath, String username, String password, String studentId, String studentType) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath, true))) {
            writer.write(username + "," + password + "," + studentId + "," + studentType);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving to file: " + e.getMessage());
        }
    }

}
