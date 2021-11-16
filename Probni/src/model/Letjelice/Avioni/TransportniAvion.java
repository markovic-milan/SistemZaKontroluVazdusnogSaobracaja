package model.Letjelice.Avioni;

import model.Interfejsi.TransportTeretaInterface;

public class TransportniAvion extends Avion implements TransportTeretaInterface {
    private int maksimalnaTezinaTereta;

    public TransportniAvion(String model, String identifikator, int visina, int maksimalnaTezinaTereta) {
        super(model, identifikator, visina);
        this.maksimalnaTezinaTereta = maksimalnaTezinaTereta;
    }

    public int getMaksimalnaTezinaTereta() {
        return maksimalnaTezinaTereta;
    }

    public void setMaksimalnaTezinaTereta(int maksimalnaTezinaTereta) {
        this.maksimalnaTezinaTereta = maksimalnaTezinaTereta;
    }
}
