package mapiranjeVazdusnogProstora;

import GUI.glavnaForma.MainForma;
import GUI.glavnaForma.MainFormaKontroler;
import model.Letjelice.Letjelica;
import model.VazdusniProstor.Mapa;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipOutputStream;

import static GUI.glavnaForma.MainFormaKontroler.TILE_SIZE;

public class MapiranjeVazdusnogProstora {
    private Rectangle rec;
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();
    private BufferedReader reader;
    private PrintWriter writer;
    public static File mapaFajl = new File(MainForma.putanjaDoFajlaSaMapom);
    private Mapa mapa = MainForma.mapa;
    private BufferedInputStream citanjeFajlaZaKopiju;
    private Logger loger = MainForma.loger;
    private List<String> sadrzaj;

    public MapiranjeVazdusnogProstora() {
        if (mapaFajl.exists()) {
            mapaFajl.delete();
        }
        try {
            writer = new PrintWriter(mapaFajl);
            for (int i = 0; i < mapa.getBrojRedova() * mapa.getBrojKolona(); i++) {
                writer.append("*\n");
                writer.flush();
            }
        } catch (IOException izuzetak) {
            loger.log(Level.WARNING, izuzetak.fillInStackTrace().toString());
        }
    }

    public void procitajFajl(File fajlZaCitanje, ZipOutputStream zip) {
        readLock.lock();
        try {
            if (zip != null) {
                byte[] bafer = new byte[1024];
                int duzina;
                citanjeFajlaZaKopiju = new BufferedInputStream(new FileInputStream(fajlZaCitanje));
                while ((duzina = citanjeFajlaZaKopiju.read(bafer)) > 0) {
                    zip.write(bafer, 0, duzina);
                    zip.flush();
                }
                zip.closeEntry();
                citanjeFajlaZaKopiju.close();
            } else {
                reader = new BufferedReader(new FileReader(mapaFajl));
                int brojac = -1;
                String linija = "";
                while ((linija = reader.readLine()) != null) {
                    ++brojac;
                    if ("*".equals(linija)) {
                        Rectangle rec = new Rectangle();
                        rec.setFill(Color.LIGHTGREEN);
                        rec.setWidth(TILE_SIZE - 3);
                        rec.setHeight(TILE_SIZE - 3);
                        StackPane stack = MainFormaKontroler.tekst[brojac / mapa.getBrojKolona()][brojac % mapa.getBrojKolona()];
                        stack.getChildren().add(rec);
                    } else {
                        String[] sadrzaj = linija.split("\\s+");
                        int x = Integer.valueOf(sadrzaj[sadrzaj.length - 2]).intValue();
                        int y = Integer.valueOf(sadrzaj[sadrzaj.length - 1]).intValue();
                        if (sadrzaj[0].contains("Avion")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack = MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.YELLOW);
                            stack.getChildren().addAll(rec, new Text("A"));
                        } else if (sadrzaj[0].contains("Helikopter")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack =  MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.VIOLET);
                            stack.getChildren().addAll(rec, new Text("H"));
                        } else if (sadrzaj[0].contains("Lovac")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack =  MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.ROYALBLUE);
                            stack.getChildren().addAll(rec, new Text("L"));
                        } else if (sadrzaj[0].contains("Bombarder")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack =  MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.RED);
                            stack.getChildren().addAll(rec, new Text("B"));
                        } else if (sadrzaj[0].contains("BespilotnaLetjelica")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack =  MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.GRAY);
                            stack.getChildren().addAll(rec, new Text("BL"));
                        } else if (sadrzaj[0].contains("Raketa")) {
                            rec = new Rectangle();
                            rec.setWidth(TILE_SIZE - 3);
                            rec.setHeight(TILE_SIZE - 3);
                            StackPane stack =  MainFormaKontroler.tekst[x][y];
                            rec.setFill(Color.BROWN);
                            stack.getChildren().addAll(rec, new Text("R"));
                        }
                    }
                }
                reader.close();
            }
        } catch (
                IOException izuzetak) {
            loger.log(Level.WARNING, izuzetak.fillInStackTrace().toString());
        } finally {
            readLock.unlock();
        }
    }


    public void upisiLetjelicu(Letjelica letjelica) {
        //sinhronizovano
        writeLock.lock();
        try {
            sadrzaj = Files.readAllLines(Paths.get(mapaFajl.getPath()));
            if (letjelica.isNapustilaVazdusniProstor() || letjelica.isSudarilaSe()) {
                for (int i = 0; i < sadrzaj.size(); i++) {
                    if (sadrzaj.get(i).contains(letjelica.getIdentifikacija())) {
                        sadrzaj.set(i, "*");
                        Files.write(Paths.get(mapaFajl.getPath()), sadrzaj);
                    }
                }
                mapa.getMatrica()[letjelica.getPozicijaX()][letjelica.getPozicijaY()] = null;
            } else {
                String string = letjelica.getModel() + " " + letjelica.getIdentifikacija() + " " + letjelica.getPozicijaX() + " " + letjelica.getPozicijaY();
                for (int i = 0; i < sadrzaj.size(); i++) {
                    if (sadrzaj.get(i).contains(letjelica.getIdentifikacija())) {
                        sadrzaj.set(i, "*");
                        Files.write(Paths.get(mapaFajl.getPath()), sadrzaj);
                    }
                }
                sadrzaj.set((letjelica.getPozicijaX() * mapa.getBrojKolona()) + letjelica.getPozicijaY(), string);
                Files.write(Paths.get(mapaFajl.getPath()), sadrzaj);
            }
        } catch (IOException izuzetak) {
            loger.log(Level.WARNING, izuzetak.fillInStackTrace().toString());
        }
        writeLock.unlock();
    }
}