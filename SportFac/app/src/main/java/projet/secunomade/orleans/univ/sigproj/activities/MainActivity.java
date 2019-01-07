package projet.secunomade.orleans.univ.sigproj.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import projet.secunomade.orleans.univ.sigproj.R;
import projet.secunomade.orleans.univ.sigproj.dao.MarkerDAO;
import projet.secunomade.orleans.univ.sigproj.modele.Marker;
import projet.secunomade.orleans.univ.sigproj.utils.WebAppInterface;

public class MainActivity extends AppCompatActivity {

    private MarkerDAO markerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView webView = findViewById(R.id.webView);
        this.markerDAO = new MarkerDAO(this);
        webView.addJavascriptInterface(new WebAppInterface(this, markerDAO), "Android");
        markerDAO.open();

        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.loadUrl("file:///android_asset/qs.html");

        JSONObject jsonPivot = new JSONObject();
        try {
            int markerId;

            for (int i =0; i<4; i++) {
                JSONObject coorinates = new JSONObject();
                coorinates.put("lon",(double) 33);
                coorinates.put("lat",(double) 42);

                jsonPivot.put("" + i, coorinates);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("fullTest", jsonPivot.toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markerDAO.close();
    }

}
