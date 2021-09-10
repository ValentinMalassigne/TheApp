package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;

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
    private Spinner gameModeSpinner;
    private static final String FILE_NAME = "custom_sentences.txt";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);

        this.gameModeSpinner = findViewById(R.id.game_mode);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.gameModeList, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        gameModeSpinner.setAdapter(adapter1);
        gameModeSpinner.setOnItemSelectedListener(this);


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
        Button validate2 = (Button) findViewById(R.id.submit);
        ImageView infoButton =(ImageView) findViewById(R.id.info_image);
        ImageView addPlayerText = (ImageView) findViewById(R.id.add_player_text);
        ImageView addPlayerAnswer = (ImageView) findViewById(R.id.add_player_answer);

        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(R.layout.info_popup);
            }
        });
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
                    text += " --joueur-- ";
                    answerEdit.setText(text);
                    maxAddAnswerText++;
                }
            }
        });

        validate2.setOnClickListener(new View.OnClickListener() {
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
        });
        //button temp pour voir l'encodage
        validateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = textEdit.getText().toString();
                String newText = text.replace("--joueur--","§");
                String answer = answerEdit.getText().toString();
                String newAnswer = answer.replace("--joueur--","§");
                String point = pointSpinner.getSelectedItem().toString();
                String encodedSentence =encoding(point,newText,newAnswer);
                validateText.setText(encodedSentence);
                //ajout de test pour les fichiers
                String temptypeOfGame = gameModeSpinner.getSelectedItem().toString();
                if(temptypeOfGame.equals("ApePiment"))
                    typeOfGame=2;
                String type = typeModeSpinner.getSelectedItem().toString();
                addSentence(encodedSentence,type,typeOfGame);

                //test pour voir dans la consol ce que l'on ajoute
                /*FileInputStream fis= null;
                String textTest="";
                StringBuilder sb = new StringBuilder();
                try {
                    fis = openFileInput(FILE_NAME);
                    InputStreamReader isr = new InputStreamReader(fis);
                    BufferedReader br = new BufferedReader(isr);



                    while ((textTest = br.readLine())!=null){
                        sb.append(textTest).append("\n");
                    }
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.d(TAG, sb.toString());*/
            }
        });

        //on consctruit le fichier si le joueur n'en a jamais créé
        fileBuilder();

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
    private void showValidatePopup(int layout,String title, String text,String answer,String point){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        // déclarer les élements de la popup
        TextView titleDisplay = layoutView.findViewById(R.id.current_title_display);
        TextView textDisplay = layoutView.findViewById(R.id.current_text_display);
        Button answerButton = layoutView.findViewById(R.id.answer_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        if(typeOfGame ==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerButton.setBackground(getDrawable(R.drawable.button2));
            }
        }
        titleDisplay.setText(title);
        textDisplay.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                showAnswerPopup(R.layout.game_answer_popup,answer,point);
            }
        });
    }
    private void showAnswerPopup(int layout, String answer,String point){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        //recuperer les éléments de la popup
        Button yesButton = layoutView.findViewById(R.id.yes_button);
        Button noButton = layoutView.findViewById(R.id.no_button);
        Button nextButton = layoutView.findViewById(R.id.next_button);
        TextView answerText = layoutView.findViewById(R.id.text_display);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        answerText.setText(HtmlCompat.fromHtml(answer,HtmlCompat.FROM_HTML_MODE_LEGACY));
        if(typeOfGame ==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                yesButton.setBackground(getDrawable(R.drawable.button2));
                noButton.setBackground(getDrawable(R.drawable.button2));
                nextButton.setBackground(getDrawable(R.drawable.button2));
            }
        }
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text="<b><i>Joueur 1</i><b> marque "+point+" point(s)";
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                answerText.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text="<b><i>Joueur 1</i><b> bois";
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                nextButton.setVisibility(View.VISIBLE);
                answerText.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        });
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

    }
    private String encoding(String point, String text, String answer){
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
    private String getCleanText(String text) {
        String res = "";
        if (numberOfOccurrences(text) == 1) {
            res = text.replaceFirst("--joueur--", "<b><i>Joueur 1</i></b>");

        } else if (numberOfOccurrences(text) == 2) {
            res = text.replaceFirst("--joueur--", "<b><i>Joueur 1</i></b>");
            res=text.replace("--joueur--","<b><i>Joueur 2</i></b>");
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
        }
        else{
            typeOfGame=2;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
