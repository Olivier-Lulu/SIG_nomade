package projet.secunomade.orleans.univ.sigproj;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import projet.secunomade.orleans.univ.sigproj.modele.Marker;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        JSONObject jsonPivot = new JSONObject();

        try {
            for (int i=0; i<4; i++) {
                jsonPivot.put("" + i, new float[] {(float) 33, (float) 42});
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        System.out.println (jsonPivot.toString());
    }
}