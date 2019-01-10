package projet.graciannethevret.SIG.modele;

public class Marker {

    private int id;
    private double lon;
    private double lat;
    private String nom;
    private String tag;

    public Marker() {
    }
    public Marker(int id, double lon, double lat, String nom, String tag) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
        this.nom = nom;
        this.tag = tag;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public double getLon() {
        return lon;
    }
    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }
    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getTag() {
        return tag;
    }
    public void setTag(String tag) {
        this.tag = tag;
    }
}

