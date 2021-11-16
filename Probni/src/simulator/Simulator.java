package simulator;

import GUI.glavnaForma.MainForma;
import model.Letjelice.Avioni.ProtivPozarniAvion;
import model.Letjelice.Avioni.PutnickiAvion;
import model.Letjelice.Avioni.TransportniAvion;
import model.Letjelice.Avioni.VojniAvioni.Bombarder;
import model.Letjelice.Avioni.VojniAvioni.Lovac;
import model.Letjelice.BespilotneLetjelice.BespilotnaLetjelica;
import model.Letjelice.Helikopteri.ProtivPozarniHelikopter;
import model.Letjelice.Helikopteri.PutnickiHelikopter;
import model.Letjelice.Helikopteri.TransportniHelikopter;
import model.Letjelice.Letjelica;
import model.VazdusniProstor.Mapa;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

import static model.Letjelice.SmjerKretanja.*;

public class Simulator extends Thread {
    private Letjelica stranaLetjelica;
    private Mapa mapa;
    private ConfigWatcher configWatcher;
    private int intervalKreiranjaLetjelica;
    private int brojKolona;
    private int brojRedova;
    private boolean straniVojniObjekat;
    private boolean domaciVojniObjekat;
    private boolean pokreniOdbranu = false;
    private boolean zabranaGenerisanjeStranihObjekata = false;
    private boolean zabranaGenerisanjeCivilnihObjekata = false;
    private int pozicijaStraneLetjeliceX;
    private int pozicijaStraneLetjeliceY;
    private ArrayList<String> identifikatori = new ArrayList<>();
    private Logger loger = MainForma.loger;

    public Simulator(Mapa mapa) {
        this.mapa = mapa;
        //citanje config fajla i setovanje
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(new File("C:\\Users\\milan\\Desktop\\Probni\\src\\config\\config.properties"));
            properties.load(in);
            brojKolona = Integer.valueOf(properties.getProperty("brojKolona"));
            brojRedova = Integer.valueOf(properties.getProperty("brojVrsta"));
            intervalKreiranjaLetjelica = Integer.valueOf(properties.getProperty("intervalKreiranjaLetjelica"));
            straniVojniObjekat = Boolean.valueOf(properties.getProperty("prisustvoStranihVojnihObjekata"));
            domaciVojniObjekat = Boolean.valueOf(properties.getProperty("prisustvoDomacihVojnihObjekata"));
            in.close();
        } catch (IOException e) {
            loger.log(Level.WARNING, e.fillInStackTrace().toString());
        }
        configWatcher = new ConfigWatcher("C:\\Users\\milan\\Desktop\\Probni\\src\\config\\config.properties");

        Thread nit = new Thread(configWatcher);
        nit.setDaemon(true);
        nit.start();
    }

    public Letjelica kreirajLetjelicu(boolean stranaVojna, boolean domacaVojna) {
        Letjelica letjelica = null;

        Random r = new Random();
        String identifikator = String.valueOf(r.nextInt(1000) + 1);
        while (identifikatori.contains(identifikator)) {
            identifikator = String.valueOf(r.nextInt((1000) + 1));
        }

        identifikatori.add(identifikator);
        int visina = new Random().nextInt(5) + 1;

        if (stranaVojna == true) {
            letjelica = new Bombarder("Bombarder", identifikator, visina);
        } else if (domacaVojna) {
            letjelica = new Lovac("Lovac", identifikator, visina);
        } else {
            int broj = new Random().nextInt(7);
            switch (broj) {
                case 0:
                    letjelica = new ProtivPozarniAvion("ProtivPozarniAvion", identifikator, visina, 100);
                    break;
                case 1:
                    letjelica = new PutnickiAvion("PutnickiAvion", identifikator, visina, 100, 100);
                    break;
                case 2:
                    letjelica = new TransportniAvion("TransportniAvion", identifikator, visina, 100);
                    break;
                case 3:
                    letjelica = new ProtivPozarniHelikopter("ProtivPozarniHelikopter", identifikator, visina, 100);
                    break;
                case 4:
                    letjelica = new PutnickiHelikopter("PutnickiHelikopter", identifikator, visina, 100);
                    break;
                case 5:
                    letjelica = new TransportniHelikopter("TransportniHelikopter", identifikator, visina, 100);
                    break;
                case 6:
                    letjelica = new BespilotnaLetjelica("BespilotnaLetjelica", identifikator, visina);
                    break;
                default:
                    break;
            }
        }
        //letjelica.setSmjerKretanja(SmjerKretanja.values()[new Random().nextInt(SmjerKretanja.values().length - 1)]);
        letjelica.setBrzina(new Random().nextInt(3) + 1);
        letjelica.setPozicijaX(new Random().nextInt(brojRedova));
        letjelica.setPozicijaY(new Random().nextInt(brojKolona));
        return letjelica;
    }

    @Override
    public void run() {
        int i = 0;
        while (true) {

            i++;
            while (mapa.isZabranaLeta()) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException izuzetak) {
                    izuzetak.printStackTrace();
                }
            }
            if (configWatcher.isPrisutnaStranaLetjelica() && !zabranaGenerisanjeStranihObjekata) {
                stranaLetjelica = kreirajLetjelicu(true, false);
                stranaLetjelica.setBrzina(3);
                stranaLetjelica.start();
                mapa.setPrisutnaStranaLetjelica(true);
                setZabranaGenerisanjeStranihObjekata(true);
                setZabranaGenerisanjeCivilnihObjekata(true);
            }
            if (configWatcher.isPrisutnaDomacaVojnaLetjelica()) {
                Letjelica domaca = kreirajLetjelicu(false, true);
                domaca.start();
            }
            if (pokreniOdbranu) {
                pokreniPotjeru();
            }
            if (!zabranaGenerisanjeCivilnihObjekata) {
                Letjelica letjelica = kreirajLetjelicu(false, false);
                letjelica.start();

/*
                stranaLetjelica = kreirajLetjelicu(true, false);
                stranaLetjelica.setPozicijaX(14);
                stranaLetjelica.setPozicijaY(0);
                stranaLetjelica.setBrzina(3);
                stranaLetjelica.setSmjerKretanja(SJEVER);
                stranaLetjelica.start();
                mapa.setPrisutnaStranaLetjelica(true);
                zabranaGenerisanjeStranihObjekata = true;
                zabranaGenerisanjeCivilnihObjekata = true;*/
/*
                Letjelica letjelica3 = new PutnickiAvion("PutnickiAvion", "77", 2, 200, 100);
                letjelica3.setBrzina(1);
                letjelica3.setPozicijaX(2);
                letjelica3.setPozicijaY(13);

                Letjelica letjelica4 = new TransportniHelikopter("TransportniHelikopter", "88", 2, 100);
                letjelica4.setBrzina(1);
                letjelica4.setPozicijaX(2);
                letjelica4.setPozicijaY(1);

                letjelica3.start();
                letjelica4.start();
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.fillInStackTrace().toString();
                }
                Letjelica letjelica = new PutnickiAvion("PutnickiAvion", "55", 2, 200, 100);
                letjelica.setBrzina(1);
                letjelica.setPozicijaX(11);
                letjelica.setPozicijaY(2);

                Letjelica letjelica1 = new TransportniHelikopter("TransportniHelikopter", "33", 2, 100);
                letjelica1.setBrzina(1);
                letjelica1.setPozicijaX(11);
                letjelica1.setPozicijaY(12);

                letjelica.start();
                letjelica1.start();*/
            }
            try {
                Thread.sleep(intervalKreiranjaLetjelica * 1000);
            } catch (InterruptedException e) {
                loger.log(Level.WARNING, e.fillInStackTrace().toString());
            }
        }
    }

    public void pokreniPotjeru() {
        Random rand = new Random();
        Letjelica lovac1 = kreirajLetjelicu(false, true);
        lovac1.setBrzina(1);
        lovac1.setPotjera(true);
        Letjelica lovac2 = kreirajLetjelicu(false, true);
        lovac2.setBrzina(1);
        lovac2.setPotjera(true);

        if (stranaLetjelica.getSmjerKretanja() == ISTOK) {
            lovac1.setSmjerKretanja(ISTOK);
            lovac2.setSmjerKretanja(ISTOK);
            if (stranaLetjelica.getPozicijaY() < 3)
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (pozicijaStraneLetjeliceX == 0) {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() + 1);
                lovac2.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
            } else if (pozicijaStraneLetjeliceX == getBrojRedova() - 1) {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() - 1);
                lovac2.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
            } else {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX() - 1);
                lovac1.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() + 1);
                lovac2.setPozicijaY(rand.nextInt(stranaLetjelica.getPozicijaY()));
            }
        } else if (stranaLetjelica.getSmjerKretanja() == ZAPAD) {
            lovac1.setSmjerKretanja(ZAPAD);
            lovac2.setSmjerKretanja(ZAPAD);
            if (stranaLetjelica.getPozicijaY() > getBrojKolona() - 3)
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (pozicijaStraneLetjeliceX == 0) {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() + 1);
                lovac2.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
            } else if (pozicijaStraneLetjeliceX == brojRedova - 1) {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() - 1);
                lovac2.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
            } else {
                lovac1.setPozicijaX(stranaLetjelica.getPozicijaX() - 1);
                lovac1.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(stranaLetjelica.getPozicijaX() + 1);
                lovac2.setPozicijaY(rand.nextInt(brojKolona - stranaLetjelica.getPozicijaY()) + stranaLetjelica.getPozicijaY());
            }
        } else if (stranaLetjelica.getSmjerKretanja() == SJEVER) {
            lovac1.setSmjerKretanja(SJEVER);
            lovac2.setSmjerKretanja(SJEVER);
            if (stranaLetjelica.getPozicijaX() > getBrojRedova() - 3)
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (pozicijaStraneLetjeliceY == 0) {
                lovac1.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() + 1);
            } else if (pozicijaStraneLetjeliceY == brojKolona - 1) {
                lovac1.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() - 1);
            } else {
                lovac1.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY() + 1);
                lovac2.setPozicijaX(rand.nextInt(brojRedova - stranaLetjelica.getPozicijaX()) + stranaLetjelica.getPozicijaX());
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() - 1);
            }
        } else if (stranaLetjelica.getSmjerKretanja() == JUG) {
            lovac1.setSmjerKretanja(JUG);
            lovac2.setSmjerKretanja(JUG);
            if (stranaLetjelica.getPozicijaX() < 3)
                try {
                    Thread.sleep(9000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            if (pozicijaStraneLetjeliceY == 0) {
                lovac1.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() + 1);
            } else if (pozicijaStraneLetjeliceY == brojKolona - 1) {
                lovac1.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY());
                lovac2.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() - 1);
            } else {
                lovac1.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac1.setPozicijaY(stranaLetjelica.getPozicijaY() - 1);
                lovac2.setPozicijaX(rand.nextInt(stranaLetjelica.getPozicijaX() + 1));
                lovac2.setPozicijaY(stranaLetjelica.getPozicijaY() + 1);
            }
        }
        lovac1.setIzadji(true);
        lovac2.setIzadji(true);
        lovac1.start();
        lovac2.start();
        setPokreniOdbranu(false);

        Thread potjeraNit = new Thread(new Potjera(stranaLetjelica, lovac1, lovac2));
        potjeraNit.setDaemon(true);
        potjeraNit.start();
    }

    public int getPozicijaStraneLetjeliceX() {
        return pozicijaStraneLetjeliceX;
    }

    public void setPozicijaStraneLetjeliceX(int pozicijaStraneLetjeliceX) {
        this.pozicijaStraneLetjeliceX = pozicijaStraneLetjeliceX;
    }

    public int getPozicijaStraneLetjeliceY() {
        return pozicijaStraneLetjeliceY;
    }

    public void setPozicijaStraneLetjeliceY(int pozicijaStraneLetjeliceY) {
        this.pozicijaStraneLetjeliceY = pozicijaStraneLetjeliceY;
    }

    public Mapa getMapa() {
        return mapa;
    }

    public void setMapa(Mapa mapa) {
        this.mapa = mapa;
    }

    public ConfigWatcher getWatcher() {
        return configWatcher;
    }

    public void setWatcher(ConfigWatcher watcher) {
        this.configWatcher = watcher;
    }

    public int getIntervalKreiranjaLetjelica() {
        return intervalKreiranjaLetjelica;
    }

    public void setIntervalKreiranjaLetjelica(int intervalKreiranjaLetjelica) {
        this.intervalKreiranjaLetjelica = intervalKreiranjaLetjelica;
    }

    public int getBrojKolona() {
        return brojKolona;
    }

    public void setBrojKolona(int brojKolona) {
        this.brojKolona = brojKolona;
    }

    public int getBrojRedova() {
        return brojRedova;
    }

    public void setBrojRedova(int brojRedova) {
        this.brojRedova = brojRedova;
    }

    public boolean isStraniVojniObjekat() {
        return straniVojniObjekat;
    }

    public void setStraniVojniObjekat(boolean straniVojniObjekat) {
        this.straniVojniObjekat = straniVojniObjekat;
    }

    public boolean isDomaciVojniObjekat() {
        return domaciVojniObjekat;
    }

    public void setDomaciVojniObjekat(boolean domaciVojniObjekat) {
        this.domaciVojniObjekat = domaciVojniObjekat;
    }

    public boolean isPokreniOdbranu() {
        return pokreniOdbranu;
    }

    public void setPokreniOdbranu(boolean pokreniOdbranu) {
        this.pokreniOdbranu = pokreniOdbranu;
    }

    public boolean isZabranaGenerisanjeStranihObjekata() {
        return zabranaGenerisanjeStranihObjekata;
    }

    public void setZabranaGenerisanjeStranihObjekata(boolean zabranaGenerisanjeStranihObjekata) {
        this.zabranaGenerisanjeStranihObjekata = zabranaGenerisanjeStranihObjekata;
    }

    public boolean isZabranaGenerisanjeCivilnihObjekata() {
        return zabranaGenerisanjeCivilnihObjekata;
    }

    public void setZabranaGenerisanjeCivilnihObjekata(boolean zabranaGenerisanjeCivilnihObjekata) {
        this.zabranaGenerisanjeCivilnihObjekata = zabranaGenerisanjeCivilnihObjekata;
    }
}
