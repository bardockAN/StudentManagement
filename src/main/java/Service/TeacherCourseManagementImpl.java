package Service;

import Model.*;
import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TeacherCourseManagementImpl implements TeacherCourseManagement{

    private static final String COURSES_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\courses.csv";
    private static final String TEACHER_REGISTER_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\teacherRegister.csv";
    private static final String TEACHERS_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\teachers.csv";
    private static final String TEACHERS_SECTIONS_FILE_PATH = "D:\\Java\\StudentManagement\\src\\main\\database\\teacherSections.csv";

    @Override
    public void addCourse(Course course) {
        List<Course> courses = getAllCourses();
        courses.add(course);
        saveCourseToFile(courses);
    }


    @Override
    public void editCourse(String courseId, String newName, String newDepartment, String newLevel) {
        List<Course> courses = getAllCourses();
        Course courseToEdit = null;

        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                courseToEdit = course;
                break;
            }
        }

        if (courseToEdit != null) {

            courseToEdit.setName(newName);
            courseToEdit.setDepartment(newDepartment);
            courseToEdit.setLevel(newLevel);

            saveCourseToFile(courses);
            System.out.println("Course with ID " + courseId + " has been updated successfully.");
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
    }


    @Override
    public void removeCourse(String courseId) {
        List<Course> courses = getAllCourses();
        Course courseToRemove = null;

        for (Course course : courses) {
            if (course.getCourseId().equals(courseId)) {
                courseToRemove = course;
                break;
            }
        }

        if (courseToRemove != null) {
            courses.remove(courseToRemove);
            saveCourseToFile(courses);
            System.out.println("Course with ID " + courseId + " has been removed successfully.");
        } else {
            System.out.println("Course with ID " + courseId + " not found.");
        }
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
    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(COURSES_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] courseDetails = line.split(",");

                Course course = new Course(courseDetails[0], courseDetails[1], courseDetails[2], courseDetails[3]);
                courses.add(course);
            }
        } catch (IOException e) {
            System.out.println("Error reading courses from file: " + e.getMessage());
        }
        return courses;
    }





    @Override
    public void saveCourseToFile(List<Course> courses) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(COURSES_FILE_PATH))) {
            for (Course course : courses) {

                writer.write(course.getName() + "," + course.getCourseId() + "," + course.getDepartment() + "," + course.getLevel());
                writer.newLine(); // Thêm dòng mới sau mỗi khóa học
            }
        } catch (IOException e) {
            System.out.println("Error saving courses to file: " + e.getMessage());
        }
    }







    @Override
    public void saveTeacherToCSV(String username, String password, Teacher teacher) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHER_REGISTER_FILE_PATH, true))) {
            writer.write(username + "," + password);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error saving teacher to register file: " + e.getMessage());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHERS_FILE_PATH, true))) {
            writer.write(teacher.getId() + "," + teacher.getName() + "," + new SimpleDateFormat("dd/MM/yyyy").format(teacher.getdOb()) + "," + username);
            writer.newLine();
            System.out.println("Teacher saved to CSV: " + teacher.getId() + "," + teacher.getName() + "," + teacher.getdOb() + "," + username);
        } catch (IOException e) {
            System.out.println("Error saving teacher to CSV: " + e.getMessage());
        }
    }


    @Override
    public Teacher findTeacherDetails(String username) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 4 && data[3].equals(username)) {
                    String id = data[0];
                    String name = data[1];
                    Date dob = new SimpleDateFormat("dd/MM/yyyy").parse(data[2]);
                    return new Teacher(name, id, dob);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading teachers file: " + e.getMessage());
        }
        return null;
    }




    @Override
    public Teacher findTeacherByUsername(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHER_REGISTER_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length >= 2 && data[0].equals(username) && data[1].equals(password)) {
                    return findTeacherDetails(username);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading teacher register file: " + e.getMessage());
        }
        return null;
    }


    @Override
    public void addSection(Section section) {
        List<Section> sections = getAllSections();
        sections.add(section);
        saveSectionsToFile(sections);
    }

    @Override
    public void editSection(String courseId, String semester, int academicYear, Date newStartDate, Date newEndDate) {
        // Lấy danh sách tất cả các section
        List<Section> sections = getAllSections();

        // Tìm section cần chỉnh sửa
        Section section = findSectionByCourseAndSemester(courseId, semester, academicYear);
        if (section != null) {
            // Cập nhật thông tin section
            section.setStartDate(newStartDate);
            section.setEndDate(newEndDate);

            // Lưu lại danh sách sections đã được cập nhật vào file
            saveSectionsToFile(sections);  // Lưu danh sách mới vào file

        } else {
            System.out.println("Section not found.");
        }
    }






    @Override
        public void removeSection(String courseId, String semester, int academicYear) {
            List<Section> sections = getAllSections();
            Section section = findSectionByCourseAndSemester(courseId, semester, academicYear);
            if (section != null) {
                sections.remove(section);
                saveSectionsToFile(sections);
            }
        }


    @Override
    public void saveSectionsToFile(List<Section> sections) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(TEACHERS_SECTIONS_FILE_PATH))) {
            for (Section section : sections) {
                String startDate = new SimpleDateFormat("dd/MM/yyyy").format(section.getStartDate());
                String endDate = new SimpleDateFormat("dd/MM/yyyy").format(section.getEndDate());
                String taughtBy = section.getTaughtBy();
                String semester = section.getSemester();
                int academicYear = section.getAcademicYear();
                String courseName = section.getCourse().getName();
                String courseId = section.getCourse().getCourseId();
                String department = section.getCourse().getDepartment();
                String level = section.getCourse().getLevel();

                // Lưu thông tin vào file CSV
                writer.write(startDate + "," + endDate + "," + taughtBy + "," + semester + "," + academicYear
                        + "," + courseName + "," + courseId + "," + department + "," + level);
                writer.newLine();
            }
            System.out.println("Sections saved to file successfully!");
        } catch (IOException e) {
            System.out.println("Error saving sections to file: " + e.getMessage());
        }
    }






    @Override
    public Section findSectionByCourseAndSemester(String courseId, String semester, int academicYear) {
        List<Section> sections = getAllSections();
        for (Section section : sections) {
            if (section.getCourse().getCourseId().equals(courseId) && section.getSemester().equals(semester) && section.getAcademicYear() == academicYear) {
                return section;
            }
        }
        return null;
    }


    @Override
    public List<Section> getAllSections() {
        List<Section> sections = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(TEACHERS_SECTIONS_FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 9) {
                    // Đọc thông tin Section từ file
                    String startDateStr = data[0];
                    String endDateStr = data[1];
                    String taughtBy = data[2];
                    String semester = data[3];
                    int academicYear = Integer.parseInt(data[4]);
                    String courseName = data[5];
                    String courseId = data[6];
                    String department = data[7];
                    String level = data[8];

                    // Chuyển đổi startDate và endDate thành đối tượng Date
                    Date startDate = new SimpleDateFormat("dd/MM/yyyy").parse(startDateStr);
                    Date endDate = new SimpleDateFormat("dd/MM/yyyy").parse(endDateStr);

                    // Tạo đối tượng Section mới từ thông tin đã đọc được
                    Course course = new Course(courseName, courseId, department, level);
                    Section section = new Section( startDate, endDate, taughtBy, semester, academicYear, course);
                    sections.add(section);
                }
            }
        } catch (IOException | ParseException e) {
            System.out.println("Error reading sections from file: " + e.getMessage());
        }
        return sections;
    }


}
