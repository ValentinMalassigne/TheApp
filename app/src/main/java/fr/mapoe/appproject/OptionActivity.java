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
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
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
import androidx.core.text.HtmlCompat;
import androidx.core.widget.TextViewCompat;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class OptionActivity extends AppCompatActivity {

    SharedPreferences language;
    private String[][] sentencesTab;
    private String[][] decodingTab;
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
        if(!checkFileExist())
            editSentenceLayout.setVisibility(View.GONE);
        editSentenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    readFile();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                fillDecodingTab();
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
        int editID= 1;
        int deleteID=1001;
        int textID=101;
        int linearID=10001;
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
        params.bottomMargin= 50;
        LinearLayout.LayoutParams deleteImageParams = new LinearLayout.LayoutParams(80, 80);
        deleteImageParams.leftMargin=10;

        for(int i =0;i< decodingTab.length;i++) {
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setLayoutParams(params);
            linearLayout.setId(linearID);
            displayLayout.addView(linearLayout);
            TextView textDisplay = new TextView(getApplicationContext());
            textDisplay.setLayoutParams(textParams);
            textDisplay.setTypeface(typeface);
            textDisplay.setTextColor(Color.WHITE);
            textDisplay.setMaxLines(4);
            textDisplay.setTextSize(18);
            textDisplay.setText(HtmlCompat.fromHtml(getCleanText(decodingTab[i][2]),HtmlCompat.FROM_HTML_MODE_LEGACY));
            textDisplay.setId(textID);
            textDisplay.setHint(sentencesTab[i][0]);//on lui donne la phrase (comme dans le fichier text en hint, utilise pour le delete sentence)


            ImageView edit = new ImageView(getApplicationContext());
            edit.setImageResource(R.drawable.edit_logo);
            edit.setLayoutParams(deleteImageParams);
            edit.setId(editID);
            int finalI = i;
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // il faut envoyer la ligne correspondante
                    String[] line = new String[8];
                    for(int j=0;j<line.length;j++){
                        line[j] = decodingTab[finalI][j];
                    }

                    Intent addSentenceActivity = new Intent(getApplicationContext(), AddSentenceActivity.class);
                    addSentenceActivity.putExtra("decodedSentence", line);
                    addSentenceActivity.putExtra("editSentence",true);
                    startActivity(addSentenceActivity);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    alertDialog.dismiss();
                    finish();
                }
            });

            deleteImageParams.gravity = Gravity.CENTER;
            ImageView delete = new ImageView(getApplicationContext());
            delete.setImageResource(R.drawable.poubelle);
            delete.setLayoutParams(deleteImageParams);
            delete.setId(deleteID);
            int finalDeleteID = deleteID;
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteSentence(finalDeleteID, layoutView);
                }
            });

            linearLayout.addView(textDisplay);
            linearLayout.addView(edit);
            linearLayout.addView(delete);

            deleteID++;
            editID++;
            textID++;
            linearID++;
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
        sentencesTab = new String[compteur][3];//0 phrase, 1 type de phrase, 2 type de jeu

        //ouverture du fichier
        fis=openFileInput(FILE_NAME);
        isr = new InputStreamReader(fis);
        br = new BufferedReader(isr);
        //lecture du fichier et on sauvegarde les phrases
        compteur=0;
        for(i=1;i<=2;i++){//on fait deux tours, un pour les phrases normals, un pour les phrases génantes
            String gameMode = "";
            if(i==1){
                gameMode ="Apechill";
            }
            else{
                gameMode="ApePiment";
            }
            br.readLine();//on passe la ligne "anecdotes"
            String tempLine = br.readLine();//on lit la première anecdote
            while (!tempLine.equals("gages")) {
                sentencesTab[compteur][0]=tempLine;
                sentencesTab[compteur][1]="anecdote";
                sentencesTab[compteur][2]=gameMode;
                compteur++;
                tempLine = br.readLine();
            }
            tempLine = br.readLine();
            while (!tempLine.equals("minigames")) {
                sentencesTab[compteur][0]=tempLine;
                sentencesTab[compteur][1]="gages";
                sentencesTab[compteur][2]=gameMode;
                compteur++;
                tempLine = br.readLine();
            }

            tempLine = br.readLine();
            while (!tempLine.equals("questions")) {
                sentencesTab[compteur][0]=tempLine;
                sentencesTab[compteur][1]="minigames";
                sentencesTab[compteur][2]=gameMode;
                compteur++;
                tempLine = br.readLine();
            }
            tempLine = br.readLine();
            while (!tempLine.equals("End")) {
                sentencesTab[compteur][0]=tempLine;
                sentencesTab[compteur][1]="questions";
                sentencesTab[compteur][2]=gameMode;
                compteur++;
                tempLine = br.readLine();
            }
        }
        fis.close();
    }

    private static String[] decoding(String gameMode, String type, String encoding){ // decoder sentence tab
        String[] decoding = new String[8]; //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: mode de jeu
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
        answers=encoding.substring(j+1,i);
        sentence=encoding.substring(i+1);//on lit tout jusqu'à la fin pour avoir la phrase

        decoding[0] = points;
        decoding[1] = answers;
        decoding[2] = sentence;
        decoding[3] = type;
        decoding[4] = rightAnswer;
        decoding[5] = button1Text;
        decoding[6] = button2Text;
        decoding[7] = gameMode;
        return decoding;
    }

    private void fillDecodingTab(){
        decodingTab = new String[sentencesTab.length][8];
        //Pour parcourir chaque ligne
        for (int i = 0; i < decodingTab.length; i++) {
            String[] currentTab = decoding(sentencesTab[i][2],sentencesTab[i][1],sentencesTab[i][0]);
            //Pour parcourir chaque colonne
            for (int j = 0; j < currentTab.length; j++) {
                decodingTab[i][j] = currentTab[j];
                Log.d(TAG,decodingTab[i][j]);
            }
        }
    }

    private String getCleanText(String text) {
        String res = "";
        if (numberOfOccurrences(text) == 1) {
            res = text.replaceFirst("§", "<b><i>Joueur 1</i></b>");

        } else if (numberOfOccurrences(text) == 2) {
            res = text.replaceFirst("§", "<b><i>Joueur 1</i></b>");
            res=res.replace("§","<b><i>Joueur 2</i></b>");
        }
        else{
            res = text.replaceFirst("§", "<b><i>Joueur 1</i></b>");
            res=res.replace("§","<b><i>Joueur 3</i></b>");
            res = res.replaceFirst("<b><i>Joueur 3</i></b>","<b><i>Joueur 2</i></b>");
        }
        return res;
    }

    private int numberOfOccurrences(String source) {
        int occurrences = 0;

        if (source.contains("--joueur--")) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace("--joueur--", "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / "--joueur--".length();
        }

        return occurrences;
    }

    private Boolean checkFileExist(){
        Boolean res = true;
        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (!file.exists()){
            res = false;
        }
        return res;
    }

    //marche pas, raison : java.lang.NullPointerException: Attempt to invoke virtual method 'java.lang.CharSequence android.widget.TextView.getText()' on a null object reference
    private void deleteSentence(int deleteID, View layoutView){
        int id = deleteID-900;
        TextView sentenceTextView = (TextView) layoutView.findViewById(id); //on trouve le textview correspondant
        String sentence = sentenceTextView.getHint().toString(); // on récupère son hint qui contient la phrase comme est enregistrer dans le fichier text
        //on retire la ligne correspond à la phrase supprimé
        LinearLayout containerLayout = layoutView.findViewById(id+9900);
        containerLayout.setVisibility(View.GONE);

        //on copie ce qu'il y a dans le fichier
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            ArrayList<String> phrase = new ArrayList<String>();
            boolean deleteDone = false;
            while ((text = br.readLine())!=null){
                if(text.equals(sentence)&&!deleteDone){ //si la phrase est celle a supprimé alors on la "passe", le boolean sert a éviter de supprimer plusieurs fois la phrases si l'utilisateur l'a sauvegardé plusieurs fois
                    deleteDone=true;
                }else{
                    phrase.add(text+"\n");
                }
            }
            fis.close();

            //on prépare le remplissage du fichier retour
            String remplissage = "";
            while (!phrase.isEmpty()){
                remplissage=remplissage+(phrase.remove(0));
            }

            //on réouvre le fichier mais cette fois ci pour y écrire
            FileOutputStream fos = null;
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(remplissage.getBytes());
            if(fos!=null){
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}