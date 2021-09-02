package fr.mapoe.appproject;

import android.app.Activity;
import android.app.Dialog;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.text.HtmlCompat;

public class GameAnswerPopup extends Dialog {
    private LinearLayout displayLayout;
    private Boolean customAnswer;
    private String answer;
    private Spanned text;
    private Button yesButton;
    private Button noButton;

    private TextView textDisplay;
    private TextView answerDisplay;


    public GameAnswerPopup(Activity activity) {

        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.game_answer_popup);

        this.yesButton = (Button) findViewById(R.id.yes_button);
        this.noButton = (Button) findViewById(R.id.no_button);

        this.answerDisplay = (TextView) findViewById(R.id.answer_display);
        this.textDisplay = (TextView) findViewById(R.id.text_display);

    }
    // recuperer les élements de la popup
    public Button getYesButton(){ return yesButton; }

    public Button getNoButton(){ return noButton; }

    public void setAnswer(String answer, Boolean customAnswer){
        this.answer = answer;
        this.customAnswer = customAnswer;
    }

    public void setText(String text){this.text = HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY);}

    public void build(){

        //setText
        textDisplay.setText(text);

        //setAnswer
        if(customAnswer) {
            answerDisplay.setVisibility(View.VISIBLE);
            answerDisplay.setText(answer);
        }
        else{
            answerDisplay.setVisibility(View.INVISIBLE);
        }
        show();
    }

}