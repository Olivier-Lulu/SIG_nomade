package projet.secunomade.orleans.univ.sigproj.utils;

import android.content.Context;
import android.webkit.JavascriptInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import projet.secunomade.orleans.univ.sigproj.dao.MarkerDAO;
import projet.secunomade.orleans.univ.sigproj.modele.Marker;

public class WebAppInterface {
    private Context mContext;

    private MarkerDAO markerDAO;

    public WebAppInterface(Context c, MarkerDAO markerDAO) {
        mContext = c;
        this.markerDAO = markerDAO;
    }

    @JavascriptInterface
    public void insertMarker(float lon, float lat) {
        markerDAO.insertMarker(new Marker(markerDAO.getMaxId() + 1, lon, lat));
    }

    @JavascriptInterface
    public String getMarkersAsJson () {
        List<Marker> markers = markerDAO.getAll();
        JSONObject jsonPivot = new JSONObject();

        try {
            int markerId;
            for (Marker marker : markers) {
                JSONObject coordinates = new JSONObject();
                coordinates.put("lon", marker.getLon());
                coordinates.put("lat", marker.getLat());

                markerId = marker.getId();

                jsonPivot.put("" + markerId, coordinates);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonPivot.toString();
    }

}