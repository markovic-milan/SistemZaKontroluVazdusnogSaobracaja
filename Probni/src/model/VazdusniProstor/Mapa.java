package model.VazdusniProstor;

import model.Letjelice.Letjelica;

public class Mapa {

    private Object[][] matrica;
    private int brojKolona;
    private int brojRedova;
    private boolean zabranaLeta = false;
    private boolean prisutnaStranaLetjelica = false;

    public Mapa(int brojRedova, int brojKolona) {
        this.brojKolona = brojKolona;
        this.brojRedova = brojRedova;
        matrica = new Letjelica[brojRedova][brojKolona];
    }

    public Object[][] getMatrica() {
        return matrica;
    }

    public int getBrojKolona() {
        return brojKolona;
    }

    public int getBrojRedova() {
        return brojRedova;
    }

    public boolean isZabranaLeta() {
        return zabranaLeta;
    }

    public void setZabranaLeta(boolean zabranaLeta) {
        this.zabranaLeta = zabranaLeta;
    }

    public boolean isPrisutnaStranaLetjelica() {
        return prisutnaStranaLetjelica;
    }

    public void setPrisutnaStranaLetjelica(boolean prisutnaStranaLetjelica) {
        this.prisutnaStranaLetjelica = prisutnaStranaLetjelica;
    }
}
