package AttendanceSheetsProject;

import javafx.fxml.FXMLLoader;

public class MasterOfControllers {
    private static MasterOfControllers ourInstance = new MasterOfControllers();

    public static MasterOfControllers getInstance() {
        return ourInstance;
    }

    private MasterOfControllers() {
    }


    private final FXMLLoader loaderMainWindow = new FXMLLoader(getClass().getResource("MainWindow.fxml"));
    private final FXMLLoader loaderGroupWindow = new FXMLLoader(getClass().getResource("GroupWindow.fxml"));
    private final FXMLLoader loaderAttendanceWindow = new FXMLLoader(getClass().getResource("AttendanceWindow.fxml"));
    private final FXMLLoader loaderGraphWindow = new FXMLLoader(getClass().getResource("AttendanceGraph.fxml"));

    FXMLLoader getLoaderMainWindow(){ return loaderMainWindow;}

    FXMLLoader getLoaderGroupWindow(){ return loaderGroupWindow;}

    FXMLLoader getLoaderAttendanceWindow(){ return loaderAttendanceWindow;}

    FXMLLoader getLoaderGraphWindow(){return loaderGraphWindow;}
}
