package projet.secunomade.orleans.univ.sigproj.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

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
        markerDAO.open();
        markerDAO.removeAllMarkers();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this, markerDAO), "Android");

        markerDAO.insertMarker(new Marker(0, (double) 1, 1));
        markerDAO.insertMarker(new Marker(1, (double) 2, 2));

        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/qs.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markerDAO.close();
    }

}
