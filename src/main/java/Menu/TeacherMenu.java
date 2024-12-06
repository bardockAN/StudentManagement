package Menu;

import Model.*;
import Service.TeacherCourseManagement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class    TeacherMenu {
    private final TeacherCourseManagement courseManagement;
    private final Scanner scanner = new Scanner(System.in);

    public TeacherMenu(TeacherCourseManagement courseManagement) {
        this.courseManagement = courseManagement;
    }

    public void displayMenu() {
        while (true) {
            System.out.println("\n=====================================");
            System.out.println("            TEACHER MENU             ");
            System.out.println("=====================================");
            System.out.println("Please choose an option:");
            System.out.println("1.  Add Course");
            System.out.println("2.  Edit Course");
            System.out.println("3.  Remove Course");
            System.out.println("4.  View All Courses");
            System.out.println("5.  Add Section");
            System.out.println("6.  Edit Section");
            System.out.println("7.  Remove Section");
            System.out.println("8.  View All Sections");
            System.out.println("9.  Return ");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addCourse();
                case 2 -> editCourse();
                case 3 -> removeCourse();
                case 4 -> viewAllCourses();
                case 5 -> addSection();
                case 6 -> editSection();
                case 7 -> removeSection();
                case 8 -> viewAllSections();
                case 9 -> {
                    System.out.println("Returning back...");
                    return;
                }
                default -> System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private void addCourse() {
        System.out.print("Enter Course Name: ");
        String name = scanner.nextLine();
        System.out.print("Enter Course ID: ");
        String courseId = scanner.nextLine();
        System.out.print("Enter Department: ");
        String department = scanner.nextLine();
        System.out.print("Enter Level (grad/undergrad): ");
        String level = scanner.nextLine();

        Course course = new Course(name, courseId, department, level);
        courseManagement.addCourse(course);

        System.out.println("Course added successfully!");
    }

    private void editCourse() {
        System.out.print("Enter Course ID to edit: ");
        String courseId = scanner.nextLine();

        Course course = courseManagement.findCourseById(courseId);
        if (course != null) {
            System.out.print("Enter new course name: ");
            String newName = scanner.nextLine();
            System.out.print("Enter new department: ");
            String newDepartment = scanner.nextLine();
            System.out.print("Enter new level (grad/undergrad): ");
            String newLevel = scanner.nextLine();

            courseManagement.editCourse( courseId,newName, newDepartment, newLevel);
            System.out.println("Course updated successfully!");
        } else {
            System.out.println("Course not found.");
        }
    }

    private void removeCourse() {
        System.out.print("Enter Course ID to remove: ");
        String courseId = scanner.nextLine();

        Course course = courseManagement.findCourseById(courseId);
        if (course != null) {
            courseManagement.removeCourse(courseId);

        } else {
            System.out.println("Course not found.");
        }
    }

    private void viewAllCourses() {
        System.out.println("All courses:");
        List<Course> courses = courseManagement.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No courses available.");
        } else {
            for (Course course : courses) {
                System.out.println("-------------------------------");
                System.out.println("Course Name: " + course.getName());
                System.out.println("Course ID: " + course.getCourseId());
                System.out.println("Department: " + course.getDepartment());
                System.out.println("Level: " + course.getLevel());
            }
            System.out.println("-------------------------------");
        }
    }


    private void addSection() {
        System.out.print("Enter Course ID for the section: ");
        String courseId = scanner.nextLine();
        Course course = courseManagement.findCourseById(courseId);
        if (course != null) {
            System.out.print("Enter Start Date (dd/MM/yyyy): ");
            String startDateStr = scanner.nextLine();
            scanner.nextLine();
            System.out.print("Enter End Date (dd/MM/yyyy): ");
            String endDateStr = scanner.nextLine();
            System.out.print("Enter Professor's name: ");
            String taughtBy = scanner.nextLine();
            System.out.print("Enter Semester: ");
            String semester = scanner.nextLine();
            System.out.print("Enter Academic Year: ");
            int academicYear = scanner.nextInt();
            scanner.nextLine();

            try {
                Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
                Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);
                Section section = new Section(startDate, endDate, taughtBy, semester, academicYear, course);
                courseManagement.addSection(section);
                System.out.println("Section added successfully!");
            } catch (ParseException e) {
                System.out.println("Invalid date format.");
            }
        } else {
            System.out.println("Course not found.");
        }
    }

    private void editSection() {
        System.out.print("Enter Course ID for the section: ");
        String courseId = scanner.nextLine();
        System.out.print("Enter Semester: ");
        String semester = scanner.nextLine();
        System.out.print("Enter Academic Year: ");
        int academicYear = scanner.nextInt();
        scanner.nextLine();

        // Tìm Section để sửa
        Section section = courseManagement.findSectionByCourseAndSemester(courseId, semester, academicYear);
        if (section != null) {
            System.out.print("Enter new start date (dd/MM/yyyy): ");
            String startDateStr = scanner.nextLine();
            System.out.print("Enter new end date (dd/MM/yyyy): ");
            String endDateStr = scanner.nextLine();

            try {
                Date newStartDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
                Date newEndDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);

                // Chỉnh sửa thông tin Section
                courseManagement.editSection(courseId, semester, academicYear, newStartDate, newEndDate);

                // Sau khi chỉnh sửa, lấy lại danh sách các sections đã được cập nhật
                List<Section> sections = courseManagement.getAllSections();

                // Lưu lại thông tin Section vào file sau khi sửa
                courseManagement.saveSectionsToFile(sections);

                System.out.println("Section updated successfully!");

            } catch (ParseException e) {
                System.out.println("Invalid date format.");
            }
        } else {
            System.out.println("Section not found.");
        }
    }






    private void removeSection() {
        System.out.print("Enter Course ID for the section: ");
        String courseId = scanner.nextLine();
        System.out.print("Enter Semester: ");
        String semester = scanner.nextLine();
        System.out.print("Enter Academic Year: ");
        int academicYear = scanner.nextInt();
        scanner.nextLine();

        Section section = courseManagement.findSectionByCourseAndSemester(courseId, semester, academicYear);
        if (section != null) {
            courseManagement.removeSection(courseId, semester, academicYear);
            System.out.println("Section removed successfully!");
        } else {
            System.out.println("Section not found.");
        }
    }

    private void viewAllSections() {
        System.out.println("All sections:");
        List<Section> sections = courseManagement.getAllSections();

        if (sections.isEmpty()) {
            System.out.println("No sections available.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            for (Section section : sections) {
                System.out.println("-------------------------------");
                System.out.println("Start Date: " + dateFormat.format(section.getStartDate()));
                System.out.println("End Date: " + dateFormat.format(section.getEndDate()));
                System.out.println("Taught By: " + section.getTaughtBy());
                System.out.println("Semester: " + section.getSemester());
                System.out.println("Academic Year: " + section.getAcademicYear());
                System.out.println("Course Name: " + section.getCourse().getName());
                System.out.println("Course ID: " + section.getCourse().getCourseId());
                System.out.println("Course Department: " + section.getCourse().getDepartment());
                System.out.println("Course Level: " + section.getCourse().getLevel());
            }
            System.out.println("-------------------------------");
        }
    }

}
