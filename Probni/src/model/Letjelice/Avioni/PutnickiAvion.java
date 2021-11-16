package model.Letjelice.Avioni;


public class PutnickiAvion extends Avion {
    private int brojSjedista, maksimalnaTezinaPrtljaga;

    public PutnickiAvion(String model, String identifikator, int visina, int brojSjedista, int maksimalnaTezinaPrtljaga) {
        super(model, identifikator, visina);
        this.brojSjedista = brojSjedista;
        this.maksimalnaTezinaPrtljaga = maksimalnaTezinaPrtljaga;
    }

    public int getBrojSjedista() {
        return brojSjedista;
    }

    public void setBrojSjedista(int brojSjedista) {
        this.brojSjedista = brojSjedista;
    }

    public int getMaksimalnaTezinaPrtljaga() {
        return maksimalnaTezinaPrtljaga;
    }

    public void setMaksimalnaTezinaPrtljaga(int maksimalnaTezinaPrtljaga) {
        this.maksimalnaTezinaPrtljaga = maksimalnaTezinaPrtljaga;
    }
}
