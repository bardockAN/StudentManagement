package Service;

import Model.Course;
import Model.Person;
import Model.Section;

import java.util.List;

public interface StudentCourseManagement {

    List<Section> getAllAvailableSections();
    Section findSectionByCourseAndSemester(String courseId, String semester, int academicYear);
    void saveEnrolledSectionsToFile(String studentId, List<Section> sections);
    void saveStudentToCSV(Person student );
    Person findStudentByUsername(String username);
    List<Course> getAllCourses();
    List<Section> getSectionsForStudentFromFile(String studentId);




}
