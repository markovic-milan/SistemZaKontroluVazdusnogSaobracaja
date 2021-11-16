package radar;

import GUI.glavnaForma.MainForma;
import model.Letjelice.Letjelica;
import model.VazdusniProstor.Mapa;
import simulator.Simulator;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Radar extends Thread {
    private int interval;
    private Logger loger = MainForma.loger;
    private Mapa mapa;
    private Simulator simulator = MainForma.simulator;
    private PrintWriter out;
    private List<String> idStranihLetjelica = new ArrayList<>();
    public static ArrayList<String> idLetjelica = new ArrayList<>();
    private File fajl;

    public Radar(Mapa mapa) {
        this.mapa = mapa;
        Properties properties = new Properties();
        try {
            FileInputStream in = new FileInputStream(new File("C:\\Users\\milan\\Desktop\\Probni\\src\\config\\radar.properties"));
            properties.load(in);
            interval = Integer.valueOf(properties.getProperty("n"));
            in.close();
        } catch (Exception e) {
            loger.log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }

    @Override
    public void run() {
        while (true) {
            for (int red = 0; red < mapa.getBrojRedova(); red++) {
                for (int kolona = 0; kolona < mapa.getBrojKolona(); kolona++) {
                    if (mapa.getMatrica()[red][kolona] instanceof Letjelica) {
                        Letjelica letjelica = (Letjelica) mapa.getMatrica()[red][kolona];
                        if (letjelica != null) {
                            if (letjelica.isStranaLetjelica() && !idStranihLetjelica.contains(letjelica.getIdentifikacija())) {
                                idStranihLetjelica.add(letjelica.getIdentifikacija());
                                zabiljeziStranuLetjelicu(letjelica);
                                signalSimulatoru(letjelica.getPozicijaX(), letjelica.getPozicijaY());
                            }
                            MainForma.mapiranjeVazdusnogProstora.upisiLetjelicu(letjelica);
                        }
                    }
                }
            }
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {
                loger.log(Level.WARNING, e.fillInStackTrace().toString());
            }
        }
    }

    private void zabiljeziStranuLetjelicu(Letjelica letjelica) {
        // String naziv = new SimpleDateFormat("yyyy.MM.dd_hh:mm:ss").format(new Date());
        fajl = new File(MainForma.putanjaDoEventsFoldera + File.separator + new SimpleDateFormat("yyyy-MM-dd hh-mm-ss'.txt'").format(new Date()));
        try {
            out = new PrintWriter(fajl);
            out.append(letjelica.getModel() + " " + letjelica.getIdentifikacija() + " " + letjelica.getPozicijaX() + " " + letjelica.getPozicijaY());
            out.append("\n");
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            loger.log(Level.WARNING, e.fillInStackTrace().toString());
        }
    }

    private void signalSimulatoru(int x, int y) {
        simulator.setPozicijaStraneLetjeliceX(x);
        simulator.setPozicijaStraneLetjeliceY(y);
        simulator.setPokreniOdbranu(true);
    }
}
