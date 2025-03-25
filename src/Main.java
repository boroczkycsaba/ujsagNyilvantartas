import hu.feladat.ujsag.nyilvantarto.Ujsag;

import java.io.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

public class Main {
    public static void main(String[] args) {
        System.out.println("Újság nyilvántartó program indul");
        System.out.println("");
        String ujsagCSVFileNev = "src/adatok/ujsagok.csv";
        if (args.length > 0 && args[0] != null) {
            ujsagCSVFileNev = args[0];
        }
        List<Ujsag> ujsagAdatokLista = null;
        try {
            ujsagAdatokLista = ujsagCSVFeldolgozas(ujsagCSVFileNev);
            ujsagAdatokMegjelenitese(ujsagAdatokLista);
            ujsagAdatokNapiXMegjelenitese(ujsagAdatokLista, 6);
            ujsagAdatokDarabszamMegjelenitese(ujsagAdatokLista);
            legDragabbUjsagArAfaMegjelenitese(ujsagAdatokLista);
            legegelsoLapHetiKoltseges(ujsagAdatokLista);
        } catch (IOException e) {
            System.err.println("Hiba a futás során\n" + e);
        }
    }

    private static List<Ujsag> ujsagCSVFeldolgozas(final String ujsagCSVFileNev) throws IOException {
        List<Ujsag> ujsagAdatokLista = new ArrayList<>();
        Path ujsagCSVFileNevPath = Paths.get(ujsagCSVFileNev);

        Ujsag ujsag = null;
        String megjelenesModja = null;
        try (BufferedReader csvReader = Files.newBufferedReader(ujsagCSVFileNevPath);) {
            String ujsagAdatSor;
            while ((ujsagAdatSor = csvReader.readLine()) != null) {
                //System.out.println("Beolvasott sor:" + ujsagAdatSor);
                String[] ujsagAdatTomben = ujsagAdatSor.split("-");
                ujsag = new Ujsag();
                if (ujsagAdatTomben.length < 4) {
                    throw new IOException("Kevés a fájlban található adat a feldolgozáshoz");
                }
                ujsag.setMegnevezes(ujsagAdatTomben[1]);
                ujsag.setKiado(ujsagAdatTomben[2]);
                ujsag.setAr(Double.parseDouble(ujsagAdatTomben[3]));
                if (!(ujsag.getAr() >= 0)) {
                    throw new IOException(ujsag.getMegnevezes() + " űjságnál hibás az ár, mert ár nem lehet negatív érték");
                }
                megjelenesModja = ujsagAdatTomben[0];
                if (megjelenesModja.equals("Hetilap")) {
                    ujsag.setHetiMegjelenes(Integer.parseInt(ujsagAdatTomben[4]));
                } else if (megjelenesModja.equals("Napilap")) {
                    ujsag.setNapiMegjelenes(Integer.parseInt(ujsagAdatTomben[4]));
                    if (ujsag.getNapiMegjelenes() < 4) {
                        throw new IOException(ujsag.getMegnevezes() + " nem lehet napilap, mert csak " + ujsag.getNapiMegjelenes() + " alkalommal jelenik meg");
                    }
                }
                ujsagAdatokLista.add(ujsag);
            }
        }
        return ujsagAdatokLista;
    }

    private static void ujsagAdatMegjelenitese(final Ujsag ujsag) {
        System.out.println("Kiadó név:" + ujsag.getKiado());
        System.out.println("Újság neve:" + ujsag.getMegnevezes());
        System.out.println("Újság ára:" + ujsag.getAr());
        System.out.println((ujsag.getHetiMegjelenes() > 0 ? ("Hetilap " + ujsag.getHetiMegjelenes()) : ("Napilap " + ujsag.getNapiMegjelenes())) + " megjeléssel");
        System.out.println("");
    }

    private static void ujsagAdatokMegjelenitese(final List<Ujsag> ujsagAdatokLista) {
        ujsagAdatokLista.forEach(Main::ujsagAdatMegjelenitese);
    }

    private static void ujsagAdatokNapiXMegjelenitese(final List<Ujsag> ujsagAdatokLista, final int napiMegjelenesSzama) {
        System.out.println("Újságok amik legalább " + napiMegjelenesSzama + " napi alkalommal megjelennek");
        ujsagAdatokLista.stream().filter(u -> u.getNapiMegjelenes() >= 6).forEach(Main::ujsagAdatMegjelenitese);
    }

    private static void ujsagAdatokDarabszamMegjelenitese(final List<Ujsag> ujsagAdatokLista) {
        System.out.println("");
        ujsagAdatokLista.stream().filter(u -> u.getNapiMegjelenes() > 0 || u.getHetiMegjelenes() > 0).forEach(u -> {
                    System.out.println(u.getMegnevezes() + " " + (u.getNapiMegjelenes() > 0 ? u.getNapiMegjelenes() : u.getHetiMegjelenes()) + " darabszámban jelenik meg");
                }
        );
    }

    private static void legDragabbUjsagArAfaMegjelenitese(final List<Ujsag> ujsagAdatokLista) {
        System.out.println("");
        System.out.println("Újságok ami a legdrágább:");
        Optional<Ujsag> legdragabbUjsagLehetsegesTalalat = ujsagAdatokLista.stream().max(Comparator.comparing(Ujsag::getAr));
        if (legdragabbUjsagLehetsegesTalalat.isPresent()) {
            Ujsag legdragabbUjsag = legdragabbUjsagLehetsegesTalalat.get();
            System.out.println(legdragabbUjsag.getMegnevezes() + " " + legdragabbUjsag.afaTartalomSzamit() + " áfa tartalommal rendelkezik");
        }
        System.out.println("");
    }

    private static void legegelsoLapHetiKoltseges(final List<Ujsag> ujsagAdatokLista) {
        System.out.println("");
        System.out.println("Legalső újság heti költsége:");
        Ujsag legelsoUjsag = ujsagAdatokLista.get(0);
        System.out.println(legelsoUjsag.getMegnevezes() + " " + (legelsoUjsag.getHetiMegjelenes() > 0 ? legelsoUjsag.getAr() : legelsoUjsag.getAr() * legelsoUjsag.getNapiMegjelenes()));
        System.out.println("");
    }
}
