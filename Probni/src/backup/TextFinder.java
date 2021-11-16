package backup;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;

public class TextFinder implements FileVisitor<Path> {
    private final PathMatcher matcher;
    private ArrayList<Path> listaPronadjenih = new ArrayList<>();
    private Path naziv;

    public TextFinder(String uzorak) {
        matcher = FileSystems.getDefault().getPathMatcher("glob:" + uzorak);
    }

    public ArrayList<Path> getListaPronadjenih() {
        return listaPronadjenih;
    }

    public void setListaPronadjenih(ArrayList<Path> listaPronadjenih) {
        this.listaPronadjenih = listaPronadjenih;
    }

    private void pronadjiFajl(Path fajl) {
        naziv = fajl.getFileName();
        if(naziv != null && matcher.matches(naziv)) {
            listaPronadjenih.add(fajl);
        }
    }

    @Override
    public FileVisitResult visitFile(Path fajl, BasicFileAttributes atributi) {
        pronadjiFajl(fajl);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult preVisitDirectory(Path direktorijum, BasicFileAttributes atributi) {
        pronadjiFajl(direktorijum);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult postVisitDirectory(Path direktorijum, IOException izuzetak) {
        pronadjiFajl(direktorijum);
        return FileVisitResult.CONTINUE;
    }

    @Override
    public FileVisitResult visitFileFailed(Path fajl, IOException izuzetak) {
        System.err.println(izuzetak);
        return FileVisitResult.CONTINUE;
    }
}
