package model.Letjelice;

import GUI.glavnaForma.MainForma;
import model.Osobe.Osoba;
import model.VazdusniProstor.Mapa;
import sudar.Sudar;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Letjelica extends Thread {
    private String model;
    private String identifikacija;
    private int visina;
    private int brzina;
    private int pozicijaX;

    public int getPrethodnaPozicijaX() {
        return prethodnaPozicijaX;
    }

    public void setPrethodnaPozicijaX(int prethodnaPozicijaX) {
        this.prethodnaPozicijaX = prethodnaPozicijaX;
    }

    public int getPrethodnaPozicijaY() {
        return prethodnaPozicijaY;
    }

    public void setPrethodnaPozicijaY(int prethodnaPozicijaY) {
        this.prethodnaPozicijaY = prethodnaPozicijaY;
    }

    private int pozicijaY;
    private int prethodnaPozicijaX;
    private int prethodnaPozicijaY;
    private ArrayList<Osoba> osobe = new ArrayList<>();
    private HashMap<String, String> karakteristike = new HashMap<>();
    private Mapa mapa = MainForma.mapa;
    private boolean stranaLetjelica = false;
    private boolean sudarilaSe = false;
    private boolean napustilaVazdusniProstor = false;
    private boolean izadji = false;
    private SmjerKretanja smjerKretanja;
    private Logger loger = MainForma.loger;

    public boolean isPotjera() {
        return potjera;
    }

    public void setPotjera(boolean potjera) {
        this.potjera = potjera;
    }

    private boolean potjera = false;

    public boolean isIzadji() {
        return izadji;
    }

    public void setIzadji(boolean izadji) {
        this.izadji = izadji;
    }

    public SmjerKretanja getSmjerKretanja() {
        return smjerKretanja;
    }

    public void setSmjerKretanja(SmjerKretanja smjerKretanja) {
        this.smjerKretanja = smjerKretanja;
    }

    public Logger getLoger() {
        return loger;
    }

    public void setLoger(Logger loger) {
        this.loger = loger;
    }


    public Letjelica(String model, String identifikator, int visina) {
        this.model = model;
        this.identifikacija = identifikator;
        this.visina = visina;

    }

    @Override
    public void run() {
        leti();
    }

    public void leti() {
        mapa.getMatrica()[pozicijaX][pozicijaY] = this;
        //System.out.println("Kreirana!");

        //System.out.println(getModel() + " " + getIdentifikacija() + " " + getPozicijaX() + " " + getPozicijaY());
        smjerKretanja = izracunajSmjerKretanja();

        if (smjerKretanja == SmjerKretanja.ISTOK) {
            for (int kolona = pozicijaY + 1; kolona < mapa.getBrojKolona() && !sudarilaSe; kolona++) {
                if (izadji || mapa.isPrisutnaStranaLetjelica() || mapa.isZabranaLeta()) {
                    napustiVazdusniProstorNajkracimPutem();
                    return;
                } else {
                    if (pozicijaY + 1 < mapa.getBrojKolona()) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX, pozicijaY + 1);
                    }
                }
            }
        } else if (smjerKretanja == SmjerKretanja.ZAPAD) {
            for (int kolona = pozicijaY; kolona > 0 && !sudarilaSe; kolona--) {
                if (izadji || mapa.isPrisutnaStranaLetjelica() || mapa.isZabranaLeta()) {
                    napustiVazdusniProstorNajkracimPutem();
                    return;
                } else {
                    if (pozicijaY > 0) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX, pozicijaY - 1);
                    }
                }

            }
        } else if (smjerKretanja == SmjerKretanja.SJEVER) {
            for (int vrsta = pozicijaX; vrsta > 0 && !sudarilaSe; vrsta--) {
                if (izadji || mapa.isPrisutnaStranaLetjelica() || mapa.isZabranaLeta()) {
                    napustiVazdusniProstorNajkracimPutem();
                    return;
                } else {
                    if (pozicijaX > 0) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX - 1, pozicijaY);
                    }
                }
            }
        } else {
            for (int vrsta = pozicijaX; vrsta < mapa.getBrojRedova() && !sudarilaSe; vrsta++) {
                if (izadji || mapa.isPrisutnaStranaLetjelica() || mapa.isZabranaLeta()) {
                    napustiVazdusniProstorNajkracimPutem();
                    return;
                } else {
                    if (pozicijaX + 1 < mapa.getBrojRedova()) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX + 1, pozicijaY);
                    }
                }

            }
        }
        napustilaVazdusniProstor = true;
    }

    public void pomjeriSeNaPolje(int x, int y) {
        //  System.out.println(getModel() + " " + getIdentifikacija() + " " + getPozicijaX() + "  " + getPozicijaY() + " " + getSmjerKretanja());
        if (mapa.getMatrica()[x][y] != null && mapa.getMatrica()[x][y] instanceof Letjelica && ((Letjelica) mapa.getMatrica()[x][y]).getVisina() == visina && !((Letjelica) mapa.getMatrica()[x][y]).isSudarilaSe() && !sudarilaSe && !mapa.isPrisutnaStranaLetjelica()) {
            Letjelica letjelica = (Letjelica) mapa.getMatrica()[x][y];
            letjelica.setSudarilaSe(true);
            setSudarilaSe(true);
            new Sudar(this, letjelica, new Date(), pozicijaX, pozicijaY).evidentirajSudar();
            return;
        } else {
            if (!sudarilaSe && smjerKretanja == SmjerKretanja.ZAPAD && pozicijaY != 1 && mapa.getMatrica()[x][y - 1] != null && mapa.getMatrica()[x][y - 1] instanceof Letjelica
                    && ((Letjelica) mapa.getMatrica()[x][y - 1]).getVisina() == visina && ((Letjelica) mapa.getMatrica()[x][y - 1]).getSmjerKretanja() == SmjerKretanja.ISTOK && !sudarilaSe && !mapa.isPrisutnaStranaLetjelica()) {
                Letjelica letjelica = (Letjelica) mapa.getMatrica()[x][y - 1];
                letjelica.setSudarilaSe(true);
                setSudarilaSe(true);
                new Sudar(this, letjelica, new Date(), pozicijaX, pozicijaY).evidentirajSudar();
                return;
            } else if (!sudarilaSe && smjerKretanja == SmjerKretanja.ISTOK && pozicijaY != (mapa.getBrojKolona() - 2) && mapa.getMatrica()[x][y + 1] != null && mapa.getMatrica()[x][y + 1] instanceof Letjelica
                    && ((Letjelica) mapa.getMatrica()[x][y + 1]).getVisina() == visina && ((Letjelica) mapa.getMatrica()[x][y + 1]).getSmjerKretanja() == SmjerKretanja.ZAPAD && !sudarilaSe && !sudarilaSe && !mapa.isPrisutnaStranaLetjelica()) {
                Letjelica letjelica = (Letjelica) mapa.getMatrica()[x][y + 1];
                letjelica.setSudarilaSe(true);
                setSudarilaSe(true);
                new Sudar(this, letjelica, new Date(), pozicijaX, pozicijaY).evidentirajSudar();
                return;
            } else if (!sudarilaSe && smjerKretanja == SmjerKretanja.JUG && pozicijaX != (mapa.getBrojRedova() - 2) && mapa.getMatrica()[x][y + 1] != null && mapa.getMatrica()[x + 1][y] instanceof Letjelica
                    && ((Letjelica) mapa.getMatrica()[x + 1][y]).getVisina() == visina && ((Letjelica) mapa.getMatrica()[x + 1][y]).getSmjerKretanja() == SmjerKretanja.SJEVER && !mapa.isPrisutnaStranaLetjelica()) {
                Letjelica letjelica = (Letjelica) mapa.getMatrica()[x + 1][y];
                letjelica.setSudarilaSe(true);
                setSudarilaSe(true);
                new Sudar(this, letjelica, new Date(), pozicijaX, pozicijaY).evidentirajSudar();
                return;
            } else if (!sudarilaSe && smjerKretanja == SmjerKretanja.SJEVER && pozicijaX != 1 && mapa.getMatrica()[x - 1][y] != null && mapa.getMatrica()[x - 1][y] instanceof Letjelica
                    && ((Letjelica) mapa.getMatrica()[x - 1][y]).getVisina() == visina && ((Letjelica) mapa.getMatrica()[x - 1][y]).getSmjerKretanja() == SmjerKretanja.JUG && !mapa.isPrisutnaStranaLetjelica()) {
                Letjelica letjelica = (Letjelica) mapa.getMatrica()[x - 1][y];
                letjelica.setSudarilaSe(true);
                setSudarilaSe(true);
                new Sudar(this, letjelica, new Date(), pozicijaX, pozicijaY).evidentirajSudar();
                return;
            }
        }
        if (sudarilaSe) {
            return;
        } else {
            try {
                mapa.getMatrica()[prethodnaPozicijaX][prethodnaPozicijaY] = null;
                if (smjerKretanja == SmjerKretanja.ZAPAD && !sudarilaSe)
                    mapa.getMatrica()[pozicijaX][--pozicijaY] = this;
                else if (smjerKretanja == SmjerKretanja.ISTOK && !sudarilaSe) {
                    mapa.getMatrica()[pozicijaX][++pozicijaY] = this;
                } else if (smjerKretanja == SmjerKretanja.SJEVER && !sudarilaSe) {
                    mapa.getMatrica()[--pozicijaX][pozicijaY] = this;
                } else if (smjerKretanja == SmjerKretanja.JUG && !sudarilaSe) {
                    mapa.getMatrica()[++pozicijaX][pozicijaY] = this;
                }
                Thread.sleep(brzina * 1000);
            } catch (InterruptedException izuzetak) {
                loger.log(Level.WARNING, izuzetak.fillInStackTrace().toString());
            }
        }
    }

    private void napustiVazdusniProstorNajkracimPutem() {
        if (smjerKretanja == SmjerKretanja.ISTOK) {
            if (pozicijaY < (mapa.getBrojKolona() - pozicijaY + 1)) {
                if (pozicijaX == 0) {
                    setSmjerKretanja(SmjerKretanja.JUG);
                    pomjeriSeNaPolje(pozicijaX + 1, pozicijaY);
                } else {
                    setSmjerKretanja(SmjerKretanja.SJEVER);
                    pomjeriSeNaPolje(pozicijaX - 1, pozicijaY);
                }
                setSmjerKretanja(SmjerKretanja.ZAPAD);
                for (int kolona = pozicijaY; kolona > 0 && !sudarilaSe; kolona--) {
                    if (pozicijaY > 0) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX, pozicijaY - 1);
                    }
                }
            } else for (int kolona = pozicijaY + 1; kolona < mapa.getBrojKolona() && !sudarilaSe; kolona++) {
                if (pozicijaY + 1 < mapa.getBrojKolona()) {
                    prethodnaPozicijaX = pozicijaX;
                    prethodnaPozicijaY = pozicijaY;
                    pomjeriSeNaPolje(pozicijaX, pozicijaY + 1);
                }
            }
        } else if (smjerKretanja == SmjerKretanja.ZAPAD) {
            if (pozicijaY > (mapa.getBrojKolona() - pozicijaY + 1)) {

                if (pozicijaX == 0) {
                    setSmjerKretanja(SmjerKretanja.JUG);
                    pomjeriSeNaPolje(pozicijaX + 1, pozicijaY);
                } else {
                    setSmjerKretanja(SmjerKretanja.SJEVER);
                    pomjeriSeNaPolje(pozicijaX - 1, pozicijaY);
                }
                setSmjerKretanja(SmjerKretanja.ISTOK);
                for (int kolona = pozicijaY + 1; kolona < mapa.getBrojKolona() && !sudarilaSe; kolona++) {
                    if (pozicijaY + 1 < mapa.getBrojKolona()) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX, pozicijaY + 1);
                    }
                }
            } else for (int kolona = pozicijaY; kolona > 0 && !sudarilaSe; kolona--) {
                if (pozicijaY > 0) {
                    prethodnaPozicijaX = pozicijaX;
                    prethodnaPozicijaY = pozicijaY;
                    pomjeriSeNaPolje(pozicijaX, pozicijaY - 1);
                }
            }
        } else if (smjerKretanja == SmjerKretanja.SJEVER) {
            if (pozicijaX > (mapa.getBrojRedova() - pozicijaX + 1)) {
                if (pozicijaY == 0) {
                    setSmjerKretanja(SmjerKretanja.ISTOK);
                    pomjeriSeNaPolje(pozicijaX, pozicijaY + 1);
                } else {
                    setSmjerKretanja(SmjerKretanja.ZAPAD);
                    pomjeriSeNaPolje(pozicijaX, pozicijaY - 1);
                }
                setSmjerKretanja(SmjerKretanja.JUG);
                for (int vrsta = pozicijaX; vrsta < mapa.getBrojRedova() && !sudarilaSe; vrsta++) {
                    if (pozicijaX + 1 < mapa.getBrojRedova()) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX + 1, pozicijaY);
                    }
                }
            } else for (int vrsta = pozicijaX; vrsta > 0 && !sudarilaSe; vrsta--) {
                if (pozicijaX > 0) {
                    prethodnaPozicijaX = pozicijaX;
                    prethodnaPozicijaY = pozicijaY;
                    pomjeriSeNaPolje(pozicijaX - 1, pozicijaY);
                }
            }
        } else if (smjerKretanja == SmjerKretanja.JUG) {
            if (pozicijaX < (mapa.getBrojRedova() - pozicijaX + 1)) {
                if (pozicijaY == 0) {
                    setSmjerKretanja(SmjerKretanja.ISTOK);
                    pomjeriSeNaPolje(pozicijaX, pozicijaY + 1);
                } else {
                    setSmjerKretanja(SmjerKretanja.ZAPAD);
                    pomjeriSeNaPolje(pozicijaX, pozicijaY - 1);
                }
                setSmjerKretanja(SmjerKretanja.SJEVER);
                for (int vrsta = pozicijaX; vrsta > 0 && !sudarilaSe; vrsta--) {
                    if (pozicijaX > 0) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX - 1, pozicijaY);
                    }
                }
            } else
                for (int vrsta = pozicijaX; vrsta < mapa.getBrojRedova() && !sudarilaSe; vrsta++) {
                    if (pozicijaX + 1 < mapa.getBrojRedova()) {
                        prethodnaPozicijaX = pozicijaX;
                        prethodnaPozicijaY = pozicijaY;
                        pomjeriSeNaPolje(pozicijaX + 1, pozicijaY);
                    }
                }
        }
        setNapustilaVazdusniProstor(true);
    }

    public SmjerKretanja izracunajSmjerKretanja() {
        double rezultatX = ((1.0 / 2) * (mapa.getBrojRedova() - 1) - pozicijaX);
        double rezultatY = ((1.0 / 2) * (mapa.getBrojKolona() - 1) - pozicijaY);
        return (Math.abs(rezultatX) - Math.abs(rezultatY) > 0) ? (rezultatX > 0 ?
                SmjerKretanja.JUG : SmjerKretanja.SJEVER) : (rezultatY > 0 ? SmjerKretanja.ISTOK : SmjerKretanja.ZAPAD);
    }

    public ArrayList<Osoba> getOsobe() {
        return osobe;
    }

    public void setOsobe(ArrayList<Osoba> osobe) {
        this.osobe = osobe;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public boolean isStranaLetjelica() {
        return stranaLetjelica;
    }

    public void setStranaLetjelica(boolean stranaLetjelica) {
        this.stranaLetjelica = stranaLetjelica;
    }

    public boolean isSudarilaSe() {
        return sudarilaSe;
    }

    public void setSudarilaSe(boolean sudarilaSe) {
        this.sudarilaSe = sudarilaSe;
    }

    public boolean isNapustilaVazdusniProstor() {
        return napustilaVazdusniProstor;
    }

    public void setNapustilaVazdusniProstor(boolean napustilaVazdusniProstor) {
        this.napustilaVazdusniProstor = napustilaVazdusniProstor;
    }

    public String getModel() {
        return model;
    }

    public int getPozicijaX() {
        return pozicijaX;
    }

    public void setPozicijaX(int pozicijaX) {
        this.pozicijaX = pozicijaX;
    }

    public int getPozicijaY() {
        return pozicijaY;
    }

    public void setPozicijaY(int pozicijaY) {
        this.pozicijaY = pozicijaY;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getIdentifikacija() {
        return identifikacija;
    }

    public void setIdentifikacija(String identifikacija) {
        this.identifikacija = identifikacija;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public int getBrzina() {
        return brzina;
    }

    public void setBrzina(int brzina) {
        this.brzina = brzina;
    }

    public HashMap<String, String> getKarakteristike() {
        return karakteristike;
    }

    public void setKarakteristike(HashMap<String, String> karakteristike) {
        this.karakteristike = karakteristike;
    }
}