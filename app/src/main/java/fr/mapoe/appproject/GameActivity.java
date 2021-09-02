package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] playerTab, alcoholTab, scoreTab;
    private ArrayList<String> challengeList=new ArrayList<String>();
    private ArrayList<String> miniGamesList = new ArrayList<String>();
    private ArrayList<String> sentenceList = new ArrayList<String>();
    private ArrayList<String> anecdotesList = new ArrayList<String>();
    private ArrayList<String> playerTurn = new ArrayList<String>();
    private ArrayList<String> gagesList = new ArrayList<String>();
    private String[] currentChallenge = new String[4];
    private String currentPlayer ="";
    private ConstraintLayout gameLayout;
    private Activity activity;
    private LinearLayout mainLayout;
    private LinearLayout cardBodyLayout;
    private LinearLayout footerCardLayout;
    private ImageView cardImage;
    private final ArrayList<Integer> idRedCardList,idBlackCardList;
    private TextView playerCardTurn;
    private int displayCounter=0;

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
        setContentView(R.layout.activity_game);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.game_layout));
        this.activity = this;
        ConstraintLayout gameLayout = (ConstraintLayout) findViewById(R.id.game_layout);

        // recuperer les données
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerTab = extras.getStringArray("playerTab");
            alcoholTab = extras.getStringArray("alcoholTab");
        }

        // initialisation du tableau scoreTab et le remplir
        scoreTab = new String[playerTab.length];
        Arrays.fill(scoreTab, "0");

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
                ScorePopup scorePopup = new ScorePopup(activity);
                scorePopup.setScore(playerTab,scoreTab);
                scorePopup.build();
            }
        });

        // Generer la popup réponse

        Button answerButton = (Button) findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameAnswerPopup gameAnswerPopup = new GameAnswerPopup(activity);
                String answer = "";
                Boolean customAnswer = false;
                String currentText= "";
                // envoie situationnel

                if (currentChallenge[3].equals("Gage")){
                    currentText =currentPlayer +" "+getString(R.string.gage_success_question);
                }
                if (currentChallenge[3].equals("Mini-Jeu")){
                    currentText = currentPlayer+" "+ getString(R.string.miniGame_success_question);
                }
                if (currentChallenge[3].equals("Question/Action")) {
                    currentText = currentPlayer +" "+ getString(R.string.answer_action_success);
                    customAnswer = true;
                    answer = currentChallenge[1];
                }
                if (currentChallenge[3].equals("Anecdote")){
                    currentText = getString(R.string.the_annecdote)+currentPlayer +" "+getString(R.string.satisfying_question);
                }

                // envoie
                gameAnswerPopup.setText(currentText);
                gameAnswerPopup.setAnswer(answer,customAnswer);

                // si on répond oui
                gameAnswerPopup.getYesButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

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
                        gameAnswerPopup.dismiss();
                        try {
                            newDisplay(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                // on répond non
                gameAnswerPopup.getNoButton().setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(getApplicationContext(), currentPlayer + " "+ getPunition(), Toast.LENGTH_SHORT).show();
                        gameAnswerPopup.dismiss();
                        try {
                            newDisplay(view);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                gameAnswerPopup.build();
            }
        });


        ImageButton xButton = (ImageButton) findViewById(R.id.x_button);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ouvre l'activity End game
                Intent gameEndActivity = new Intent(getApplicationContext(), GameEndActivity.class);
                gameEndActivity.putExtra("playerTab", playerTab);
                gameEndActivity.putExtra("scoreTab", scoreTab);
                startActivity(gameEndActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();

            }
        });

    }
    @Override
    public void onBackPressed() {}

    public void newDisplay(View view) throws IOException{
        displayCounter++;
        getPlayerTurn();
        if(GetNextInformations()){//on vérifie si on continue a construire la page avec lex textView
            String phrase = currentChallenge[2];
            String type = currentChallenge[3];

            TextView currentTitleDisplay = (TextView) findViewById(R.id.current_title_display);
            TextView currentTextDisplay = (TextView) findViewById(R.id.current_text_display);

            currentTitleDisplay.setText(type);
            currentTextDisplay.setText(phrase);
        }
    }

    public void setUpList() throws IOException {

        String language = "fr";
        InputStream inputStream = null;
        if(language.equals("fr"))
            inputStream = this.getResources().openRawResource(R.raw.frsentences);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        reader.readLine();
        String tempLine = reader.readLine();
        while (!tempLine.equals("gages")) {
            anecdotesList.add(tempLine);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("minigames")) {
            gagesList.add(tempLine);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("questions")) {
            miniGamesList.add(tempLine);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (tempLine!=null) {
            sentenceList.add(tempLine);
            tempLine = reader.readLine();
        }

        inputStream.close();

    }

    public boolean GetNextInformations() throws  IOException
    {
        String type = getChallengeTurn();
        String[] tempTab =GetNextChallenge(type);
        String ligne = tempTab[1];
        if(ligne.equals("Spinning Wheel")){
            setUpWheel();
            return(false);//false veut dire que l'on ne continue pas la construction de la page avec les textView
        }else if(ligne.equals("Red or Black")) {
            startCardGame();
            return(false);//false veut dire que l'on ne continue pas la construction de la page avec les textView
        }else{
            currentChallenge[3]= tempTab[0];
            String points;
            String answers;
            String sentence;
            points= ligne.substring(0,1);

            boolean temp = false;
            int i=0;
            while (!temp){
                if(ligne.substring(i,i+1).equals("ç")){
                    temp=true;
                }else {
                    i++;
                }
            }
            answers=ligne.substring(2,i);
            sentence=ligne.substring(i+1);

            currentChallenge[0]=points;
            currentChallenge[1]=SetNamesInSentence(answers);
            currentChallenge[2]=SetNamesInSentence(sentence);
            return(true);//true veut dire que l'on continue la construction de la page avec les textView
        }


    }
    private String getPunition(){
        String res="";
        int i=0;
        while(!playerTab[i].equals(currentPlayer)){
            i++;
        }
        if(alcoholTab[i].equals("drink0")){
            res=getString(R.string.drink0_punition);
        }else if(alcoholTab[i].equals("drink1")){
            res=getString(R.string.drink1_punition);
        } else if(alcoholTab[i].equals("drink2")){
            res=getString(R.string.drink2_punition);
        } else if(alcoholTab[i].equals("drink3")){
            res=getString(R.string.drink3_punition);
        }
        return res;
    }

    private String SetNamesInSentence(String sentence){
        StringBuilder res= new StringBuilder();
        String Nom="";
        boolean temp = false;
        for(int i=0;i<sentence.length();i++){
            if(sentence.substring(i,i+1).equals("§")){
                if(Nom.equals("")) {
                    Nom=currentPlayer;
                    res.append(Nom);
                }else{
                    res.append(getRandomPlayer(Nom));
                }
            }else if(sentence.substring(i,i+1).equals("¤")) {
                res.append(getPunition());
            }else{
                res.append(sentence.charAt(i));
            }
        }
        return res.toString();
    }

    private String[] GetNextChallenge(String type) {
        String[] res=new String[2];

        if(type.equals("gage")){
            res[0]="Gage";
            res[1]=GetRandomGage();
        }
        if(type.equals("miniGame")){
            res[0]="Mini-Jeu";
            res[1]=GetRandomMiniGame();
        }
        if (type.equals("sentence")){
            res[0]="Question/Action";
            res[1]=GetRandomSentence();
        }
        if (type.equals("anecdote")){
            res[0]="Anecdote";
            res[1]=GetRandomAnecdote();
        }
        return res;
    }

    private String GetRandomGage() {

        int min=0;
        int max = gagesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);

        String res=gagesList.get(number);
        gagesList.remove(number);

        return res;
    }

    private String GetRandomMiniGame() {

        int min=0;
        int max = miniGamesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);

        String res=miniGamesList.get(number);
        miniGamesList.remove(number);
        return res;
    }

    private String GetRandomSentence() {

        int min=0;
        int max = sentenceList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);

        String res=sentenceList.get(number);
        sentenceList.remove(number);

        return res;
    }

    private String GetRandomAnecdote() {

        int min=0;
        int max = anecdotesList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min; //génère de min (inclus) a max(inclus);

        String res=anecdotesList.get(number);
        anecdotesList.remove(number);

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
            startActivity(gameEndActivity);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }
    }

    private String getChallengeTurn(){
        //remplir la list
        if (challengeList.size()==0){
            challengeList.add("gage");
            challengeList.add("gage");
            challengeList.add("miniGame");
            challengeList.add("miniGame");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("sentence");
            challengeList.add("anecdote");
            challengeList.add("anecdote");
        }
        int min=0;
        int max = challengeList.size()-1;
        Random r = new Random();
        int number = r.nextInt((max - min) + 1) + min;

        return challengeList.remove(number);
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
        wheel_player_call.setText(currentPlayer+getString(R.string.wheel_player_call));
        getWheelGame();
    }

    private void getWheelGame(){
        final String[] sectors = {"0","3","2","1","1","2","1","1","1","2","1","3"};
        final int[] sectorDegrees = new int[sectors.length];
        final Random random = new Random();

        ImageView spinBtn = findViewById(R.id.spinBtn);
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
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                //on fait disparaitre le bouton de spin
                ImageView spinBtn= findViewById(R.id.spinBtn);
                spinBtn.setVisibility(View.GONE);

                //on fait apparaitre le résultat de la roue
                TextView wheel_result_display;
                wheel_result_display= findViewById(R.id.wheel_result_display);
                wheel_result_display.setVisibility(View.VISIBLE);
                String result =currentPlayer+getString(R.string.you_drink)+sectors[sectors.length-(finalDegree +1)]+getString(R.string.sips);
                wheel_result_display.setText(result);

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
                next_button.setVisibility(View.VISIBLE);
                next_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //on fait disparaitre les elements de la roulette et on remet les textview de base
                        LinearLayout main_layout=findViewById(R.id.main_layout);
                        main_layout.setVisibility(View.VISIBLE);
                        TextView wheel_result_display = findViewById(R.id.wheel_result_display);
                        wheel_result_display.setVisibility(View.GONE);
                        Button next_button = findViewById(R.id.next_button);
                        next_button.setVisibility(View.GONE);
                        LinearLayout wheel_layout=findViewById(R.id.wheel_layout);
                        wheel_layout.setVisibility(View.GONE);
                        //on supprime le gone du spinBtn car il doit être a nouveau visible si la roulette est encore appelée
                        ImageView spinBtn= findViewById(R.id.spinBtn);
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
        playerCardTurn.setText(currentPlayer + " à toi de jouer mon grand !");

        // pour card Game
        Button blackButton = (Button) findViewById(R.id.black_button);
        Button redButton = (Button) findViewById(R.id.red_button);
        Button nextButton = (Button) findViewById(R.id.next_button_red_or_black_game);
        TextView cardColor = (TextView) findViewById(R.id.card_color_display);
        TextView orText = (TextView) findViewById(R.id.or_text);
        this.cardImage = (ImageView) findViewById(R.id.card_image);

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
                    cardColor.setText(getString(R.string.loose)+"\n"+ getPunition());

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
                    cardColor.setText(getString(R.string.loose)+"\n"+ getPunition());
                }

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
}