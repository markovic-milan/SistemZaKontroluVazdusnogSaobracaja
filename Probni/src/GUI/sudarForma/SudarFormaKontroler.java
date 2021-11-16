package GUI.sudarForma;

import GUI.glavnaForma.MainForma;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import sudar.Sudar;
import sudar.Upozorenje;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import static GUI.glavnaForma.MainForma.putanjaDoAlertFoldera;

public class SudarFormaKontroler {
    @FXML
    private TableView<Upozorenje> tableView;
    @FXML
    private TableColumn<Upozorenje, String> model;
    @FXML
    private TableColumn<Upozorenje, String> pozicija;
    @FXML
    private TableColumn<Upozorenje, String> vrijeme;
    @FXML
    private Button sudarButton;

    private static ObservableList<Upozorenje> lista = FXCollections.observableArrayList();

    private static String putanjaDoFolderaSudari = putanjaDoAlertFoldera;
    private static File fajl;
    private static Label poruka;
    private static Logger loger = MainForma.loger;

    public SudarFormaKontroler() {
        fajl = new File(putanjaDoFolderaSudari);
    }

    public void initialize() {
        model.setCellValueFactory(new PropertyValueFactory<Upozorenje, String>("model"));
        pozicija.setCellValueFactory(new PropertyValueFactory<Upozorenje, String>("pozicija"));
        vrijeme.setCellValueFactory(new PropertyValueFactory<Upozorenje, String>("vrijeme"));
        tableView.setItems(lista);
    }

    public void setDugmeOKIzabrano() {
        Stage stage = (Stage) sudarButton.getScene().getWindow();
        stage.close();
    }

    public static void deserijalizuj(File fajl, TableView sudari) throws IOException, ClassNotFoundException {
        ObjectInputStream citanje = new ObjectInputStream(new FileInputStream(fajl));
        Upozorenje upozorenje = (Upozorenje) citanje.readObject();

        lista.add(upozorenje);
    }

    private static void pratiFolderAlert(TableView tableView) {
        tableView.getItems().clear();
        if (fajl.exists() && fajl.list().length > 0) {
            try {

                /* fajlZaPrikaz signalizira da li je potrebno prikazati sve sudare (ako je izabrano dugme sa glavne forme) i tada nije null,
                a ako zelimo da nam se prikazu svi sudari (lista i detalji) pritiskom na dugme, tada je fajlZaPrikaz null */
                if (SudarForma.fajlZaPrikaz != null) {
                    deserijalizuj(SudarForma.fajlZaPrikaz, tableView);
                } else {
                    for (int i = 0; i < fajl.list().length; i++) {
                        deserijalizuj(new File(putanjaDoFolderaSudari + File.separator + fajl.list()[i]), tableView);
                    }
                }
            } catch (IOException | ClassNotFoundException izuzetak) {
                loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
            }
        }
    }

    public static void prikaziPromjene(TableView tableView) {
        Runnable zadatak = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pratiFolderAlert(tableView);
                    }
                });
            }
        };
        Thread prikazPromjene = new Thread(zadatak);
        prikazPromjene.setDaemon(true);
        prikazPromjene.start();
    }
}