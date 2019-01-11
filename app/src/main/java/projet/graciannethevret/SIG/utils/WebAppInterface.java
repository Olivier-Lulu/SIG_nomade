package projet.graciannethevret.SIG.utils;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import projet.graciannethevret.SIG.dao.BatimentDAO;
import projet.graciannethevret.SIG.dao.MarkerDAO;
import projet.graciannethevret.SIG.modele.Marker;

import static projet.graciannethevret.SIG.utils.Options.contains;


public class WebAppInterface {
    private Context mContext;

    private MarkerDAO markerDAO;

    public WebAppInterface(Context c, MarkerDAO markerDAO) {
        mContext = c;
        this.markerDAO = markerDAO;
    }

    private JSONObject buildJsonObj(Marker marker) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("lon", marker.getLon());
        jsonObject.put("lat", marker.getLat());
        jsonObject.put("nom", marker.getNom());
        jsonObject.put("tag", marker.getTag());

        return jsonObject;
    }

    @JavascriptInterface
    public void insertMarker(double lon, double lat, String markerNom, String markerTag) {
        markerDAO.insertMarker(new Marker(markerDAO.getMaxId() + 1, lon, lat, markerNom, markerTag));
    }

    @JavascriptInterface
    public void deleteMarkerWithId(int id) {
        markerDAO.removeMarkerWithId(id);
    }

    @JavascriptInterface
    public String getMarkersAsJson () {

        JSONObject jsonPivot = new JSONObject();
        List<Marker> markers = markerDAO.getAll();

        try {
            int markerId;
            for (Marker marker : markers) {
                JSONObject coordinates = buildJsonObj(marker);
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
        return Options.IPGEO;
    }

    @JavascriptInterface
    public boolean getCritere(){
        return Options.CRITERE != null;
    }

    @JavascriptInterface
    public String getLocations(){
        List<Marker> markers = null;

        JSONObject result = new JSONObject();

        try {
            markers = BatimentDAO.requestBatiments();
            for (Marker m : markers) {
                if (contains(m.getTag(), Options.CRITERE)) {
                    result.put("" + m.getId(), buildJsonObj(m));
                }
            }

            markers = markerDAO.getAll();
            for (Marker m : markers) {
                if (contains(m.getTag(), Options.CRITERE)) {
                    result.put("" + m.getId(), buildJsonObj(m));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Options.CRITERE = null;

        return result.toString();
    }

    @JavascriptInterface
    public String getTags() {
        JSONArray tags = new JSONArray();

        for (String cursor : Options.TYPES) {
            tags.put(cursor);
        }

        return tags.toString();
    }

    @JavascriptInterface
    public int getMaxId() {
        return markerDAO.getMaxId();
    }

}