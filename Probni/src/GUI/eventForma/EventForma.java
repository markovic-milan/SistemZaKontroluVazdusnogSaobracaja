package GUI.eventForma;

import GUI.glavnaForma.MainForma;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class EventForma {

    public static String tekst;

    public EventForma(String tekst) {
        EventForma.tekst = tekst;
    }

    public void prikazi() throws Exception {
        Stage stage = new Stage();
        stage.initModality(Modality.NONE);
        Parent korijen = FXMLLoader.load(getClass().getResource("eventForma.fxml"));
        stage.getIcons().add(new Image(MainForma.class.getResourceAsStream("/slike/plane.jpg")));
        stage.setTitle("Upozorenje");
        Scene scena = new Scene(korijen, 480, 300);
        stage.setScene(scena);
        stage.show();
    }
}