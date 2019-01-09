package projet.graciannethevret.SIG.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

import projet.graciannethevret.SIG.R;
import projet.graciannethevret.SIG.utils.Options;

public class Recherche extends AppCompatActivity {
    private TableLayout listeTypes;
    private Switch[] checkListe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recherche);
        listeTypes = findViewById(R.id.listeType);
        checkListe = new Switch[Options.TYPES.length];
        init();
    }
    private void init(){
        for(int i = 0; i < Options.TYPES.length;i++){
            String type = Options.TYPES[i];
            TableRow row = new TableRow(this);
            TextView text = new TextView(this);
            text.setText(type);
            row.addView(text);
            Switch check = new Switch(this);
            check.setGravity(Gravity.LEFT);
            checkListe[i] = check;
            row.addView(check);
            listeTypes.addView(row);
        }
    }

    public void rechercher(View v){
        List<String> listeCritere = new LinkedList<>();
        for(int i=0; i < checkListe.length;i++){
            if(checkListe[i].isChecked()){
                listeCritere.add(Options.TYPES[i]);
            }
        }
    }
}
