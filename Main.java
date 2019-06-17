package AttendanceSheetsProject;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage window) throws Exception{
        MasterOfControllers MOC = MasterOfControllers.getInstance();
        Parent root = MOC.getLoaderMainWindow().load();
        window.setTitle("AttendanceSheet");
        window.setScene(new Scene(root, 800, 600));
        window.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
