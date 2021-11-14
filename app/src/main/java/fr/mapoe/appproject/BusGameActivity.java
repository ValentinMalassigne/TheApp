package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import fr.mapoe.appproject.tools.ThemeManager;

public class BusGameActivity extends AppCompatActivity {
    private ImageView cardBack;
    private ImageView plusButton;
    private ImageView minusButton;
    private TextView text,cardRemain;
    private int currentCard=0;
    private ArrayList<ImageView> cardList; // list pour stocker les cartes restantes dans le paquet
    private ArrayList<ImageView> cardFold;
    private ImageView[] cardTurn;
    private int nbCard;
    private LinearLayout.LayoutParams normalParams;
    private LinearLayout.LayoutParams zoomParams;
    private HorizontalScrollView horizontalScrollView;
    private SharedPreferences blockPopup;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //on change la langue
        getLanguage();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_game);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            nbCard = extras.getInt("nbCard");
            Log.d("nbCard:",Integer.toString(nbCard));
        }

        //on vérifie si l'utilisateur à bloqué la popup
        String alcohol_reminder_popup_blocked = "";
        blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        if (blockPopup.contains("block_apebus_tutorial_popup")) {
            alcohol_reminder_popup_blocked = blockPopup.getString("block_apebus_tutorial_popup", "");
        }
        if (!alcohol_reminder_popup_blocked.equals("blocked"))
            showInfoDialog(R.layout.info_popup,1);
        else
            init();

        ImageView xButton = (ImageView) findViewById(R.id.x_button);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ouvre l'activity End game
                Intent mainActivity = new Intent(getApplicationContext(), SplashScreenActivity.class);
                finish();
                mainActivity.putExtra("apebus restart", false);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
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
        ImageView infoButton = (ImageView) findViewById(R.id.info_button);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLanguage();
                showInfoDialog(R.layout.info_popup,0);
            }
        });
        infoButton.setOnTouchListener(new View.OnTouchListener() {
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
    @SuppressLint("ClickableViewAccessibility")
    private void init(){
        float scale = getResources().getDisplayMetrics().density;
        // paramètre des cartes
        int normalWidth = (int) (107 * scale);
        int normalHeight = (int) (150 * scale);
        int zoomWidth = (int) (128 * scale);
        int zoomHeight = (int) (180 * scale);
        int margin = (int) (15 * scale);
        normalParams = new LinearLayout.LayoutParams(normalWidth,normalHeight);
        normalParams.leftMargin = margin;
        normalParams.rightMargin = margin;
        zoomParams = new LinearLayout.LayoutParams(zoomWidth,zoomHeight);
        zoomParams.leftMargin = margin;
        normalParams.rightMargin = margin;

        cardTurn = new ImageView[nbCard]; // tableau qui stock les cartes
        // créer les cartes:
        LinearLayout cardLayout = findViewById(R.id.cardLayout);
        for(int i=0;i<nbCard;i++){
            ImageView card = new ImageView(getApplicationContext());
            card.setLayoutParams(normalParams);
            cardLayout.addView(card);
            cardTurn[i] = card;
        }

        this.plusButton = findViewById(R.id.plus_button);
        this.minusButton = findViewById(R.id.minus_button);
        this.horizontalScrollView = findViewById(R.id.horizontalScrollView);
        plusButton.setOnTouchListener(new View.OnTouchListener() {
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
        minusButton.setOnTouchListener(new View.OnTouchListener() {
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
        this.cardBack = findViewById(R.id.cardBack);
        this.text = findViewById(R.id.text);
        this.cardRemain = findViewById(R.id.card_remain);
        cardList = new ArrayList<ImageView>();
        cardFold = new ArrayList<ImageView>();


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
        cardRemain.setText(Integer.toString(cardList.size()));
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
            // animation sur la cardBack
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    slide_out_right.reset();
                    cardBack.clearAnimation();
                    cardBack.startAnimation(slide_out_right);
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
                    int nb = Integer.valueOf(cardRemain.getText().toString())-1;
                    cardRemain.setText(Integer.toString(nb));

                }
            }, time);
            time+=1000;

        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                cardTurn[0].setLayoutParams(zoomParams);
                plusButton.setVisibility(View.VISIBLE);
                minusButton.setVisibility(View.VISIBLE);
                Log.d("carte",Integer.toString(cardList.size()));
            }
        },time);
        OnClickButton();
    }
    private void OnClickButton(){
        Random generate = new Random(System.currentTimeMillis());
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rdmIndex=0;
                if(cardList.size()!=1) {
                    rdmIndex = generate.nextInt(cardList.size() - 1);
                }
                int previousId = cardTurn[currentCard].getId();
                int previousIndex = currentCard;
                ImageView newCard = cardList.get(rdmIndex); // pour stocker la nouvelle carte
                cardTurn[currentCard].setId(newCard.getId());
                cardFold.add(cardList.remove(rdmIndex)); // on retir la card de la liste des carte restante
                Boolean correct = CompareCard(previousId,newCard.getId(),"+");
                // fonction qui regarde si la répons est correct et qui renvoie un string contenant le message à afficher
                String message = CheckCorrect(correct);
                if(currentCard!=nbCard){
                    SetImageDynamically(cardTurn[previousIndex], newCard.getDrawable(), message);
                }
                else{
                    text.setText(message);
                }
                ResetOnclick();
            }
        });

        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int rdmIndex=0;
                if(cardList.size()!=1) {
                    rdmIndex = generate.nextInt(cardList.size() - 1);
                }
                int previousId = cardTurn[currentCard].getId();
                int previousIndex = currentCard;
                ImageView newCard = cardList.get(rdmIndex); // pour stocker la nouvelle carte
                cardTurn[currentCard].setId(newCard.getId());
                cardFold.add(cardList.remove(rdmIndex)); // on retir la card de la liste des carte restante
                Boolean correct = CompareCard(previousId,newCard.getId(),"-");
                // fonction qui regarde si la répons est correct et qui renvoie un string contenant le message à afficher
                String message = CheckCorrect(correct);
                if(currentCard!=nbCard){
                    SetImageDynamically(cardTurn[previousIndex], newCard.getDrawable(), message);
                }
                else{
                    text.setText(message);
                }
                ResetOnclick();
            }
        });
    }
    // fonction qui réinitialise le onclick pour éviter que le mec spam pendant l'animation
    private void ResetOnclick(){
        plusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        minusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    // pour comparer 2 cartes entres elles
    private Boolean CompareCard(int id1,int id2,String type){
        Boolean correct = true;
        //recuperer l'id des deux cartes
        id1 = reduceId(id1);
        id2 = reduceId(id2);

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
    private String CheckCorrect(Boolean correct){
        int nb = currentCard + 1;
        String message = "";
        // si le joueur à bon on passe à la carte suivante
        Log.d("Index",Integer.toString(currentCard));
        if(cardList.size()!=0) {

            if (correct) {
                if (currentCard != nbCard - 1) {
                    currentCard++;
                    message = getString(R.string.temp_win_bus);
                } else {
                    message = getString(R.string.well_done_u_win);
                    restart();
                }
            }
            // perdu on recommence à0
            else {
                currentCard = 0;
                message = getString(R.string.lost_game_bus);
                message = message.replace("µ",Integer.toString(nb));

            }
        }
        else{
            message=getString(R.string.loose);
            restart();
        }
        return message;
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

    // recommencer
    private void restart(){
        plusButton.setVisibility(View.INVISIBLE);
        minusButton.setVisibility(View.INVISIBLE);
        Button restart = findViewById(R.id.restart_button);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        ThemeManager themeManager = new ThemeManager(this,sharedPreferences.getString("theme",""));
        restart.setBackground(themeManager.getButtonDrawable());
        cardBack.setVisibility(View.GONE);
        cardRemain.setVisibility(View.GONE);
        restart.setVisibility(View.VISIBLE);
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                mainActivity.putExtra("apebus restart", true);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            }
        });
    }

    /**
     *
     * @param image à afficher dynamiquement
     * @param drawable
     * @param message du text à changer à la fin de l'animation
     */
    private void SetImageDynamically(ImageView image, Drawable drawable, String message){

        int time=300;
        Animation slideUpToDown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_up_to_down);
        Animation slide_out_right = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.slide_out_right);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                slide_out_right.reset();
                cardBack.clearAnimation();
                cardBack.startAnimation(slide_out_right);
                text.setText("");
                cardRemain.setText(Integer.toString(cardList.size()));
            }
        }, time);
        time+=500;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                image.setImageDrawable(drawable);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    image.setBackground(getDrawable(R.drawable.corner_shape));
                }
                slideUpToDown.reset();
                image.clearAnimation();
                image.startAnimation(slideUpToDown);
            }
        }, time);
        time+=1000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SetCurrentCardEffect();
                text.setText(message);
                Scroll();
                OnClickButton();
            }
        },time);

    }

    /**
     * permet de zoomer sur la carte actuelle
     */
    private void SetCurrentCardEffect(){
        for(int i=0;i<cardTurn.length;i++){
            cardTurn[i].setLayoutParams(normalParams);
        }
        cardTurn[currentCard].setLayoutParams(zoomParams);
    }

    // popup des règles
    private void showInfoDialog(int layout, int typeOfCall){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(BusGameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        Button okButton = layoutView.findViewById(R.id.ok_button);
        CheckBox blockPopupCheckBox = layoutView.findViewById(R.id.block_popup_checkBox);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        ImageView nextButton = layoutView.findViewById(R.id.right_popup_arrow);
        ImageView leftButton = layoutView.findViewById(R.id.left_popup_arrow);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        leftButton.setVisibility(View.GONE);
        imageInfo.setImageResource(R.drawable.check);
        imageInfo.setVisibility(View.GONE);
        okButton.setVisibility(View.GONE);
        textInfo.setText(R.string.ape_bus_rules);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                if(typeOfCall==1)
                    init();
            }
        });
        alertDialog.show();
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getLanguage();
                textInfo.setText(R.string.ape_bus_rules2);
                nextButton.setVisibility(View.GONE);
                imageInfo.setVisibility(View.VISIBLE);
            }
        });
        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                if (typeOfCall == 1){
                    init();
                }
            }
        });
        blockPopupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                //ici on enregistre dans les SharedPreferences si l'utilisateur bloque la popup
                blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = blockPopup.edit();
                if (checked) {
                    editor.putString("block_apebus_tutorial_popup", "blocked");
                } else {
                    editor.putString("block_apebus_tutorial_popup", "activated");
                }
                editor.apply();
            }
        });
    }

    // fonction pour scroll correctement
    private void Scroll(){
        // retour à 0
        if(currentCard == 0){
            horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        }
        else if (currentCard == 4){
            horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
        }
    }

    private void getLanguage(){
        //on charge le langage enregistrée dans les shared preferences
        SharedPreferences language = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        String choseLanguage = language.getString("language","");
        Log.d(TAG, "getLanguage: chose: "+choseLanguage);
        //on récup la langue acctuelement utilisé par l'appli
        Configuration conf = getResources().getConfiguration();
        String localLanguage = conf.locale.getLanguage();
        Log.d(TAG, "getLanguage: local: "+choseLanguage);
        //on vérifie si la langue actuelle et la langue enregistré par l'utilisateur sont la même (pour éviter de changer en boucle la langue)
        if(!choseLanguage.equals("") & !localLanguage.equals(choseLanguage)){
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
    }

    @Override
    public void onBackPressed() {

    }
}