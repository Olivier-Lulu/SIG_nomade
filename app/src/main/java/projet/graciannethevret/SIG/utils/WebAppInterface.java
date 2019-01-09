package projet.graciannethevret.SIG.utils;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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

}