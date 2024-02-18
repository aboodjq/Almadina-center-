package test;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Student {

    // Attributes
    private SimpleIntegerProperty studentId;
    private SimpleStringProperty name;
    private SimpleStringProperty familyPhone;
    private SimpleStringProperty grade;
    private SimpleStringProperty address;

    // Constructor
    public Student(int studentId, String name, String familyPhone, String grade, String address) {
        this.studentId = new SimpleIntegerProperty(studentId);
        this.name = new SimpleStringProperty(name);
        this.familyPhone = new SimpleStringProperty(familyPhone);
        this.grade = new SimpleStringProperty(grade);
        this.address = new SimpleStringProperty(address);
    }

    // Getter and Setter methods
    public int getStudentId() {
        return studentId.get();
    }

    public void setStudentId(int studentId) {
        this.studentId.set(studentId);
    }

    public SimpleIntegerProperty studentIdProperty() {
        return studentId;
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getFamilyPhone() {
        return familyPhone.get();
    }

    public void setFamilyPhone(String familyPhone) {
        this.familyPhone.set(familyPhone);
    }

    public SimpleStringProperty familyPhoneProperty() {
        return familyPhone;
    }

    public String getGrade() {
        return grade.get();
    }

    public void setGrade(String grade) {
        this.grade.set(grade);
    }

    public SimpleStringProperty gradeProperty() {
        return grade;
    }

    public String getAddress() {
        return address.get();
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public SimpleStringProperty addressProperty() {
        return address;
    }
}
