// Sửa class Person
package Model;

import java.util.Date;

public class Person {
    private String name;
    private String id;
    private Date dOb;

    public Person(String name, String id, Date dOb) {
        this.name = name;
        this.id = id;
        this.dOb = dOb;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getdOb() {
        return dOb;
    }

    public void setdOb(Date dOb) {
        this.dOb = dOb;
    }
}
