package sudar;

import GUI.glavnaForma.MainForma;

import java.io.*;

public class Upozorenje implements Serializable {
    private String model;
    private String vrijeme;
    private String pozicija;

    public Upozorenje(String modeli, String pozicija, String vrijeme) {
        this.model = modeli;
        this.vrijeme = vrijeme;
        this.pozicija = pozicija;
    }

    public void serijalizuj(String nazivFajla) throws IOException {
        ObjectOutputStream upis = new ObjectOutputStream(new FileOutputStream(new File(MainForma.putanjaDoAlertFoldera + File.separator + nazivFajla)));
        upis.writeObject(this);
        upis.close();
    }

    public String getOpis() {
        return model;
    }

    public void setOpis(String modeli) {
        this.model = modeli;
    }

    public String getVrijeme() {
        return vrijeme;
    }

    public void setVrijeme(String vrijeme) {
        this.vrijeme = vrijeme;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getPozicija() {
        return pozicija;
    }

    public void setPozicija(String pozicija) {
        this.pozicija = pozicija;
    }
}
