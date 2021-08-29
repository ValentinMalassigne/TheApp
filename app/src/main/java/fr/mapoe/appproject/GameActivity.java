package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    public static String[][] TAB_PLAYER = CharacterChooseActivity.getPlayersFromCharacterChoose();
    public static String[][] scoreTab = new String[TAB_PLAYER.length][2];

    private ArrayList<String> challengeList=new ArrayList<String>();
    private ArrayList<String> miniGamesList = new ArrayList<String>();
    private ArrayList<String> sentenceList = new ArrayList<String>();
    private ArrayList<String> anecdotesList = new ArrayList<String>();
    private ArrayList<String> playerTurn = new ArrayList<String>();
    private ArrayList<String> gagesList = new ArrayList<String>();
    private String[] currentChallenge = new String[4];
    private String currentPlayer ="";
    private ConstraintLayout gameLayout;
    private TextView currentTextDisplay;
    private TextView currentTitleDisplay;
    private ImageButton scoreButton;
    private ImageButton xButton;
    private Button answerButton;
    private Activity activity;
    private int turnNumber=-1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.game_layout));
        this.activity = this;
        this.gameLayout = (ConstraintLayout) findViewById(R.id.game_layout);
        //setUp des list
        try {
            SetupList();
            newDisplay(gameLayout);
        } catch (IOException e) {
            e.printStackTrace();
        }
        //pour test le score
        for(int i=0;i<scoreTab.length;i++){
            scoreTab[i][0]= TAB_PLAYER[i][0];
            scoreTab[i][1] = "0";
        }
        //Generer la popup du score
        this.scoreButton = (ImageButton) findViewById(R.id.score_button);
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ScorePopup scorePopup = new ScorePopup(activity);
                scorePopup.setScore(scoreTab);
                scorePopup.build();
            }
        });

        // Generer la popup réponse

        this.answerButton = (Button) findViewById(R.id.answer_button);
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameAnswerPopup gameAnswerPopup = new GameAnswerPopup(activity);
                String answer = "";
                Boolean customAnswer = false;
                String currentText= "";
                // envoie situationnel

                if (currentChallenge[3].equals("Gage")){
                    currentText =currentPlayer +" à reussi le gage ?";
                }
                if (currentChallenge[3].equals("Mini-Jeu")){
                    currentText = currentPlayer+ " à t'il réussi le Mini-Jeu";
                }
                if (currentChallenge[3].equals("Question/Action")) {
                    currentText = currentPlayer + " à t'il répondu/ fait l'action";
                    customAnswer = true;
                    answer = currentChallenge[1];
                }
                if (currentChallenge[3].equals("Anecdote")){
                    currentText = "L'annecdote de "+currentPlayer +" est-elle satisfaisante ?";
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
                            if (scoreTab[i][0].equals(currentPlayer)){

                                int scoreInt = Integer.parseInt(currentChallenge[0]);
                                int currentPlayerScore = Integer.parseInt(scoreTab[i][1]);

                                scoreTab[i][1] = Integer.toString(scoreInt+currentPlayerScore);
                            }
                        }
                        Toast.makeText(getApplicationContext(), currentPlayer+" marque "+currentChallenge[0]+" points !"   , Toast.LENGTH_SHORT).show();
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


        this.xButton = (ImageButton) findViewById(R.id.x_button);
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // ouvre l'activity End game
                Intent gameEndActivity = new Intent(getApplicationContext(), GameEndActivity.class);
                startActivity(gameEndActivity);
                finish();

            }
        });

    }
    @Override
    public void onBackPressed() {

    }
    public void newDisplay(View view) throws IOException{
        getPlayerTurn();
        String[] temp = new String[4];
        GetNextInformations();
        String phrase = currentChallenge[2];
        String type = currentChallenge[3];

        this.currentTitleDisplay = (TextView) findViewById(R.id.current_title_display);
        this.currentTextDisplay = (TextView) findViewById(R.id.current_text_display);

        currentTitleDisplay.setText(type);
        currentTextDisplay.setText(phrase);
    }

    public void SetupList() throws IOException {

        InputStream inputStream;
        inputStream = this.getResources().openRawResource(R.raw.gages);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String tempLine = reader.readLine();
        while (tempLine!= null) {
            gagesList.add(tempLine);
            tempLine = reader.readLine();
        }
        inputStream.close();

        inputStream = this.getResources().openRawResource(R.raw.anecdotes);
        reader = new BufferedReader(new InputStreamReader(inputStream));

        tempLine = reader.readLine();
        while (tempLine != null) {
            anecdotesList.add(tempLine);
            tempLine = reader.readLine();
        }

        inputStream.close();
        inputStream = this.getResources().openRawResource(R.raw.minijeux);
        reader = new BufferedReader(new InputStreamReader(inputStream));

        tempLine = reader.readLine();
        while (tempLine != null) {
            miniGamesList.add(tempLine);
            tempLine = reader.readLine();
        }

        inputStream.close();
        inputStream = this.getResources().openRawResource(R.raw.questionsactions);
        reader = new BufferedReader(new InputStreamReader(inputStream));

        tempLine = reader.readLine();
        while (tempLine != null) {
            sentenceList.add(tempLine);
            tempLine = reader.readLine();
        }
        inputStream.close();
    }

    public void GetNextInformations() throws  IOException
    {

        String type = getChallengeTurn();
        String[] tempTab =GetNextChallenge(type);
        String ligne = tempTab[1];
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

    }
    private String getPunition(){
        String res="";
        int i=0;
        while(!TAB_PLAYER[i][0].equals(currentPlayer)){
            i++;
        }
        if(TAB_PLAYER[i][1].equals("drink0")){
            res="fais 5 pompes";
        }else if(TAB_PLAYER[i][1].equals("drink1")){
            res="bois 1 gorgée";
        } else if(TAB_PLAYER[i][1].equals("drink2")){
            res="bois 2 gorgées";
        } else if(TAB_PLAYER[i][1].equals("drink3")){
            res="bois 3 gorgées";
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
                    res.append(" ").append(Nom).append(" ");
                }else{
                    res.append(" ").append(getRandomPlayer(Nom)).append(" ");
                }
            }else if(sentence.substring(i,i+1).equals("¤")) {
                res.append(" ").append(getPunition()).append(" ");
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

    public void getPlayerTurn(){

        if(!checkGameEnd()) {
            if (playerTurn.size()==0){
                for (int i=0;i<TAB_PLAYER.length;i++){
                    playerTurn.add(TAB_PLAYER[i][0]);
                }
                turnNumber++;//un tour correspond à avoir vidé la liste des joueurs cad que tout le monde ait joué
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
            startActivity(gameEndActivity);
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

    private String getRandomPlayer(String nom){

        String res=nom;

        while (res.equals(nom)){
            int min=0;
            int max = TAB_PLAYER.length-1;
            Random r = new Random();
            int number = r.nextInt((max - min) + 1) + min;
            res=TAB_PLAYER[number][0];
        }

        return res;
    }
    private Boolean checkGameEnd(){
        Boolean res=false;
        if(TAB_PLAYER.length==2 && turnNumber>=15){
            res=true;
        }
        if(TAB_PLAYER.length==3 && turnNumber>=10){
            res=true;
        }
        if(TAB_PLAYER.length==4 && turnNumber>=7){
            res=true;
        }
        if(TAB_PLAYER.length==5 && turnNumber>=6){
            res=true;
        }
        if(TAB_PLAYER.length==6 && turnNumber>=5){
            res=true;
        }
        if(TAB_PLAYER.length==7 && turnNumber>=4){
            res=true;
        }
        if(TAB_PLAYER.length==8 && turnNumber>=3){
            res=true;
        }
        if(TAB_PLAYER.length==9 && turnNumber>=3){
            res=true;
        }
        if(TAB_PLAYER.length>=10 && turnNumber>=3) {
            res = true;
        }
        return res;
    }

    public static String[][] getScoreTab() {
        return scoreTab;
    }

    public static void resetGameActivity(){
        GameActivity gameActivity=new GameActivity();
    }
    
}

