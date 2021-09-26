package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class OptionActivity extends AppCompatActivity {

    SharedPreferences language;
    String[][] sentencesTab;
    private static final String FILE_NAME = "custom_sentences.txt";

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
                try {
                    readFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
        // declaration des éléments de la popup
        LinearLayout displayLayout = (LinearLayout) layoutView.findViewById(R.id.spinner_layout);
        ImageView xButton = (ImageView) layoutView.findViewById(R.id.x_popup_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(230, ViewGroup.LayoutParams.WRAP_CONTENT,1);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.bottomMargin= 30;
        for(int i =0;i<5;i++) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setLayoutParams(params);
            displayLayout.addView(linearLayout);
            TextView textDisplay = new TextView(getApplicationContext());
            textDisplay.setLayoutParams(textParams);
            textDisplay.setTypeface(typeface);
            textDisplay.setTextColor(Color.WHITE);
            textDisplay.setMaxLines(4);
            textDisplay.setTextSize(18);
            textDisplay.setText("phrases 1 blablablablaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaablaaaaaaaaaaa");

            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(80, 80);
            imageParams.leftMargin = 50;
            imageParams.gravity = Gravity.CENTER;
            ImageView delete = new ImageView(getApplicationContext());
            delete.setImageResource(R.drawable.poubelle);
            delete.setLayoutParams(imageParams);

            imageParams.rightMargin = 30;
            ImageView edit = new ImageView(getApplicationContext());
            edit.setImageResource(R.drawable.edit_logo);
            edit.setLayoutParams(imageParams);

            linearLayout.addView(textDisplay);
            linearLayout.addView(delete);
            linearLayout.addView(edit);
        }
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

    private void readFile() throws IOException {
        //def des variables
        String text;
        int compteur;
        //initialisation
        int i=0;
        compteur=-10;//compte le nombre de phrases customs (il y a 10 lignes déjà utilisées dans le fichier donc on commence a -10)

        //ouverture du fichier
        FileInputStream fis=openFileInput(FILE_NAME);
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        //on compte le nombre de ligne pour créer un tableau de bonne taille
        while ((text = br.readLine())!=null){
            compteur++;
        }
        fis.close();

        sentencesTab = new String[3][compteur];//0 phrase, 1 type de phrase, 2 type de jeu

        //ouverture du fichier
        fis=openFileInput(FILE_NAME);
        isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
        //lecture du fichier et on sauvegarde les phrases
        compteur=0;
        for(i=1;i<=2;i++){//on fait deux tours, un pour les phrases normals, un pour les phrases génantes
            br.readLine();//on passe la ligne "anecdotes"
            String tempLine = br.readLine();//on lit la première anecdote
            while (!tempLine.equals("gages")) {
                sentencesTab[0][compteur]=tempLine;
                sentencesTab[1][compteur]="anecdote";
                sentencesTab[2][compteur]=valueOf(i);
                compteur++;
                tempLine = br.readLine();
            }
            tempLine = br.readLine();
            while (!tempLine.equals("minigames")) {
                sentencesTab[0][compteur]=tempLine;
                sentencesTab[1][compteur]="gages";
                sentencesTab[2][compteur]=valueOf(i);
                compteur++;
                tempLine = br.readLine();
            }

            tempLine = br.readLine();
            while (!tempLine.equals("questions")) {
                sentencesTab[0][compteur]=tempLine;
                sentencesTab[1][compteur]="minigames";
                sentencesTab[2][compteur]=valueOf(i);
                compteur++;
                tempLine = br.readLine();
            }
            tempLine = br.readLine();
            while (!tempLine.equals("End")) {
                sentencesTab[0][compteur]=tempLine;
                sentencesTab[1][compteur]="questions";
                sentencesTab[2][compteur]=valueOf(i);
                compteur++;
                tempLine = br.readLine();
            }
        }
        fis.close();

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