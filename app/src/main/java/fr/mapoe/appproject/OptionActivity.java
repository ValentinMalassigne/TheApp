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
import android.widget.Button;
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

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}