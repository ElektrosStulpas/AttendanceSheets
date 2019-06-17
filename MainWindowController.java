package AttendanceSheetsProject;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainWindowController {

    public TextField groupIdField;

    private Map<String, Map<String, Map<String, Boolean>>> whole = new HashMap<>();

    private MasterOfControllers MOC = MasterOfControllers.getInstance();

    public Map<String, Map<String, Map<String, Boolean>>> getWhole(){ return whole; }
    String getGroupId(){
        return groupIdField.getText();
    }

    private Parent rootGroup, rootAttendance, rootGraph;
    private Scene sceneGroup, sceneAttendance, sceneGraph;
    private GroupWindowController groupWindowController;
    private AttendanceWindowController attendanceWindowController;

    public void showGroupWindow() {
        if (getGroupId().equals("")){ return; }
        try {
            Stage window = new Stage();
            if (rootGroup == null){
                FXMLLoader loaderGroupWindow = MOC.getLoaderGroupWindow();
                rootGroup = loaderGroupWindow.load();
                sceneGroup = new Scene(rootGroup, 800, 600);
                groupWindowController = loaderGroupWindow.getController();
            }
            window.setTitle("Group");
            window.setScene(sceneGroup);

            if (whole.containsKey(getGroupId())){
                groupWindowController.printGroup();
                window.setOnCloseRequest(e -> groupWindowController.onShutdownUpdate());
            }
            else { window.setOnCloseRequest(e -> groupWindowController.onShutdown()); }

            window.showAndWait();
            Map<String, Map<String, Boolean>> groupData = groupWindowController.getGroup();
            if (groupData.size() > 0){
                whole.put(getGroupId(), groupData);
            }

        } catch (IOException e){ e.printStackTrace();}
    }

    public void markAttendanceWindow() {
        if (!whole.containsKey(getGroupId())){ return; }
        try{
            Stage window = new Stage();
            if (rootAttendance == null){
                FXMLLoader loaderAttendanceWindow = MOC.getLoaderAttendanceWindow();
                rootAttendance = loaderAttendanceWindow.load();
                sceneAttendance = new Scene(rootAttendance, 800, 600);
                attendanceWindowController = loaderAttendanceWindow.getController();
            }
            window.setTitle("Mark Attendance");
            window.setScene(sceneAttendance);

            attendanceWindowController.printGroup();

            window.setOnCloseRequest(e -> attendanceWindowController.onShutdown());
            window.showAndWait();

            Map<String, Map<String, Boolean>> groupData = attendanceWindowController.getGroup();
            whole.put(getGroupId(), groupData);
        } catch (IOException e){e.printStackTrace();}
    }

    public void viewAttendanceWindow() {
        if (!whole.containsKey(getGroupId())){ return; }
        Stage window = new Stage();
        window.setTitle("AttendanceView");

        String attended;
        TreeItem<String> root, studentName;

        root = new TreeItem<>();
        root.setExpanded(true);

        Map<String, Map<String, Boolean>> groupData = whole.get(getGroupId());
        for (String x: groupData.keySet())
        {
            studentName = new TreeItem<>(x);
            root.getChildren().add(studentName);

            Map<String, Boolean> studentData = groupData.get(x);
            for (String y: studentData.keySet())
            {
                if (studentData.get(y)) {attended = "Present";}
                else{attended = "Absent";}

                String attendanceData = y + " " + attended;
                TreeItem<String> date = new TreeItem<>(attendanceData);
                studentName.getChildren().add(date);
            }
        }

        TreeView<String> tree = new TreeView<>(root);
        tree.setShowRoot(false);

        StackPane layout = new StackPane();
        layout.getChildren().add(tree);
        Scene scene = new Scene(layout, 300, 250);
        window.setScene(scene);
        window.showAndWait();
    }

    public void attendanceGraphWindow() {
        if (getGroupId().equals("")){ return; }
        try{
            Stage window = new Stage();
            if (rootGraph == null){
                FXMLLoader loaderGraphWindow = MOC.getLoaderGraphWindow();
                rootGraph = loaderGraphWindow.load();
                sceneGraph = new Scene(rootGraph, 800, 600);

            }
            window.setTitle("Attendance Graph");
            window.setScene(sceneGraph);
            window.showAndWait();
        }catch (IOException e){e.printStackTrace();}
    }

    public void printAttendancePDF() {
        if (!whole.containsKey(getGroupId())){ return; }
        Document document = new Document();
        String attended;
        try{
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream("StudentAttendance.pdf"));
            document.open();

            Map<String, Map<String, Boolean>> groupData = whole.get(getGroupId());
            for(String name: groupData.keySet()){
                document.add(new Paragraph(name));
                List orderedList = new List(List.ORDERED);
                Map<String, Boolean> studentData = groupData.get(name);

                for (String date: studentData.keySet()){
                    if (studentData.get(date)) {attended = "Present";}
                    else{attended = "Absent";}

                    String attendanceData = date + " " + attended;
                    orderedList.add(new ListItem(attendanceData));
                }
                document.add(orderedList);
            }
            document.close();
            writer.close();
        }catch (FileNotFoundException | DocumentException e){e.printStackTrace();}
    }
}
