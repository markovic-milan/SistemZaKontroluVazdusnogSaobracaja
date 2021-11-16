package simulator;

import GUI.eventForma.EventForma;
import GUI.glavnaForma.MainForma;
import model.Letjelice.Letjelica;
import model.Letjelice.SmjerKretanja;
import javafx.application.Platform;

public class Potjera extends Thread {
    private Letjelica neprijatelj, lovac1, lovac2;
    private Simulator simulator = MainForma.simulator;


    public Potjera(Letjelica neprijatelj, Letjelica lovac1, Letjelica lovac2) {
        this.neprijatelj = neprijatelj;
        this.lovac1 = lovac1;
        this.lovac2 = lovac2;
    }


    @Override
    public void run() {
        while (true) {
            if (neprijatelj.getSmjerKretanja() == SmjerKretanja.SJEVER) {
                if (neprijatelj.getPozicijaY() == simulator.getMapa().getBrojKolona() + 1) {
                    if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY()) {
                        if (lovac1.getPozicijaX() == neprijatelj.getPozicijaX() + 1) {
                            lovac1.setBrzina(neprijatelj.getBrzina());
                        }
                    } else if (neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                        if (lovac2.getPozicijaX() == neprijatelj.getPozicijaX() + 1) {
                            lovac2.setBrzina(neprijatelj.getBrzina());
                        }
                    }
                    if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() && neprijatelj.getPozicijaX() == lovac2.getPozicijaX() - 1) {
                        obori();
                        return;
                    } else if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() - 1 && neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                        obori();
                        return;
                    }
                }
                if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX()) {
                    lovac1.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                    lovac2.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() && neprijatelj.getPozicijaX() == lovac2.getPozicijaX() && !neprijatelj.isNapustilaVazdusniProstor() && !lovac2.isNapustilaVazdusniProstor() && !lovac1.isNapustilaVazdusniProstor()) {
                    obori();
                    return;
                }
            } else if (neprijatelj.getSmjerKretanja() == SmjerKretanja.JUG) {
                if (neprijatelj.getPozicijaY() == simulator.getMapa().getBrojKolona() - 1) {
                    if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY()) {
                        if (lovac1.getPozicijaX() == neprijatelj.getPozicijaX() - 1) {
                            lovac1.setBrzina(neprijatelj.getBrzina());
                        }
                    } else if (neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                        if (lovac2.getPozicijaX() == neprijatelj.getPozicijaX() - 1) {
                            lovac2.setBrzina(neprijatelj.getBrzina());
                        }
                    }
                    if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() && neprijatelj.getPozicijaX() == lovac2.getPozicijaX() + 1) {
                        obori();
                        return;
                    } else if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() + 1 && neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                        obori();
                        return;
                    }
                }
                if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX()) {
                    lovac1.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                    lovac2.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX() && neprijatelj.getPozicijaX() == lovac2.getPozicijaX() && !neprijatelj.isNapustilaVazdusniProstor() && !lovac2.isNapustilaVazdusniProstor() && !lovac1.isNapustilaVazdusniProstor()) {
                    obori();
                    return;
                }
            } else if (neprijatelj.getSmjerKretanja() == SmjerKretanja.ISTOK) {
                if (neprijatelj.getPozicijaX() == simulator.getMapa().getBrojRedova() - 1) {
                    if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX()) {
                        if (lovac1.getPozicijaY() == neprijatelj.getPozicijaY() - 1) {
                            lovac1.setBrzina(neprijatelj.getBrzina());
                        }
                    } else if (neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                        if (lovac2.getPozicijaY() == neprijatelj.getPozicijaY() - 1) {
                            lovac2.setBrzina(neprijatelj.getBrzina());
                        }
                    }
                    if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() && neprijatelj.getPozicijaY() == lovac2.getPozicijaY() + 1) {
                        obori();
                        return;
                    } else if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() + 1 && neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                        obori();
                        return;
                    }
                }
                if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY()) {
                    lovac1.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                    lovac2.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() && neprijatelj.getPozicijaY() == lovac2.getPozicijaY() && !neprijatelj.isNapustilaVazdusniProstor() && !lovac2.isNapustilaVazdusniProstor() && !lovac1.isNapustilaVazdusniProstor()) {
                    obori();
                    return;
                }
            }
            if (neprijatelj.getSmjerKretanja() == SmjerKretanja.ZAPAD) {
                if (neprijatelj.getPozicijaX() == simulator.getMapa().getBrojRedova() + 1) {
                    if (neprijatelj.getPozicijaX() == lovac1.getPozicijaX()) {
                        if (lovac1.getPozicijaY() == neprijatelj.getPozicijaY() + 1) {
                            lovac1.setBrzina(neprijatelj.getBrzina());
                        }
                    } else if (neprijatelj.getPozicijaX() == lovac2.getPozicijaX()) {
                        if (lovac2.getPozicijaY() == neprijatelj.getPozicijaY() + 1) {
                            lovac2.setBrzina(neprijatelj.getBrzina());
                        }
                    }
                    if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() && neprijatelj.getPozicijaY() == lovac2.getPozicijaY() - 1) {
                        obori();
                        return;
                    } else if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() - 1 && neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                        obori();
                        return;
                    }
                }
                if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY()) {
                    lovac1.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaY() == lovac2.getPozicijaY()) {
                    lovac2.setBrzina(neprijatelj.getBrzina());
                }
                if (neprijatelj.getPozicijaY() == lovac1.getPozicijaY() && neprijatelj.getPozicijaY() == lovac2.getPozicijaY() && !neprijatelj.isNapustilaVazdusniProstor() && !lovac2.isNapustilaVazdusniProstor() && !lovac1.isNapustilaVazdusniProstor()) {
                    obori();
                    return;
                }
            }
            try {
                Thread.sleep(150);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void obori() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        lovac1.setBrzina(1);
        lovac2.setBrzina(1);
        neprijatelj.setNapustilaVazdusniProstor(true);
     //   MainForma.mapa.setPrisutnaStranaLetjelica(false);
        simulator.setPokreniOdbranu(false);
        simulator.setZabranaGenerisanjeCivilnihObjekata(false);
        simulator.setZabranaGenerisanjeStranihObjekata(false);

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new EventForma("Oborena strana letjelica : " + neprijatelj.getModel() + "    id = " + neprijatelj.getIdentifikacija() + ".").prikazi();
                } catch (Exception izuzetak) {
                    izuzetak.printStackTrace();
                }
            }
        });
    }
}
