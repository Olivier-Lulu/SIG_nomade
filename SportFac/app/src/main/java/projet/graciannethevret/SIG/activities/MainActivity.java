package projet.graciannethevret.SIG.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import projet.graciannethevret.SIG.dao.MarkerDAO;
import projet.graciannethevret.SIG.modele.Marker;
import projet.graciannethevret.SIG.utils.WebAppInterface;
import projet.secunomade.orleans.univ.sigproj.R;


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
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this, markerDAO), "Android");

        markerDAO.insertMarker(new Marker(0, (double) 1, 1));
        markerDAO.insertMarker(new Marker(1, (double) 2, 2));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        webView.loadUrl("file:///android_asset/index.html");
        WebView.setWebContentsDebuggingEnabled(true);
        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webView.setWebViewClient(new WebViewClient());
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationDatabasePath(getFilesDir().getPath());
        webSetting.setGeolocationEnabled(true);
        webView.loadUrl("file:///android_asset/qs.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markerDAO.close();
    }

}
