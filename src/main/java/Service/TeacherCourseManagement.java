package Service;

import Model.Course;
import Model.Section;
import Model.Teacher;

import java.util.Date;
import java.util.List;

public interface TeacherCourseManagement {

    void addCourse(Course course);
    void editCourse(String newName,String courseId,  String newDepartment, String newLevel);
    void removeCourse(String courseId);

    // các phương thức hỗ trọ về coures
    Course findCourseById(String courseId);
    List<Course> getAllCourses();
    void saveCourseToFile(List<Course> courses);

    void addSection(Section section);
    void editSection(String courseId, String semester, int academicYear, Date newStartDate, Date newEndDate);
    void removeSection(String courseId, String semester, int academicYear);

    // các phương thức hỗ trợ về section
    void saveSectionsToFile(List<Section> sections);
    Section findSectionByCourseAndSemester(String courseId, String semester, int academicYear);
    List<Section> getAllSections();

    Teacher findTeacherByUsername(String username, String password);
    void saveTeacherToCSV(String username, String password, Teacher teacher);
    Teacher findTeacherDetails(String username);


}
