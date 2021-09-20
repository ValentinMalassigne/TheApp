package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import fr.mapoe.appproject.databinding.ActivityAddSentence2Binding;

public class AddSentenceActivity2 extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    private int maxAddPlayerText=0,maxAddAnswerText=0,typeOfGame = 1;;
    private Spinner gameModeSpinner;
    private Button buttonAnecdote;
    private Button buttonGages;
    private Button buttonMiniGames;
    private Button buttonQuestion;
    private Button sentenceEditNextButton;
    private Button answerEditNextButton;
    private Button buttonsEditNextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence2);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.add_sentence_layout));

        init();
    }

    private void init(){
        this.buttonAnecdote = findViewById(R.id.button_anecdote);
        this.buttonGages = findViewById(R.id.button_gages);
        this.buttonMiniGames = findViewById(R.id.button_mini_game);
        this.buttonQuestion = findViewById(R.id.button_question);
        this.sentenceEditNextButton = findViewById(R.id.sentence_edit_next_button);
        this.answerEditNextButton = findViewById(R.id.answer_edit_next_button);
        this.buttonsEditNextButton = findViewById(R.id.buttons_edit_next_button);
        //bouton ApeChill
        Button buttonApeChill = findViewById(R.id.button_ape_chill);
        buttonApeChill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout gameTypeLayout = findViewById(R.id.game_type_layout);
                gameTypeLayout.setVisibility(View.GONE);
                LinearLayout sentenceTypeLayout = findViewById(R.id.sentence_type_layout);
                sentenceTypeLayout.setVisibility(View.VISIBLE);
                LinearLayout scrollableGameModeLayout = findViewById(R.id.scrollable_game_mode_layout);
                scrollableGameModeLayout.setVisibility(View.VISIBLE);
                gameModeSpinner = findViewById(R.id.game_mode_spinner);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.gameModeList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameModeSpinner.setAdapter(adapter1);
                //gameModeSpinner.setOnItemSelectedListener(this);
                TextView questionTextView = findViewById(R.id.question_textView);
                questionTextView.setText(R.string.what_sentence_type);
                TextView textViewResume = findViewById(R.id.textViewResume);
                textViewResume.setVisibility(View.VISIBLE);

            }
        });

        //bouton ApePiment
        Button buttonApePiment = findViewById(R.id.button_ape_piment);
        buttonApePiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LinearLayout gameTypeLayout = findViewById(R.id.game_type_layout);
                gameTypeLayout.setVisibility(View.GONE);
                LinearLayout sentenceTypeLayout = findViewById(R.id.sentence_type_layout);
                sentenceTypeLayout.setVisibility(View.VISIBLE);
                LinearLayout scrollableGameModeLayout = findViewById(R.id.scrollable_game_mode_layout);
                scrollableGameModeLayout.setVisibility(View.VISIBLE);
                Spinner gameModeSpinner = findViewById(R.id.game_mode_spinner);
                gameModeSpinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.gameModeList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameModeSpinner.setAdapter(adapter1);
                gameModeSpinner.setSelection(1);//permet que l'élément selectionné soit le deuxième élément de la liste
                //gameModeSpinner.setOnItemSelectedListener(this);
                TextView questionTextView = findViewById(R.id.question_textView);
                questionTextView.setText(R.string.what_sentence_type);
                TextView textViewResume = findViewById(R.id.textViewResume);
                textViewResume.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    buttonAnecdote.setBackground(getDrawable(R.drawable.button2));
                }


            }
        });

        //bouton Anecdote

        buttonAnecdote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSentenceTypeSelection("Anecdote");
            }
        });

        //bouton Gages

        buttonGages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSentenceTypeSelection("Gages");
            }
        });

        //bouton MiniGame

        buttonMiniGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSentenceTypeSelection("MiniGame");
            }
        });

        //bouton Question

        buttonQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageSentenceTypeSelection("Question");
            }
        });

        //bouton Next après avoir tappé la phrase

        sentenceEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on change la visibilité des éléments nécessaire
                LinearLayout gameTypeLayout = findViewById(R.id.sentence_edit_layout);
                gameTypeLayout.setVisibility(View.GONE);
                LinearLayout sentenceTypeLayout = findViewById(R.id.answer_edit_layout);
                sentenceTypeLayout.setVisibility(View.VISIBLE);
                LinearLayout scrollableSentenceLayout = findViewById(R.id.scrollable_sentence_layout);
                scrollableSentenceLayout.setVisibility(View.VISIBLE);
                //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview
                EditText scrollableEditText = findViewById(R.id.scrollable_sentence_edit_text);
                EditText sentenceEditText = findViewById(R.id.sentence_edit_text);
                scrollableEditText.setText(sentenceEditText.getText().toString());
                //on change le text qui guide
                TextView questionTextView = findViewById(R.id.question_textView);
                questionTextView.setText("Write what should be shown in the answer pop up");

            }
        });

        //bouton Next après avoir tappé la réponse

        answerEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on change la visibilité des éléments nécessaire
                LinearLayout answerEditLayout = findViewById(R.id.answer_edit_layout);
                answerEditLayout.setVisibility(View.GONE);
                LinearLayout answerButtonLayout = findViewById(R.id.answer_button_layout);
                answerButtonLayout.setVisibility(View.VISIBLE);
                LinearLayout scrollableAnswerLayout = findViewById(R.id.scrollable_answer_layout);
                scrollableAnswerLayout.setVisibility(View.VISIBLE);
                //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview
                EditText scrollableEditText = findViewById(R.id.scrollable_answer_edit_text);
                EditText answerEditText = findViewById(R.id.answer_edit_text);
                scrollableEditText.setText(answerEditText.getText().toString());
                //on change le text qui guide
                TextView questionTextView = findViewById(R.id.question_textView);
                questionTextView.setText("Edit the buttons to give them the text you want");

            }
        });

        //bouton Next après avoir modifier les boutons

        buttonsEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on change la visibilité des éléments nécessaire
                LinearLayout answerEditLayout = findViewById(R.id.answer_button_layout);
                answerEditLayout.setVisibility(View.GONE);
                LinearLayout answerButtonLayout = findViewById(R.id.points_layout);
                answerButtonLayout.setVisibility(View.VISIBLE);
                LinearLayout scrollableAnswerLayout = findViewById(R.id.scrollable_buttons_layout);
                scrollableAnswerLayout.setVisibility(View.VISIBLE);
                //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview
                EditText scrollableEditButton1 = findViewById(R.id.scrollable_edit_button1);
                EditText scrollableEditButton2 = findViewById(R.id.scrollable_edit_button2);
                EditText editButton1 = findViewById(R.id.edit_button1);
                EditText editButton2 = findViewById(R.id.edit_button2);
                scrollableEditButton1.setText(editButton1.getText().toString());
                scrollableEditButton2.setText(editButton2.getText().toString());
                //on change le text qui guide
                TextView questionTextView = findViewById(R.id.question_textView);
                questionTextView.setText("How many points in case of right answer ?");
                //on active le spinner du nombre de points
                Spinner pointListSpinner = findViewById(R.id.point_list);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.pointList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pointListSpinner.setAdapter(adapter1);

            }
        });

        //bouton ajouter un joueur a la phrase quand on te le demande
        ImageView addPlayerToSentence = findViewById(R.id.add_player_to_sentence);
        addPlayerToSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerToSentence(findViewById(R.id.sentence_edit_text));
            }
        });

        //bouton ajouter un joueur a la réponse quand on te le demande
        ImageView addPlayerToAnswer = findViewById(R.id.add_player_to_answer);
        addPlayerToAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerToSentence(findViewById(R.id.answer_edit_text));
            }
        });

        //bouton ajouter un joueur a la phrase dans le scrollview
        ImageView scrollableAddPlayerToSentence = findViewById(R.id.scrollable_add_player_to_sentence);
        scrollableAddPlayerToSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerToSentence(findViewById(R.id.scrollable_sentence_edit_text));
            }
        });

        //bouton ajouter un joueur a la réponse dans le scrollview
        ImageView scrollableAddPlayerToAnswer = findViewById(R.id.scrollable_add_player_to_answer);
        scrollableAddPlayerToAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerToSentence(findViewById(R.id.scrollable_answer_edit_text));
            }
        });


    }

    //permet d'éviter d'écrire 4 fois le même code pour la selection du type de phrase
    private void manageSentenceTypeSelection(String usedButtonText){
        LinearLayout gameTypeLayout = findViewById(R.id.sentence_type_layout);
        gameTypeLayout.setVisibility(View.GONE);
        LinearLayout sentenceTypeLayout = findViewById(R.id.sentence_edit_layout);
        sentenceTypeLayout.setVisibility(View.VISIBLE);
        Spinner sentenceTypeSpinner = findViewById(R.id.sentence_type_spinner);
        LinearLayout scrollableSentenceTypeLayout = findViewById(R.id.scrollable_sentence_type_layout);
        scrollableSentenceTypeLayout.setVisibility(View.VISIBLE);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.typeList, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sentenceTypeSpinner.setAdapter(adapter1);
        if(usedButtonText.equals("Gages")){
            sentenceTypeSpinner.setSelection(1);//permet que l'élément selectionné soit le deuxième élément de la liste
        }else if(usedButtonText.equals("MiniGame")){
            sentenceTypeSpinner.setSelection(2);
        }else if (usedButtonText.equals("Question")){
            sentenceTypeSpinner.setSelection(3);
        }

        //gameModeSpinner.setOnItemSelectedListener(this);
        TextView questionTextView = findViewById(R.id.question_textView);
        questionTextView.setText("Write the "+usedButtonText+" in the box bellow.");
    }

    private void addPlayerToSentence(EditText EditText){
        String text = EditText.getText().toString();
        if(numberOfOccurrences(text)<3) {
            text += "--joueur--";
            EditText.setText(text);
            maxAddPlayerText ++;
        }
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

    @Override
    public void onBackPressed() {
        Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
        startActivity(optionActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}