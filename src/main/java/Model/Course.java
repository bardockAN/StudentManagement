// Sửa class Course
package Model;

public class Course {
    private String name;
    private String courseId;
    private String department;
    private String level;
    public Course(String name, String courseId, String department, String level) {
        this.name = name;
        this.courseId = courseId;
        this.department = department;
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
