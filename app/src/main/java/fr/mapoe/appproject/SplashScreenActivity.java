package fr.mapoe.appproject;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

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

import fr.mapoe.appproject.sqlite.UpdateLocalDataBase;
import fr.mapoe.appproject.tools.Parser;

public class SplashScreenActivity extends AppCompatActivity {
    private String address = "https://apetime.000webhostapp.com/request.php";
    private final int SPLASH_SCREEN_TIMEOUT = 1500;
    public String[][] sentenceTab;
    private Context context = SplashScreenActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        Log.d("internet:",Boolean.toString(isConnected));
        // si on est co:
        if(isConnected)
            new AccesHTTP().execute();
        else
            Toast.makeText(context,"No internet connection",Toast.LENGTH_SHORT).show();

    }

    private class AccesHTTP extends AsyncTask<Void, Integer, String> {

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

            if (s != null) {
                // si on a réussi à parse le json on lance le main
                if (parse(s) == 1) {
                    // lance l'udapte de la base SQLITE
                    UpdateLocalDataBase updateLocalDataBase = new UpdateLocalDataBase();
                    updateLocalDataBase.updateFromOnlineDB(sentenceTab, context);

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


                } else {
                    Toast.makeText(context, "Unable to parse data", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(context, "Unable to dowload data", Toast.LENGTH_SHORT).show();
            }
        }

        private int parse(String data) {

            try {
                JSONArray jsonArray = new JSONArray(data);
                sentenceTab = new String[jsonArray.length()][10];
                // pour chaque élements du JSON array
                for (int i = 0; i < jsonArray.length(); i++) {
                    // appel de la class parser
                    Parser parser = new Parser(jsonArray.getJSONObject(i));
                    parser.parseData(); // parse les data
                    String[] dataParsed = parser.result;
                    // remplie le tableau 2d avec toutes les phrases de la base
                    for (int j = 0; j < dataParsed.length; j++) {
                        sentenceTab[i][j] = dataParsed[j];
                        Log.d("test",sentenceTab[i][j]);
                    }
                }

                return 1;

            } catch (JSONException e) {
                e.printStackTrace();
            }

            return 0;
        }

        private String downloadData() {
            // connect and get stream
            InputStream inputStream = null;
            String line = null;
            try {
                URL url = new URL(address);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                inputStream = new BufferedInputStream(con.getInputStream());
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer stringBuffer = new StringBuffer();

                if (bufferedReader != null) {
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("\n");
                    }
                } else {
                    return null;
                }
                return stringBuffer.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }
}

