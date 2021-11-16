package GUI.glavnaForma;

import GUI.sudarForma.SudarForma;
import mapiranjeVazdusnogProstora.MapiranjeVazdusnogProstora;
import model.VazdusniProstor.Mapa;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import watcher.Watcher;

import java.io.File;
import java.util.logging.Level;

import static GUI.glavnaForma.MainForma.*;


public class MainFormaKontroler {
    @FXML
    private GridPane matrica;
    @FXML
    private Button dugmeExit;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    public static Button prikazSudaraButton;
    @FXML
    private Label obavjestenjeLabel;
    @FXML
    private Button aktivirajZabranuLetenjaDugme;

    public static final int TILE_SIZE = 20;

    private boolean zabrana = false;
    private SudarForma sudarForma;
    private static Watcher posmatracEventsFoldera;
    private static Watcher posmatracAlertFoldera = new Watcher(putanjaDoAlertFoldera, null);
    private static Mapa mapa = MainForma.mapa;
    public static StackPane[][] tekst = new StackPane[mapa.getBrojRedova()][mapa.getBrojKolona()];
    public static MapiranjeVazdusnogProstora mapiranjeVazdusnogProstora = MainForma.mapiranjeVazdusnogProstora;


    public static void osvjeziPrikazMatrice() {
        Runnable zadatak = new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            mapiranjeVazdusnogProstora.procitajFajl(mapiranjeVazdusnogProstora.mapaFajl, null);
                        }
                    });
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException izuzetak) {
                        loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
                    }
                }
            }
        };

        Thread osvjezavanjeMatrice = new Thread(zadatak);
        osvjezavanjeMatrice.setDaemon(true);
        osvjezavanjeMatrice.start();
    }

    public void initialize() {
        // dugme za deaktiviranje zabrane letenja je onemoguceno, prije nego sto se odabere dugme za aktiviranje
        //deaktivirajZabranuLetenjaDugme.setDisable(true);
        // na pocetku prikaz matrice cine prazne labele
        Text text = new Text("");
        Rectangle rectangle;
        for (int vrsta = 0; vrsta < mapa.getBrojRedova(); vrsta++) {
            for (int kolona = 0; kolona < mapa.getBrojKolona(); kolona++) {
                rectangle = new Rectangle();
                tekst[vrsta][kolona] = new StackPane();
                rectangle.setHeight(TILE_SIZE);
                rectangle.setWidth(TILE_SIZE);
                rectangle.setFill(Color.LIGHTGREEN);
                rectangle.setStroke(Color.BLACK);
                tekst[vrsta][kolona].getChildren().addAll(rectangle, text);

                //tekst[vrsta][kolona].setFont(Font.font("Courier New", 10));
                //tekst[vrsta][kolona].setAlignment(Pos.CENTER);
                matrica.add(tekst[vrsta][kolona], kolona, vrsta);
            }
        }
    }

    public void dugmeExitIzabrano() {
        Stage prozor = (Stage) dugmeExit.getScene().getWindow();
        prozor.close();
    }

    public void aktivirajZabranuLetenjaIzabrano() {
        if (zabrana == false) {
            mapa.setZabranaLeta(true);
            zabrana = true;
            aktivirajZabranuLetenjaDugme.setText("Deaktiviraj zabranu");
        } else {
            mapa.setZabranaLeta(false);
            zabrana = false;
            aktivirajZabranuLetenjaDugme.setText("Aktiviraj zabranu");
        }
    }

    public void prikaziSudareIzabrano() {
        // prikaz detektovanih sudara u folderu alert
        try {
            sudarForma = new SudarForma();
            sudarForma.fajlZaPrikaz = null;
            sudarForma.prikazi();
        } catch (Exception izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    private static void pratiEventsFolder(Label tekstualnaPoruka) {
        posmatracEventsFoldera = new Watcher(putanjaDoEventsFoldera, tekstualnaPoruka);
        File fajl = new File(putanjaDoEventsFoldera);
        if (fajl.exists()) {
            Thread posmatranjeNit = new Thread(posmatracEventsFoldera);
            posmatranjeNit.setDaemon(true);
            posmatranjeNit.start();
        }
    }

    private static void pratiAlertFolder() {
        File fajl = new File(putanjaDoAlertFoldera);
        if (fajl.exists()) {
            Thread posmatranjeNit = new Thread(posmatracAlertFoldera);
            posmatranjeNit.setDaemon(true);
            posmatranjeNit.start();
        }
    }


    public static void prikaziPromjene(Label tekstualnaPoruka) {

        Runnable zadatakEvents = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        pratiEventsFolder(tekstualnaPoruka);
                        pratiAlertFolder();
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException izuzetak) {
                    loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
                }
            }
        };

        Thread prikazPromjene = new Thread(zadatakEvents);
        prikazPromjene.setDaemon(true);
        prikazPromjene.start();
    }
}
