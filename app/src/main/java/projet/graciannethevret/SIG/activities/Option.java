package projet.graciannethevret.SIG.activities;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import projet.graciannethevret.SIG.R;

import static projet.graciannethevret.SIG.utils.Options.IPGEO_DEFAULT;
import static projet.graciannethevret.SIG.utils.Options.KEYGEO;
import static projet.graciannethevret.SIG.utils.Options.PREF;

public class Option extends AppCompatActivity {
    private EditText ipGeo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option);
        ipGeo = findViewById(R.id.editIP);
        String ip = getSharedPreferences(PREF,0).getString(KEYGEO,IPGEO_DEFAULT);
        ipGeo.getText().clear();
        ipGeo.getText().append(ip);
        ipGeo.setOnEditorActionListener(
                new EditText.OnEditorActionListener() {

                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                                // the user is done typing.
                                SharedPreferences.Editor prefEdit =  getSharedPreferences(PREF,0).edit();
                                prefEdit.putString(KEYGEO,v.getText().toString());
                                prefEdit.apply();
                                return true;
                            }
                        return false;
                    }
                }
        );
    }
}
