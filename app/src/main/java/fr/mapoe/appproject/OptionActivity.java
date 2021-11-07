package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;
import static java.lang.String.valueOf;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

public class OptionActivity extends AppCompatActivity {

    SharedPreferences language;
    private float scale;
    private ArrayList<String[]> customSentencesList;
    private static final String FILE_NAME = "custom_sentences.txt";
    private DataBaseManager dataBaseManager = new DataBaseManager();
    private String[] currentSentence;
    @SuppressLint("ClickableViewAccessibility")
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activty_option);
        scale = getResources().getDisplayMetrics().density;

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
        goTomMenu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
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

        //on vérifie si il y a des phrases custom :
        SharedPreferences customSentences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        boolean isCustomSentencesCreated = customSentences.getBoolean("customSentences",false);
        //on vérifie aussi s'il y a au moins 1 phrase custom (le cas oû il a supprimé son unique phrase)
        if((!isCustomSentencesCreated) || (dataBaseManager.getNumberOfSentences("CUSTOM",getApplicationContext())==0))
            editSentenceLayout.setVisibility(View.GONE);
        editSentenceLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getCustomSentencesFromDB();
                showSentence(R.layout.sentences_popup);

            }
        });

    }

    private void getCustomSentencesFromDB(){
        customSentencesList = new ArrayList<String[]>();
        String[] tempSentence;
        String language = "CUSTOM";
        int length = dataBaseManager.getNumberOfSentences(language,getApplicationContext());
        for(int i=1;i<=length;i++){
            tempSentence=dataBaseManager.getSentenceFromDB(language,i,getApplicationContext());
            if(tempSentence[3].equals("custom")) {
                customSentencesList.add(tempSentence);
            }
        }

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

    @SuppressLint("ClickableViewAccessibility")
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
        xButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        ImageView view = (ImageView) v;
                        //overlay is black with transparency of 0x77 (119)
                        view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                        view.invalidate();
                        break;
                    }
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL: {
                        ImageView view = (ImageView) v;
                        //clear the overlay
                        view.getDrawable().clearColorFilter();
                        view.invalidate();
                        break;
                    }
                }

                return false;
            }
        });


        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        // paramètre des TextView
        int dpWidthInPx  = (int) (300 * scale);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(dpWidthInPx, ViewGroup.LayoutParams.MATCH_PARENT);

        //paramètre du global Layout
        LinearLayout.LayoutParams globalLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        globalLayoutParams.bottomMargin= 70;

        int dpSizeInPx = (int) (33*scale);
        //paramètre des images
        LinearLayout.LayoutParams editImageParams = new LinearLayout.LayoutParams(dpSizeInPx,dpSizeInPx);
        editImageParams.bottomMargin = 30;
        LinearLayout.LayoutParams deleteImageParams = new LinearLayout.LayoutParams(dpSizeInPx, dpSizeInPx);

        // paramètre layout des images
        LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        for(int i =0;i< customSentencesList.size();i++) {

            currentSentence=customSentencesList.get(i);
            //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame

            // linear layout qui contient tout
            LinearLayout globalLinearLayout = new LinearLayout(getApplicationContext());
            globalLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
            globalLinearLayout.setLayoutParams(globalLayoutParams);
            globalLinearLayout.setId(linearID);
            displayLayout.addView(globalLinearLayout);

            // le text de la phrase
            TextView textDisplay = new TextView(getApplicationContext());
            textDisplay.setLayoutParams(textParams);
            textDisplay.setTypeface(typeface);
            textDisplay.setGravity(Gravity.CENTER);
            textDisplay.setTextColor(Color.WHITE);
            textDisplay.setMaxLines(4);
            textDisplay.setTextSize(18);
            textDisplay.setText(HtmlCompat.fromHtml(getCleanText(currentSentence[2]),HtmlCompat.FROM_HTML_MODE_LEGACY));
            textDisplay.setId(textID);

            textDisplay.setHint(Integer.toString(i));//normalement i correspond à l'id de la phrase (, utilise pour le delete sentence)
            globalLinearLayout.addView(textDisplay);
            // un layout vertical qui contient les images

            LinearLayout imageLayout = new LinearLayout(getApplicationContext());
            imageLayout.setOrientation(LinearLayout.VERTICAL);
            imageLayout.setGravity(Gravity.RIGHT);
            imageLayout.setLayoutParams(imageLayoutParams);
            globalLinearLayout.addView(imageLayout);

            ImageView edit = new ImageView(getApplicationContext());
            edit.setImageResource(R.drawable.edit_logo);
            edit.setLayoutParams(editImageParams);
            edit.setId(editID);
            int finalEditID = editID;
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent addSentenceActivity = new Intent(getApplicationContext(), AddSentenceActivity.class);
                    addSentenceActivity.putExtra("decodedSentence", customSentencesList.get(finalEditID-1));
                    addSentenceActivity.putExtra("editSentence",true);
                    startActivity(addSentenceActivity);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    alertDialog.dismiss();
                    finish();
                }
            });
            edit.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            ImageView view = (ImageView) v;
                            //overlay is black with transparency of 0x77 (119)
                            view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            view.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            ImageView view = (ImageView) v;
                            //clear the overlay
                            view.getDrawable().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                    }

                    return false;
                }
            });


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
            delete.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN: {
                            ImageView view = (ImageView) v;
                            //overlay is black with transparency of 0x77 (119)
                            view.getDrawable().setColorFilter(0x77000000, PorterDuff.Mode.SRC_ATOP);
                            view.invalidate();
                            break;
                        }
                        case MotionEvent.ACTION_UP:
                        case MotionEvent.ACTION_CANCEL: {
                            ImageView view = (ImageView) v;
                            //clear the overlay
                            view.getDrawable().clearColorFilter();
                            view.invalidate();
                            break;
                        }
                    }

                    return false;
                }
            });
            imageLayout.addView(edit);
            imageLayout.addView(delete);



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

    private String getCleanText(String text) {
        String res = "";
        if (numberOfOccurrences(text) == 1) {
            res = text.replaceFirst("§", getString(R.string.player1_bold_italic));

        } else if (numberOfOccurrences(text) == 2) {
            res = text.replaceFirst("§", getString(R.string.player1_bold_italic));
            res=res.replace("§",getString(R.string.player2_bold_italic));
        }
        else{
            res = text.replaceFirst("§", getString(R.string.player1_bold_italic));
            res=res.replace("§",getString(R.string.player3_bold_italic));
            res = res.replaceFirst(getString(R.string.player3_bold_italic),getString(R.string.player2_bold_italic));
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

    private void deleteSentence(int deleteID, View layoutView){
        int id = deleteID-900;
        TextView sentenceTextView = (TextView) layoutView.findViewById(id); //on trouve le textview correspondant
        String sentence = sentenceTextView.getHint().toString(); // on récupère son hint qui contient la phrase comme est enregistrer dans le fichier text
        //on retire la ligne correspond à la phrase supprimé
        LinearLayout containerLayout = layoutView.findViewById(id+9900);
        containerLayout.setVisibility(View.GONE);

        //on effectue la requette SQL qui supprime la sentence
        dataBaseManager.deleteSentenceInDB("CUSTOM",customSentencesList.get(id-101)[2],getApplicationContext());
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}