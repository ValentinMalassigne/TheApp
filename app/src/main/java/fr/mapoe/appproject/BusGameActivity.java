package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class BusGameActivity extends AppCompatActivity {
    private ImageView card1,card2,card3,card4,card5,packqueCard;
    private Button plusButton;
    private Button minusButton;
    private TextView text;
    private int currentCard=0;
    private ArrayList<ImageView> cardList; // list pour stocker les cartes restantes dans le paquet
    private ArrayList<ImageView> cardFold;
    private ImageView[] cardTurn;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_game);
        init();
        ImageView xButton = (ImageView) findViewById(R.id.x_button);
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
    }
    private void init(){
        this.card1=findViewById(R.id.card1);
        this.card2=findViewById(R.id.card2);
        this.card3=findViewById(R.id.card3);
        this.card4=findViewById(R.id.card4);
        this.card5=findViewById(R.id.card5);
        this.plusButton = findViewById(R.id.plus_button);
        this.minusButton = findViewById(R.id.minus_button);
        this.packqueCard = findViewById(R.id.cardBack);
        this.text = findViewById(R.id.text);
        cardList = new ArrayList<ImageView>();
        cardFold = new ArrayList<ImageView>();
        // ajout des cartes d'affichages dans le tableau
        cardTurn = new ImageView[]{card1,card2,card3,card4,card5};
        // remplir le tableau des cartes
        for(int i=1;i<53;i++){
            String cardName ="";
            // entre 1 et 13 ajoute les trèfles
            if(i<=13){
                int index = i;
                cardName = "c"+Integer.toString(index);
            }
            // entre 14 et 26 les piques
            else if(i>13 && i<=26){
                int index = i-13;
                cardName="s"+Integer.toString(index);
            }
            // entre 27 et 39 les carreaux
            else if(i>26 && i<=39){
                int index =i-26;
                cardName="d"+Integer.toString(index);
            }
            // entre 40 et 52 les coeurs
            else{
                int index = i-39;
                cardName="h"+Integer.toString(index);
            }
            // ajout dans le tableau
            int id = getResources().getIdentifier(cardName,"drawable",getPackageName());
            ImageView card = new ImageView(getApplicationContext());
            card.setImageResource(id);
            card.setId(i);
            cardList.add(card);
        }
        int time =300;
        // on affiches les 5 cartes sur l'écran
        Random generate = new Random(System.currentTimeMillis());
        Animation slideUpToDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_to_down);
        Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        slide_out_right.setDuration(500);
        // on supprime 5 cartes de cardList qu'on ajoute directement dans cardFold
        for(int i=0;i<cardTurn.length;i++){
            int rdmIndex = generate.nextInt(cardList.size()-1);
            cardFold.add(cardList.remove(rdmIndex));

        }
        //  afficher les 5 premières cartes avec animation
        for(int i=0;i<cardFold.size();i++) {
            Log.d("N° de la carte:", Integer.toString(cardFold.get(i).getId()));
            // animation sur la cardBack
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    slide_out_right.reset();
                    packqueCard.clearAnimation();
                    packqueCard.startAnimation(slide_out_right);
                }
            }, time);
            time+=500;
            int finalI = i;
            //animation sur les cartes
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Drawable drawable = cardFold.get(finalI).getDrawable();
                    int id = cardFold.get(finalI).getId();
                    // change les propriétés de la carte actuel dans cardTurn
                    cardTurn[finalI].setImageDrawable(drawable);
                    cardTurn[finalI].setId(id);

                    // lance l'animation
                    slideUpToDown.reset();
                    cardTurn[finalI].clearAnimation();
                    cardTurn[finalI].startAnimation(slideUpToDown);


                }
            }, time);
            time+=1000;

        }
        game();
    }
    private void game(){
        Random generate = new Random(System.currentTimeMillis());
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("carte acutelle",Integer.toString(currentCard));
                int rdmIndex = generate.nextInt(cardList.size()-1);
                int previousId = cardTurn[currentCard].getId();
                ImageView newCard = cardList.get(rdmIndex); // pour stocker la nouvelle carte
                newCard.setBackground(null);
                SetImageDynamically(cardTurn[currentCard],newCard.getDrawable());
                cardTurn[currentCard].setId(newCard.getId());
                cardFold.add(cardList.remove(rdmIndex)); // on retir la card de la liste des carte restante
                Boolean correct = CompareCard(previousId,newCard.getId(),"+");
                CheckCorrect(correct);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cardTurn[currentCard].setBackground(getDrawable(R.drawable.hightlight_corner_shape));
                }
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("carte acutelle",Integer.toString(currentCard));
                int rdmIndex = generate.nextInt(cardList.size()-1);
                int previousId = cardTurn[currentCard].getId();
                ImageView newCard = cardList.get(rdmIndex); // pour stocker la nouvelle carte
                newCard.setBackground(null);
                SetImageDynamically(cardTurn[currentCard],newCard.getDrawable());
                cardTurn[currentCard].setId(newCard.getId());
                cardFold.add(cardList.remove(rdmIndex)); // on retir la card de la liste des carte restante
                Boolean correct = CompareCard(previousId,newCard.getId(),"-");
                CheckCorrect(correct);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    cardTurn[currentCard].setBackground(getDrawable(R.drawable.hightlight_corner_shape));
                }
            }
        });

    }
    // pour comparer 2 cartes entres elles
    private Boolean CompareCard(int id1,int id2,String type){
        Boolean correct = true;
            //recuperer l'id des deux cartes
            id1 = reduceId(id1);
            id2 = reduceId(id2);
            Log.d("","id1= "+Integer.toString(id1)+" id2: "+Integer.toString(id2));
            if(id2>id1 && type.equals("+")){
                correct=true;
            }
            else if(id2<id1 && type.equals("+")){
                correct=false;
            }
            else if(id2>id1 && type.equals("-")){
                correct=false;
            }
            else if(id2<id1 && type.equals("-")){
                correct=true;
            }
            else if(id2==id1){
                correct=false;
            }
        return correct;
    }
    private void CheckCorrect(Boolean correct){
        // si le joueur à bon on passe à la carte suivante
        if(correct){
            currentCard++;
            if(currentCard==5){
                text.setText(getString(R.string.well_done_u_win));
                plusButton.setVisibility(View.GONE);
                minusButton.setVisibility(View.GONE);
            }
            else{
                text.setText(getString(R.string.temp_win_bus));
            }
        }
        else{
            currentCard=0;
            text.setText(getString(R.string.lost_game_bus));
        }
    }
    // fonction qui retourne un id réduit entre 1 et 13 suivant la carte
    private int reduceId(int id){
        //1er cas: La carte est un coeur
        if(id>39){
            id-=39;
        }
        // 2nd cas la carte est un carreau
        else if(id>26 && id<=39){
            id-=26;
        }
        // 3eme cas la carte est un pique
        else if(id>13 && id<=26){
            id-=13;
        }
        //4eme cas la carte est un trèfle
        else{}
        return id;
    }
    private void SetImageDynamically(ImageView image, Drawable drawable){

        int time=300;
        Animation slideUpToDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_to_down);
        Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slide_out_right.reset();
                packqueCard.clearAnimation();
                packqueCard.startAnimation(slide_out_right);
            }
        }, time);
        time+=500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image.setImageDrawable(drawable);
                slideUpToDown.reset();
                image.clearAnimation();
                image.startAnimation(slideUpToDown);
            }
        }, time);
    }
}