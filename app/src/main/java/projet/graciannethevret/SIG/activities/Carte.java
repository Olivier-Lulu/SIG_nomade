package projet.graciannethevret.SIG.activities;

import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.GeolocationPermissions;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import projet.graciannethevret.SIG.R;
import projet.graciannethevret.SIG.dao.MarkerDAO;
import projet.graciannethevret.SIG.utils.WebAppInterface;


public class Carte extends AppCompatActivity {

    private MarkerDAO markerDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebView webView = findViewById(R.id.webView);

        this.markerDAO = new MarkerDAO(this);
        markerDAO.open();
        //markerDAO.removeAllMarkers();
        WebSettings webSetting = webView.getSettings();
        webSetting.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this, markerDAO), "Android");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            webView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        webView.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
                callback.invoke(origin, true, false);
            }
        });
        webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true);
        webSetting.setGeolocationDatabasePath(getFilesDir().getPath()); //deprecated mais obligatoirs T.T
        webSetting.setGeolocationEnabled(true);
        webView.loadUrl("file:///android_asset/index.html");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        markerDAO.close();
    }

}