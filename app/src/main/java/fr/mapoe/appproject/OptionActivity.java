package fr.mapoe.appproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class OptionActivity extends AppCompatActivity {

    SharedPreferences language;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_option);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.option_layout));

        //initialisation du SharedPreferences
        language= getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);

        LinearLayout languageLayout = (LinearLayout) findViewById(R.id.language_layout);
        languageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLanguagePopup(R.layout.langue_popup);
            }
        });

        // go to menu
        ImageView goTomMenu = (ImageView) findViewById(R.id.menu_button);
        goTomMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        LinearLayout popup_layout = findViewById(R.id.popup_layout);
        popup_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ici on enregistre dans les SharedPreferences si l'utilisateur bloque la popup
                SharedPreferences blockPopup;
                blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = blockPopup.edit();
                editor.putString("block_apechill_tutorial_popup","activated");
                editor.putString("block_alcohol_reminder_popup","activated");
                editor.apply();
                Toast.makeText(getApplicationContext(),getString(R.string.popups_enabled),Toast.LENGTH_SHORT).show();
            }
        });

        LinearLayout addSentenceLayout = (LinearLayout) findViewById(R.id.add_sentence);
        addSentenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addSentenceActivity = new Intent(getApplicationContext(), AddSentenceActivity.class);
                startActivity(addSentenceActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        LinearLayout editSentenceLayout  = (LinearLayout) findViewById(R.id.edit_sentence);
        editSentenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSentence(R.layout.sentences_popup);
            }
        });

    }

    private void showLanguagePopup(int layout) {
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OptionActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        // déclaration des élements de la popup
        ImageView englishButton = (ImageView) layoutView.findViewById(R.id.english_button);
        ImageView frenchButton = (ImageView) layoutView.findViewById(R.id.french_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        englishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("en");
                Toast.makeText(getApplicationContext(),"english",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        frenchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale("fr");
                Toast.makeText(getApplicationContext(),"français",Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });

        alertDialog.show();
    }
    private void showSentence(int layout){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(OptionActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
    }

    private void setLocale(String choseLanguage) {
        //ici on enregistre dans les SharedPreferences la langue choisie par l'utilisateur
        SharedPreferences.Editor editor = language.edit();
        editor.putString("language",choseLanguage);
        editor.apply();
        //instructinos pour changer la langue actuelement utilisé par l'appli
        Locale myLocale = new Locale(choseLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //on refresh la page pour y appliquer le changement de langue
        Intent refresh = new Intent(this, OptionActivity.class);
        finish();
        startActivity(refresh);
    }
    private void readFile(){

    }
    private String[] decoding(String gameMode, String type, String encoding){
        String[] decodingTab = new String[7]; //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: mode de jeu
        boolean temp = false;
        String points;
        String answers;
        String sentence;
        String rightAnswer;
        String button1Text;
        String button2Text;
        int i=0;
        int j;
        //on lit tant que l'on est pas a / pour savoir quelle est le premier bouton
        while (!temp){
            if(encoding.substring(i,i+1).equals("/")){
                temp=true;
            }else {
                i++;
            }
        }
        button1Text=encoding.substring(0,i);
        j=i+1;//on sauvergarde à quelle caractère il faut reprendre la lecture

        //on lit tant que l'on est pas a + ou - pour savoir quelle est le deuxième bouton
        temp=false;
        while (!temp){
            if(encoding.substring(i,i+1).equals("+") || encoding.substring(i,i+1).equals("-")){
                temp=true;
            }else {
                i++;
            }
        }

        button2Text=encoding.substring(j,i);

        rightAnswer = encoding.substring(i,i+1);//on recup le + ou - qui se trouve juste après la deuxième rep
        points= encoding.substring(i+1,i+2);//on recup le nb de points qui est juste après le + ou -

        j=i+1;//on sauvergarde à quelle caractère il faut reprendre la lecture

        //on lit tant que l'on a est pas a ç pour avoir la réponse
        temp=false;
        while (!temp){
            if(encoding.substring(i,i+1).equals("¤")){
                temp=true;
            }else {
                i++;
            }
        }
        answers=encoding.substring(j,i);
        sentence=encoding.substring(i+1);//on lit tout jusqu'à la fin pour avoir la phrase

        decodingTab[0] = points;
        decodingTab[1] = answers;
        decodingTab[2] = sentence;
        decodingTab[3] = type;
        decodingTab[4] = rightAnswer;
        decodingTab[5] = button1Text;
        decodingTab[6] = button2Text;
        decodingTab[7] = gameMode;
        return decodingTab;
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}