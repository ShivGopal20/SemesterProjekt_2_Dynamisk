package sample;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainMenuController {
    @FXML
    public LineChart<Number, Number> ecgGraph;
    @FXML
    public TextArea ecgText;
    @FXML
    private TextField CPR_Nummer;

    Measurements measurements = new Measurements();
    ThreadClass threadclass = new ThreadClass();
    String CprString;

    //Dette er onAction metoden til start-knappen, og der er 3 metoder her.
    public void ECGstarter() {
        measurements.Set(ecgText, CprString);
        measurements.ShowGraph(ecgGraph);
        threadclass.getThread(threadclass.getMainThread());
    }

    //Dette er onAction metoden til stop-knappen, som sætter en boolean til false, så tråden stopper.
    public void ECGstop() {
        threadclass.setActiveChecker(false);
    }

    //Metoden nulstiller både Linechartens indhold og TextAreas inhold, så GUI er nulstillet.
    public void Clear() {
        ecgText.clear();
        ecgGraph.getData().clear();
    }

    //Denne metode be- eller afkræfter det indtastede CPR-nummer ud fra passende kriterier såsom:
    //længde og at den indtastede tekst udelukkende indeholder tal, hvilket gøres vha. Numberchecker().
    public void CPR_Check() {
        try {
            CprString = String.valueOf(CPR_Nummer.getText());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (CprString.length() == 10) {
            measurements.CprTilSQL = CprString;
            AlertPopUp("CPR", "CPR is verified");
        } else {
            AlertPopUp("CPR Error", "CPR shall be 10 digits");
        }
    }

    //Denne metoder bruges til at lave AlertPopUps, som bruges hvis man indtaster et ugyldigt CPR-nummer.
    public void AlertPopUp(String AlertTitle, String AlertNote) {
        Stage AlertBox = new Stage();
        AlertBox.initModality(Modality.APPLICATION_MODAL);
        AlertBox.setTitle(AlertTitle);
        AlertBox.setMinWidth(200);
        Label Note = new Label();
        Note.setText(AlertNote);
        Button Close = new Button("OK");
        Close.setOnAction(e -> AlertBox.close());
        VBox AlertBoxLayout = new VBox(10);
        AlertBoxLayout.getChildren().addAll(Note, Close);
        AlertBoxLayout.setAlignment(Pos.CENTER);
        Scene AlertScene = new Scene(AlertBoxLayout);
        AlertBox.setScene(AlertScene);
        AlertBox.show();
    }
}
