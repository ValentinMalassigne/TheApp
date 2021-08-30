package fr.mapoe.appproject;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.res.ResourcesCompat;

import com.google.android.material.resources.TextAppearance;

public class ScorePopup extends Dialog {


    private int id = 1;
    private TableRow rowLayout;
    private TableLayout tableLayout;



    public ScorePopup (Activity activity){

        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.score_popup);


        this.tableLayout = (TableLayout) findViewById(R.id.idTable);
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.convergence);




        // ajout 1ere ligne
        TableRow row = new TableRow(getContext());
        row.setId(id);
        tableLayout.addView(row);
        this.rowLayout = (TableRow) findViewById(id);

        TextView name = new TextView(getContext());
        name.setText(R.string.name);
        name.setTypeface(typeface);
        name.setTextSize(25);
        name.setTextColor(Color.BLACK);
        name.setTypeface(null, Typeface.BOLD);
        rowLayout.addView(name);


        TextView score = new TextView(getContext());
        score.setText(R.string.score);
        score.setTypeface(typeface);
        score.setTextSize(25);
        score.setTextColor(Color.BLACK);
        score.setGravity(Gravity.CENTER);
        score.setTypeface(null,Typeface.BOLD);
        rowLayout.addView(score);
        id++;
    }
    public void setScore(String[] playerTab,String[] scoreTab){

        this.tableLayout = (TableLayout) findViewById(R.id.idTable);

        //paramètres
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.convergence);


        // ajout des elements 1 par 1
        for(int i=0;i<scoreTab.length;i++) {

            TableRow row = new TableRow(getContext());
            row.setId(id);
            tableLayout.addView(row);
            this.rowLayout = (TableRow) findViewById(id);
            // affichage du nom
            TextView name = new TextView(getContext());
            name.setText(playerTab[i]);
            name.setTypeface(typeface);
            name.setTextSize(25);
            name.setTextColor(Color.BLACK);
            rowLayout.addView(name);


            // affichage du score
            TextView score = new TextView(getContext());
            score.setText(scoreTab[i]);
            score.setTypeface(typeface);
            score.setTextSize(25);
            score.setTextColor(Color.BLACK);
            score.setGravity(Gravity.CENTER);
            rowLayout.addView(score);

            id++;

        }
    }


    // construction de la popup
    public void build(){
        show();
    }
}
