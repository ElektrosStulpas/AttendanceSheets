package AttendanceSheetsProject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.time.LocalDate;
import java.util.Map;
import java.util.ResourceBundle;

public class AttendanceWindowController implements  Initializable{

    public TableColumn<Student, String> fullNameColumn;
    public TableColumn<Student, Boolean> checkBoxColumn;
    public TableView<Student> attendanceTable;
    public DatePicker datePicker;

    private MasterOfControllers MOC = MasterOfControllers.getInstance();
    private MainWindowController mainWindowController = MOC.getLoaderMainWindow().getController();
    private Map<String, Map<String, Boolean>> group;
    private ObservableList<Student> presentStudents = FXCollections.observableArrayList();
    private String tempGroupId = mainWindowController.getGroupId();

    public Map<String, Map<String, Boolean>> getGroup(){ return group; }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        checkBoxColumn.setCellValueFactory(new PropertyValueFactory<>("ticked"));
        fullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullName"));
    }

    void printGroup(){
        Map<String, Map<String, Map<String, Boolean>>> whole = mainWindowController.getWhole();
        if (!mainWindowController.getGroupId().equals(tempGroupId)){
            tempGroupId = mainWindowController.getGroupId();
            datePicker.setValue(null);
        }
        Map<String, Map<String, Boolean>> group = whole.get(mainWindowController.getGroupId());
        for (String fullName: group.keySet()){
            Student student = new Student(fullName);
            if (datePicker.getValue() != null){
                Map<String, Boolean> attendance = group.get(fullName);
                if (attendance.size() != 0 && attendance.get(datePicker.getValue().toString())){
                    CheckBox check = student.getTicked();
                    check.setSelected(true);
                    student.setTicked(check);
                }
            }
            attendanceTable.getItems().addAll(student);
            presentStudents.add(student);
        }
    }

    void onShutdown() {
        if (datePicker.getValue() == null){return;}
        Map<String, Map<String, Map<String, Boolean>>> whole = mainWindowController.getWhole();
        Map<String, Map<String, Boolean>> group = whole.get(mainWindowController.getGroupId());
        LocalDate date = datePicker.getValue();
        for (Student person: presentStudents){
            Map<String, Boolean> attendance = group.get(person.getFullName());
            attendance.put(date.toString(), person.getTicked().isSelected());
            group.put(person.getFullName(), attendance);
        }
        attendanceTable.getItems().clear();
        presentStudents.clear();
        this.group = group;
    }
}
