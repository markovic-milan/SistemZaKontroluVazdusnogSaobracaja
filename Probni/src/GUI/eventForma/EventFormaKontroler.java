package GUI.eventForma;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class EventFormaKontroler {
    @FXML
    private Label eventLabela;

    @FXML
    private Button eventButtonOK;

    @FXML
    public void initialize() {
        eventLabela.setText(EventForma.tekst);
    }

    public void dugmeOKIzabrano() {
        Stage prozor = (Stage) eventButtonOK.getScene().getWindow();
        prozor.close();
    }
}
