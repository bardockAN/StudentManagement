package Service;

import Model.Course;
import Model.Person;
import Model.Section;

import java.util.List;

public interface StudentCourseManagement {

    /*void enrollSection(String studentId, String courseId, String semester, int academicYear);
    void unrollSection(String studentId, String courseId, String semester, int academicYear);*/

    List<Section> getAllAvailableSections();

    Section findSectionByCourseAndSemester(String courseId, String semester, int academicYear);

    void saveEnrolledSectionsToFile(String studentId, List<Section> sections);
    void saveStudentToCSV(Person student );

    Person findStudentByUsername(String username);
    Person findStudentById(String studentId);
    Course findCourseById(String courseId);
    List<Course> getAllCourses();
    List<Section> getSectionsForStudentFromFile(String studentId);




}
