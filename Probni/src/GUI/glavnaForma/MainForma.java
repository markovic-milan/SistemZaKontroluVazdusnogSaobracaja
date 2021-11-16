package GUI.glavnaForma;

import mapiranjeVazdusnogProstora.MapiranjeVazdusnogProstora;
import model.VazdusniProstor.Mapa;
import radar.Radar;
import simulator.Simulator;
import backup.Backup;
import izuzeci.IzuzeciLogger;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MainForma extends Application {
    public static String putanjaDoFajlaSaMapom = "C:\\Users\\milan\\Desktop\\Probni\\src\\mapiranjeVazdusnogProstora\\map.txt";
    public static String putanjaDoAlertFoldera = "C:\\Users\\milan\\Desktop\\Probni\\src\\sudar\\alert";
    public static String putanjaDoEventsFoldera = "C:\\Users\\milan\\Desktop\\Probni\\src\\radar\\events";
    public static Simulator simulator;
    public static Radar radar;
    public static Mapa mapa;
    public static Logger loger;
    public static MapiranjeVazdusnogProstora mapiranjeVazdusnogProstora;

    public MainForma() {
        loger = new IzuzeciLogger().getLoger();
        mapiranjeVazdusnogProstora = new MapiranjeVazdusnogProstora();

        File alert = new File(putanjaDoAlertFoldera);
        if (alert != null && alert.listFiles().length > 0) {
            for (File fajl : alert.listFiles()) {
                fajl.delete();
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("mainForma.fxml"));
        root.setStyle("-fx-background-color: E9E9E9;");
        Scene scena = new Scene(root, 720, 480);
        primaryStage.setTitle("Kontrola leta");
        primaryStage.setResizable(false);

        Thread simulatorNit = new Thread(simulator);
        simulatorNit.setDaemon(true);
        simulatorNit.start();

        Thread radarNit = new Thread(radar);
        radarNit.setDaemon(true);
        radarNit.start();

        Thread backup = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        new Backup("src" + File.separator, "*.txt").zipuj();
                        Thread.sleep(60000);
                    } catch (InterruptedException | IOException izuzetak) {

                    }
                }
            }
        });
        backup.setDaemon(true);
        backup.start();

        MainFormaKontroler.osvjeziPrikazMatrice();

        Label obavjestenje = (Label) scena.lookup("#obavjestenjeLabel");
        MainFormaKontroler.prikaziPromjene(obavjestenje);

        primaryStage.setScene(scena);
        primaryStage.getIcons().add(new Image(MainForma.class.getResourceAsStream("/slike/plane.jpg")));
        primaryStage.show();
    }

    public static void main(String[] args) {


        Properties svojstva = new Properties();
        try {
            FileInputStream citanje = new FileInputStream(new File("C:\\Users\\milan\\Desktop\\Probni\\src\\config\\config.properties"));
            svojstva.load(citanje);
            int brojVrsta = Integer.valueOf(svojstva.getProperty("brojVrsta"));
            int brojKolona = Integer.valueOf(svojstva.getProperty("brojKolona"));
            mapa = new Mapa(brojVrsta, brojKolona);
            citanje.close();
            mapa = new Mapa(brojVrsta, brojKolona);
        } catch (IOException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
        simulator = new Simulator(mapa);
        radar = new Radar(mapa);

        launch(args);
    }
}
