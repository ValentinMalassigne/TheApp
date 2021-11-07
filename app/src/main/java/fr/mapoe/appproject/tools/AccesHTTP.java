package fr.mapoe.appproject.tools;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import fr.mapoe.appproject.UpdateLocalDataBase;

/**
 * Accès à la page PHP
 * Décode Le JSON
 * A besoin du context
 */

public class AccesHTTP extends AsyncTask<Void,Integer,String> {

    Context context;
    String address="https://apetime.000webhostapp.com/request.php";
    public String[][] sentenceTab;
    public AccesHTTP(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        String data = downloadData();
        return data;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }


    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        if(s !=null){
            if(parse(s)==1){
            }
            else{
                Toast.makeText(context,"Unable to parse data",Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(context,"Unable to dowload data",Toast.LENGTH_SHORT).show();
        }
    }

    private int parse(String data){

        try{
            JSONArray jsonArray = new JSONArray(data);
            sentenceTab = new String[jsonArray.length()][10];
            // pour chaque élements du JSON array
            for(int i=0;i<jsonArray.length();i++) {
                // appel de la class parser
                Parser parser = new Parser(jsonArray.getJSONObject(i));
                parser.parseData(); // parse les data
                String[] dataParsed = parser.result;
                // remplie le tableau 2d avec toutes les phrases de la base
                for (int j = 0; j < dataParsed.length; j++) {
                    sentenceTab[i][j] = dataParsed[j];
                }
            }

            //vu que le getter marche pas je suis obligé de lancer l'update comme ça
            UpdateLocalDataBase updateLocalDataBase = new UpdateLocalDataBase();
            updateLocalDataBase.updateFromOnlineDB(sentenceTab,context);

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private String downloadData(){
        // connect and get stream
        InputStream inputStream = null;
        String line =  null;
        try{
            URL url = new URL(address);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            inputStream = new BufferedInputStream(con.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuffer stringBuffer = new StringBuffer();

            if(bufferedReader != null){
                while((line = bufferedReader.readLine()) != null){
                    stringBuffer.append(line).append("\n");
                }
            }
            else{
                return null;
            }
            return stringBuffer.toString();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public String[][] getSentenceTab() {
        return sentenceTab;
    }
}