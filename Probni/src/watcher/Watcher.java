package watcher;

import GUI.glavnaForma.MainForma;
import GUI.sudarForma.SudarForma;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;

public class Watcher extends Thread {

    private Stage stage;
    private String putanjaDoFoldera;
    private Label labela;
    private SudarForma prikazSudaraForma;
    private Logger loger = MainForma.loger;

    public Watcher(String putanjaDoFoldera, Label labela) {
        this.putanjaDoFoldera = putanjaDoFoldera;
        this.labela = labela;
    }

    @Override
    public void run() {
        pratiPromjene();
    }

    private void pratiPromjene() {
        try {
            WatchService servis = FileSystems.getDefault().newWatchService();
            Path putanja = Paths.get(putanjaDoFoldera);
            putanja.register(servis, ENTRY_CREATE);

            while (true) {
                WatchKey kljuc;
                try {
                    kljuc = servis.take();
                } catch (InterruptedException izuzetak) {
                    return;
                }
                for (WatchEvent<?> dogadjaj : kljuc.pollEvents()) {
                    WatchEvent.Kind<?> tip = dogadjaj.kind();
                    WatchEvent<Path> vrsta = (WatchEvent<Path>) dogadjaj;
                    Path naziv = vrsta.context();
                    if (labela != null) {
                        // prikaz na glavnoj formi u vidu labele o detektovanju strane letjelice i kreiranja fajla u folderu events
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                labela.setText("Detektovana strana letjelica, " + naziv.getFileName().toString().substring(0, 20));
                            }
                        });
                    }
                    if (labela == null) {
                        // posmatranje alert foldera za prikaz sudara
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    if (stage != null) {
                                        stage.close();
                                    }
                                    prikazSudaraForma = new SudarForma();
                                    prikazSudaraForma.naslov = "Desio se sudar";
                                    prikazSudaraForma.fajlZaPrikaz = new File(putanjaDoFoldera + File.separator + naziv.getFileName().toString());
                                    prikazSudaraForma.prikazi();
                                    stage = prikazSudaraForma.stage;
                                } catch (Exception izuzetak) {
                                    loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
                                }
                            }
                        });
                    }
                }
                boolean validan = kljuc.reset();
                if (!validan) {
                    return;
                }
            }
        } catch (IOException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }
}