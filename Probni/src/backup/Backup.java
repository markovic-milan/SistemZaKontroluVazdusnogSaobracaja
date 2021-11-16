package backup;

import GUI.glavnaForma.MainForma;
import mapiranjeVazdusnogProstora.MapiranjeVazdusnogProstora;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Backup {
    private String putanjaZaPretragu;
    private String uzorakTipaFajla;
    private ZipOutputStream kopija;
    private Calendar kalendar = Calendar.getInstance();
    private String putanjaDoKopije = "src" + File.separator + "backup" + File.separator + ("backup_" + kalendar.get(Calendar.YEAR) + "_" + (kalendar.get(Calendar.MONTH) + 1) +
            "_" + kalendar.get(Calendar.DAY_OF_MONTH) + "_" + kalendar.get(Calendar.HOUR) + "_" + kalendar.get(Calendar.MINUTE)) + ".zip";
    private TextFinder textFinder;
    private MapiranjeVazdusnogProstora mapiranjeVazdusnogProstora = MainForma.mapiranjeVazdusnogProstora;

    public Backup(String putanjaZaPretragu, String uzorakTipaFajla) {
        this.putanjaZaPretragu = putanjaZaPretragu;
        this.uzorakTipaFajla = uzorakTipaFajla;
    }

    public void zipuj() throws IOException {
        ZipEntry zipUlaz;
        textFinder = new TextFinder(uzorakTipaFajla);
        Files.walkFileTree(Paths.get(putanjaZaPretragu), textFinder);

        if (!textFinder.getListaPronadjenih().isEmpty()) {
            kopija = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(new File(putanjaDoKopije))));
            for (Path fajl : textFinder.getListaPronadjenih()) {
                zipUlaz = new ZipEntry(fajl.toFile().getName());
                kopija.putNextEntry(zipUlaz);
                mapiranjeVazdusnogProstora.procitajFajl(fajl.toFile(), kopija);
            }
            kopija.finish();
            kopija.close();
        }
    }
}
