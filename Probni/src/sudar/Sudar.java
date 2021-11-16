package sudar;

import GUI.glavnaForma.MainForma;
import model.Letjelice.Letjelica;
import radar.Radar;

import java.io.IOException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sudar {
    private int pozicijaX;
    private int pozicijaY;
    private Letjelica letjelica1;
    private Letjelica letjelica2;
    private Date datumIVrijeme;
    private Logger loger = MainForma.loger;

    public Sudar(Letjelica letjelica1, Letjelica letjelica2, Date datumIVrijeme, int pozicijaX, int pozicijaY) {
        this.letjelica1 = letjelica1;
        this.letjelica2 = letjelica2;
        this.pozicijaX = pozicijaX;
        this.pozicijaY = pozicijaY;
        this.datumIVrijeme = datumIVrijeme;
    }

    public void evidentirajSudar() {
      //  System.out.println(radar.idLetjelica.contains(letjelica1.getIdentifikacija()) + " " + radar.idLetjelica.contains(letjelica2.getIdentifikacija()));
        if (Radar.idLetjelica.contains(letjelica1.getIdentifikacija()) || Radar.idLetjelica.contains(letjelica2.getIdentifikacija())) {
          //  System.out.println("Obradjena vec");
            return;
        }
        Radar.idLetjelica.add(letjelica1.getIdentifikacija());
        Radar.idLetjelica.add(letjelica2.getIdentifikacija());
        Upozorenje upozorenje = new Upozorenje(letjelica1.getModel() + " i " + letjelica2.getModel(), "(" + pozicijaX + "," + pozicijaY + ")", datumIVrijeme.toString());
        try {
            upozorenje.serijalizuj("sudar " + pozicijaX + "_" + pozicijaY + ".ser");
        } catch (IOException izuzetak) {
            loger.log(Level.SEVERE, izuzetak.fillInStackTrace().toString());
        }
    }
}
