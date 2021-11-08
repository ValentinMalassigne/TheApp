package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class CardGameActivity extends AppCompatActivity {

    private ImageView cardImage;

    private ArrayList<Integer> idRedCardList;
    private ArrayList<Integer> idBlackCardList;
    private TextView nbCard;
    private TextView orText;

    {
        idRedCardList = new ArrayList<Integer>(Arrays.asList
                (R.drawable.h1, R.drawable.h2, R.drawable.h3, R.drawable.h4,
                        R.drawable.h5, R.drawable.h6, R.drawable.h7, R.drawable.h8,
                        R.drawable.h9, R.drawable.h10, R.drawable.h11, R.drawable.h12,
                        R.drawable.h13, R.drawable.d1, R.drawable.d2, R.drawable.d3,
                        R.drawable.d4, R.drawable.d5, R.drawable.d6, R.drawable.d7,
                        R.drawable.d8, R.drawable.d9, R.drawable.d10, R.drawable.d11,
                        R.drawable.d12, R.drawable.d13)
        );
    }
    {
        idBlackCardList = new ArrayList<Integer>(Arrays.asList
                (R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
                        R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9,
                        R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13,
                        R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4,
                        R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
                        R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12,
                        R.drawable.s13)
        );
    }
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_game);

        init();

        ImageButton xButton = (ImageButton) findViewById(R.id.x_button);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ouvre l'activity End game
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();

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
        Button blackButton = (Button) findViewById(R.id.black_button);
        Button redButton = (Button) findViewById(R.id.red_button);
        TextView cardColor = (TextView) findViewById(R.id.card_color_display);
        this.cardImage = (ImageView) findViewById(R.id.card_image);
        this.nbCard = (TextView) findViewById(R.id.nb_card);
        this.orText = (TextView) findViewById(R.id.or_text);
        ImageView nextArrow = (ImageView) findViewById(R.id.next_arrow);
        ImageView replayButton = (ImageView) findViewById(R.id.replay_button);

        Random generate = new Random(System.currentTimeMillis());
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomNumber = generate.nextInt(2);
                if (randomCard(randomNumber).equals("black"))
                    cardColor.setText(R.string.you_win);
                else {
                    cardColor.setText(R.string.you_lost);
                }
                blackButton.setVisibility(View.GONE);
                redButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);
                nextArrow.setVisibility(View.VISIBLE);
            }
        });

        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int randomNumber = generate.nextInt(2);
                if (randomCard(randomNumber).equals("red")) {
                    cardColor.setText(R.string.you_win);
                } else {
                    cardColor.setText(R.string.you_lost);
                }
                blackButton.setVisibility(View.GONE);
                redButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);
                nextArrow.setVisibility(View.VISIBLE);
                }
        });
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int nb = idBlackCardList.size() + idRedCardList.size() ;
                if (nb == -1) {
                } else {
                    nbCard.setText(Integer.toString(nb));
                }
                blackButton.setVisibility(View.VISIBLE);
                redButton.setVisibility(View.VISIBLE);
                orText.setVisibility(View.VISIBLE);
                nextArrow.setVisibility(View.GONE);
                cardColor.setText(R.string.what_is_next_card);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animation slideDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_to_down);
                    slideDown.reset();
                    cardImage.clearAnimation();
                    cardImage.startAnimation(slideDown);
                    cardImage.setImageDrawable(getDrawable(R.drawable.cardback));

                }
                if(nb==0){
                    replayButton.setVisibility(View.VISIBLE);
                    nextArrow.setVisibility(View.GONE);
                    cardColor.setText(R.string.what_is_next_card);
                    blackButton.setVisibility(View.GONE);
                    orText.setVisibility(View.GONE);
                    redButton.setVisibility(View.GONE);
                    cardColor.setText(getString(R.string.restart_button));
                }
            }
        });
        nextArrow.setOnTouchListener(new View.OnTouchListener() {
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
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                init();
                replayButton.setVisibility(View.GONE);
                blackButton.setVisibility(View.VISIBLE);
                redButton.setVisibility(View.VISIBLE);
                orText.setVisibility(View.VISIBLE);
                cardColor.setText(R.string.what_is_next_card);
                nbCard.setText("52");

            }
        });
        replayButton.setOnTouchListener(new View.OnTouchListener() {
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

    }

    private void init() {
        {
            idRedCardList = new ArrayList<Integer>(Arrays.asList
                    (R.drawable.h1, R.drawable.h2, R.drawable.h3, R.drawable.h4,
                            R.drawable.h5, R.drawable.h6, R.drawable.h7, R.drawable.h8,
                            R.drawable.h9, R.drawable.h10, R.drawable.h11, R.drawable.h12,
                            R.drawable.h13, R.drawable.d1, R.drawable.d2, R.drawable.d3,
                            R.drawable.d4, R.drawable.d5, R.drawable.d6, R.drawable.d7,
                            R.drawable.d8, R.drawable.d9, R.drawable.d10, R.drawable.d11,
                            R.drawable.d12, R.drawable.d13)
            );
        }
        {
            idBlackCardList = new ArrayList<Integer>(Arrays.asList
                    (R.drawable.c1, R.drawable.c2, R.drawable.c3, R.drawable.c4, R.drawable.c5,
                            R.drawable.c6, R.drawable.c7, R.drawable.c8, R.drawable.c9,
                            R.drawable.c10, R.drawable.c11, R.drawable.c12, R.drawable.c13,
                            R.drawable.s1, R.drawable.s2, R.drawable.s3, R.drawable.s4,
                            R.drawable.s5, R.drawable.s6, R.drawable.s7, R.drawable.s8,
                            R.drawable.s9, R.drawable.s10, R.drawable.s11, R.drawable.s12,
                            R.drawable.s13)
            );
        }
    }

    private String randomCard(int randomNumber){
        String cardColor = "";
        Random generate = new Random(System.currentTimeMillis());
        this.nbCard = (TextView) findViewById(R.id.nb_card);

        // si les 2 listes sont pleines
        if(idBlackCardList.size()!=0 && idRedCardList.size()!=0) {
            if (randomNumber==0) {
                // on tire une rouge
                int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
                SetImageSrc(rdmId);
                cardColor = "red";
            }
            else {
                // on tire une noir
                int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
                SetImageSrc(rdmId);
                cardColor = "black";

            }
        }
        // si rouge pleines et noir vide
        else if(idRedCardList.size()!=0 && idBlackCardList.size()==0){
            int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
            SetImageSrc(rdmId);
            cardColor = "red";
        }
        // si rouge vide et noir pleines
        else if(idRedCardList.size() ==0 && idBlackCardList.size()!=0){
            int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
            SetImageSrc(rdmId);
            cardColor = "black";
        }
        return cardColor;
    }
    private void SetImageSrc(int id){
        Animation slideLeft = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
        slideLeft.reset();
        cardImage.clearAnimation();
        cardImage.startAnimation(slideLeft);
        cardImage.setImageResource(id);
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}