package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;
import javax.sound.sampled.Line;

public class Measurements {
    Sensor sensorObject = Sensor.getGlobalSensor();
    //singleton pattern for at have global sensor, andre ikke kan overskrive, men tilgå.
    //hensigtmæssigt i projekt?
    public String[] ArrayData;
    String buffer = "";
    String CprTilSQL = "0";
    DB_MySQL database = new DB_MySQL();
    public TextArea textArea;
    public String CprString;
    public XYChart.Series <Number,Number> ecgValues = new XYChart.Series<>();


    //Nedenstående metoden bruges til at behandle dataen.
    //Her splittes inputtet op i en array, og der bruges også en buffer, så der ikke mistes data.
    public void DataProcessing() {
        String data = sensorObject.sensorData();
        if (data != null) {
            buffer = buffer + data;
            int SkilletegnPlacering = buffer.indexOf(",");
            if (SkilletegnPlacering > -1) {
                ArrayData = buffer.split(",");
                if (ArrayData != null && ArrayData.length > 0) {
                    if (buffer.charAt(buffer.length() - 1) != 44) {// tallet 44 er chartat nummer til komma
                        buffer = ArrayData[ArrayData.length - 1];
                    } else {
                        buffer = "";
                    }
                    for(int i =0; i < ArrayData.length; i++){
                      //  System.out.println(ArrayData[i]);
                    }
                }
            }
        }
    }

    public void Set(TextArea textArea, String cpr) {
        this.textArea = textArea;
        this.CprString = cpr;

    }

    public void ShowValues() {
     //   textArea.setText("ECG & time \n-----------");
        ecgValues.getData().clear();
        for (int counter = 0; counter < ArrayData.length; counter++) {
            if (NumberChecker(counter)) {
                int ECGDataValue = Integer.parseInt(ArrayData[counter]);
                System.out.println(ECGDataValue);
                ecgValues.getData().add(new XYChart.Data(counter,ECGDataValue));
              //  textArea.appendText("\n" + counter + "ms  ,  " + ArrayData[counter] + "mV");
              //  String NewEcgText = textArea.getText();
             //   textArea.setText(NewEcgText);
            }
        }
    }


    public void ShowGraph(LineChart lineChart1) {
        ecgValues.getData().clear();
        lineChart1.getData().clear();
        lineChart1.getData().add(ecgValues);
    }


    public boolean NumberChecker(int counter) {
        String maaling = ArrayData[counter];
        if (maaling.matches("^[0-9]*$")) {
            return true;
        } else {
            return false;
        }
    }

    public void SaveData() {
        for (int counter = 0; counter < ArrayData.length; counter++) {
            if (NumberChecker(counter)) {
                database.ECG_Inserter(Integer.parseInt(ArrayData[counter]), CprTilSQL);
            }
        }
    }

}