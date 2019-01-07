package projet.secunomade.orleans.univ.sigproj.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

import projet.secunomade.orleans.univ.sigproj.backend.Database;
import projet.secunomade.orleans.univ.sigproj.modele.Marker;

public class MarkerDAO {

    private static final int VERSION_BDD = 1;
    private static final String NOM_BDD = "appProjetSig.db";

    private static final String TABLE_MARKER = "table_marker";
    private static final String COL_ID = "ID";
    private static final int NUM_COL_ID = 0;
    private static final String COL_LON = "lon";
    private static final int NUM_COL_LON = 1;
    private static final String COL_LAT = "lat";
    private static final int NUM_COL_LAT = 2;

    private SQLiteDatabase bdd;

    private Database appDatabase;

    public MarkerDAO(Context context) {
        appDatabase = new Database(context, NOM_BDD, null, VERSION_BDD);
    }

    public void open () {
        bdd = appDatabase.getWritableDatabase();
    }

    public void close () {
        bdd.close();
    }

    public SQLiteDatabase getBdd () {
        return bdd;
    }

    public long insertMarker (Marker marker) {
        //equivalent a une hashmap
        ContentValues values = new ContentValues();

        values.put(COL_LON, marker.getLon());
        values.put(COL_LAT, marker.getLat());

        return bdd.insert(TABLE_MARKER, null, values);
    }

    public int updateMarker (int id, Marker marker) {
        ContentValues values = new ContentValues();

        values.put(COL_LON, marker.getLon());
        values.put(COL_LAT, marker.getLat());

        return bdd.update(TABLE_MARKER, values, COL_ID + "=" + id, null);
    }

    public int removeMarkerWithId (int id) {
        return bdd.delete(TABLE_MARKER, COL_ID + " = " +id, null);
    }

    public Marker getMarkerWithId(int id){
        //Récupère dans un Cursor les valeurs correspondant à un livre contenu dans la BDD (ici on sélectionne le livre grâce à son titre)
        Cursor c = bdd.query(TABLE_MARKER, new String[] {COL_ID, COL_LON, COL_LAT}, COL_ID + " LIKE \"" + id +"\"",
                null, null, null, null);
        return cursorToMarker(c);
    }

    public List<Marker> getAll() {
        Cursor c = bdd.rawQuery("SELECT * FROM " + TABLE_MARKER, new String[] {});

        LinkedList<Marker> markers = new LinkedList<>();
        while (!(c.isLast())) {
            markers.addLast(cursorToMarker(c));
            c.moveToNext();
        }
        markers.addLast(cursorToMarker(c));

        return markers;
    }

    public int getMaxId () {
        Cursor c = bdd.rawQuery("SELECT max(id) FROM " + TABLE_MARKER, new String[] {});
        return cursorToMarker(c).getId();
    }

    //SQLiteDatabase ne renvoyant que des Cursor (pointeurs sur entree de tableau), il faut bien
    //un traducteur
    private Marker cursorToMarker(Cursor c){
        //si aucun élément n'a été retourné dans la requête, on renvoie null
        if (c.getCount() == 0)
            return null;

        //Sinon on se place sur le premier élément
        c.moveToFirst();
        //On créé un livre
        Marker marker= new Marker();
        //on lui affecte toutes les infos grâce aux infos contenues dans le Cursor
        marker.setId(c.getInt(NUM_COL_ID));
        marker.setLon(c.getFloat(NUM_COL_LON));
        marker.setLat(c.getFloat(NUM_COL_LAT));
        //On ferme le cursor
        c.close();

        //On retourne le livre
        return marker;
    }

}
