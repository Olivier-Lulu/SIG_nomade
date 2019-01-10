package projet.graciannethevret.SIG.utils;

import android.content.Context;
import android.util.JsonReader;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import projet.graciannethevret.SIG.dao.MarkerDAO;
import projet.graciannethevret.SIG.modele.Marker;


public class WebAppInterface {
    private Context mContext;

    private MarkerDAO markerDAO;

    public WebAppInterface(Context c, MarkerDAO markerDAO) {
        mContext = c;
        this.markerDAO = markerDAO;
    }

    @JavascriptInterface
    public void insertMarker(double lon, double lat, String markerNom, String markerTag) {
        markerDAO.insertMarker(new Marker(markerDAO.getMaxId() + 1, lon, lat, markerNom, markerTag));
    }

    @JavascriptInterface
    public String getMarkersAsJson () {

        JSONObject jsonPivot = new JSONObject();
        List<Marker> markers = markerDAO.getAll();

        try {
            int markerId;
            for (Marker marker : markers) {
                JSONObject coordinates = new JSONObject();
                coordinates.put("lon", marker.getLon());
                coordinates.put("lat", marker.getLat());
                coordinates.put("nom", marker.getNom());
                coordinates.put("tag", marker.getTag());

                markerId = marker.getId();

                jsonPivot.put("" + markerId, coordinates);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonPivot.toString();
    }

    @JavascriptInterface
    public void logFromJs (String msg){
        Log.d("fromJS", msg);
    }

    @JavascriptInterface
    public String getGeoServerIp(){
        return mContext.getSharedPreferences(Options.PREF,0).getString(Options.KEYGEO,Options.IPGEO_DEFAULT);
    }

    @JavascriptInterface
    public boolean getCritere(){
        return Options.CRITERE != null;
    }

    @JavascriptInterface
    public String getLocationCritere(String location){
        String[] loc = location.split(",");
        double longitude = Double.parseDouble(loc[0]);
        double latitude = Double.parseDouble(loc[1]);
        Marker closest = null;
        double distance = Double.POSITIVE_INFINITY;

        List<Marker> markers = null;
        try {
            markers = requestBatiments();
            if(markers != null) {
                for (Marker m : markers) {
                    if (contains(m.getTag(), Options.CRITERE)) {
                        double distanceB = (longitude - m.getLon()) * (longitude - m.getLon()) + (latitude - m.getLat()) * (latitude - m.getLat());
                        if (distanceB < distance) {
                            closest = m;
                            distance = distanceB;
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        markers = markerDAO.getAll();
        for (Marker m : markers) {
            if (contains(m.getTag(), Options.CRITERE)) {
                double distanceB = (longitude - m.getLon()) * (longitude - m.getLon()) + (latitude - m.getLat()) * (latitude - m.getLat());
                if (distanceB < distance) {
                    closest = m;
                    distance = distanceB;
                }
            }
        }
        Options.CRITERE = null;
        if (closest == null) {
            return null;
        } else {
            try {
                JSONObject coordinates = new JSONObject();
                coordinates.put("lon", closest.getLon());
                coordinates.put("lat", closest.getLat());
                coordinates.put("nom", closest.getNom());
                coordinates.put("tag", closest.getTag());
                return coordinates.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }

    private List<Marker> requestBatiments() throws IOException {
        List<Marker> liste = new ArrayList<>();
        String urls = "http://"+getGeoServerIp() +":8081/batiments?";
        for (int i = 0; i < Options.CRITERE.length;i++){
            urls += "criteres="+Options.CRITERE[i];
            if(i != Options.CRITERE.length-1){
                urls += "&";
            }
        }
        URL url = new URL(urls);
        HttpURLConnection httpclient = (HttpURLConnection) url.openConnection();
        if (httpclient.getResponseCode() == 200) {
            InputStream responseBody = httpclient.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginArray();
            while(jsonReader.hasNext()){
                jsonReader.beginObject();
                jsonReader.nextName();
                int id = jsonReader.nextInt();
                jsonReader.nextName();
                double lon = jsonReader.nextDouble();
                jsonReader.nextName();
                double lat = jsonReader.nextDouble();
                jsonReader.nextName();
                String nom = jsonReader.nextString();
                jsonReader.nextName();
                String tag = jsonReader.nextString();
                liste.add(new Marker(id,lon,lat,nom,tag));
                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
            return liste;
        } else {
            return null;
        }
    }

    private boolean contains(String tag, String[] critere) {
        for(String s : critere)
            if(s.equals(tag))
                return true;
        return false;
    }

}