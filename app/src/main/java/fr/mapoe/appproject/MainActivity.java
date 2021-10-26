package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

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
                /*Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChooseActivity.putExtra("typeOfGame",2);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();*/
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
        // go to bus game
        LinearLayout goToBusGame = (LinearLayout) findViewById(R.id.bus_game_layout);
        goToBusGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent busGameActivity = new Intent(getApplicationContext(), BusGameActivity.class);
                startActivity(busGameActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // text responsive si l'api le permet
        if(Build.VERSION.SDK_INT >= 26){
            setResponsiveText();
        }

        //setup la popup warning
        warningPopup();
    }

    private void warningPopup(){
        long currentTime= System.currentTimeMillis();
        //on charge la dernière date d'affichage enregistrée dans les shared preferences
        SharedPreferences language = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        long lastTime = language.getLong("lastWarningTime",0);
        if(currentTime-lastTime>3600000){//une heure en millisecondes
            showInfoDialog(R.layout.info_popup);
            //ici on enregistre dans les SharedPreferences la langue choisie par l'utilisateur
            SharedPreferences.Editor editor = language.edit();
            editor.putLong("lastWarningTime",currentTime);
            editor.apply();
        }
    }

    private void showInfoDialog(int layout){// créer la popup info
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(MainActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        Button okButton = layoutView.findViewById(R.id.ok_button);
        CheckBox checkBox = layoutView.findViewById(R.id.block_popup_checkBox);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        ImageView nextButton = layoutView.findViewById(R.id.right_popup_arrow);
        ImageView leftButton = layoutView.findViewById(R.id.left_popup_arrow);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        leftButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        imageInfo.setVisibility(View.GONE);
        checkBox.setVisibility(View.GONE);
        textInfo.setText(R.string.drink_warning);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private void setResponsiveText() {

        TextView apeChillTitle = findViewById(R.id.apechill_title);
        TextView apeChillDescription = findViewById(R.id.apepiment_description);
        TextView apePiementTitle = findViewById(R.id.apepiment_description);
        TextView apePiementDescription = findViewById(R.id.apepiment_description);
        TextView rouletteTitle = findViewById(R.id.roulette_title);
        TextView rouletteDescription = findViewById(R.id.roulette_description);
        TextView cardTitle = findViewById(R.id.card_title);
        TextView cardDescription = findViewById(R.id.card_discription);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            apeChillTitle.setAutoSizeTextTypeWithDefaults(TextView.AUTO_SIZE_TEXT_TYPE_UNIFORM);
        }
    }

    private void getLanguage(){
        //on charge le langage enregistrée dans les shared preferences
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