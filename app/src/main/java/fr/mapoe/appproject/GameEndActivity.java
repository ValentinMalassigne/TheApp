package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;



public class GameEndActivity extends AppCompatActivity {

    private int id = 1;
    private TableRow rowLayout;
    private TableLayout tableLayout;
    private Button menuButton;
    private String[][] tabClass;
    private String[] playerTab,scoreTab;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_end);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.score_main_layout));

        // recuperer les données
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            playerTab = extras.getStringArray("playerTab");
            scoreTab = extras.getStringArray("scoreTab");
        }
        // remplir le tableau à 2 dim contenant pseudo+score
        String[][] SCORE_TAB = new String[playerTab.length][2];
        for(int i=0;i<SCORE_TAB.length;i++){
                SCORE_TAB[i][0] = playerTab[i];
                SCORE_TAB[i][1] = scoreTab[i];
        }
        this.tableLayout = (TableLayout) findViewById(R.id.idTable);
        this.menuButton = (Button) findViewById(R.id.menu_button);
        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });
        //paramÃ¨tres
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        //margin


        // ajout 1ere ligne
        TableRow row = new TableRow(getApplicationContext());

        row.setId(id);
        tableLayout.addView(row);
        TableLayout.LayoutParams rowParams = new TableLayout.LayoutParams();
        rowParams.setMargins(0,10,0,0);

        row.setLayoutParams(rowParams);
        this.rowLayout = (TableRow) findViewById(id);

        TextView name = new TextView(getApplicationContext());
        name.setText("Nom");
        name.setTypeface(typeface);
        name.setTextSize(25);
        name.setTextColor(Color.BLACK);
        name.setTypeface(null, Typeface.BOLD);
        name.setGravity(Gravity.CENTER);
        rowLayout.addView(name);


        TextView score = new TextView(getApplicationContext());
        score.setText("Score");
        score.setTypeface(typeface);
        score.setTextSize(25);
        score.setTextColor(Color.BLACK);
        score.setGravity(Gravity.CENTER);
        score.setTypeface(null,Typeface.BOLD);
        score.setGravity(Gravity.CENTER);
        rowLayout.addView(score);
        id++;

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

        // ajout des elements 1 par 1
        this.tableLayout = (TableLayout) findViewById(R.id.idTable);
        typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
        for(int i=0;i<SCORE_TAB.length;i++) {

            row = new TableRow(getApplicationContext());
            row.setLayoutParams(rowParams);
            row.setId(id);
            tableLayout.addView(row);

            row.setLayoutParams(rowParams);
            this.rowLayout = (TableRow) findViewById(id);

            name = new TextView(getApplicationContext());
            name.setText(tabClass[i][0]);
            name.setTypeface(typeface);
            name.setTextSize(25);
            name.setTextColor(Color.BLACK);
            name.setGravity(Gravity.CENTER);
            rowLayout.addView(name);



            score = new TextView(getApplicationContext());
            score.setText(tabClass[i][1]);
            score.setTypeface(typeface);
            score.setTextSize(25);
            score.setTextColor(Color.BLACK);
            score.setGravity(Gravity.CENTER);
            rowLayout.addView(score);

            id++;
        }

    }
    @Override
    public void onBackPressed() {

        Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}
