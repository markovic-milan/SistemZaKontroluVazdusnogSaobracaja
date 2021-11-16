package model.Letjelice.Avioni.VojniAvioni;

import GUI.glavnaForma.MainForma;
import model.Interfejsi.BombardovanjeCiljevaNaZemlji;
import model.Letjelice.SmjerKretanja;
import simulator.Simulator;

import java.util.logging.Level;

public class Bombarder extends VojniAvion implements BombardovanjeCiljevaNaZemlji {
    public Bombarder(String model, String identifikator, int visina) {
        super(model, identifikator, visina);
        setStranaLetjelica(true);
    }

    private Simulator simulator = MainForma.simulator;

    @Override
    public void run() {
        leti();
        getMapa().setPrisutnaStranaLetjelica(false);
        simulator.setPokreniOdbranu(false);
     //   simulator.setZabranaGenerisanjeStranihObjekata(false);
        simulator.setZabranaGenerisanjeCivilnihObjekata(false);
    }

    @Override
    public void leti() {
        setSmjerKretanja(super.izracunajSmjerKretanja());
        getMapa().getMatrica()[getPozicijaX()][getPozicijaY()] = this;
        if (getSmjerKretanja() == SmjerKretanja.ISTOK) {
            for (int kolona = getPozicijaY() + 1; kolona < getMapa().getBrojKolona() && !isNapustilaVazdusniProstor(); kolona++) {
                if (getPozicijaY() + 1 < getMapa().getBrojKolona()) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaY(), getPozicijaY() + 1);
                }
            }
        } else if (getSmjerKretanja() == SmjerKretanja.ZAPAD) {
            for (int kolona = getPozicijaY(); kolona > 0 && !isNapustilaVazdusniProstor(); kolona--) {
                if (getPozicijaY() > 0) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX(), getPozicijaY() - 1);
                }
            }
        } else if (getSmjerKretanja() == SmjerKretanja.SJEVER) {
            for (int vrsta = getPozicijaX(); vrsta > 0 && !isNapustilaVazdusniProstor(); vrsta--) {
                if (getPozicijaX() > 0) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX() - 1, getPozicijaY());
                }
            }
        } else {
            for (int vrsta = getPozicijaX(); vrsta < getMapa().getBrojRedova() && !isNapustilaVazdusniProstor(); vrsta++) {
                if (getPozicijaX() + 1 < getMapa().getBrojRedova()) {
                    setPrethodnaPozicijaX(getPozicijaX());
                    setPrethodnaPozicijaY(getPozicijaY());
                    pomjeriSeNaPolje(getPozicijaX() + 1, getPozicijaY());
                }
            }
        }
        if (isNapustilaVazdusniProstor()) {
            getMapa().getMatrica()[getPrethodnaPozicijaX()][getPozicijaY()] = null;
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


