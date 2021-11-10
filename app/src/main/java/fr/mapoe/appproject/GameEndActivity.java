package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

import fr.mapoe.appproject.tools.ThemeManager;


public class GameEndActivity extends AppCompatActivity {

    private int id = 1;
    private String[][] tabClass;
    private String[] playerTab,scoreTab,alcoholTab;
    private int typeOfGame = 0;
    private ArrayList<String> savedSentenceList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        // recupère le theme
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        ThemeManager themeManager = new ThemeManager(this,sharedPreferences.getString("theme",""));
        ConstraintLayout constraintLayout = findViewById(R.id.game_end_layout);
        constraintLayout.setBackground(themeManager.getBackgroundDrawable());
        // recuperer les données
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerTab = extras.getStringArray("playerTab");
            alcoholTab = extras.getStringArray("alcoholTab");
            scoreTab = extras.getStringArray("scoreTab");
            typeOfGame = extras.getInt("typeOfGame");
            savedSentenceList = extras.getStringArrayList("savedList");
        }

        // init les tableaux
        classTab();
        printWinner();


        // go to menu
        ImageButton backButton = (ImageButton) findViewById(R.id.menu_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        // rejouer
        Button restart = (Button) findViewById(R.id.restart_button);
        if(typeOfGame==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                restart.setBackground(getDrawable(R.drawable.button_apepiment));
            }
        }
        else{
            restart.setBackground(themeManager.getButtonDrawable());
        }
        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChosse = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChosse.putExtra("playerTab", playerTab);
                characterChosse.putExtra("alcoholTab", alcoholTab);
                characterChosse.putExtra("restart",true);
                characterChosse.putExtra("TypeOfGame", typeOfGame);
                characterChosse.putExtra("savedList",savedSentenceList);
                startActivity(characterChosse);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });





    }
    @Override
    public void onBackPressed() {

        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    private void classTab(){
        // remplir le tableau à 2 dim contenant pseudo+score
        String[][] SCORE_TAB = new String[playerTab.length][2];
        for(int i=0;i<SCORE_TAB.length;i++){
            SCORE_TAB[i][0] = playerTab[i];
            SCORE_TAB[i][1] = scoreTab[i];
        }
        // refaire un tableau dans le bonne ordre
        tabClass = new String[SCORE_TAB.length][2];
        int index;
        int min;
        for (int i=0;i<SCORE_TAB.length;i++) {
            index=0;
            min= Integer.parseInt(SCORE_TAB[0][1]);
            for(int j=0;j<SCORE_TAB.length;j++) {
                if(min>Integer.parseInt(SCORE_TAB[j][1])) {
                    min=Integer.parseInt(SCORE_TAB[j][1]);
                    index=j;

                }
            }

            tabClass[SCORE_TAB.length-i-1][0]=SCORE_TAB[index][0];
            tabClass[SCORE_TAB.length-i-1][1]=SCORE_TAB[index][1];
            SCORE_TAB[index][1]="100000";
        }
    }
    //fonction pour afficher les 3 premièrs
    private void printWinner(){
        TextView winnerText = (TextView) findViewById(R.id.winner);
        TextView secondText = (TextView) findViewById(R.id.second);
        TextView thirdText = (TextView) findViewById(R.id.third);
        ImageView medal3 = (ImageView) findViewById(R.id.medal3);
        View lineView = (View) findViewById(R.id.line_view);
        if(playerTab.length == 2){
            winnerText.setText(tabClass[0][0].toString() +"\n"+tabClass[0][1]+" "+getString(R.string.points));
            secondText.setText(tabClass[1][0].toString() +"\n"+tabClass[1][1]+" "+getString(R.string.points));
            thirdText.setVisibility(View.INVISIBLE);
            medal3.setVisibility(View.INVISIBLE);
            lineView.setVisibility(View.GONE);
        }
        else if(playerTab.length==3){
            winnerText.setText(tabClass[0][0].toString() +"\n"+tabClass[0][1]+" "+getString(R.string.points));
            secondText.setText(tabClass[1][0].toString() +"\n"+tabClass[1][1]+" "+getString(R.string.points));
            thirdText.setText(tabClass[2][0].toString() +"\n"+tabClass[2][1]+" "+getString(R.string.points));
            lineView.setVisibility(View.GONE);
        }
        else{
            winnerText.setText(tabClass[0][0].toString() +"\n"+tabClass[0][1]+" "+getString(R.string.points));
            secondText.setText(tabClass[1][0].toString() +"\n"+tabClass[1][1]+" "+getString(R.string.points));
            thirdText.setText(tabClass[2][0].toString() +"\n"+tabClass[2][1]+" "+getString(R.string.points));
            setUpTab();
        }

    }
    // foncton qui crée le tableau
    private void setUpTab(){
        TableLayout tableLayout = (TableLayout) findViewById(R.id.idTable);
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
        rowParams.setMargins(25,10,0,0);

        // ajout des elements 1 par 1

        for(int i=3;i<tabClass.length;i++) {

            //textParams.leftMargin = 50;
            TableRow row = new TableRow(getApplicationContext());
            row.setLayoutParams(rowParams);
            row.setId(id);
            tableLayout.addView(row);
            TextView name = new TextView(getApplicationContext());

            name.setText(tabClass[i][0]);
            name.setTypeface(typeface);
            name.setTextSize(25);
            name.setTextColor(Color.WHITE);
            name.setPadding(20,20,20,20);
            //name.setLayoutParams(textParams);
            row.addView(name);


            TextView score = new TextView(getApplicationContext());
            score.setText(tabClass[i][1]);
            score.setTypeface(typeface);
            score.setTextSize(25);
            score.setTextColor(Color.WHITE);
            score.setGravity(Gravity.CENTER);
            score.setPadding(20,20,20,20);
            row.addView(score);

            id++;
        }

    }
}