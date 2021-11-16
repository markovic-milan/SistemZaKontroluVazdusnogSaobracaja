package model.Letjelice.Avioni.VojniAvioni;

import model.Interfejsi.NapadanjeNaCiljeveInterface;
import model.Interfejsi.NosenjeNaoruzanjaInterface;
import model.Letjelice.Avioni.Avion;

public class VojniAvion extends Avion implements NosenjeNaoruzanjaInterface, NapadanjeNaCiljeveInterface {

    public VojniAvion(String model, String identifikator, int visina) {
        super(model, identifikator, visina);
    }
}
