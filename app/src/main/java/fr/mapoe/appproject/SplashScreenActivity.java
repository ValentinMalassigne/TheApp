package fr.mapoe.appproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import org.json.JSONArray;
import org.json.JSONException;
import fr.mapoe.appproject.conhttp.AccesHTTP;
import fr.mapoe.appproject.conhttp.AsyncResponse;
import fr.mapoe.appproject.sqlite.DataBaseManager;
import fr.mapoe.appproject.tools.Parser;
import fr.mapoe.appproject.tools.ThemeManager;

public class SplashScreenActivity extends AppCompatActivity {

    private String requestFRAddress = "https://apetime.000webhostapp.com/requestFR.php";
    private String versionAddress = "https://apetime.000webhostapp.com/dbVersion.php";
    private String requestENAddress = "https://apetime.000webhostapp.com/requestEN.php";
    private final int SPLASH_SCREEN_TIMEOUT = 1500;
    public String[][] sentenceTab;
    private boolean restartApeBus=false;
    private boolean isCalledFromApeBus=false;
    private SharedPreferences sharedPreferences;
    private Context context = SplashScreenActivity.this;
    private DataBaseManager dataBaseManager = new DataBaseManager();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            restartApeBus = extras.getBoolean("apebus restart");
            isCalledFromApeBus=true;
            startMain();
        }



        ConstraintLayout constraintLayout = findViewById(R.id.splash_screen_layout);
        this.sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        ThemeManager themeManager = new ThemeManager(this, sharedPreferences.getString("theme", ""));
        constraintLayout.setBackground(themeManager.getBackgroundDrawable());
        boolean isConnected = checkCon();
        Log.d("internet:", Boolean.toString(isConnected));

        //int localVersion = 1;
        int localVersion = sharedPreferences.getInt("localVersion",0); // VALENTIN -> obligé de mettre un nombre pour i et du coup ça compte tout le temps 0 ajout en ligne 85
        Log.d("numéro de version local",Integer.toString(localVersion));
        // si on est co:
        if (isConnected) {
            requestDBVersion(localVersion); // on lance la requete pour obtenir le num de version

        } else if(!isConnected && localVersion!=0) // si on a pas de co mais que le num dé version est !=0
            startMain();
        else { //  cas ou c'est la première fois qu'on se co
            Log.d("checkConnection","pas de co et c'est la première fois qu'il lance l'appli");
            showInfoDialog(R.layout.info_popup);

        }

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
                            // lance le main activity avec un delay de 1,5sec
                            startMain();
                        }
                        // il faut recupérer les nouvelles phrases
                        else{
                            String[] urls = new String[]{requestENAddress,requestFRAddress};
                            Log.d("checkVersion","nouvelle version disponible de la db");
                            int newDBVersion = result[1];
                            requestSentence(urls,newDBVersion);
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
    private void requestSentence(String[] urls,int newDBVersion){
        for(int i=0;i< urls.length;i++) {
            String url = urls[i];
            int finalI = i;
            AccesHTTP accesHTTP = new AccesHTTP(url, new AsyncResponse() {
                @Override
                public void onTaskCompleted(String output) {
                    if (output != null) {
                        // si on a réussi à parse le json on lance le main
                        if (parseSentence(output) == 1) {
                            if(finalI==0){//cas ou l'on a les phrases anglaises
                                dataBaseManager.updateFromOnlineDB(sentenceTab,"EN",context);
                            }else if(finalI==1){
                                dataBaseManager.updateFromOnlineDB(sentenceTab,"FR",context);
                            }
                            if(finalI ==urls.length-1){ //
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putInt("localVersion",newDBVersion);
                                editor.apply();
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
                if(isCalledFromApeBus) {
                    intent.putExtra("fixBugApeBus", true);
                    intent.putExtra("apebus restart", restartApeBus);
                }
                finish();
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            }
        };
        new Handler().postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);
    }
    // pour le cas particulier de la première utilisation et qu'il a pas internet
    private void showInfoDialog(int layout){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View layoutView = getLayoutInflater().inflate(layout, null);
        ImageView imageView = layoutView.findViewById(R.id.image_info);
        imageView.setVisibility(View.GONE);
        ImageView imageView1 = layoutView.findViewById(R.id.left_popup_arrow);
        imageView1.setVisibility(View.GONE);
        ImageView imageView2 = layoutView.findViewById(R.id.right_popup_arrow);
        imageView2.setVisibility(View.GONE);
        CheckBox checkBox = layoutView.findViewById(R.id.block_popup_checkBox);
        checkBox.setVisibility(View.GONE);
        Button okButton = layoutView.findViewById(R.id.ok_button);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        textInfo.setText(R.string.con_error);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isConnect = checkCon();
                if(isConnect) {
                    Log.d("connection", "connection rétablis");
                    String[] urls = new String[]{requestENAddress,requestFRAddress};
                    requestDBVersion(0);
                    alertDialog.dismiss();
                }
                else{
                    textInfo.setText(R.string.no_con);
                }

            }
        });
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                boolean isConnect = checkCon();
                if(isConnect) {
                    Log.d("connection", "connection rétablis");
                    String[] urls = new String[]{requestENAddress,requestFRAddress};
                    requestDBVersion(0);
                    alertDialog.dismiss();
                }
                else{
                    showInfoDialog(R.layout.info_popup);
                }
            }
        });
    }
    private Boolean checkCon(){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean isConnect = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnect;
    }
}


