package izuzeci;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class IzuzeciLogger {
    private static Logger loger = Logger.getLogger("Loger");

    public IzuzeciLogger() {
        try {
            FileHandler obradjivacIzuzetka = new FileHandler("src" + File.separator + "evidentiraniIzuzeci" + File.separator + "izuzeci.log", true);
            obradjivacIzuzetka.setFormatter(new SimpleFormatter());
            loger.addHandler(obradjivacIzuzetka);
        } catch (IOException izuzetak) {

        }
    }

    public static Logger getLoger() {
        return loger;
    }

    public static void setLoger(Logger loger) {
        IzuzeciLogger.loger = loger;
    }
}
