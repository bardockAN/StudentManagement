// Sửa class UndergraduateStudent
package Model;

import java.util.Date;

public class UndergraduateStudent extends Person {
    private String undergraduateMajor;

    public UndergraduateStudent(String name, String id, Date dOb, String undergraduateMajor) {
        super(name, id, dOb);
        this.undergraduateMajor = undergraduateMajor;
    }

    public String getUndergraduateMajor() {
        return undergraduateMajor;
    }

    public void setUndergraduateMajor(String undergraduateMajor) {
        this.undergraduateMajor = undergraduateMajor;
    }
}
