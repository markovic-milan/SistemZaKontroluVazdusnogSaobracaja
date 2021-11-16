package model.Rakete;

import GUI.glavnaForma.MainForma;
import model.Letjelice.SmjerKretanja;
import model.VazdusniProstor.Mapa;

import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Raketa extends Thread {
    private Mapa mapa = MainForma.mapa;
    private SmjerKretanja smjerKretanja;
    private Random random = new Random();
    private int domet, visina, brzinaLetenja;
    private int pozicijaY = random.nextInt(mapa.getBrojKolona());
    private int pozicijaX = random.nextInt(mapa.getBrojRedova());
    private boolean zavrsila = true;
    private Logger loger = MainForma.loger;

    public Raketa() {
        this.domet = random.nextInt(100);
        this.visina = random.nextInt(91) + 10;
        this.brzinaLetenja = random.nextInt(3) + 1;
    }

    @Override
    public void run() {
        mapa.getMatrica()[pozicijaX][pozicijaY] = this;
        leti();
    }

    public void leti() {
        mapa.getMatrica()[pozicijaX][pozicijaX] = this;
        smjerKretanja = izracunajSmjerKretanja();
        if (smjerKretanja == SmjerKretanja.ZAPAD) {
            if (domet >= mapa.getBrojKolona() - pozicijaY - 1) {
                for (int kolona = pozicijaY + 1; kolona < mapa.getBrojKolona(); kolona++) {
                    idiIstocno();
                }
            } else {
                for (int brojac = 0; brojac < domet; brojac++) {
                    idiIstocno();
                }
            }
        } else if (smjerKretanja == SmjerKretanja.ISTOK) {
            if (domet >= pozicijaY - 1) {
                for (int kolona = pozicijaY - 1; kolona >= 0; kolona--) {
                    idiZapadno();
                }
            } else {
                for (int brojac = 0; brojac < domet; brojac++) {
                    idiZapadno();
                }
            }
        } else if (smjerKretanja == SmjerKretanja.SJEVER) {
            if (domet >= mapa.getBrojRedova() - pozicijaX - 1) {
                for (int vrsta = pozicijaX + 1; vrsta < mapa.getBrojRedova(); vrsta++) {
                    idiJuzno();
                }
            } else {
                for (int brojac = 0; brojac < domet; brojac++) {
                    idiJuzno();
                }
            }
        } else {
            if (domet >= pozicijaX - 1) {
                for (int vrsta = pozicijaX - 1; vrsta >= 0; vrsta--) {
                    idiSjeverno();
                }
            } else {
                for (int brojac = 0; brojac < domet; brojac++) {
                    idiJuzno();
                }
            }
        }
        zavrsila = true;
    }

    public void idiIstocno() {
        try {
            Thread.sleep(brzinaLetenja * 1000);
            mapa.getMatrica()[pozicijaX][++pozicijaY] = this;
            mapa.getMatrica()[pozicijaX][pozicijaY - 1] = null;
        } catch (InterruptedException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    public void idiZapadno() {
        try {
            Thread.sleep(brzinaLetenja * 1000);
            mapa.getMatrica()[pozicijaX][--pozicijaY] = this;
            mapa.getMatrica()[pozicijaX][pozicijaY + 1] = null;
        } catch (InterruptedException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    public void idiSjeverno() {
        try {
            Thread.sleep(brzinaLetenja * 1000);
            mapa.getMatrica()[--pozicijaX][pozicijaY] = this;
            mapa.getMatrica()[pozicijaX + 1][pozicijaY] = null;
        } catch (InterruptedException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    public void idiJuzno() {
        try {
            Thread.sleep(brzinaLetenja * 1000);
            mapa.getMatrica()[++pozicijaX][pozicijaY] = this;
            mapa.getMatrica()[pozicijaX - 1][pozicijaY] = null;
        } catch (InterruptedException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    public SmjerKretanja izracunajSmjerKretanja() {
        double rezultatX = ((1.0 / 2) * (mapa.getBrojRedova() - 1) - pozicijaX);
        double rezultatY = ((1.0 / 2) * (mapa.getBrojKolona() - 1) - pozicijaY);
        return (Math.abs(rezultatX) - Math.abs(rezultatY) > 0) ? (rezultatX > 0 ?
                SmjerKretanja.JUG : SmjerKretanja.SJEVER) : (rezultatY > 0 ? SmjerKretanja.ISTOK : SmjerKretanja.ZAPAD);
    }

    public int getDomet() {
        return domet;
    }

    public void setDomet(int domet) {
        this.domet = domet;
    }

    public int getVisina() {
        return visina;
    }

    public void setVisina(int visina) {
        this.visina = visina;
    }

    public int getBrzinaLetenja() {
        return brzinaLetenja;
    }

    public void setBrzinaLetenja(int brzinaLetenja) {
        this.brzinaLetenja = brzinaLetenja;
    }
}
