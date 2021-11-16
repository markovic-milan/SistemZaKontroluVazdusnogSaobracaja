package model.Letjelice.Helikopteri;

public class PutnickiHelikopter extends Helikopter {
    private int brojSjedista;

    public PutnickiHelikopter(String model, String identifikator, int visina, int brojSjedista) {
        super(model, identifikator, visina);
        this.brojSjedista = brojSjedista;
    }

    public int getBrojSjedista() {
        return brojSjedista;
    }

    public void setBrojSjedista(int brojSjedista) {
        this.brojSjedista = brojSjedista;
    }
}
