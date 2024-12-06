// Tạo class Section
package Model;

import java.util.Date;
import java.util.Objects;

public class Section {
    private Date startDate;
    private Date endDate;
    private String taughtBy;
    private String semester;
    private int academicYear;
    private Course course;

    public Section(Date startDate, Date endDate, String taughtBy, String semester, int academicYear, Course course) {
        this.startDate = startDate;
        this.endDate = endDate;
        this.taughtBy = taughtBy;
        this.semester = semester;
        this.academicYear = academicYear;
        this.course = course;
    }

    // Getters and Setters
    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getTaughtBy() {
        return taughtBy;
    }

    public void setTaughtBy(String taughtBy) {
        this.taughtBy = taughtBy;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public int getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(int academicYear) {
        this.academicYear = academicYear;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Section section = (Section) obj;
        return academicYear == section.academicYear &&
                Objects.equals(course.getCourseId(), section.course.getCourseId()) &&
                Objects.equals(semester, section.semester);
    }

    @Override
    public int hashCode() {
        return Objects.hash(course.getCourseId(), semester, academicYear);
    }
}
