package model.Osobe;

import java.io.File;

public class Pilot extends Osoba {
    private File licenca;

    public Pilot(String ime, String prezime, File licenca) {
        super(ime, prezime);
        this.licenca = licenca;
    }

    public File getLicenca() {
        return licenca;
    }

    public void setLicenca(File licenca) {
        this.licenca = licenca;
    }
}
