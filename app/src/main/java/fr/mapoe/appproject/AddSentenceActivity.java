package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AddSentenceActivity extends AppCompatActivity {
    private int maxAddPlayerText=0,maxAddAnswerText=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);

        Spinner gameModeSpinner = findViewById(R.id.game_mode);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gameModeList, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpinner.setAdapter(adapter1);



        Spinner typeModeSpinner = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.typeList
                , android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeModeSpinner.setAdapter(adapter2);

        Spinner pointSpinner = findViewById(R.id.pointList);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.pointList,
                android.R.layout.simple_spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointSpinner.setAdapter(adapter3);
        EditText textEdit = (EditText) findViewById(R.id.text_edit);
        EditText answerEdit = (EditText) findViewById(R.id.answer_edit);
        TextView validateText = (TextView) findViewById(R.id.text);
        Button validateButton = (Button) findViewById(R.id.validate);

        ImageView addPlayerText = (ImageView) findViewById(R.id.add_player_text);
        ImageView addPlayerAnswer = (ImageView) findViewById(R.id.add_player_answer);

        addPlayerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEdit.getText().toString();
                if(numberOfOccurrences(text)<3) {
                    text += "--joueur--";
                    textEdit.setText(text);
                    maxAddPlayerText ++;
                }
            }
        });

        addPlayerAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = answerEdit.getText().toString();
                if(numberOfOccurrences(text) ==0) {
                    text += "--joueur--";
                    answerEdit.setText(text);
                    maxAddAnswerText++;
                }
            }
        });




        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEdit.getText().toString();
                String newText = text.replace("--joueur--","§");
                String answer = answerEdit.getText().toString();
                String newAnswer = answer.replace("--joueur--","§");
                String point = pointSpinner.getSelectedItem().toString();
                validateText.setText(encodage(point,newText,newAnswer));

            }
        });
    }
    private String encodage(String point, String text, String answer){
        String encodageSentence = "";
        encodageSentence+=point+ " ";
        encodageSentence+=answer+" "+"ç";
        encodageSentence+=text;
        return encodageSentence;

    }
    private int numberOfOccurrences(String source) {
        int occurrences = 0;

        if (source.contains("--joueur--")) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace("--joueur--", "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / "--joueur--".length();
        }

        return occurrences;
    }
}
