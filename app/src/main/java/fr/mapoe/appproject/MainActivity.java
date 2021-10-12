package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        //changement de la langue
        getLanguage();

        AnimationBg.startBackgroundAnimation(findViewById(R.id.main_layout));

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

        goToOption.setOnTouchListener(new View.OnTouchListener() {
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


        // go to character choose by ApeChill
        LinearLayout goToCharacter = (LinearLayout) findViewById(R.id.character_choose_image);
        goToCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChooseActivity.putExtra("typeOfGame",1);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // go to character choose by ApePiment
        LinearLayout goToApePiment = (LinearLayout) findViewById(R.id.ape_piment) ;
        goToApePiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChooseActivity.putExtra("typeOfGame",2);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        // go to Simple Wheel
        LinearLayout goToSimpleWheel = (LinearLayout) findViewById(R.id.wheel_image);
        goToSimpleWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent simpleWheelActivity = new Intent(getApplicationContext(), SimpleWheelActivity.class);
                startActivity(simpleWheelActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // go to card game
        LinearLayout goToCardGame = (LinearLayout) findViewById(R.id.card_image);
        goToCardGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cardGameActivity = new Intent(getApplicationContext(), CardGameActivity.class);
                startActivity(cardGameActivity);
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
        if(choseLanguage!="" & !localLanguage.equals(choseLanguage)){
            setLocale(choseLanguage);//si la langue de sharedpréférencies existe et qu'elle est différente de celle du tel alors on l'utilise
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
    public void onBackPressed() {

    }

}