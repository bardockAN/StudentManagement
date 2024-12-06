package Menu;

import Model.*;
import Service.StudentCourseManagementImpl;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

public class StudentMenu {
    private final StudentCourseManagementImpl studentCourseManagement;
    private final Person loggedInStudent;
    private final Scanner scanner = new Scanner(System.in);

    public StudentMenu(StudentCourseManagementImpl studentCourseManagement, Person loggedInStudent) {
        this.studentCourseManagement = studentCourseManagement;
        this.loggedInStudent = loggedInStudent;
    }

    public void displayMenu() {
        System.out.println("\n+-----------------------------------+");
        System.out.println("|           STUDENT MENU            |");
        System.out.println("+-----------------------------------+");

        if (loggedInStudent instanceof UndergraduateStudent) {
            displayUndergraduateMenu();
        } else if (loggedInStudent instanceof GraduateStudent) {
            displayGraduateMenu();
        } else {
            System.out.println("Unknown student type. Please check your registration.");
        }
    }


    private void displayUndergraduateMenu() {
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("  1. View Available Courses");
            System.out.println("  2. View Available Sections");
            System.out.println("  3. Enroll in Section");
            System.out.println("  4. View Enrolled Sections");
            System.out.println("  5. Unroll from a Section");
            System.out.println("  6. Return");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableCourses();
                    break;
                case "2":
                    viewAvailableSections();
                    break;
                case "3":
                    enrollInSection();
                    break;
                case "4":
                    viewEnrolledSections();
                    break;
                case "5":
                    unrollFromSection();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }


    private void displayGraduateMenu() {
        while (true) {
            System.out.println("\nOptions:");
            System.out.println("  1. View Available Courses");
            System.out.println("  2. View Available Sections");
            System.out.println("  3. Register Thesis Topic");
            System.out.println("  4. Enroll in Section");
            System.out.println("  5. View Enrolled Sections");
            System.out.println("  6. Unroll from a Section");
            System.out.println("  7. Return");
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    viewAvailableCourses();
                    break;
                case "2":
                    viewAvailableSections();
                    break;
                case "3":
                    registerThesisTopic();
                    break;
                case "4":
                    enrollInSection();
                    break;
                case "5":
                    viewEnrolledSections();
                    break;
                case "6":
                    unrollFromSection();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        }
    }

    private void viewAvailableCourses() {
        System.out.println("Available courses:");

        String studentLevel = loggedInStudent instanceof UndergraduateStudent ? "undergrad" : "grad";

        List<Course> courses = studentCourseManagement.getAllCourses();
        boolean found = false;

        for (Course course : courses) {
            if (course.getLevel().equalsIgnoreCase(studentLevel)) {
                found = true;
                System.out.println("-------------------------------");
                System.out.println("Course ID: " + course.getCourseId());
                System.out.println("Course Name: " + course.getName());
                System.out.println("Department: " + course.getDepartment());
                System.out.println("Level: " + course.getLevel());
                System.out.println("-------------------------------");
            }
        }

        if (!found) {
            System.out.println("No available courses for your level.");
        }
    }

    private void registerThesisTopic() {
        if (loggedInStudent instanceof GraduateStudent graduateStudent) {
            System.out.print("Enter thesis topic to enroll: ");
            String thesisTopic = scanner.nextLine();
            System.out.println("Thesis topic enrolled successfully: " + thesisTopic);
            saveThesisTopicToFile(graduateStudent.getId(), thesisTopic);
        } else {
            System.out.println("Error: Only Graduate Students can enroll in a thesis topic.");
        }
    }


    private void saveThesisTopicToFile(String studentId, String thesisTopic) {
        String filePath = "D:\\Java\\StudentManagement\\src\\main\\database\\thesisTopic.csv";
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.append(studentId).append(",").append(thesisTopic).append("\n");
            System.out.println("Thesis topic saved for student ID: " + studentId);
        } catch (IOException e) {
            System.out.println("Error saving thesis topic: " + e.getMessage());
        }
    }


    private void viewAvailableSections() {
        System.out.println("All sections:");
        List<Section> sections = studentCourseManagement.getAllAvailableSections();

        if (sections.isEmpty()) {
            System.out.println("No sections available.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (Section section : sections) {
                System.out.println("-------------------------------");
                System.out.println("Course ID: " + section.getCourse().getCourseId());
                System.out.println("Course Name: " + section.getCourse().getName());
                System.out.println("Course Department: " + section.getCourse().getDepartment());
                System.out.println("Course Level: " + section.getCourse().getLevel());
                System.out.println("Semester: " + section.getSemester());
                System.out.println("Academic Year: " + section.getAcademicYear());
                System.out.println("Start Date: " + dateFormat.format(section.getStartDate()));
                System.out.println("End Date: " + dateFormat.format(section.getEndDate()));
                System.out.println("Taught By: " + section.getTaughtBy());
            }
            System.out.println("-------------------------------");
        }

    }

    private void enrollInSection() {
        System.out.print("Enter Course ID to enroll: ");
        String courseId = scanner.nextLine();
        System.out.print("Enter Semester: ");
        String semester = scanner.nextLine();
        System.out.print("Enter Academic Year: ");
        int academicYear = Integer.parseInt(scanner.nextLine());  // Fix parsing issue

        List<Section> availableSections = studentCourseManagement.getAllAvailableSections();
        Section selectedSection = null;

        for (Section section : availableSections) {
            if (section.getCourse().getCourseId().equals(courseId) &&
                    section.getSemester().equals(semester) &&
                    section.getAcademicYear() == academicYear) {
                selectedSection = section;
                break;
            }
        }

        if (selectedSection != null) {
            System.out.print("Enter your Student ID: ");
            String studentId = scanner.nextLine();

            studentCourseManagement.saveEnrolledSectionsToFile(studentId, List.of(selectedSection));

            System.out.println("Enrolled successfully in Section: " + selectedSection.getCourse().getName());
        } else {
            System.out.println("No matching section found.");
        }
    }

    private void viewEnrolledSections() {
        System.out.println("\nEnrolled Sections:");

        List<Section> enrolledSections = studentCourseManagement.getSectionsForStudentFromFile(loggedInStudent.getId());

        if (enrolledSections.isEmpty()) {
            System.out.println("No enrolled sections.");
        } else {
            enrolledSections.forEach(section -> {
                System.out.println(" - Course ID: " + section.getCourse().getCourseId() +
                        ", Course Name: " + section.getCourse().getName() +
                        ", Course Department: " + section.getCourse().getDepartment() +
                        ", Course Level: " + section.getCourse().getLevel() +
                        ", Semester: " + section.getSemester() +
                        ", Academic Year: " + section.getAcademicYear() +
                        ", Start Date: " + section.getStartDate() +
                        ", End Date: " + section.getEndDate() +
                        ", Lecturer: " + section.getTaughtBy());
            });
        }
    }

    private void unrollFromSection() {
        System.out.println("\nYour Enrolled Sections:");
        List<Section> enrolledSections = studentCourseManagement.getSectionsForStudentFromFile(loggedInStudent.getId());
        if (enrolledSections.isEmpty()) {
            System.out.println("No enrolled sections to unroll.");
            return;
        }

        enrolledSections.forEach(section -> System.out.println(" - Section ID: " + section.getCourse().getCourseId() +
                ", Course: " + section.getCourse().getName() +
                ", Professor: " + section.getTaughtBy()));

        System.out.print("Enter Course ID to unroll: ");
        String courseId = scanner.nextLine();

        Section sectionToRemove = null;
        for (Section section : enrolledSections) {
            if (section.getCourse().getCourseId().equals(courseId)) {
                sectionToRemove = section;
                break;
            }
        }

        if (sectionToRemove != null) {
            enrolledSections.remove(sectionToRemove);
            updateEnrolledSectionsFile(enrolledSections);
            System.out.println("Successfully unrolled from Section: " + sectionToRemove.getCourse().getName());
        } else {
            System.out.println("No matching section found.");
        }
    }

    private void updateEnrolledSectionsFile(List<Section> enrolledSections) {
        String filePath = "D:\\Java\\StudentManagement\\src\\main\\database\\studentSections.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (Section section : enrolledSections) {
                // Lưu thông tin mỗi section vào file
                writer.write(section.getCourse().getCourseId() + "," + section.getCourse().getName() + "," + section.getTaughtBy());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error updating enrolled sections: " + e.getMessage());
        }
    }
}