package simulator;

import GUI.glavnaForma.MainForma;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigWatcher extends Thread {

    private int intervalKreiranja;
    private String putanjaDoKonfiguracionogFajla;
    private boolean prisutnaStranaLetjelica;
    private boolean prisutnaDomacaVojnaLetjelica;
    private FileInputStream citanje;
    private Properties properties;
    private Logger loger = MainForma.loger;
    private void prisutneStraneLetjelice() {
        try {
            citanje = new FileInputStream(new File(putanjaDoKonfiguracionogFajla));
            properties = new Properties();
            properties.load(citanje);
            intervalKreiranja = Integer.valueOf(properties.getProperty("intervalKreiranjaLetjelica"));
            prisutnaStranaLetjelica = Boolean.valueOf(properties.getProperty("prisustvoStranihVojnihObjekata"));
            prisutnaDomacaVojnaLetjelica = Boolean.valueOf(properties.getProperty("prisustvoDomacihVojnihObjekata"));
            citanje.close();

        } catch (IOException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(1000);
                prisutneStraneLetjelice();
            } catch (InterruptedException izuzetak) {
                loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
            }
        }
    }
    public ConfigWatcher(String putanjaDoKonfiguracionogFajla) {
        this.putanjaDoKonfiguracionogFajla = putanjaDoKonfiguracionogFajla;
    }

    public String getPutanjaDoKonfiguracionogFajla() {
        return putanjaDoKonfiguracionogFajla;
    }

    public void setPutanjaDoKonfiguracionogFajla(String putanjaDoKonfiguracionogFajla) {
        this.putanjaDoKonfiguracionogFajla = putanjaDoKonfiguracionogFajla;
    }

    public boolean isPrisutnaStranaLetjelica() {
        return prisutnaStranaLetjelica;
    }

    public void setPrisutnaStranaLetjelica(boolean prisutnaStranaLetjelica) {
        this.prisutnaStranaLetjelica = prisutnaStranaLetjelica;
    }

    public boolean isPrisutnaDomacaVojnaLetjelica() {
        return prisutnaDomacaVojnaLetjelica;
    }

    public void setPrisutnaDomacaVojnaLetjelica(boolean prisutnaDomacaVojnaLetjelica) {
        this.prisutnaDomacaVojnaLetjelica = prisutnaDomacaVojnaLetjelica;
    }
    public int getIntervalKreiranja() {
        return intervalKreiranja;
    }

    public void setIntervalKreiranja(int intervalKreiranja) {
        this.intervalKreiranja = intervalKreiranja;
    }


}