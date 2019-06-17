package AttendanceSheetsProject;

import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Scanner;


public class GroupWindowController implements Initializable {

    public TextField studentName;
    public TextField studentSurname;
    public TableView<Student> groupTable;
    public TableColumn<Student, String> nameColumn;
    public TableColumn<Student, String> surnameColumn;

    private Map<String, Map<String, Boolean>> group;
    private MasterOfControllers MOC = MasterOfControllers.getInstance();
    private MainWindowController mainWindowController = MOC.getLoaderMainWindow().getController();

    public Map<String, Map<String, Boolean>> getGroup(){
        return group;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        groupTable.setEditable(true);
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        surnameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    void printGroup(){
        Map<String, Map<String, Map<String, Boolean>>> whole = mainWindowController.getWhole();
        Map<String, Map<String, Boolean>> existingGroup = whole.get(mainWindowController.getGroupId());
        this.group = existingGroup;
        for (String fullName: existingGroup.keySet()) {
            String[] hatch = fullName.split(" ");
            groupTable.getItems().addAll(new Student(hatch[0], hatch[1]));
        }
    }

    public void addStudent() {
        if(studentName.getText().equals("") || studentSurname.getText().equals("")){return;}
        groupTable.getItems().addAll(new Student(studentName.getText(), studentSurname.getText()));
        studentName.clear();
        studentSurname.clear();
    }

    void onShutdown(){
        group = new HashMap<>();
        for (int i = 0; i < groupTable.getItems().size(); i++){
            Student student = groupTable.getItems().get(i);
            String keyStudent = student.getName() + " " + student.getSurname();
            Map<String, Boolean> attendance = new HashMap<>();
            group.put(keyStudent, attendance);
        }
        groupTable.getItems().clear();
    }

    void onShutdownUpdate(){
        Map<String, Boolean> attendance;
        for (int i = 0; i < groupTable.getItems().size(); i++){
            Student student = groupTable.getItems().get(i);
            String keyStudent = student.getName() + " " + student.getSurname();
            if(group.containsKey(keyStudent)){
                attendance = group.get(keyStudent);
            }else{attendance = new HashMap<>();}

            group.put(keyStudent, attendance);
        }
        groupTable.getItems().clear();
    }

    public void changeNameCell(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
        Map<String, Boolean> attendance = null;
        Student studentSelected = groupTable.getSelectionModel().getSelectedItem();
        String beforeFullName = studentSelected.getName() + " " + studentSelected.getSurname();
        if (group != null){
            attendance = group.get(beforeFullName);
            group.remove(beforeFullName);
        }
        studentSelected.setName(studentStringCellEditEvent.getNewValue());
        String afterFullName = studentSelected.getName() + " " + studentSelected.getSurname();
        if (group != null){
            group.put(afterFullName, attendance);
        }

    }

    public void changeSurnameCell(TableColumn.CellEditEvent<Student, String> studentStringCellEditEvent) {
        Map<String, Boolean> attendance = null;
        Student studentSelected = groupTable.getSelectionModel().getSelectedItem();
        String beforeFullName = studentSelected.getName() + " " + studentSelected.getSurname();
        if (group != null){
            attendance = group.get(beforeFullName);
            group.remove(beforeFullName);
        }

        studentSelected.setSurname(studentStringCellEditEvent.getNewValue());
        String afterFullName = studentSelected.getName() + " " + studentSelected.getSurname();
        if (group != null){
            group.put(afterFullName, attendance);
        }

    }

    public void importCSV() {
        String fileName = "Students.csv";
        File file = new File(fileName);
        try {
            Scanner inputStream = new Scanner(file);

            groupTable.getItems().clear();

            while (inputStream.hasNext()){
                String data = inputStream.next();
                String[] hatch = data.split(",");
                groupTable.getItems().addAll(new Student(hatch[0], hatch[1]));
            }
            inputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void importExcel() {
        String[] hatch = new String[2];
        int i = 0;
        try {
            Workbook workbook = WorkbookFactory.create(new File("Students.xlsx"));
            Sheet sheet = workbook.getSheetAt(0);

            groupTable.getItems().clear();

            for (Row row: sheet){
                for (Cell cell: row){
                    String value = cell.toString();
                    hatch[i++] = value;
                }
                i = 0;
                groupTable.getItems().addAll(new Student(hatch[0], hatch[1]));
            }
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportCSV() {
        Writer writer;
        try {
            writer = new BufferedWriter(new FileWriter(new File("StudentsOutput.csv")));
            for (int i = 0; i < groupTable.getItems().size(); i++) {
                Student student = groupTable.getItems().get(i);
                String output = student.getName() + "," + student.getSurname() + "\n";

                writer.write(output);
            }
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void exportExcel() {
        int rowNum = 0;
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet(mainWindowController.getGroupId());
        for (int i = 0; i < groupTable.getItems().size(); i++){
            Row row = sheet.createRow(rowNum++);
            Student student = groupTable.getItems().get(i);
            row.createCell(0).setCellValue(student.getName());
            row.createCell(1).setCellValue(student.getSurname());
        }
        try{
            FileOutputStream fileOut = new FileOutputStream("StudentsOutput.xlsx");
            workbook.write(fileOut);
            fileOut.close();

            workbook.close();
        }catch(IOException e){e.printStackTrace();}
    }
}
