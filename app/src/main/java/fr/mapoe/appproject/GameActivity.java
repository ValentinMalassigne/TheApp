package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.text.HtmlCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;

public class GameActivity extends AppCompatActivity {

    private String[] playerTab, alcoholTab, scoreTab;
    private ArrayList<String> challengeList =new ArrayList<String>();
    private ArrayList<String> miniGamesList = new ArrayList<String>();
    private ArrayList<String> sentenceList = new ArrayList<String>();
    private ArrayList<String> anecdotesList = new ArrayList<String>();
    private ArrayList<String> playerTurn = new ArrayList<String>();
    private ArrayList<String> gagesList = new ArrayList<String>();
    private String[] currentChallenge = new String[7]; //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2
    private String currentPlayer ="";
    private ConstraintLayout gameLayout;
    private Activity activity;
    private int displayCounter = 0;
    private LinearLayout mainLayout;
    private LinearLayout cardBodyLayout;
    private LinearLayout footerCardLayout;
    private ImageView cardImage;
    private final ArrayList<Integer> idRedCardList,idBlackCardList;
    private TextView playerCardTurn;
    private int typeOfGame =0;
    private static final String FILE_NAME = "custom_sentences.txt";

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
            typeOfGame = extras.getInt("typeOfGame");
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
                showScorePopup(R.layout.score_popup);
            }
        });

        // Generer la popup réponse
        Button answerButton = (Button) findViewById(R.id.answer_button);
        if(typeOfGame ==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerButton.setBackground(getDrawable(R.drawable.button2));
            }
        }
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswerPopup(R.layout.game_answer_popup,currentChallenge[1]);
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
                startActivity(gameEndActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        {
            //on vérifie si l'utilisateur à bloqué la popup
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            String alcohol_reminder_popup_blocked = "";
            if(sharedPreferences.contains("block_apechill_tutorial_popup")){
                alcohol_reminder_popup_blocked = sharedPreferences.getString("block_apechill_tutorial_popup","");
            }
            if(!alcohol_reminder_popup_blocked.equals("blocked"))
                showInfoDialog(R.layout.info_popup);

        }
    }

    private void showInfoDialog(int layout) {// créer la popup info
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button okButton = layoutView.findViewById(R.id.ok_button);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        textInfo.setText(getString(R.string.game_description));
        imageInfo.setVisibility(View.GONE);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        // chnagement du bg button on fonction du mode de jeu
        if (typeOfGame == 2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                okButton.setBackground(getDrawable(R.drawable.button2));
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
                boolean checked =  ((CheckBox) view).isChecked();
                //ici on enregistre dans les SharedPreferences si l'utilisateur bloque la popup
                SharedPreferences blockPopup;
                blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = blockPopup.edit();
                if(checked){
                    editor.putString("block_apechill_tutorial_popup","blocked");
                }else{
                    editor.putString("block_apechill_tutorial_popup","activated");
                }
                editor.apply();
            }
        });
    }

    private void showAnswerPopup(int layout, String text){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        // déclarer les éléments de la popup
        TextView textDisplay = (TextView) layoutView.findViewById(R.id.text_display);
        Button yesButton = (Button) layoutView.findViewById(R.id.yes_button);
        Button noButton = (Button) layoutView.findViewById(R.id.no_button);
        Button nextButton = (Button) layoutView.findViewById(R.id.next_button);

        //on change la réponse des boutons avec celles du fichier
        yesButton.setText(currentChallenge[5]);//la première rep
        noButton.setText(currentChallenge[6]);//la deuxième rep

        // change bg suivant le jeu
        if(typeOfGame ==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                yesButton.setBackground(getDrawable(R.drawable.button2));
                noButton.setBackground(getDrawable(R.drawable.button2));
                nextButton.setBackground(getDrawable(R.drawable.button2));
            }
        }
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        textDisplay.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentChallenge[4].equals("+")){//si c'est + et que le joueur répond oui alors il a gagné
                    String temp = "<b>"+currentPlayer+"</b> "+getString(R.string.scoring)+" "+currentChallenge[0]+" "+getString(R.string.points);
                    textDisplay.setText(HtmlCompat.fromHtml(temp,HtmlCompat.FROM_HTML_MODE_LEGACY));
                    noButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                    // ajout au score
                    for (int i=0;i<scoreTab.length;i++){
                        if (playerTab[i].equals(currentPlayer)){

                            int scoreInt = Integer.parseInt(currentChallenge[0]);
                            int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                            int newScore = scoreInt+currentPlayerScore;
                            scoreTab[i] = Integer.toString(newScore);
                        }
                    }
                }else{ //sinon il a perdu
                    String temp = "<b>"+currentPlayer+"</b> "+getPunition();
                    textDisplay.setText(HtmlCompat.fromHtml(temp,HtmlCompat.FROM_HTML_MODE_LEGACY));
                    yesButton.setVisibility(View.GONE);
                    noButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentChallenge[4].equals("-")){//si c'est - et que le joueur répond oui alors il a gagné
                    String temp = "<b>"+currentPlayer+"</b> "+getString(R.string.scoring)+" "+currentChallenge[0]+" "+getString(R.string.points);
                    textDisplay.setText(HtmlCompat.fromHtml(temp,HtmlCompat.FROM_HTML_MODE_LEGACY));
                    noButton.setVisibility(View.GONE);
                    yesButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                    // ajout au score
                    for (int i=0;i<scoreTab.length;i++){
                        if (playerTab[i].equals(currentPlayer)){

                            int scoreInt = Integer.parseInt(currentChallenge[0]);
                            int currentPlayerScore = Integer.parseInt(scoreTab[i]);
                            int newScore = scoreInt+currentPlayerScore;
                            scoreTab[i] = Integer.toString(newScore);
                        }
                    }
                }else{ //sinon il a perdu
                    String temp = "<b>"+currentPlayer+"</b> "+getPunition();
                    textDisplay.setText(HtmlCompat.fromHtml(temp,HtmlCompat.FROM_HTML_MODE_LEGACY));
                    yesButton.setVisibility(View.GONE);
                    noButton.setVisibility(View.GONE);
                    nextButton.setVisibility(View.VISIBLE);
                }
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    newDisplay(view);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void showScorePopup(int layout){
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
        rowParams.setMargins(0,10,0,0);

        int id = 1;
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(GameActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
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
        name.setTypeface(typeface,Typeface.BOLD);
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
        for(int i=0;i<scoreTab.length;i++){
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
    public void onBackPressed() {}

    private void setUpList() throws IOException {
        int nbFiles=1;
        //on vérifie si l'utilisateur à des phrases custom
        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (file.exists()){
            nbFiles=2;
        }

        //on récup la langue acctuelement utilisé par l'appli
        String language = getResources().getConfiguration().locale.getLanguage();

        InputStream inputStream = null;
        if(language.equals("fr")) {
            inputStream = this.getResources().openRawResource(R.raw.fr_sentences);
        }else{
            inputStream = this.getResources().openRawResource(R.raw.en_sentences);
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        //une fois la boucle lit le fichier raw et une fois elle lit le fichier custom (s'il existe)
        for(int i=1;i<=nbFiles;i++) {
            if(i==2){
                FileInputStream fis=openFileInput(FILE_NAME);
                InputStreamReader isr = new InputStreamReader(fis);
                reader = new BufferedReader(isr);
            }

            //on passe toute les lignes tant que l'on est pas aux lignes du mode génant
            if (typeOfGame == 2) {
                while (!reader.readLine().equals("End")) {
                }
            }
            reader.readLine();//on passe la ligne "anecdotes"
            String tempLine = reader.readLine();//on lit la première anecdote
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
            while (!tempLine.equals("End")) {
                sentenceList.add(tempLine);
                tempLine = reader.readLine();
            }

            inputStream.close();
        }
    }

    private void newDisplay(View view) throws IOException{
        displayCounter++;
        getPlayerTurn();

        if(GetNextInformations()) {//on vérifie si on continue a construire la page avec lex textView
            String phrase = currentChallenge[2];
            String type = currentChallenge[3];

            TextView currentTitleDisplay = (TextView) findViewById(R.id.current_title_display);
            TextView currentTextDisplay = (TextView) findViewById(R.id.current_text_display);

            currentTitleDisplay.setText(type);
            currentTextDisplay.setText(HtmlCompat.fromHtml(phrase,HtmlCompat.FROM_HTML_MODE_LEGACY));
        }
    }

    private boolean GetNextInformations()
    {
        String type = getChallengeTurn();
        String[] tempTab =GetNextChallenge(type);
        String ligne = tempTab[1];
        boolean result=false;
        boolean temp = false;
        int i=0;
        int j;
        if(ligne.equals("Spinning Wheel")){
            setUpWheel();
        }else if(ligne.equals("Red or Black")) {
            startCardGame();
        }else{
            currentChallenge[3]= tempTab[0];
            String points;
            String answers;
            String sentence;
            String rightAnswer;
            String button1Text;
            String button2Text;

            //on lit tant que l'on est pas a / pour savoir quelle est le premier bouton
            while (!temp){
                if(ligne.substring(i,i+1).equals("/")){
                    temp=true;
                }else {
                    i++;
                }
            }
            button1Text=ligne.substring(0,i);
            j=i+1;//on sauvergarde à quelle caractère il faut reprendre la lecture

            //on lit tant que l'on est pas a + ou - pour savoir quelle est le deuxième bouton
            temp=false;
            while (!temp){
                if(ligne.substring(i,i+1).equals("+") || ligne.substring(i,i+1).equals("-")){
                    temp=true;
                }else {
                    i++;
                }
            }

            button2Text=ligne.substring(j,i);

            rightAnswer = ligne.substring(i,i+1);//on recup le + ou - qui se trouve juste après la deuxième rep
            points= ligne.substring(i+1,i+2);//on recup le nb de points qui est juste après le + ou -

            j=i+1;//on sauvergarde à quelle caractère il faut reprendre la lecture

            //on lit tant que l'on a est pas a ç pour avoir la réponse
            temp=false;
            while (!temp){
                if(ligne.substring(i,i+1).equals("¤")){
                    temp=true;
                }else {
                    i++;
                }
            }
            answers=ligne.substring(j,i);
            sentence=ligne.substring(i+1);//on lit tout jusqu'à la fin pour avoir la phrase

            currentChallenge[0]=points;
            currentChallenge[1]=SetNamesInSentence(answers);
            currentChallenge[2]=SetNamesInSentence(sentence);
            currentChallenge[4]=rightAnswer;
            currentChallenge[5]=button1Text;
            currentChallenge[6]=button2Text;
            Log.d(TAG, "GetNextInformations: "+currentChallenge[4]);
            result =true;
        }

        return result;
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
        //on lit la phrase caractères par caractères
        for(int i=0;i<sentence.length();i++){
            if(sentence.substring(i,i+1).equals("§")){
                //obtention des noms a mettre dans la phrase
                if(Nom.equals("")) {
                    //nom du joueur principal
                    Nom=currentPlayer;
                    res.append("<b>").append(Nom).append("</b>");
                }else{
                    //nom des joueurs qui participent aussi
                    res.append("<b>").append(getRandomPlayer(Nom)).append("</b>");
                }
            }else{
                //si on lit un caractère "normal" de la phrase on le copie cole sans modification
                res.append(sentence.charAt(i));
            }
        }
        return res.toString();
    }

    private String[] GetNextChallenge(String type) {
        String[] res=new String[2];

        if(type.equals("gage")){
            res[0]=getString(R.string.type_gage);
            res[1]=GetRandomGage();
        }
        if(type.equals("miniGame")){
            res[0]=getString(R.string.type_mini_game);
            res[1]=GetRandomMiniGame();
        }
        if (type.equals("sentence")){
            res[0]=getString(R.string.type_sentence);
            res[1]=GetRandomSentence();
        }
        if (type.equals("anecdote")){
            res[0]=getString(R.string.type_anecdote);
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
            gameEndActivity.putExtra("alcoholTab", alcoholTab);
            gameEndActivity.putExtra("typeOfGame", typeOfGame);
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
        wheel_player_call.setText(HtmlCompat.fromHtml("<b>"+currentPlayer+"</b>"+getString(R.string.wheel_player_call),HtmlCompat.FROM_HTML_MODE_LEGACY));
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
                String result ="<b>"+currentPlayer+"</b>"+getString(R.string.you_drink)+sectors[sectors.length-(finalDegree +1)]+getString(R.string.sips);
                wheel_result_display.setText(HtmlCompat.fromHtml(result,HtmlCompat.FROM_HTML_MODE_LEGACY));

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
        playerCardTurn.setText(HtmlCompat.fromHtml("<b>"+ currentPlayer + "</b> "+getString(R.string.your_turn),HtmlCompat.FROM_HTML_MODE_LEGACY));

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