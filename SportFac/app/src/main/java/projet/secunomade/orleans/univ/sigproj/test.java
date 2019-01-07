package projet.secunomade.orleans.univ.sigproj;

import org.json.JSONException;
import org.json.JSONObject;

public class test {
    public void test (String[]args){
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
