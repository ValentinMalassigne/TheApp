package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import fr.mapoe.appproject.sqlite.DataBaseManager;
import fr.mapoe.appproject.tools.ThemeManager;

public class GameActivity extends AppCompatActivity {

    private String[] playerTab, alcoholTab, scoreTab;
    private ArrayList<String> challengeList = new ArrayList<String>();
    private ArrayList<String[]> customSentencesList = new ArrayList<String[]>();
    private ArrayList<String[]> miniGamesList = new ArrayList<String[]>();
    private ArrayList<String[]> sentenceList = new ArrayList<String[]>();
    private ArrayList<String> playerTurn = new ArrayList<String>();
    private ArrayList<String[]> gagesList = new ArrayList<String[]>();
    private ArrayList<String> savedSentenceList = null;
    private String[] currentChallenge = new String[8]; //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition
    private String currentPlayer = "";
    private ConstraintLayout gameLayout;
    private Activity activity;
    private int displayCounter = 0;
    private LinearLayout mainLayout;
    private LinearLayout cardBodyLayout;
    private LinearLayout footerCardLayout;
    private ImageView cardImage;
    private final ArrayList<Integer> idRedCardList, idBlackCardList;
    private TextView playerCardTurn;
    private int typeOfGame = 0;
    private Button answerButton,rightButtonGame,leftButtonGame;
    private ImageView skipButton;
    private String secondPlayer;
    private String thirdPlayer;
    private DataBaseManager dataBaseManager;
    boolean restart = false;
    private Drawable buttonDrawable;

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
        setContentView(R.layout.activity_game);
        this.activity = this;
        dataBaseManager = new DataBaseManager();
        savedSentenceList = new ArrayList<String>();
        ConstraintLayout gameLayout = (ConstraintLayout) findViewById(R.id.game_layout);
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        // recuperer les données
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerTab = extras.getStringArray("playerTab");
            alcoholTab = extras.getStringArray("alcoholTab");
            typeOfGame = extras.getInt("typeOfGame");
            restart = extras.getBoolean("restart");
            if (restart) {
                savedSentenceList = extras.getStringArrayList("savedList");
            }
        }
        String color;
        if(typeOfGame == 1){
            color = sharedPreferences.getString("theme","");
        }
        else{
            color ="apepiment";
        }
        ThemeManager themeManager = new ThemeManager(this,color);
        this.buttonDrawable = themeManager.getButtonDrawable();
        gameLayout.setBackground(themeManager.getBackgroundDrawable());

        // initialisation du tableau scoreTab et le remplir
        scoreTab = new String[playerTab.length];
        Arrays.fill(scoreTab, "0");
        answerButton = (Button) findViewById(R.id.answer_button);
        answerButton.setBackground(buttonDrawable);
        rightButtonGame = findViewById(R.id.right_button);
        leftButtonGame = findViewById(R.id.left_button);
        rightButtonGame.setBackground(buttonDrawable);
        leftButtonGame.setBackground(buttonDrawable);
        //setUp des list
        try {
            setUpList();
            newDisplay(gameLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Generer la popup du score
        ImageButton scoreButton = (ImageButton) findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showScorePopup(R.layout.score_popup);
            }
        });
        scoreButton.setOnTouchListener(new View.OnTouchListener() {
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

        // Generer la popup réponse

        if (typeOfGame == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerButton.setBackground(getDrawable(R.drawable.button_apepiment));
            }
        }
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int typeOfCall = 1;
                if (currentChallenge[5].equals("skip")) {
                    typeOfCall = 2;
                }
                showAnswerPopup(R.layout.game_answer_popup, currentChallenge[1], typeOfCall,false);
            }
        });
        skipButton = (ImageView) findViewById(R.id.skip_button);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerPopup(R.layout.game_answer_popup, currentChallenge[1], 3,false);
            }
        });
        skipButton.setOnTouchListener(new View.OnTouchListener() {
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

        ImageButton xButton = (ImageButton) findViewById(R.id.x_button);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ouvre l'activity End game
                Intent gameEndActivity = new Intent(getApplicationContext(), GameEndActivity.class);
                gameEndActivity.putExtra("playerTab", playerTab);
                gameEndActivity.putExtra("alcoholTab", alcoholTab);
                gameEndActivity.putExtra("typeOfGame", typeOfGame);
                gameEndActivity.putExtra("scoreTab", scoreTab);
                gameEndActivity.putExtra("savedList", savedSentenceList);
                startActivity(gameEndActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
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
        {
            //on vérifie si l'utilisateur à bloqué la popup
            String alcohol_reminder_popup_blocked = "";
            if (sharedPreferences.contains("block_apechill_tutorial_popup")) {
                alcohol_reminder_popup_blocked = sharedPreferences.getString("block_apechill_tutorial_popup", "");
            }
            if (!alcohol_reminder_popup_blocked.equals("blocked"))
                showInfoDialog(R.layout.info_popup);

        }
    }

    private void showInfoDialog(int layout) {// créer la popup info
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button okButton = layoutView.findViewById(R.id.ok_button);
        okButton.setBackground(buttonDrawable);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        ImageView nextButton = layoutView.findViewById(R.id.right_popup_arrow);
        nextButton.setVisibility(View.GONE);
        ImageView leftButton = layoutView.findViewById(R.id.left_popup_arrow);
        leftButton.setVisibility(View.GONE);
        textInfo.setText(getString(R.string.game_description));
        imageInfo.setImageResource(R.drawable.skip_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        // chnagement du bg button on fonction du mode de jeu
        if (typeOfGame == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                okButton.setBackground(getDrawable(R.drawable.button_apepiment));
            }
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        CheckBox blockPopupCheckBox = layoutView.findViewById(R.id.block_popup_checkBox);
        blockPopupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked = ((CheckBox) view).isChecked();
                //ici on enregistre dans les SharedPreferences si l'utilisateur bloque la popup
                SharedPreferences blockPopup;
                blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = blockPopup.edit();
                if (checked) {
                    editor.putString("block_apechill_tutorial_popup", "blocked");
                } else {
                    editor.putString("block_apechill_tutorial_popup", "activated");
                }
                editor.apply();
            }
        });
    }

    /**
     *
     * @param layout
     * @param text
     * @param typeOfCall
     * @param win -> pour typeOfCall = 4
     * typeOfCall = 1 -> appel avec answer et text dans les bouttons
     * typeOfCall = 2 -> pas de réponse et pas de texte dans les boutons
     * typeOfCall = 3 -> le joueur passe donc il boit
     * typeOfCall = 4 -> Cas ou y a pas de réponse mais des boutons qui sont dans le gameLayout
     *                   donc on affiche soit la punition soit l'ajout de point
     */
    private void showAnswerPopup(int layout, String text, int typeOfCall, Boolean win) {
        Log.d("typeOfCall",Integer.toString(typeOfCall));
        final boolean[] rightAnswer = {false};
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        // déclarer les éléments de la popup
        TextView textDisplay = (TextView) layoutView.findViewById(R.id.text_display);
        Button yesButton = (Button) layoutView.findViewById(R.id.left_button);
        Button noButton = (Button) layoutView.findViewById(R.id.right_button);
        Button nextButton = (Button) layoutView.findViewById(R.id.next_button);

        yesButton.setText(currentChallenge[5]);//la première rep
        noButton.setText(currentChallenge[6]);//la deuxième rep est la rightAnswer



        // change bg suivant le jeu
        if (typeOfGame == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                yesButton.setBackground(getDrawable(R.drawable.button_apepiment));
                noButton.setBackground(getDrawable(R.drawable.button_apepiment));
                nextButton.setBackground(getDrawable(R.drawable.button_apepiment));
            }
        }
        else{
            yesButton.setBackground(buttonDrawable);
            noButton.setBackground(buttonDrawable);
            nextButton.setBackground(buttonDrawable);
        }

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // appel normal y a une réponse
        if (typeOfCall == 1) { // appel normal par réponse
            textDisplay.setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY));
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentChallenge[4].equals("left")) {//si c'est + et que le joueur répond oui alors il a gagné
                        String temp = getRandomWinSentence();
                        textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
                        noButton.setVisibility(View.GONE);
                        yesButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                        // ajout au score
                        rightAnswer[0] = true;
                    } else { //sinon il a perdu
                        String temp = getPunition(0);
                        textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
                        yesButton.setVisibility(View.GONE);
                        noButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                    }
                }
            });
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (currentChallenge[4].equals("right")) {//si c'est - et que le joueur répond non alors il a gagné
                        String temp = getRandomWinSentence();
                        textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
                        noButton.setVisibility(View.GONE);
                        yesButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                        // ajout au score
                        rightAnswer[0] = true;
                    } else { //sinon il a perdu
                        String temp = getPunition(0);
                        textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
                        yesButton.setVisibility(View.GONE);
                        noButton.setVisibility(View.GONE);
                        nextButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
        // appel sans réponse
        else if (typeOfCall == 2) {
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
            nextButton.setText("ok");
            // affiche le score marqué
            String temp = getRandomWinSentence();
            textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
            rightAnswer[0] = true;
        }
        // appel par skip
        else if (typeOfCall == 3){ // appel par skip
            String temp = getPunition(1);
            textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
        }
        // on affiche soit la punition soit les points
        else{
            yesButton.setVisibility(View.GONE);
            noButton.setVisibility(View.GONE);
            nextButton.setVisibility(View.VISIBLE);
            if(win){
                // affiche le score marqué
                String temp = getRandomWinSentence();
                textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
                rightAnswer[0] = true;
            }
            else{
                String temp = getPunition(0);
                textDisplay.setText(HtmlCompat.fromHtml(temp, HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        }
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // ajout au score (s'il a bien répondu)
                    if (rightAnswer[0]) {
                        for (int i = 0; i < scoreTab.length; i++) {
                            if (playerTab[i].equals(currentPlayer)) {

                                int scoreInt = Integer.parseInt(currentChallenge[0]);
                                int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                                int newScore = scoreInt + currentPlayerScore;
                                scoreTab[i] = Integer.toString(newScore);
                            }
                        }
                    }
                    newDisplay(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showScorePopup(int layout) {
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
        rowParams.setMargins(0, 10, 0, 0);

        int id = 1;
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        // déclarer les élements de la popup
        TableLayout tableLayout = (TableLayout) layoutView.findViewById(R.id.idTable);
        ImageButton xButton = (ImageButton) layoutView.findViewById(R.id.x_popup_button);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // ajout de la 1er ligne
        TableRow topRow = new TableRow(getApplicationContext());
        topRow.setId(id);
        topRow.setLayoutParams(rowParams);
        tableLayout.addView(topRow);

        TextView name = new TextView(getApplicationContext());
        name.setText(R.string.name);
        name.setTypeface(typeface, Typeface.BOLD);
        name.setTextSize(25);
        name.setTextColor(Color.WHITE);
        topRow.addView(name);

        TextView score = new TextView(getApplicationContext());
        score.setText(R.string.score);
        score.setTypeface(typeface, Typeface.BOLD);
        score.setTextSize(25);
        score.setTextColor(Color.WHITE);
        score.setGravity(Gravity.CENTER);
        topRow.addView(score);
        id++;
        // ajout des lignes 1 par 1
        for (int i = 0; i < scoreTab.length; i++) {
            TableRow row = new TableRow(getApplicationContext());
            row.setId(id);
            row.setLayoutParams(rowParams);
            tableLayout.addView(row);

            TextView playerName = new TextView(getApplicationContext());
            playerName.setText(playerTab[i]);
            playerName.setTypeface(typeface);
            playerName.setTextSize(25);
            playerName.setTextColor(Color.WHITE);
            row.addView(playerName);

            TextView scorePlayer = new TextView(getApplicationContext());
            scorePlayer.setText(scoreTab[i]);
            scorePlayer.setTypeface(typeface);
            scorePlayer.setTextSize(25);
            scorePlayer.setTextColor(Color.WHITE);
            scorePlayer.setGravity(Gravity.CENTER);
            row.addView(scorePlayer);
        }
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
    }

    private void setUpList() throws IOException {
        String[] tempSentence = new String[9];
        //on récup la langue acctuelement utilisé par l'appli
        String language = getResources().getConfiguration().locale.getLanguage();

        if (language.equals("fr")) {
            language="FR";
        } else {
            language="EN";
        }

        int numberOfSentences = dataBaseManager.getNumberOfSentences(language,getApplicationContext());

        for(int i=1;i<=numberOfSentences;i++) {
            tempSentence=dataBaseManager.getSentenceFromDB(language,i,getApplicationContext());
            if((typeOfGame==1 && tempSentence[8].equals("ApeChill")) || (typeOfGame==2 && tempSentence[8].equals("ApePiment"))){
                if (tempSentence[3].equals("gages")) {
                    gagesList.add(tempSentence);
                } else if (tempSentence[3].equals("minigames")) {
                    miniGamesList.add(tempSentence);
                } else if (tempSentence[3].equals("questions")) {
                    sentenceList.add(tempSentence);
                } else if (tempSentence[3].equals("custom")) {
                    customSentencesList.add(tempSentence);
                }
            }
        }
        //ajout des minijeux custom à la liste des mini-jeux possible :
        tempSentence=new String[9];
        tempSentence[0]="1";
        tempSentence[3]="minigames";
        tempSentence[2]="Spinning Wheel";
        for(int i=0;i<3;i++){
            miniGamesList.add(tempSentence);
        }
        tempSentence[2]="Red or Black";
        for(int i=0;i<3;i++){
            miniGamesList.add(tempSentence);
        }

        // on vérifie si la list savedSentenceList est rempli
        if (savedSentenceList != null) {
            RemoveSentenceFromList();
        }
    }

    private void RemoveSentenceFromList() {
        // on parcours savedSentenceList
        for (int j = 0; j < savedSentenceList.size(); j++) {
            // on parcours toutes les listes et on suprimes les phrases présentent dans savedSentenceList
            for (int i = 0; i < gagesList.size(); i++) {
                if (gagesList.get(i)[2].equals(savedSentenceList.get(j))) {
                    gagesList.remove(i);
                }
            }
            for (int i = 0; i < miniGamesList.size(); i++) {
                if (miniGamesList.get(i)[2].equals(savedSentenceList.get(j))
                        && !miniGamesList.get(i)[2].equals("Spinning Wheel") && !miniGamesList.get(i)[2].equals("Red or Black")) {
                    miniGamesList.remove(i);
                }
            }
            for (int i = 0; i < sentenceList.size(); i++) {
                if (sentenceList.get(i)[2].equals(savedSentenceList.get(j))) {
                    sentenceList.remove(i);
                }
            }
            for (int i = 0; i < customSentencesList.size(); i++) {
                if (customSentencesList.get(i)[2].equals(savedSentenceList.get(j))) {
                    customSentencesList.remove(i);
                }
            }
        }
        savedSentenceList=new ArrayList<String>();
    }

    private void newDisplay(View view) throws IOException {
        displayCounter++;
        getPlayerTurn();
        // on réinitialise pour éviter que ça face de la merde
        secondPlayer = "";
        thirdPlayer= "";
        skipButton = findViewById(R.id.skip_button);
        skipButton.setVisibility(View.INVISIBLE);

        LinearLayout buttonLayout = findViewById(R.id.button_layout);
        buttonLayout.setVisibility(View.GONE);
        answerButton.setVisibility(View.VISIBLE);


        if (GetNextInformations()) {//on vérifie si on continue a construire la page avec les textView
            String type = currentChallenge[3];

            TextView currentTitleDisplay = (TextView) findViewById(R.id.current_title_display);

            currentTitleDisplay.setText(type);
            // appel la méthode pour afficher le texte
            SetDynamicText();

            TextView helpText = findViewById(R.id.help_text);
            if (currentChallenge[5].equals("skip")) {
                answerButton.setText(R.string.answerd_button);
                helpText.setVisibility(View.VISIBLE);
            }
            else if(currentChallenge[1].equals("")){
                answerButton.setVisibility(View.GONE);
                buttonLayout.setVisibility(View.VISIBLE);
                leftButtonGame.setText(currentChallenge[5]);
                rightButtonGame.setText(currentChallenge[6]);
                // le left right est inversé mais bon osef, bah non en fait
                rightButtonGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean win;
                        if(currentChallenge[4].equals("right")){
                            win = false;
                        }
                        else{
                            win = true;
                        }
                        showAnswerPopup(R.layout.game_answer_popup,"",4,win);
                    }
                });
                leftButtonGame.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean win;
                        if(currentChallenge[4].equals("left")){
                            win = false;
                        }
                        else{
                            win = true;
                        }
                        showAnswerPopup(R.layout.game_answer_popup,"",4,win);
                    }
                });
            }
            else {
                answerButton.setText(getString(R.string.show_answer));
                helpText.setVisibility(View.GONE);
            }

        }
    }

    private boolean GetNextInformations(){
        //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame
        String type = getChallengeTurn();
        String[] tempSentenceTab =GetNextChallenge(type);
        boolean result = false; //il sert a savoir si on a une page classique ou un mini jeu custom

        if(tempSentenceTab[2].equals("Spinning Wheel")){
            setUpWheel();
        }else if(tempSentenceTab[2].equals("Red or Black")) {
            startCardGame();
        }else{
            currentChallenge[0]=tempSentenceTab[0];
            currentChallenge[1]=SetNamesInAnswer(tempSentenceTab[1]);
            currentChallenge[2]=SetNameInSentence(tempSentenceTab[2]);
            currentChallenge[3]=tempSentenceTab[3];
            currentChallenge[4]=tempSentenceTab[4];
            String[] btnText = SetNameInButton(tempSentenceTab[5],tempSentenceTab[6]); // 0: btn1 1:btn2
            currentChallenge[5]=btnText[0];
            currentChallenge[6]=btnText[1];
            currentChallenge[7]=tempSentenceTab[7];
            result =true;
        }
        return result;
    }

    private String getPunition(int typeOfPunition){
        StringBuilder res = new StringBuilder();
        String rawPunition="";
        if(typeOfPunition==0){
            rawPunition=currentChallenge[7];
        }else{
            rawPunition=getString(R.string.default_custom_sentence_punition);
            rawPunition=rawPunition.substring(1);//on retire le premier charactère
        }
        String alcoholQuantity="";
        int i=0;
        while(!playerTab[i].equals(currentPlayer)){
            i++;
        }
        switch (alcoholTab[i]){
            case "drink0":
                alcoholQuantity=getString(R.string.drink0_sips);
                break;
            case "drink1":
                alcoholQuantity=getString(R.string.drink1_sips);
                break;
            case "drink2":
                alcoholQuantity=getString(R.string.drink2_sips);
                break;
            case "drink3":
                alcoholQuantity=getString(R.string.drink3_sips);
                break;
            default:
        }

        for(i=0;i<rawPunition.length();i++){
            if(rawPunition.substring(i,i+1).equals("§")){
                res.append("<b>").append(currentPlayer).append("</b>");
            }else if(rawPunition.substring(i,i+1).equals("µ")){
                res.append(alcoholQuantity);
            }else{
                res.append(rawPunition.substring(i,i+1));
            }
        }

        return res.toString();
    }

    private String SetNamesInAnswer(String sentence){
        StringBuilder res= new StringBuilder();
        String Nom="";
        boolean temp = false;
        //on lit la phrase caractères par caractères
        for(int i=0;i<sentence.length();i++){
            if(sentence.substring(i,i+1).equals("§")){
                //obtention des noms a mettre dans la phrase
                if(Nom.equals("")) {
                    //nom du joueur principal
                    Nom=currentPlayer;
                    res.append("<b>").append(Nom).append("</b>");
                }else{
                    res.append("<b>").append(secondPlayer).append("</b>");
                }
            }else{
                //si on lit un caractère "normal" de la phrase on le copie cole sans modification
                res.append(sentence.charAt(i));
            }
        }
        return res.toString();
    }

    private String[] SetNameInButton(String button1,String button2){
        String btn1=button1;
        String btn2=button2;
        boolean nameInBtn1 = button1.substring(0,button1.length()).equals("§");
        boolean nameInBtn2 = button2.substring(0,button2.length()).equals("§");
        // si nom dans boutton 1 mais pas boutton 2
        if(nameInBtn1 &!nameInBtn2){
            btn1 = currentPlayer;
        }
        // inverse
        else if(!nameInBtn1 & nameInBtn2){
            btn2=currentPlayer;
        }
        // si un nom dans chaque boutton
        else if(nameInBtn1 & nameInBtn2){
            btn1 = currentPlayer;
            btn2 = secondPlayer;
        }
        return new String[]{btn1, btn2};
    }

    private int numberOfOccurrences(String source,String test) {
        int occurrences = 0;

        if (source.contains(test)) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace(test, "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / test.length();
        }

        return occurrences;
    }

    private String SetNameInSentence(String text) {
        String res = "";
        int nbPlayer = numberOfOccurrences(text,"§");
        String str=nbPlayer+"";
        if (numberOfOccurrences(text,"§") == 1) {
            res = text.replaceFirst("§", currentPlayer);

        }
        else if (numberOfOccurrences(text,"§") == 2) {
            secondPlayer=getRandomPlayer(currentPlayer);
            res = text.replaceFirst("§", currentPlayer);
            res=res.replaceFirst("§",secondPlayer);
        }
        else{
            secondPlayer = getRandomPlayer(currentPlayer);
            thirdPlayer = getRandomPlayer(currentPlayer);
            res = text.replaceFirst("§", currentPlayer);
            res=res.replace("§",thirdPlayer);
            res = res.replaceFirst(thirdPlayer,secondPlayer);
        }
        return res;
    }

    private String[] GetNextChallenge(String type) {
        String[] res=new String[9];

        if(type.equals("gage")){
            res=GetRandomGage();
        }
        if(type.equals("miniGame")){
            res=GetRandomMiniGame();
        }
        if (type.equals("sentence")){
            res=GetRandomSentence();
        }
        if (type.equals("custom")){
            res=GetRandomCustomSentence();
        }
        return res;
    }

    // pour chaque méthode on ajoutera la phrase à la liste contenant toutes les phrases déjà tombé
    private String[] GetRandomCustomSentence() {
        int min=0;
        int max = customSentencesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);
        String[] res=customSentencesList.remove(number);
        savedSentenceList.add(res[2]);//on rajoute la deuxième case qui correspond à la deuxième
        return res;
    }

    private String[] GetRandomGage() {

        int min=0;
        int max = gagesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);
        String[] res=gagesList.remove(number);
        savedSentenceList.add(res[2]);//on rajoute la deuxième case qui correspond à la deuxième
        return res;
    }

    private String[] GetRandomMiniGame() {
        int min=0;
        int max = miniGamesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);
        String[] res= miniGamesList.remove(number);
        savedSentenceList.add(res[2]);//on rajoute la deuxième case qui correspond à la deuxième
        return res;
    }

    private String[] GetRandomSentence() {
        int min=0;
        int max = sentenceList.size()-1;
        Log.d(TAG, "GetRandomSentence: "+max);
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);
        String[] res=sentenceList.remove(number);
        savedSentenceList.add(res[2]);//on rajoute la deuxième case qui correspond à la deuxième
        return res;
    }

    private void getPlayerTurn(){

        if(!checkGameEnd()) {
            if (playerTurn.size()==0){
                for (int i=0;i<playerTab.length;i++){
                    playerTurn.add(playerTab[i]);
                }

            }
            int min=0;
            int max = playerTurn.size()-1;
            Random r = new Random();
            int number = r.nextInt((max - min) + 1) + min;
            currentPlayer = playerTurn.remove(number);
        }
        else{
            // ouvre l'activity End game
            Intent gameEndActivity = new Intent(getApplicationContext(), GameEndActivity.class);
            gameEndActivity.putExtra("playerTab", playerTab);
            gameEndActivity.putExtra("scoreTab", scoreTab);
            gameEndActivity.putExtra("alcoholTab", alcoholTab);
            gameEndActivity.putExtra("typeOfGame", typeOfGame);
            gameEndActivity.putExtra("savedList",savedSentenceList);
            startActivity(gameEndActivity);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
    }

    private String getChallengeTurn(){
        //remplir la list
        String res;
        if (challengeList.size()==0){
            challengeList.add("gage");
            challengeList.add("gage");
            challengeList.add("gage");
            challengeList.add("miniGame");
            challengeList.add("miniGame");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("sentence");

            //on vérifie si des phrases custom sont disponibles
            if (!customSentencesList.isEmpty()){
                challengeList.set(9,"custom");//on remple le 5eme element (une sentence) par custom
            }
        }
        if(displayCounter==1){ //si on est au premier tour, on prend forcément entre une sentence et une anecdote
            int min=4;
            int max = challengeList.size()-1;
            Random r = new Random();
            int number = r.nextInt((max - min) + 1) + min;
            res = challengeList.remove(number);
        }else{
            int min=0;
            int max = challengeList.size()-1;
            Random r = new Random();
            int number = r.nextInt((max - min) + 1) + min;
            res = challengeList.remove(number);
        }

        return res;
    }

    private String getRandomPlayer(String name){

        String res=name;

        while (res.equals(name)){
            int min=0;
            int max = playerTab.length-1;
            Random r = new Random();
            int number = r.nextInt((max - min) + 1) + min;
            res=playerTab[number];
        }

        return res;
    }

    private void SetDynamicText(){
        TextView currentTextDisplay = (TextView) findViewById(R.id.current_text_display);
        String phrase = currentChallenge[2];
        int[] firstNameIndex = {phrase.indexOf(currentPlayer),phrase.indexOf(currentPlayer)+currentPlayer.length()-1};
        int[] secondNameIndex ={1000,1000};
        if(!secondPlayer.equals("")){
            secondNameIndex = new int[]{phrase.indexOf(secondPlayer), phrase.indexOf(secondPlayer) + secondPlayer.length() - 1};
        }
        int[] thirdNameIndex ={1000,1000};

        if(!thirdPlayer.equals("")){
            int t = numberOfOccurrences(phrase,thirdPlayer);
            String str = t+"";
            if(numberOfOccurrences(phrase,thirdPlayer)!=1){
                StringBuilder tempReplace = new StringBuilder();
                for(int i=0;i<thirdPlayer.length();i++){
                    tempReplace.append("r");
                }
                phrase.replaceFirst(thirdPlayer, String.valueOf(tempReplace));
                thirdNameIndex = new int[]{phrase.indexOf(thirdPlayer), phrase.indexOf(thirdPlayer) + thirdPlayer.length() - 1};
                phrase.replace(String.valueOf(tempReplace),thirdPlayer);
            }
            else{
                thirdNameIndex = new int[]{phrase.indexOf(thirdPlayer), phrase.indexOf(thirdPlayer) + thirdPlayer.length() - 1};
            }

        }

        int time = 300;
        // 1er caratère
        String firstChar = String.valueOf(phrase.charAt(0));
        // si le nom du joueur est au début on met la 1er lettre en gras
        if(firstNameIndex[0]==0){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentTextDisplay.setText(HtmlCompat.fromHtml("<b><i>"+firstChar+"</i></b>",HtmlCompat.FROM_HTML_MODE_LEGACY));
                }
            }, time);
        }
        // sinon normal
        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentTextDisplay.setText(firstChar);
                }
            }, time);
        }
        time += 25;

        // pour ajouter les autres caractères
        for(int i=1;i<phrase.length();i++) {
            String character = String.valueOf(phrase.charAt(i));
            // si i est compris dans l'index d'un nom on met le character en gras
            if((firstNameIndex[0]<=i && i<=firstNameIndex[1])
                    || (secondNameIndex[0]<=i && i<=secondNameIndex[1])
                    || (thirdNameIndex[0]<=i && i<=thirdNameIndex[1])
            ){
                if(!character.equals(" ")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentTextDisplay.append(HtmlCompat.fromHtml("<b><i>" + character + "</i></b>", HtmlCompat.FROM_HTML_MODE_LEGACY));
                        }
                    }, time);
                }
                else{
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            currentTextDisplay.append(character);
                        }
                    }, time);
                }
            }
            else {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        currentTextDisplay.append(character);
                    }
                }, time);
            }
            time+=25;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                skipButton.setVisibility(View.VISIBLE);
            }
        }, time);
    }

    private boolean checkGameEnd(){
        boolean res=false;
        switch (playerTab.length){
            case 2:
            case 3:
            case 5:
            case 6:
            case 10:
                if(displayCounter>30){
                    res=true;
                }
                break;
            case 4:
            case 7:
                if(displayCounter>28){
                    res=true;
                }
                break;
            case 8:
                if(displayCounter>24){
                    res=true;
                }
                break;
            case 9:
                if(displayCounter>27){
                    res=true;
                }
                break;
            default:
        }
        return res;
    }

    private void setUpWheel(){
        LinearLayout main_layout=findViewById(R.id.main_layout);
        main_layout.setVisibility(View.GONE);
        LinearLayout wheel_layout=findViewById(R.id.wheel_layout);
        wheel_layout.setVisibility(View.VISIBLE);
        TextView wheel_player_call = findViewById(R.id.wheel_player_call);
        wheel_player_call.setText(HtmlCompat.fromHtml("<b>"+currentPlayer+"</b>"+getString(R.string.wheel_player_call),HtmlCompat.FROM_HTML_MODE_LEGACY));
        getWheelGame();
    }
    @SuppressLint("ClickableViewAccessibility")
    private void getWheelGame(){
        final String[] sectors = {"1","2","3","1","2","3","1","2","3"};
        final int[] sectorDegrees = new int[sectors.length];
        final Random random = new Random();

        Button spinBtn = findViewById(R.id.spinBtn);
        ImageView wheel = findViewById(R.id.wheel);

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinWheel(random,sectors,sectorDegrees,wheel);
            }
        });

        //on donne le degré lié à chaque valeur de la roue
        int sectorDegree=360/sectors.length;
        for(int i=0;i<sectors.length;i++){
            sectorDegrees[i]=(i+1)*sectorDegree;
        }

    }

    private void spinWheel(Random random,String[] sectors,int[] sectorDegrees, ImageView wheel){
        int degree=0;
        degree= random.nextInt(sectors.length-1);
        RotateAnimation rotateAnimation = new RotateAnimation(0, (360* sectors.length)+sectorDegrees[degree],RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        int finalDegree = degree;
        TextView wheel_player_call = findViewById(R.id.wheel_player_call);
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //on fait disparaitre le bouton de spin
                Button spinBtn= findViewById(R.id.spinBtn);
                spinBtn.setVisibility(View.GONE);

                String result ="<b>"+currentPlayer+"</b>"+getString(R.string.you_drink)+sectors[sectors.length-(finalDegree +1)]+getString(R.string.sips);
                wheel_player_call.setText(HtmlCompat.fromHtml(result,HtmlCompat.FROM_HTML_MODE_LEGACY));

                // ajout au score
                for (int i=0;i<scoreTab.length;i++){
                    if (playerTab[i].equals(currentPlayer)){

                        int scoreInt = Integer.parseInt(sectors[sectors.length-(finalDegree +1)]);
                        int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                        int newScore = scoreInt+currentPlayerScore;
                        scoreTab[i] = Integer.toString(newScore);
                    }
                }

                //on fait apparaitre le bouton suivant
                Button next_button = findViewById(R.id.next_button);
                next_button.setBackground(buttonDrawable);
                next_button.setVisibility(View.VISIBLE);
                next_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //on fait disparaitre les elements de la roulette et on remet les textview de base
                        LinearLayout main_layout=findViewById(R.id.main_layout);
                        main_layout.setVisibility(View.VISIBLE);
                        Button next_button = findViewById(R.id.next_button);
                        next_button.setVisibility(View.GONE);
                        LinearLayout wheel_layout=findViewById(R.id.wheel_layout);
                        wheel_layout.setVisibility(View.GONE);
                        //on supprime le gone du spinBtn car il doit être a nouveau visible si la roulette est encore appelée
                        Button spinBtn= findViewById(R.id.spinBtn);
                        spinBtn.setVisibility(View.VISIBLE);
                        try {
                            newDisplay(gameLayout);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        wheel.startAnimation(rotateAnimation);
    }

    private String randomCard(int randomNumber){
        String cardColor = "";
        Random generate = new Random(System.currentTimeMillis());

        // si les 2 listes sont pleines
        if(idBlackCardList.size()!=0 && idRedCardList.size()!=0) {
            if (randomNumber==0) {
                // on tire une rouge
                int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
                cardImage.setImageResource(rdmId);
                cardColor = "red";
            }
            else {
                // on tire une noir
                int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
                cardImage.setImageResource(rdmId);
                cardColor = "black";
            }
        }
        // si rouge pleines et noir vide
        else if(idRedCardList.size()!=0 && idBlackCardList.size()==0){
            int rdmId = idRedCardList.remove(generate.nextInt(idRedCardList.size()));
            cardImage.setImageResource(rdmId);
            cardColor = "red";
        }
        // si rouge vide et noir pleines
        else if(idRedCardList.size() ==0 && idBlackCardList.size()!=0){
            int rdmId = idBlackCardList.remove(generate.nextInt(idBlackCardList.size()));
            cardImage.setImageResource(rdmId);
            cardColor = "black";
        }
        return cardColor;
    }

    private void startCardGame(){
        //card

        this.footerCardLayout = (LinearLayout) findViewById(R.id.footer_layout);
        this.mainLayout = (LinearLayout) findViewById(R.id.main_layout);
        this.cardBodyLayout = (LinearLayout) findViewById(R.id.card_body_layout);
        this.playerCardTurn = (TextView) findViewById(R.id.player_name_display);

        mainLayout.setVisibility(View.GONE);
        footerCardLayout.setVisibility(View.VISIBLE);
        cardBodyLayout.setVisibility(View.VISIBLE);
        playerCardTurn.setText(HtmlCompat.fromHtml("<b>"+ currentPlayer + "</b> "+getString(R.string.your_turn),HtmlCompat.FROM_HTML_MODE_LEGACY));

        // pour card Game
        Button blackButton = (Button) findViewById(R.id.black_button);
        Button redButton = (Button) findViewById(R.id.red_button);
        Button nextButton = (Button) findViewById(R.id.next_button_red_or_black_game);
        nextButton.setBackground(buttonDrawable);
        TextView cardColor = (TextView) findViewById(R.id.card_color_display);
        TextView orText = (TextView) findViewById(R.id.or_text);
        this.cardImage = (ImageView) findViewById(R.id.card_image);
        currentChallenge[7] = "§ "+getString(R.string.drink)+" µ";
        blackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Random generate = new Random(System.currentTimeMillis());
                int randomNumber = generate.nextInt(2);
                if (randomCard(randomNumber).equals("black")) { // gagné
                    cardColor.setText(R.string.win);

                    // ajout au score
                    for (int i=0;i<scoreTab.length;i++){
                        if (playerTab[i].equals(currentPlayer)){

                            int scoreInt = Integer.parseInt(currentChallenge[0]);
                            int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                            int newScore = scoreInt+currentPlayerScore;
                            scoreTab[i] = Integer.toString(newScore);
                        }
                    }
                    Toast.makeText(getApplicationContext(), currentPlayer+" "+getString(R.string.scoring)+" "+currentChallenge[0]+" "+getString(R.string.points)   , Toast.LENGTH_SHORT).show();
                }
                else{ //perdu

                    cardColor.setText(HtmlCompat.fromHtml(getString(R.string.loose)+"<br>"+ getPunition(0),HtmlCompat.FROM_HTML_MODE_LEGACY));

                }
                blackButton.setVisibility(View.GONE);
                redButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);
                playerCardTurn.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);

            }
        });
        redButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random generate = new Random(System.currentTimeMillis());
                int randomNumber = generate.nextInt(2);
                //gagné
                if(randomCard(randomNumber).equals("red")){
                    cardColor.setText(R.string.win);
                    // ajout au score
                    for (int i=0;i<scoreTab.length;i++){
                        if (playerTab[i].equals(currentPlayer)){

                            int scoreInt = Integer.parseInt(currentChallenge[0]);
                            int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                            int newScore = scoreInt+currentPlayerScore;
                            scoreTab[i] = Integer.toString(newScore);
                        }
                    }
                    Toast.makeText(getApplicationContext(), currentPlayer+" "+getString(R.string.scoring)+" "+currentChallenge[0]+" "+getString(R.string.points)   , Toast.LENGTH_SHORT).show();
                }
                // perdu
                else{
                    cardColor.setText(HtmlCompat.fromHtml(getString(R.string.loose)+"<br>"+ getPunition(0),HtmlCompat.FROM_HTML_MODE_LEGACY));                }

                blackButton.setVisibility(View.GONE);
                redButton.setVisibility(View.GONE);
                orText.setVisibility(View.GONE);
                playerCardTurn.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);

            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // reviens à main
                mainLayout.setVisibility(View.VISIBLE);
                footerCardLayout.setVisibility(View.GONE);
                cardBodyLayout.setVisibility(View.GONE);
                // reinit les paramètre pour la prochaine fois
                blackButton.setVisibility(View.VISIBLE);
                redButton.setVisibility(View.VISIBLE);
                orText.setVisibility(View.VISIBLE);
                playerCardTurn.setVisibility(View.VISIBLE);
                nextButton.setVisibility(View.GONE);
                cardColor.setText("");
                cardImage.setImageResource(R.drawable.cardback);
                try {
                    newDisplay(gameLayout);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String getRandomWinSentence(){

        //on récupère les win sentences disponibles
        Resources res = getResources();
        String[] sentences = res.getStringArray(R.array.win_sentences_array);

        //on selectionne une phrase au hasard dans le tableau sentences
        int min=0;
        int max = sentences.length-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);

        String winSentence = sentences[number];
        StringBuilder finalSentence = new StringBuilder();

        //on remplace les caractères § et ¤ par le nom du joueur et le nombre de points gagnés
        for(int i=0;i<winSentence.length();i++){
            if(winSentence.substring(i,i+1).equals("§")){
                finalSentence.append("<b>").append(currentPlayer).append("</b>");
            }else if(winSentence.substring(i,i+1).equals("¤")) {
                finalSentence.append("<b>").append(currentChallenge[0]).append("</b>");
            }else{
                //si on lit un caractère "normal" de la phrase on le copie cole sans modification
                finalSentence.append(winSentence.charAt(i));
            }
        }

        //la c'est assez technique, on retourne le résultat
        return finalSentence.toString() ;
    }
}