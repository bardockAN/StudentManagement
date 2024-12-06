package Service;

import Model.*;
import java.io.*;
import java.sql.SQLOutput;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class StudentCourseManagementImpl implements StudentCourseManagement {
    private static final String ENROLLED_SECTIONS_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\studentSections.csv";
    private static final String STUDENTS_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\students.csv";
    private static final String TEACHERS_SECTIONS_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\teacherSections.csv";
    private static final String COURSES_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\courses.csv";
    private static final String STUDENT_REGISTER_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\studentRegister.csv";

   /* @Override
    public void enrollSection(String studentId, String courseId, String semester, int academicYear) {
        Person student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Enrollment failed. Student not found.");
            return;
        }

        // Tìm kiếm Section từ Course, semester và academicYear
        Section sectionToEnroll = findSectionByCourseAndSemester(courseId, semester, academicYear);
        if (sectionToEnroll == null) {
            System.out.println("Section not found for the given criteria.");
            return;
        }

        // Kiểm tra xem sinh viên đã đăng ký chưa
        List<Section> enrolledSections = getSectionsForStudentFromFile(studentId);
        if (enrolledSections.contains(sectionToEnroll)) {
            System.out.println("You are already enrolled in this section.");
            return;
        }

        // Lưu thông tin đăng ký vào file studentSections.csv
        saveEnrolledSectionsToFile(studentId, List.of(sectionToEnroll));
        System.out.println("Successfully enrolled in the section: " + sectionToEnroll.getCourse().getName());
    }



    @Override
    public void unrollSection(String studentId, String courseId, String semester, int academicYear) {
        Person student = findStudentById(studentId);
        if (student == null) {
            System.out.println("Unroll failed. Student not found.");
            return;
        }

        // Tìm kiếm Section từ Course, semester và academicYear
        Section sectionToUnroll = findSectionByCourseAndSemester(courseId, semester, academicYear);
        if (sectionToUnroll == null) {
            System.out.println("Section not found for the given criteria.");
            return;
        }

        // Lấy danh sách các section sinh viên đã đăng ký từ file
        List<Section> enrolledSections = getSectionsForStudentFromFile(studentId);
        if (enrolledSections.contains(sectionToUnroll)) {
            enrolledSections.remove(sectionToUnroll);
            saveEnrolledSectionsToFile(studentId, enrolledSections);
            System.out.println("Successfully unrolled from the section: " + sectionToUnroll.getCourse().getName());
        } else {
            System.out.println("Section not found in enrolled sections.");
        }
    }*/



    @Override
    public List<Section> getAllAvailableSections() {
        List<Section> sections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_SECTIONS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 8) {

                    // Lấy thông tin của Section
                    String semester = data[3];
                    int academicYear = Integer.parseInt(data[4]);
                    Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(data[0]);
                    Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(data[1]);
                    String taughtBy = data[2];

                    // Lấy thông tin của Course
                    String courseId = data[6];
                    String courseName = data[5];
                    String department = data[7];
                    String level = data[8];

                    Course course = new Course(courseName, courseId, department, level);

                    sections.add(new Section(startDate, endDate, taughtBy, semester, academicYear, course));
                } else {
                    System.out.println("Invalid data format for section line: " + line);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading sections file: " + e.getMessage());
        }
        return sections;

    }

    @Override
    public Section findSectionByCourseAndSemester(String courseId, String semester, int academicYear) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_SECTIONS_FILE_PATH))) { // Đã sửa
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length >= 9) { // Đảm bảo đủ dữ liệu
                    String startDateStr = data[0];
                    String endDateStr = data[1];
                    String taughtBy = data[2];
                    String semesterFromFile = data[3];
                    int academicYearFromFile = Integer.parseInt(data[4]);
                    String courseName = data[5];
                    String courseIdFromFile = data[6];
                    String department = data[7];
                    String level = data[8];

                    System.out.println("Checking section: " + courseIdFromFile + ", " + semesterFromFile + ", " + academicYearFromFile);

                    if (courseIdFromFile.equals(courseId) && semesterFromFile.equalsIgnoreCase(semester) && academicYearFromFile == academicYear) {
                        Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
                        Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);

                        Course course = new Course(courseName, courseIdFromFile, department, level);
                        /*System.out.println("Section found: " + courseName);*/
                        return new Section(startDate, endDate, taughtBy, semesterFromFile, academicYearFromFile, course);
                    }
                } else {
                    System.out.println("Invalid data format for section line: " + line);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading section data: " + e.getMessage());
        }

        return null;
    }



    @Override
    public void saveEnrolledSectionsToFile(String studentId, List<Section> sections) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ENROLLED_SECTIONS_FILE_PATH, true))) {
            for (Section section : sections) {
                String line = studentId + "," + section.getCourse().getCourseId() + ","
                        + section.getCourse().getName() + ","
                        + section.getCourse().getDepartment() + ","
                        + section.getCourse().getLevel() + ","
                        + section.getSemester() + ","
                        + section.getAcademicYear() + ","
                        + new SimpleDateFormat("dd/MM/yyyy").format(section.getStartDate()) + ","
                        + new SimpleDateFormat("dd/MM/yyyy").format(section.getEndDate()) + ","
                        + section.getTaughtBy();
                writer.write(line);
                writer.newLine();
                System.out.println("Enrolled section saved: " + line); // Debug line
            }
        } catch (IOException e) {
            System.out.println("Error saving enrolled sections: " + e.getMessage());
        }
    }



    @Override
    public Person findStudentById(String studentId) {

        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data[1].equals(studentId)) {
                    Date dOb = null;
                    try {
                        dOb = new SimpleDateFormat("dd/MM/yyyy").parse(data[2]);
                    } catch (ParseException e) {
                        System.out.println("Error parsing date: " + e.getMessage());
                        continue;
                    }

                    if (data.length > 3) {
                        String major = data[3];
                        if (major != null) {
                            return new UndergraduateStudent(data[0], data[1], dOb, major);
                        }
                    } else {
                        return new Person(data[0], data[1], dOb);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student data: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Course findCourseById(String courseId) {
        List<Course> courses = getAllCourses();
        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                return course;
            }
        }
        return null;
    }


    @Override
    public void saveStudentToCSV(Person student) {
        try (FileWriter writer = new FileWriter(STUDENTS_FILE_PATH, true)) {
            if (student instanceof UndergraduateStudent) {
                UndergraduateStudent undergrad = (UndergraduateStudent) student;
                writer.append(String.format("%s,%s,%s,%s\n", undergrad.getName(), undergrad.getId(), new SimpleDateFormat("dd/MM/yyyy").format(undergrad.getdOb()), undergrad.getUndergraduateMajor()));
            } else if (student instanceof GraduateStudent) {
                GraduateStudent grad = (GraduateStudent) student;
                writer.append(String.format("%s,%s,%s,%s\n", grad.getName(), grad.getId(), new SimpleDateFormat("dd/MM/yyyy").format(grad.getdOb()), grad.getGraduateMajor()));
            } else {
                writer.append(String.format("%s,%s,%s\n", student.getName(), student.getId(), new SimpleDateFormat("dd/MM/yyyy").format(student.getdOb())));
            }
        } catch (IOException e) {
            System.out.println("Error saving student data: " + e.getMessage());
        }
    }

    @Override
    public Person findStudentByUsername(String username) {
        String studentId = null;
        String studentType = null;

        // Đọc từ studentRegister.csv để lấy studentId và studentType
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENT_REGISTER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[0].equals(username)) { // username, password, studentId, studentType
                    studentId = data[2];
                    studentType = data[3];
                    break;
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading student register file: " + e.getMessage());
        }

        if (studentId == null) {
            System.out.println("Student ID not found for username: " + username);
            return null;
        }

        // Đọc thông tin chi tiết từ students.csv dựa trên studentId
        try (BufferedReader reader = new BufferedReader(new FileReader(STUDENTS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[1].equals(studentId)) { // name, id, dOb, major
                    String name = data[0];
                    Date dOb = new SimpleDateFormat("dd/MM/yyyy").parse(data[2]);
                    String major = data[3];

                    if (studentType.equalsIgnoreCase("undergrad")) {
                        return new UndergraduateStudent(name, studentId, dOb, major);
                    } else if (studentType.equalsIgnoreCase("grad")) {
                        return new GraduateStudent(name, studentId, dOb, major);
                    } else {
                        System.out.println("Unknown student type: " + studentType);
                        return null;
                    }
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading student data file: " + e.getMessage());
        }

        System.out.println("Student details not found for student ID: " + studentId);
        return null;
    }



    @Override
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        try (BufferedReader reader = new BufferedReader(new FileReader(COURSES_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4) {
                    String name = data[0];
                    String courseId = data[1];
                    String department = data[2];
                    String level = data[3];

                    courses.add(new Course(name, courseId, department, level));
                } else {
                    System.out.println("Invalid data format for course line: " + line);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("File at path: " + COURSES_FILE_PATH + " not found!");
        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return courses;
    }


    @Override
    public List<Section> getSectionsForStudentFromFile(String studentId) {
        List<Section> enrolledSections = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(ENROLLED_SECTIONS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] data = line.split(",");
                if (data.length < 9) { // Đã sửa để đảm bảo đủ dữ liệu
                    System.out.println("Invalid data format: " + line);
                    continue;
                }

                String studentIdFromFile = data[0];
                String courseId = data[1];
                String semester = data[5];
                int academicYear = Integer.parseInt(data[6]);

                System.out.println("Processing line for studentId: " + studentIdFromFile + ", target studentId: " + studentId);

                if (studentIdFromFile.equals(studentId)) {
                    Section section = findSectionByCourseAndSemester(courseId, semester, academicYear);

                    if (section != null) {
                        enrolledSections.add(section);
                        /*System.out.println("Added section: " + section.getCourse().getName());*/
                    } else {
                        System.out.println("Section not found for courseId: " + courseId + ", semester: " + semester + ", academicYear: " + academicYear);
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading enrolled sections file: " + e.getMessage());
        }

        return enrolledSections;
    }

}




