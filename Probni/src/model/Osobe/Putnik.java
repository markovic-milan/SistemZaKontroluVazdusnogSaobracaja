package model.Osobe;

public class Putnik extends Osoba {
    private String brojPasosa;

    public Putnik(String ime, String prezime, String brojPasosa) {
        super(ime, prezime);
        this.brojPasosa = brojPasosa;
    }

    public String getBrojPasosa() {
        return brojPasosa;
    }

    public void setBrojPasosa(String brojPasosa) {
        this.brojPasosa = brojPasosa;
    }
}
