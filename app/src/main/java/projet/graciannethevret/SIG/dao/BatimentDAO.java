package projet.graciannethevret.SIG.dao;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import projet.graciannethevret.SIG.modele.Marker;
import projet.graciannethevret.SIG.utils.Options;

public class BatimentDAO {

    public static List<Marker> requestBatiments() throws IOException {
        List<Marker> liste = new ArrayList<>();
        String urls = "http://"+ Options.IPGEO +":8081/batiments?";

        for (int i = 0; i < Options.CRITERE.length; i++){
            urls += "criteres="+Options.CRITERE[i];

            if(i != Options.CRITERE.length-1){
                urls += "&";
            }
        }

        URL url = new URL(urls);
        HttpURLConnection httpclient = (HttpURLConnection) url.openConnection();

        if (httpclient.getResponseCode() == 200) {
            InputStream responseBody = httpclient.getInputStream();
            InputStreamReader responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
            JsonReader jsonReader = new JsonReader(responseBodyReader);
            jsonReader.beginArray();

            while(jsonReader.hasNext()){
                jsonReader.beginObject();
                jsonReader.nextName();

                int id = jsonReader.nextInt();
                jsonReader.nextName();

                double lon = jsonReader.nextDouble();
                jsonReader.nextName();

                double lat = jsonReader.nextDouble();
                jsonReader.nextName();

                String nom = jsonReader.nextString();
                jsonReader.nextName();

                String tag = jsonReader.nextString();
                liste.add(new Marker(id,lon,lat,nom,tag));

                jsonReader.endObject();
            }
            jsonReader.endArray();
            jsonReader.close();
        }


        return liste;
    }
}
