package fr.mapoe.appproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.nfc.Tag;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

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

import fr.mapoe.appproject.conhttp.AccesHTTP;
import fr.mapoe.appproject.conhttp.AsyncResponse;
import fr.mapoe.appproject.sqlite.UpdateLocalDataBase;
import fr.mapoe.appproject.tools.Parser;
import fr.mapoe.appproject.tools.ThemeManager;

public class SplashScreenActivity extends AppCompatActivity {

    private String requestFRAddress = "https://apetime.000webhostapp.com/requestFR.php";
    private String versionAddress = "https://apetime.000webhostapp.com/dbVersion.php";
    private String requestENAddress = "https://apetime.000webhostapp.com/requestEN.php";
    private final int SPLASH_SCREEN_TIMEOUT = 1500;
    public String[][] sentenceTab;
    private SharedPreferences sharedPreferences;
    private Context context = SplashScreenActivity.this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConstraintLayout constraintLayout = findViewById(R.id.splash_screen_layout);
        this.sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        ThemeManager themeManager = new ThemeManager(this, sharedPreferences.getString("theme", ""));
        constraintLayout.setBackground(themeManager.getBackgroundDrawable());
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d("internet:", Boolean.toString(isConnected));

        //int localVersion = 1;
        int localVersion = sharedPreferences.getInt("localVersion",0); // VALENTIN -> obligé de mettre un nombre pour i et du coup ça compte tout le temps 0 ajout en ligne 85
        Log.d("numéro de version local",Integer.toString(localVersion));
        // si on est co:
        if (isConnected) {
            requestDBVersion(localVersion); // on lance la requete pour obtenir le num de version

        } else
            Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();

    }
    private void requestDBVersion(int localVersion){
        AccesHTTP accesHTTP = new AccesHTTP(versionAddress, new AsyncResponse() {
            @Override
            public void onTaskCompleted(String output) {
                if (output != null) {
                    // si le JSON est parse
                    int [] result = parseVersion(output);
                    // si on a réussi à parse les données
                    if (result[0]==1) {
                        // si le numéro de version est le meme que celui passé en params
                        if(result[1]==localVersion) {
                            Log.d("checkVersion","pas de nouvelle version de la db");
                            // on change le numéro de version local
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putInt("localVersion",result[1]);
                            editor.apply();
                            // lance le main activity avec un delay de 1,5sec
                            startMain();
                        }
                        // il faut recupérer les nouvelles phrases
                        else{
                            String[] urls = new String[]{requestENAddress,requestFRAddress};
                            Log.d("checkVersion","nouvelle version disponible de la db");
                            requestSentence(urls);
                        }
                    } else {
                        Toast.makeText(context, "Unable to parse data", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "Unable to dowload data", Toast.LENGTH_SHORT).show();
                }
            }
        });
        accesHTTP.execute();
    }
    private void requestSentence(String[] urls){
        for(int i=0;i< urls.length;i++) {
            String url = urls[i];
            int finalI = i;
            AccesHTTP accesHTTP = new AccesHTTP(url, new AsyncResponse() {
                @Override
                public void onTaskCompleted(String output) {
                    if (output != null) {
                        // si on a réussi à parse le json on lance le main
                        if (parseSentence(output) == 1) {
                            // lance l'udapte de la base SQLITE VALENTIN
                        /*UpdateLocalDataBase updateLocalDataBase = new UpdateLocalDataBase();
                        updateLocalDataBase.updateFromOnlineDB(sentenceTab, context);*/
                            if(finalI ==urls.length-1){ //
                                startMain();
                            }

                        } else {
                            Toast.makeText(context, "Unable to parse data", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(context, "Unable to dowload data", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            accesHTTP.execute();
        }
    }
    // fonction pour parse les phrases
    private int parseSentence(String data) {
        try {
            JSONArray jsonArray = new JSONArray(data);
            sentenceTab = new String[jsonArray.length()][10];
            // pour chaque élements du JSON array
            for (int i = 0; i < jsonArray.length(); i++) {
                // appel de la class parser
                Parser parser = new Parser(jsonArray.getJSONObject(i));
                parser.parseSentenceData(); // parse les data
                String[] dataParsed = parser.getSentenceResult();
                // remplie le tableau 2d avec toutes les phrases de la base
                for (int j = 0; j < dataParsed.length; j++) {
                    sentenceTab[i][j] = dataParsed[j];
                    Log.d("test", sentenceTab[i][j]);
                }
            }

            return 1;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return 0;
    }

    // fonction pour parse le numéro de version
    private int[] parseVersion(String data) {
        int[] result = new int[2];// 0: succes 1: numéro de version
        result[0] = 0;
        try {
            JSONArray jsonArray = new JSONArray(data);
            Parser parser = new Parser(jsonArray.getJSONObject(0)); // il n'y a qu'une ligne dans la réponse donc pas besoin de faire une boucle
            parser.parseVersionNumber(); // parse les data
            result[0]=1;
            result[1] = parser.getDbVersion();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return result;
    }
    private void startMain(){
        // lance le main activity avec un delay de 1,5sec
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // start page
                Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        new Handler().postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);
    }
}


