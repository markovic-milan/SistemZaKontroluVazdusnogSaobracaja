package model.Letjelice.Avioni;

import model.Interfejsi.GasenjePozaraInterface;

public class ProtivPozarniAvion extends Avion implements GasenjePozaraInterface {
    private int kolicinaVodeZaGasenje;

    public ProtivPozarniAvion(String model, String identifikator, int visina, int kolicinaVodeZaGasenje) {
        super(model, identifikator, visina);
        this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
    }

    public int getKolicinaVodeZaGasenje() {
        return kolicinaVodeZaGasenje;
    }

    public void setKolicinaVodeZaGasenje(int kolicinaVodeZaGasenje) {
        this.kolicinaVodeZaGasenje = kolicinaVodeZaGasenje;
    }
}
