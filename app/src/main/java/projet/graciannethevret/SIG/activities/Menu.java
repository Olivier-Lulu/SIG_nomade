package projet.graciannethevret.SIG.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import projet.graciannethevret.SIG.R;

public class Menu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
    }

    public void goToOption(View v){
        startActivity(new Intent(this, Option.class));
    }

    public void goToRecherche(View v){
        startActivity(new Intent(this, Recherche.class));
    }

    public void goToCarte(View v){
        startActivity(new Intent(this, Carte.class));
    }
}
