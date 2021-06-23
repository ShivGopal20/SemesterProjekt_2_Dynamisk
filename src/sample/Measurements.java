package sample;

import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.TextArea;

import javax.sound.sampled.Line;

public class Measurements {
    Sensor sensorObject = Sensor.getGlobalSensor();
    public String[] ArrayData;
    String buffer = "";
    String CprTilSQL = "0";
    DB_MySQL database = new DB_MySQL();
    public TextArea textArea;
    public String CprString;
    public XYChart.Series<Number, Number> ecgValues = new XYChart.Series<>();


    //I denne metode splittes inputtet op i en array, og der bruges en buffer, så der ikke mistes data.
    //Denne metode er adskilt fra sensorData() metoden, for at bevare SOC, Seperation og concerns.
    public void DataProcessing() {
        String data = sensorObject.sensorData();
        if (data != null) {
            buffer = buffer + data;
            int SkilletegnPlacering = buffer.indexOf(",");
            if (SkilletegnPlacering > -1) {
                ArrayData = buffer.split(",");
                if (ArrayData != null && ArrayData.length > 0) {
                    if (buffer.charAt(buffer.length() - 1) != 44) {//44 er kommas charat nummer. TODO: Det skal rettes på din PC!
                        buffer = ArrayData[ArrayData.length - 1];
                    } else {
                        buffer = "";
                    }
                    for (int i = 0; i < ArrayData.length; i++) {
                    }
                }
            }
        }
    }

    //Dette er en set-metode, hvis formål så er at afhente nogle FXML variable fra controllr-klassen.
    //På denne måde kan variablerne bruges i denne klasse også.
    public void Set(TextArea textArea, String cpr) {
        this.textArea = textArea;
        this.CprString = cpr;

    }

    //Metoden sørger for både at indsætte ECG værdier i TextArea, og i XY.Chart serien.
    //Dog fungerer indsættelsen af værdier i TextArea ikke i denne version.
    public void ShowValues() {
        textArea.setText("ECG & time \n-----------");
        ecgValues.getData().clear();
        for (int counter = 0; counter < ArrayData.length; counter++) {
            if (NumberChecker(counter)) {
                int ECGDataValue = Integer.parseInt(ArrayData[counter]);
                System.out.println(ECGDataValue);
                ecgValues.getData().add(new XYChart.Data(counter, ECGDataValue));
                textArea.appendText("\n" + counter + "ms  ,  " + ArrayData[counter] + "mV");
                String NewEcgText = textArea.getText();
                textArea.setText(NewEcgText);
            }
        }
    }


    //Metoden sørger for at plotte XY.Chart seriens værdier i grafen.
    public void ShowGraph(LineChart lineChart1) {
        ecgValues.getData().clear();
        lineChart1.getData().clear();
        lineChart1.getData().add(ecgValues);
    }

    //NumberChecker metoden sørger for at frasortere ugyldige CPR-værdier, som indeholder andet end tal
    public boolean NumberChecker(int counter) {
        String maaling = ArrayData[counter];
        if (maaling.matches("^[0-9]*$")) {
            return true;
        } else {
            return false;
        }
    }
    //Denne metoder indsætter de målte værdier samt evt et CPR-nummer i SQL-databasen.
    //Her bliver der kaldt på metoder fra Database-klassen, hvor de forklares yderligere.
    public void SaveData() {
        for (int counter = 0; counter < ArrayData.length; counter++) {
            if (NumberChecker(counter)) {
                database.ECG_Inserter(Integer.parseInt(ArrayData[counter]), CprTilSQL);
            }
        }
    }

}