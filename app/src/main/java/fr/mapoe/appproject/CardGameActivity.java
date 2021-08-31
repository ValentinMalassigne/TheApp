package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CardGameActivity extends AppCompatActivity {

    private ImageView cardImage;

    private final ArrayList<Integer> idRedCardList,idBlackCardList;

    {
        idRedCardList = new ArrayList<Integer>(Arrays.asList
                (R.drawable.h01, R.drawable.h02, R.drawable.h03, R.drawable.h04,
                        R.drawable.h05, R.drawable.h06, R.drawable.h07, R.drawable.h08,
                        R.drawable.h09, R.drawable.h10, R.drawable.h11, R.drawable.h12,
                        R.drawable.h13, R.drawable.d01, R.drawable.d02, R.drawable.d03,
                        R.drawable.d04, R.drawable.d05, R.drawable.d06, R.drawable.d07,
                        R.drawable.d08, R.drawable.d09, R.drawable.d10, R.drawable.d11,
                        R.drawable.d12, R.drawable.d13)
        );
    }
    {
        idBlackCardList = new ArrayList<Integer>(Arrays.asList
                (R.drawable.c01, R.drawable.c02, R.drawable.c03, R.drawable.c04, R.drawable.c05,
                        R.drawable.c06, R.drawable.c07, R.drawable.c08, R.drawable.c09,
                        R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13,
                        R.drawable.s01, R.drawable.s02, R.drawable.s03, R.drawable.s04,
                        R.drawable.s05, R.drawable.s06, R.drawable.s07, R.drawable.s08,
                        R.drawable.s09, R.drawable.s10, R.drawable.s11, R.drawable.s12,
                        R.drawable.s13)
        );
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);

        AnimationBg.startBackgroundAnimation(findViewById(R.id.card_game_layout));
        Button blackButton = (Button) findViewById(R.id.black_button);
        Button redButton = (Button) findViewById(R.id.red_button);
        TextView cardColor = (TextView) findViewById(R.id.card_color_display);
        this.cardImage = (ImageView) findViewById(R.id.card_image);

        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (randomCard().equals("black"))
                    cardColor.setText(R.string.win);
                else
                    cardColor.setText(R.string.loose);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(randomCard().equals("red")){
                    cardColor.setText(R.string.win);
                }
                else
                    cardColor.setText(R.string.loose);
            }
        });
    }
    private String randomCard(){
        String cardColor = "";
        Random generate = new Random(System.currentTimeMillis());
        TextView nbCard = (TextView) findViewById(R.id.nb_card);

        // si les 2 listes sont pleines
        if(idBlackCardList.size()!=0 && idRedCardList.size()!=0) {
            if (generate.nextBoolean()) {
                // on tire une rouge
                int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
                cardImage.setImageResource(rdmId);
                nbCard.setText(getString(R.string.remain)+" " + Integer.toString(idBlackCardList.size()+idRedCardList.size())+" " + getString(R.string.cardPack));
                cardColor = "red";
            }
            else {
                // on tire une noir
                int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
                cardImage.setImageResource(rdmId);
                nbCard.setText(getString(R.string.remain)+" " + Integer.toString(idBlackCardList.size()+idRedCardList.size())+" " + getString(R.string.cardPack));
                cardColor = "black";

            }
        }
        // si rouge pleines et noir vide
        else if(idRedCardList.size()!=0 && idBlackCardList.size()==0){
            int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
            cardImage.setImageResource(rdmId);
            nbCard.setText(getString(R.string.remain)+" " + Integer.toString(idBlackCardList.size()+idRedCardList.size())+" " + getString(R.string.cardPack));
            cardColor = "red";
        }
        // si rouge vide et noir pleines
        else if(idRedCardList.size() ==0 && idBlackCardList.size()!=0){
            int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
            cardImage.setImageResource(rdmId);
            nbCard.setText(getString(R.string.remain)+" " + Integer.toString(idBlackCardList.size()+idRedCardList.size())+" " + getString(R.string.cardPack));
            cardColor = "black";
        }
        // si les 2 sont vides
        else if(idRedCardList.size()==0 && idBlackCardList.size()==0){
            nbCard.setText("Fin du fun");
        }
        return cardColor;
    }
    @Override
    public void onBackPressed() {
        Intent gameSelectionActivity = new Intent(getApplicationContext(), GameSelectionActivity.class);
        startActivity(gameSelectionActivity);
        finish();
    }
}