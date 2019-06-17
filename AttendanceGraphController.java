package AttendanceSheetsProject;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AttendanceGraphController implements Initializable {

    public CategoryAxis dates;
    public NumberAxis timesAttended;
    public BarChart chart;
    private Map<String, Integer> chartData = new HashMap<>();
    private int i = 0;

    private MasterOfControllers MOC = MasterOfControllers.getInstance();
    private FXMLLoader loaderMainWindow = MOC.getLoaderMainWindow();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        MainWindowController mainWindowController = loaderMainWindow.getController();
        Map<String, Map<String, Map<String, Boolean>>> whole = mainWindowController.getWhole();
        if (whole.containsKey(mainWindowController.getGroupId())){
            Map<String, Map<String, Boolean>> existingGroup = whole.get(mainWindowController.getGroupId());
            for (String x: existingGroup.keySet()){
                Map<String, Boolean> studentData = existingGroup.get(x);
                for (String y: studentData.keySet()){
                    Boolean attended = studentData.get(y);
                    if (attended.equals(true)){chartData.put(y, i++);}
                    else{chartData.put(y, i);}

                }
            }
        }else {return;}

        XYChart.Series set = new XYChart.Series();
        for (String y: chartData.keySet())
            set.getData().add(new XYChart.Data<>(y, chartData.get(y)));
        chart.getData().addAll(set);
    }
}
