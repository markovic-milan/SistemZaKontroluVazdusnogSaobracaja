package model.Letjelice.Helikopteri;

import model.Interfejsi.TransportTeretaInterface;

public class TransportniHelikopter extends Helikopter implements TransportTeretaInterface {
    private int maksimalnaTezinaTereta;

    public TransportniHelikopter(String model, String identifikator, int visina, int maksimalnaTezinaTereta) {
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
