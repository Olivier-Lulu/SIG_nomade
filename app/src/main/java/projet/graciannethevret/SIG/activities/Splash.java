package projet.graciannethevret.SIG.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import projet.graciannethevret.SIG.R;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onStart() {
        super.onStart();
        startActivity(new Intent(this, Menu.class));
    }

    @Override
    protected void onResume() {
        super.onResume();
        super.finish();
    }
}
