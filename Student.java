package AttendanceSheetsProject;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

public class Student {
    private SimpleStringProperty fullName;
    private SimpleStringProperty name;
    private SimpleStringProperty surname;
    private CheckBox ticked;

    Student(String name, String surname) {
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
    }

    Student(String fullName){
        this.fullName = new SimpleStringProperty(fullName);
        this.ticked = new CheckBox();
    }

    public String getFullName(){ return fullName.get(); }

    public void setFullName(String fullName){ this.fullName = new SimpleStringProperty(fullName); }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name = new SimpleStringProperty(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public void setSurname(String surname) {
        this.surname = new SimpleStringProperty(surname);
    }

    public CheckBox getTicked(){ return ticked; }

    public void setTicked(CheckBox ticked){ this.ticked = ticked; }
}
