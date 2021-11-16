package model.Letjelice.Avioni.VojniAvioni;

import model.Interfejsi.GadjanjeCiljevaNaZemljiInterface;
import model.Interfejsi.GadjanjeCiljevaUVazduhuInterface;
import model.Letjelice.SmjerKretanja;

import java.util.logging.Level;

public class Lovac extends VojniAvion implements GadjanjeCiljevaNaZemljiInterface, GadjanjeCiljevaUVazduhuInterface {

    public Lovac(String model, String identifikator, int visina) {
        super(model, identifikator, visina);
    }

    @Override
    public void run() {
        if (isPotjera()) {
            leti();
        } else
            super.leti();
    }

    @Override
    public void leti() {

        // setSmjerKretanja(super.izracunajSmjerKretanja());
        getMapa().getMatrica()[getPozicijaX()][getPozicijaY()] = this;
        if (getSmjerKretanja() == SmjerKretanja.ISTOK) {
            for (int kolona = getPozicijaY() + 1; kolona < getMapa().getBrojKolona(); kolona++) {
                if (getPozicijaY() + 1 < getMapa().getBrojKolona()) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaY(), getPozicijaY() + 1);
                }
            }
        } else if (getSmjerKretanja() == SmjerKretanja.ZAPAD) {
            for (int kolona = getPozicijaY(); kolona > 0; kolona--) {
                if (getPozicijaY() > 0) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX(), getPozicijaY() - 1);
                }
            }
        } else if (getSmjerKretanja() == SmjerKretanja.SJEVER) {
            for (int vrsta = getPozicijaX(); vrsta > 0; vrsta--) {
                if (getPozicijaX() > 0) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX() - 1, getPozicijaY());
                }
            }
        } else {
            for (int vrsta = getPozicijaX(); vrsta < getMapa().getBrojRedova(); vrsta++) {
                if (getPozicijaX() + 1 < getMapa().getBrojRedova()) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX() + 1, getPozicijaY());
                }
            }
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            getLoger().log(Level.WARNING, e.fillInStackTrace().toString());
        }
        setNapustilaVazdusniProstor(true);
    }

    @Override
    public void pomjeriSeNaPolje(int x, int y) {
        try {
            Thread.sleep(getBrzina() * 1000);
            getMapa().getMatrica()[getPrethodnaPozicijaX()][getPozicijaY()] = null;
            if (getSmjerKretanja() == SmjerKretanja.ZAPAD) {
                getMapa().getMatrica()[getPozicijaX()][getPozicijaY() - 1] = this;
                setPozicijaY(getPozicijaY() - 1);
            } else if (getSmjerKretanja() == SmjerKretanja.ISTOK) {
                getMapa().getMatrica()[getPozicijaX()][getPozicijaY() + 1] = this;
                setPozicijaY(getPozicijaY() + 1);
            } else if (getSmjerKretanja() == SmjerKretanja.SJEVER) {
                getMapa().getMatrica()[getPozicijaX() - 1][getPozicijaY()] = this;
                setPozicijaX(getPozicijaX() - 1);
            } else if (getSmjerKretanja() == SmjerKretanja.JUG) {
                getMapa().getMatrica()[getPozicijaX() + 1][getPozicijaY()] = this;
                setPozicijaX(getPozicijaX() + 1);

            }
        } catch (InterruptedException izuzetak) {
            getLoger().log(Level.WARNING, izuzetak.fillInStackTrace().toString());
        }
    }
}
