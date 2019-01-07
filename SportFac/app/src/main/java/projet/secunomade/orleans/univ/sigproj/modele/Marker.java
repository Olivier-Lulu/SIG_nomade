package projet.secunomade.orleans.univ.sigproj.modele;

public class Marker {

    private int id;
    private float lon;
    private float lat;

    public Marker() {
    }
    public Marker(int id, float lon, float lat) {
        this.id = id;
        this.lon = lon;
        this.lat = lat;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public float getLon() {
        return lon;
    }
    public void setLon(float lon) {
        this.lon = lon;
    }

    public float getLat() {
        return lat;
    }
    public void setLat(float lat) {
        this.lat = lat;
    }

}

