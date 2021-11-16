package GUI.sudarForma;

import GUI.glavnaForma.MainForma;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.io.File;

public class SudarForma {
    public static Stage stage;
    public static String naslov = "";
    public static File fajlZaPrikaz = null;

    public void prikazi() throws Exception {
        stage = new Stage();
        stage.initModality(Modality.NONE);
        Parent root = FXMLLoader.load(getClass().getResource("sudarForma.fxml"));
        stage.setTitle("Informacije o sudarima");
        Scene scene = new Scene(root, 600, 400);

        TableView tableView = (TableView) scene.lookup("#tableView");
        SudarFormaKontroler.prikaziPromjene(tableView);

        stage.getIcons().add(new Image(MainForma.class.getResourceAsStream("/slike/plane.jpg")));
        stage.setScene(scene);
        stage.show();
    }
}
