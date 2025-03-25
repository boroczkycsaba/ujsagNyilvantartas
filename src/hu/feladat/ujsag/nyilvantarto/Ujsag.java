package hu.feladat.ujsag.nyilvantarto;

public class Ujsag {
    public Kiado kiado;
    public String megnevezes;
    public double ar;

    private int hetiMegjelenes = 0;
    private  int napiMegjelenes = 0;

    public Kiado getKiadoObj() {
        if (kiado == null) {
            kiado = new Kiado();
        }
        return kiado;
    }

    public String getKiado() {
        return getKiadoObj().getKiado();
    }

    public void setKiado(String kiadoNev) {
        getKiadoObj().setKiado(kiadoNev);
    }

    public String getMegnevezes() {
        return megnevezes;
    }

    public void setMegnevezes(String megnevezes) {
        this.megnevezes = megnevezes;
    }

    public double getAr() {
        return ar;
    }

    public void setAr(double ar) {
        this.ar = ar;
    }

    public int getHetiMegjelenes() {
        return hetiMegjelenes;
    }

    public void setHetiMegjelenes(int hetiMegjelenes) {
        this.hetiMegjelenes = hetiMegjelenes;
    }

    public int getNapiMegjelenes() {
        return napiMegjelenes;
    }

    public void setNapiMegjelenes(int napiMegjelenes) {
        this.napiMegjelenes = napiMegjelenes;
    }

    public double afaTartalomSzamit() {
        return ar - (ar / (1.05));
    }

    final public double hetiArKalkulacio () {
        return afaTartalomSzamit() * hetiMegjelenes;
    }
}
