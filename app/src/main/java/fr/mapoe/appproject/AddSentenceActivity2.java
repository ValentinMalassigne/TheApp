package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
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

import androidx.core.text.HtmlCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import fr.mapoe.appproject.databinding.ActivityAddSentence2Binding;

public class AddSentenceActivity2 extends AppCompatActivity {

    private int maxAddPlayerText=0,maxAddAnswerText=0,typeOfGame=1;
    private Button buttonAnecdote;
    private Button buttonGages;
    private Button buttonMiniGames;
    private Button buttonQuestion;
    private Button sentenceEditNextButton;
    private Button answerEditNextButton;
    private Button buttonsEditNextButton;

    private Button pointNextButton;
    private EditText editButton1;
    private EditText editButton2;
    private EditText scrollableEditButton1;
    private EditText scrollableEditButton2;
    private LinearLayout gameTypeLayout;
    private LinearLayout sentenceTypeLayout;
    private LinearLayout scrollableGameModeLayout;
    private Spinner gameModeSpinner;
    private TextView questionTextView;
    private TextView textViewResume;
    private Spinner sentenceTypeSpinner;
    private LinearLayout scrollableSentenceTypeLayout;
    private LinearLayout sentenceEditLayout;
    private LinearLayout answerEditLayout;
    private LinearLayout scrollableSentenceLayout;
    private EditText scrollableEditText;
    private LinearLayout answerButtonLayout;
    private LinearLayout scrollableAnswerLayout;
    private EditText scrollableAnswerEditText;
    private LinearLayout pointLayout;
    private LinearLayout scrollableButtonLayout;
    private Spinner pointListSpinner;
    private LinearLayout scrollablePointLayout;
    private Spinner scrollablePointList;
    private Button visualizeButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence2);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.add_sentence_layout));
        ImageView infoButton = findViewById(R.id.info_image);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(R.layout.info_popup);
            }
        });
        init();
        start();
    }
    //déclaration
    private void init(){
        this.buttonAnecdote = findViewById(R.id.button_anecdote);
        this.buttonGages = findViewById(R.id.button_gages);
        this.buttonMiniGames = findViewById(R.id.button_mini_game);
        this.buttonQuestion = findViewById(R.id.button_question);
        this.sentenceEditNextButton = findViewById(R.id.sentence_edit_next_button);
        this.answerEditNextButton = findViewById(R.id.answer_edit_next_button);
        this.buttonsEditNextButton = findViewById(R.id.buttons_edit_next_button);
        this.editButton1 = findViewById(R.id.edit_button1);
        this.editButton2 = findViewById(R.id.edit_button2);
        this.scrollableEditButton1 = findViewById(R.id.scrollable_edit_button1);
        this.scrollableEditButton2 = findViewById(R.id.scrollable_edit_button2);
        this.pointNextButton = findViewById(R.id.next_point_button);
        this.visualizeButton = findViewById(R.id.visualize_button);
        // affichage du haut
        this.questionTextView = findViewById(R.id.question_textView);
        this.gameTypeLayout = findViewById(R.id.game_type_layout);
        this.sentenceTypeLayout = findViewById(R.id.sentence_type_layout);
        this.sentenceEditLayout = findViewById(R.id.sentence_edit_layout);
        this.answerEditLayout = findViewById(R.id.answer_edit_layout);
        this.answerButtonLayout = findViewById(R.id.answer_button_layout);
        this.pointLayout = findViewById(R.id.point_layout);
        this.pointListSpinner = findViewById(R.id.point_list);

        //affichage du scrollView
        this.scrollableGameModeLayout = findViewById(R.id.scrollable_game_mode_layout);
        this.gameModeSpinner = findViewById(R.id.game_mode_spinner);
        this.textViewResume = findViewById(R.id.textViewResume);
        this.sentenceTypeSpinner = findViewById(R.id.sentence_type_spinner);
        this.scrollableSentenceTypeLayout = findViewById(R.id.scrollable_sentence_type_layout);
        this.scrollableSentenceLayout = findViewById(R.id.scrollable_sentence_layout);
        this.scrollableEditText = findViewById(R.id.scrollable_sentence_edit_text);
        this.scrollableAnswerLayout = findViewById(R.id.scrollable_answer_layout);
        this.scrollableAnswerEditText = findViewById(R.id.scrollable_answer_edit_text);
        this.scrollableButtonLayout = findViewById(R.id.scrollable_buttons_layout);
        this.scrollablePointLayout = findViewById(R.id.scrollable_point_layout);
        this.scrollablePointList = findViewById(R.id.scrollable_point_list);
    }

    private void start(){

        //bouton ApeChill
        Button buttonApeChill = findViewById(R.id.button_ape_chill);
        buttonApeChill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeOfGame=1;
                gameTypeLayout.setVisibility(View.GONE);
                sentenceTypeLayout.setVisibility(View.VISIBLE);

                scrollableGameModeLayout.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.gameModeList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameModeSpinner.setAdapter(adapter1);
                gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(gameModeSpinner.getSelectedItem().toString().equals("ApeChill")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                buttonAnecdote.setBackground(getDrawable(R.drawable.button));
                                buttonGages.setBackground(getDrawable(R.drawable.button));
                                buttonQuestion.setBackground(getDrawable(R.drawable.button));
                                buttonMiniGames.setBackground(getDrawable(R.drawable.button));
                                sentenceEditNextButton.setBackground(getDrawable(R.drawable.button));
                                answerEditNextButton.setBackground(getDrawable(R.drawable.button));
                                buttonsEditNextButton.setBackground(getDrawable(R.drawable.button));
                                editButton1.setBackground(getDrawable(R.drawable.button));
                                editButton2.setBackground(getDrawable(R.drawable.button));
                                scrollableEditButton1.setBackground(getDrawable(R.drawable.button));
                                scrollableEditButton2.setBackground(getDrawable(R.drawable.button));
                                visualizeButton.setBackground(getDrawable(R.drawable.button));
                                pointNextButton.setBackground(getDrawable(R.drawable.button));
                                typeOfGame=1;
                            }
                        }
                        else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                buttonAnecdote.setBackground(getDrawable(R.drawable.button2));
                                buttonGages.setBackground(getDrawable(R.drawable.button2));
                                buttonQuestion.setBackground(getDrawable(R.drawable.button2));
                                buttonMiniGames.setBackground(getDrawable(R.drawable.button2));
                                sentenceEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                answerEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                buttonsEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                editButton1.setBackground(getDrawable(R.drawable.button2));
                                editButton2.setBackground(getDrawable(R.drawable.button2));
                                scrollableEditButton1.setBackground(getDrawable(R.drawable.button2));
                                scrollableEditButton2.setBackground(getDrawable(R.drawable.button2));
                                visualizeButton.setBackground(getDrawable(R.drawable.button2));
                                pointNextButton.setBackground(getDrawable(R.drawable.button2));
                                typeOfGame=2;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                questionTextView.setText(R.string.what_sentence_type);
                textViewResume.setVisibility(View.VISIBLE);
            }
        });

        //bouton ApePiment
        Button buttonApePiment = findViewById(R.id.button_ape_piment);
        buttonApePiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeOfGame=2;
                gameTypeLayout.setVisibility(View.GONE);

                sentenceTypeLayout.setVisibility(View.VISIBLE);

                scrollableGameModeLayout.setVisibility(View.VISIBLE);

                gameModeSpinner.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.gameModeList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameModeSpinner.setAdapter(adapter1);
                gameModeSpinner.setSelection(1);//permet que l'élément selectionné soit le deuxième élément de la liste
                gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        if(gameModeSpinner.getSelectedItem().toString().equals("ApePiment")) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                buttonAnecdote.setBackground(getDrawable(R.drawable.button2));
                                buttonGages.setBackground(getDrawable(R.drawable.button2));
                                buttonQuestion.setBackground(getDrawable(R.drawable.button2));
                                buttonMiniGames.setBackground(getDrawable(R.drawable.button2));
                                sentenceEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                answerEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                buttonsEditNextButton.setBackground(getDrawable(R.drawable.button2));
                                editButton1.setBackground(getDrawable(R.drawable.button2));
                                editButton2.setBackground(getDrawable(R.drawable.button2));
                                scrollableEditButton1.setBackground(getDrawable(R.drawable.button2));
                                scrollableEditButton2.setBackground(getDrawable(R.drawable.button2));
                                visualizeButton.setBackground(getDrawable(R.drawable.button2));
                                pointNextButton.setBackground(getDrawable(R.drawable.button2));
                                typeOfGame=2;

                            }
                        }
                        else{
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                buttonAnecdote.setBackground(getDrawable(R.drawable.button));
                                buttonGages.setBackground(getDrawable(R.drawable.button));
                                buttonQuestion.setBackground(getDrawable(R.drawable.button));
                                buttonMiniGames.setBackground(getDrawable(R.drawable.button));
                                sentenceEditNextButton.setBackground(getDrawable(R.drawable.button));
                                answerEditNextButton.setBackground(getDrawable(R.drawable.button));
                                buttonsEditNextButton.setBackground(getDrawable(R.drawable.button));
                                editButton1.setBackground(getDrawable(R.drawable.button));
                                editButton2.setBackground(getDrawable(R.drawable.button));
                                scrollableEditButton1.setBackground(getDrawable(R.drawable.button));
                                scrollableEditButton2.setBackground(getDrawable(R.drawable.button));
                                visualizeButton.setBackground(getDrawable(R.drawable.button));
                                pointNextButton.setBackground(getDrawable(R.drawable.button));
                                typeOfGame=1;
                            }
                        }
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                questionTextView.setText(R.string.what_sentence_type);
                textViewResume.setVisibility(View.VISIBLE);
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
                EditText sentenceEditText = findViewById(R.id.sentence_edit_text);
                String text = sentenceEditText.getText().toString();
                if(!text.equals("")) {
                    sentenceEditLayout.setVisibility(View.GONE);
                    answerEditLayout.setVisibility(View.VISIBLE);
                    scrollableSentenceLayout.setVisibility(View.VISIBLE);
                    //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview

                    scrollableEditText.setText(text);
                    //on change le text qui guide
                    questionTextView.setText("Write what should be shown in the answer pop up");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Veuillez remplir le champ",Toast.LENGTH_SHORT).show();
                }
            }
        });

        //bouton Next après avoir tappé la réponse

        answerEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on change la visibilité des éléments nécessaire
                EditText answerEditText = findViewById(R.id.answer_edit_text);
                String answer = answerEditText.getText().toString();
                if(!answer.equals("")) {
                    answerEditLayout.setVisibility(View.GONE);
                    answerButtonLayout.setVisibility(View.VISIBLE);
                    scrollableAnswerLayout.setVisibility(View.VISIBLE);
                    //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview

                    scrollableAnswerEditText.setText(answer);
                    //on change le text qui guide
                    questionTextView = findViewById(R.id.question_textView);
                    questionTextView.setText("Edit the buttons to give them the text you want");
                }
                else{
                    Toast.makeText(getApplicationContext(),"Veuillez remplir le champ",Toast.LENGTH_SHORT).show();
                }
            }
        });
        //bouton Next après avoir modifier les boutons
        buttonsEditNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //on change la visibilité des éléments nécessaire
                answerButtonLayout.setVisibility(View.GONE);
                pointLayout.setVisibility(View.VISIBLE);
                scrollableButtonLayout.setVisibility(View.VISIBLE);
                //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview
                scrollableEditButton1.setText(editButton1.getText().toString());
                scrollableEditButton2.setText(editButton2.getText().toString());
                //on change le text qui guide
                questionTextView.setText("How many points in case of right answer ?");
                //on active le spinner du nombre de points
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.pointList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                pointListSpinner.setAdapter(adapter1);


            }
        });

        // button Next après avoir choisis les points
        pointNextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pointLayout.setVisibility(View.GONE);
                scrollablePointLayout.setVisibility(View.VISIBLE);
                visualizeButton.setVisibility(View.VISIBLE);
                questionTextView.setText("Check every information and press next !");
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.pointList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                scrollablePointList.setAdapter(adapter1);

            }
        });
        //button pour visualiser la phrase
        visualizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = scrollableEditText.getText().toString();
                String answer = scrollableAnswerEditText.getText().toString();
                if (numberOfOccurrences(text) == 0) {
                    text = "--joueur--" + " " + text;
                }
                text = getCleanText(text);
                answer = getCleanText(answer);
                String title = sentenceTypeSpinner.getSelectedItem().toString();
                String point = scrollablePointList.getSelectedItem().toString();
                showValidatePopup(R.layout.visualisation_popup, title, text, answer, point);

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
        sentenceTypeLayout.setVisibility(View.GONE);
        sentenceEditLayout.setVisibility(View.VISIBLE);
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
        questionTextView.setText("Write the "+usedButtonText+" in the box bellow.");
    }
    private void showInfoPopup(int layout){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity2.this);
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button okButton = layoutView.findViewById(R.id.ok_button);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        ImageView nextArrow = layoutView.findViewById(R.id.right_popup_arrow);
        ImageView previousArrow = layoutView.findViewById(R.id.left_popup_arrow);
        final int[] pageCpt = {1};
        CheckBox checkBox = layoutView.findViewById(R.id.block_popup_checkBox);
        textInfo.setText("Page 1 ");
        okButton.setVisibility(View.INVISIBLE);
        imageInfo.setVisibility(View.GONE);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        checkBox.setVisibility(View.INVISIBLE);
        imageInfo.setVisibility(View.GONE);
        previousArrow.setVisibility(View.INVISIBLE);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageCpt[0] ==1){// passage à la page 2
                    textInfo.setText("Page 2 ");
                    previousArrow.setVisibility(View.VISIBLE);
                    pageCpt[0]++;
                }
                else if(pageCpt[0] ==2){ // passage la page 3
                    textInfo.setText("Page 3");
                    nextArrow.setImageResource(R.drawable.check);
                    pageCpt[0]++;
                }
                else{
                    alertDialog.dismiss();
                }
            }
        });
        previousArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageCpt[0]==1){

                }
                else if(pageCpt[0]==2){// retour à la page 1
                    textInfo.setText("Page 1");
                    previousArrow.setVisibility(View.INVISIBLE);
                    pageCpt[0]--;
                }
                else{ //retour page 2
                    textInfo.setText("Page 2");
                    nextArrow.setImageResource(R.drawable.right_arrow);
                    pageCpt[0]--;
                }
            }
        });

    }
    private void addPlayerToSentence(EditText editText){
        String text = editText.getText().toString();
        if(numberOfOccurrences(text)<3) {
            text += "--joueur--";
            editText.setText(text);
            editText.setSelection(editText.getText().length());
            maxAddPlayerText ++;
        }
    }

    private void showValidatePopup(int layout,String title, String text,String answer,String point){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity2.this);
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
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity2.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        //recuperer les éléments de la popup
        Button yesButton = layoutView.findViewById(R.id.yes_button);
        Button noButton = layoutView.findViewById(R.id.no_button);
        Button addButton = layoutView.findViewById(R.id.next_button);
        Button editButton = layoutView.findViewById(R.id.edit_button);
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
                addButton.setBackground(getDrawable(R.drawable.button2));
            }
        }
        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text="<b><i>Joueur 1</i><b> marque "+point+" point(s)";
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                answerText.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text="<b><i>Joueur 1</i><b> bois";
                yesButton.setVisibility(View.GONE);
                noButton.setVisibility(View.GONE);
                addButton.setVisibility(View.VISIBLE);
                answerText.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String encoding = encoding();
                answerText.setText(encoding);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
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
            res = res.replaceFirst("<b><i>Joueur 3</i></b>","<b><i>Joueur 2</i></b>");
        }
        return res;
    }
    private String encoding(){
            String text = scrollableEditText.getText().toString().replace("--joueur--","§");
            String answer = scrollableAnswerEditText.getText().toString().replace("--joueur--","§");
            String encodingSentence = "";
            encodingSentence+=scrollablePointList.getSelectedItem().toString()+ " ";
            encodingSentence+=answer+" "+"ç";
            encodingSentence+=text;
            return encodingSentence;
    }
    @Override
    public void onBackPressed() {
        Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
        startActivity(optionActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}