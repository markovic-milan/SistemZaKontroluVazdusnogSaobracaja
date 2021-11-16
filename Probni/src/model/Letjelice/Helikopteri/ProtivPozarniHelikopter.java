package model.Letjelice.Helikopteri;

import model.Interfejsi.GasenjePozaraInterface;

public class ProtivPozarniHelikopter extends Helikopter implements GasenjePozaraInterface {
    private int kolicinaVodeZaGasenjePozara;

    public ProtivPozarniHelikopter(String model, String identifikator, int visina, int kolicinaVodeZaGasenjePozara) {
        super(model, identifikator, visina);
        this.kolicinaVodeZaGasenjePozara = kolicinaVodeZaGasenjePozara;
    }

    public int getKolicinaVodeZaGasenjePozara() {
        return kolicinaVodeZaGasenjePozara;
    }

    public void setKolicinaVodeZaGasenjePozara(int kolicinaVodeZaGasenjePozara) {
        this.kolicinaVodeZaGasenjePozara = kolicinaVodeZaGasenjePozara;
    }
}
