// Sửa class GraduateStudent
package Model;

import java.util.Date;

public class GraduateStudent extends Person {
    private String graduateMajor;

    public GraduateStudent(String name, String id, Date dOb, String graduateMajor) {
        super(name, id, dOb);
        this.graduateMajor = graduateMajor;
    }

    public String getGraduateMajor() {
        return graduateMajor;
    }

    public void setGraduateMajor(String graduateMajor) {
        this.graduateMajor = graduateMajor;
    }
}
