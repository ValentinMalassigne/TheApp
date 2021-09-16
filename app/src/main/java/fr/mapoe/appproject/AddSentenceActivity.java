package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class AddSentenceActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    private int maxAddPlayerText=0,maxAddAnswerText=0;
    private int typeOfGame = 1;

    private static final String FILE_NAME = "custom_sentences.txt";
    private Spinner gameModeSpinner;
    private String currentText,currentAnswer,currentPoint;

    private Button answerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.add_sentence_layout));

        showText();
        ImageView infoButton =(ImageView) findViewById(R.id.info_image);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(R.layout.info_popup);
            }
        });


        /*visualizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEdit.getText().toString();
                String answer = answerEdit.getText().toString();
                if(!text.equals("") && !answer.equals("")) {
                    if (numberOfOccurrences(text) == 0) {
                        text = "--joueur--" + " "+text;
                    }
                    text = getCleanText(text);

                    answer = getCleanText(answer);
                    String title = gameModeSpinner.getSelectedItem().toString() + ": " + typeModeSpinner.getSelectedItem().toString();
                    String point = pointSpinner.getSelectedItem().toString();
                    showValidatePopup(R.layout.visualisation_popup, title, text, answer, point);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Veuillez remplir tout les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });*/


        //on consctruit le fichier si le joueur n'en a jamais créé
        fileBuilder();

    }

    private void showText(){
        LinearLayout textLayout = (LinearLayout) findViewById(R.id.addText_layout);
        textLayout.setVisibility(View.VISIBLE);

        gameModeSpinner = (Spinner) findViewById(R.id.game_mode);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gameModeList, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpinner.setAdapter(adapter1);
        gameModeSpinner.setOnItemSelectedListener(this);

        Spinner typeModeSpinner = findViewById(R.id.type);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,R.array.typeList
                , R.layout.spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeModeSpinner.setAdapter(adapter2);

        EditText textEdit = (EditText) findViewById(R.id.text_edit);
        ImageView addPlayerText = (ImageView) findViewById(R.id.add_player_text);
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
        answerButton = (Button) findViewById(R.id.answer_button);

        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEdit.getText().toString();

                if(!text.equals("")) {
                    if (numberOfOccurrences(text) == 0) {
                        text = "--joueur--" + " " + text;
                    }
                    currentText = text;
                    showAnswer();
                    textLayout.setVisibility(View.GONE);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Veuillez remplir le champ",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showAnswer(){
        LinearLayout answerLayout = (LinearLayout) findViewById(R.id.answer_layout);
        answerLayout.setVisibility(View.VISIBLE);
        EditText answerEdit = (EditText) findViewById(R.id.answer_edit);
        ImageView addPlayerAnswer = (ImageView) findViewById(R.id.add_player_answer);
        ImageView nextArrow = (ImageView) findViewById(R.id.next_button);
        ImageView previousArrow = (ImageView) findViewById(R.id.previous_arrow);
        EditText leftButton = (EditText) findViewById(R.id.edit_button1);
        EditText rightButton = (EditText) findViewById(R.id.edit_button2);

        if (typeOfGame==2 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            leftButton.setBackground(getDrawable(R.drawable.button2));
            rightButton.setBackground(getDrawable(R.drawable.button2));
        }
        addPlayerAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = answerEdit.getText().toString();
                if(numberOfOccurrences(text) ==0) {
                    text += " --joueur-- ";
                    answerEdit.setText(text);
                    maxAddAnswerText++;
                }
                else{
                    Toast.makeText(getApplicationContext(),"seul 1 joueur peut aparaitre dans la réponse", Toast.LENGTH_SHORT).show();
                }
            }
        });
        //back to show text
        previousArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showText();
                answerLayout.setVisibility(View.GONE);
            }
        });
        // go to point
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String answer = answerEdit.getText().toString();
                if(!answer.equals("")) {
                    currentAnswer = answer;
                    showPoint();
                    answerLayout.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(getApplicationContext(), "Veuillez remplir la réponse", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void showPoint(){
        LinearLayout pointLayout = (LinearLayout) findViewById(R.id.point_layout);
        pointLayout.setVisibility(View.VISIBLE);
        ImageView validate = (ImageView) findViewById(R.id.validate);
        ImageView previousArrow = (ImageView) findViewById(R.id.previous1_arrow);
        Spinner pointSpinner = findViewById(R.id.pointList);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this,R.array.pointList,
                R.layout.spinner_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pointSpinner.setAdapter(adapter3);

        previousArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAnswer();
                pointLayout.setVisibility(View.GONE);
            }
        });
        TextView test = findViewById(R.id.test);
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentPoint = pointSpinner.getSelectedItem().toString();
                // pour voir l'encodage
                test.setText(encoding());
            }
        });

    }

    private void showInfoPopup(int layout){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button okButton = layoutView.findViewById(R.id.ok_button);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        CheckBox checkBox = layoutView.findViewById(R.id.block_popup_checkBox);
        textInfo.setText(getString(R.string.game_description));
        imageInfo.setVisibility(View.GONE);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        checkBox.setVisibility(View.GONE);
        imageInfo.setVisibility(View.GONE);
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
    }

    private String encoding(){
        String text = currentText.replace("--joueur--","§");
        String answer = currentAnswer.replace("--joueur--","§");
        String encodageSentence = "";
        encodageSentence+=currentPoint+ " ";
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
    private String getCleanText(String text) {
        String res = "";
        if (numberOfOccurrences(text) == 1) {
            res = text.replaceFirst("--joueur--", "<b><i>Joueur 1</i></b>");

        } else if (numberOfOccurrences(text) == 2) {
            res = text.replaceFirst("--joueur--", "<b><i>Joueur 1</i></b>");
            res=res.replace("--joueur--","<b><i>Joueur 2</i></b>");
        }
        else{
            res = text.replaceFirst("--joueur--", "<b><i>Joueur 1</i></b>");
            res=res.replace("--joueur--","<b><i>Joueur 3</i></b>");
            res = res.replaceFirst("<b><i>Joueur 3</b></i>","<b><i>Joueur 2</i></b>");
        }
        return res;
    }

    private void fileBuilder(){
        //construction du fichier
        String fileText ="anecdotes\ngages\nminigames\nquestions\nEnd\nanecdotes\ngages\nminigames\nquestions\nEnd";

        File file = new File(getFilesDir()+"/"+FILE_NAME);
        if (!file.exists()){
            FileOutputStream fos = null;

            try {
                fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
                fos.write(fileText.getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void addSentence(String ligne, String sentenceType, int typeOfGame){
        //on adaptes le sentenceType
        if(sentenceType.equals("Annecdote"))
            sentenceType="anecdotes";
        if(sentenceType.equals("Gages"))
            sentenceType="gages";
        if(sentenceType.equals("Mini-jeu"))
            sentenceType="minigames";
        if(sentenceType.equals("Question"))
            sentenceType="questions";

        //on copie ce qu'il y a dans le fichier
        try {
            FileInputStream fis = openFileInput(FILE_NAME);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            String text;
            ArrayList<String> phrase = new ArrayList<String>();
            int typeOfGameCounter=1;
            while ((text = br.readLine())!=null){
                phrase.add(text+"\n");
                if(text.equals(sentenceType) && typeOfGame==typeOfGameCounter){ //on trouve l'endroit où on doit insérer la ligne du joueur
                    phrase.add(ligne+"\n");
                }
                if(text.equals("End"))
                    typeOfGameCounter++;
            }
            fis.close();

            //on prépare le remplissage du fichier retour
            String remplissage = "";
            while (!phrase.isEmpty()){
                remplissage=remplissage+(phrase.remove(0));
            }

            //on réouvre le fichier mais cette fois ci pour y écrire
            FileOutputStream fos = null;
            fos = openFileOutput(FILE_NAME,MODE_PRIVATE);
            fos.write(remplissage.getBytes());
            if(fos!=null){
                fos.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        if(gameModeSpinner.getSelectedItem().toString().equals("ApeChill")){
            typeOfGame=1;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    answerButton.setBackground(getDrawable(R.drawable.button));
                }

        }
        else{
            typeOfGame=2;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerButton.setBackground(getDrawable(R.drawable.button2));
            }
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
    @Override
    public void onBackPressed() {
        Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
        startActivity(optionActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}
