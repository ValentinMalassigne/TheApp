package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //animation du bg
        AnimationBg.startBackgroundAnimation(findViewById(R.id.menu_layout));

        //changement de la langue
        getLanguage();

        //go to option
        ImageView goToOption = (ImageView) findViewById(R.id.setting_button);
        goToOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        // go to GameSelection
        Button gotToGameSelection = (Button) findViewById(R.id.game_selection_button);
        gotToGameSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameSelectionActivity = new Intent(getApplicationContext(), GameSelectionActivity.class);
                startActivity(gameSelectionActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

    }
    private void getLanguage(){
        //on charge le langage enregistrer dans les shared preferences
        SharedPreferences language = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String choseLanguage = language.getString("language","");
        //on récup la langue acctuelement utilisé par l'appli
        Configuration conf = getResources().getConfiguration();
        String localLanguage = conf.locale.getLanguage();
        //on vérifie si la langue actuelle et la langue enregistré par l'utilisateur sont la même (pour éviter de changer en boucle la langue)
        if(choseLanguage!=null & !localLanguage.equals(choseLanguage)){
            setLocale(choseLanguage);
        }
    }

    //on met a jour la langue de l'appli
    private void setLocale(String choseLanguage) {
        Locale myLocale = new Locale(choseLanguage);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        //on refresh la page pour y appliquer le changement de langue
        Intent refresh = new Intent(this, MainActivity.class);
        finish();
        startActivity(refresh);
    }

    @Override
    public void onBackPressed() {}

}