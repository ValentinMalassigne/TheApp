package fr.mapoe.appproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.text.HtmlCompat;

import fr.mapoe.appproject.sqlite.DataBaseManager;
import fr.mapoe.appproject.tools.ThemeManager;


public class AddSentenceActivity extends AppCompatActivity {

    private int maxAddPlayerText=0,maxAddAnswerText=0,typeOfGame=1,rightAnswer=0;//rightAnswer=0 veut dire que la bonne réponse est celle de gauche
    private Button sentenceEditNextButton;
    private Button buttonsEditNextButton;


    private LinearLayout gameTypeLayout;
    private LinearLayout scrollableGameModeLayout;
    private Spinner gameModeSpinner;
    private TextView questionTextView;
    private TextView textViewResume;
    private LinearLayout sentenceEditLayout;
    private LinearLayout scrollableSentenceLayout;
    private EditText scrollableSentenceEditText;
    private LinearLayout answerButtonLayout;
    private LinearLayout pointLayout;
    private LinearLayout scrollablePointLayout;
    private Spinner scrollablePointList;
    private Button visualizeButton;
    private boolean editSentence = false;
    private String[] decodedSentence;
    private Button button1Point;
    private Button button2Points;
    private Button button3Points;
    private Button button4Points;
    private Drawable buttonDrawable;
    private String color;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sentence);
        init();
        // recuperer les données
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editSentence = extras.getBoolean("editSentence");
            decodedSentence = extras.getStringArray("decodedSentence");//0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7:typeOfGame 8: la punition        }
        }

        ImageView infoButton = findViewById(R.id.info_image);
        infoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInfoPopup(R.layout.info_popup);
            }
        });
        start();
    }
    //déclaration
    private void init(){
        SharedPreferences sharedPreferences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        color = sharedPreferences.getString("theme","");
        ThemeManager themeManager = new ThemeManager(this,color);
        this.buttonDrawable = themeManager.getButtonDrawable();
        this.sentenceEditNextButton = findViewById(R.id.sentence_edit_next_button);

        this.visualizeButton = findViewById(R.id.visualize_button);
        sentenceEditNextButton.setBackground(buttonDrawable);

        // affichage du haut
        this.questionTextView = findViewById(R.id.question_textView);
        this.gameTypeLayout = findViewById(R.id.game_type_layout);
        this.sentenceEditLayout = findViewById(R.id.sentence_edit_layout);

        this.pointLayout = findViewById(R.id.point_layout);
        this.button1Point = findViewById(R.id.button_1_point);
        this.button2Points = findViewById(R.id.button_2_points);
        this.button3Points = findViewById(R.id.button_3_points);
        this.button4Points = findViewById(R.id.button_4_points);


        //affichage du scrollView
        this.scrollableGameModeLayout = findViewById(R.id.scrollable_game_mode_layout);
        this.gameModeSpinner = findViewById(R.id.game_mode_spinner);
        this.textViewResume = findViewById(R.id.textViewResume);
        this.scrollableSentenceLayout = findViewById(R.id.scrollable_sentence_layout);
        this.scrollableSentenceEditText = findViewById(R.id.scrollable_sentence_edit_text);

        this.scrollablePointLayout = findViewById(R.id.scrollable_point_layout);
        this.scrollablePointList = findViewById(R.id.scrollable_point_list);

        ConstraintLayout constraintLayout = findViewById(R.id.add_sentence_layout);
        constraintLayout.setBackground(themeManager.getBackgroundDrawable());
    }

    private void start(){

        //ouverture du fichier
        //situation ou l'on ouvre cette activity pour modifier une phrase

        //bouton ApeChill
        Button buttonApeChill = findViewById(R.id.button_ape_chill);
        buttonApeChill.setBackground(buttonDrawable);
        buttonApeChill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                typeOfGame=1;
                gameTypeLayout.setVisibility(View.GONE);
                sentenceEditLayout.setVisibility(View.VISIBLE);

                scrollableGameModeLayout.setVisibility(View.VISIBLE);
                ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                        R.array.gameModeList, R.layout.spinner_item);
                adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                gameModeSpinner.setAdapter(adapter1);
                gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        setButtonBg(gameModeSpinner.getSelectedItem().toString(),color);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                questionTextView.setText(R.string.complete);
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

                sentenceEditLayout.setVisibility(View.VISIBLE);

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
                        setButtonBg(gameModeSpinner.getSelectedItem().toString(),color);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                questionTextView.setText(R.string.what_sentence_type);
                textViewResume.setVisibility(View.VISIBLE);
            }
        });

        //situation ou l'on ouvre cette activity pour modifier une phrase
        if(editSentence){
            questionTextView.setText(getString(R.string.modify_what_you_want));
            buttonApeChill.setVisibility(View.GONE);
            buttonApePiment.setVisibility(View.GONE);
            visualizeButton.setVisibility(View.VISIBLE);
            scrollableGameModeLayout.setVisibility(View.VISIBLE);
            scrollableSentenceLayout.setVisibility(View.VISIBLE);
            scrollablePointLayout.setVisibility(View.VISIBLE);
            scrollableGameModeLayout.setVisibility(View.VISIBLE);

            ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                    R.array.gameModeList, R.layout.spinner_item);
            adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            gameModeSpinner.setAdapter(adapter1);
            gameModeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    setButtonBg(gameModeSpinner.getSelectedItem().toString(),color);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(getApplicationContext(),
                    R.array.pointList, R.layout.spinner_item);
            adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            scrollablePointList.setAdapter(adapter3);

            //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7:typeOfGame
            if(decodedSentence[7].equals("ApePiment")){
                gameModeSpinner.setSelection(1);
            }

            decodedSentence[2]= decodedSentence[2].replaceAll("§",getString(R.string.player_landmark));
            decodedSentence[1]=decodedSentence[1].replaceAll("§",getString(R.string.player_landmark));
            scrollableSentenceEditText.setText(decodedSentence[2]);




            scrollablePointList.setSelection(Integer.parseInt(decodedSentence[0])-1);
            visualizeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String text = scrollableSentenceEditText.getText().toString();
                    if (numberOfOccurrences(text) == 0) {
                        text = getString(R.string.player_landmark) + " " + text;
                    }
                    text = getCleanText(text);

                    String point = scrollablePointList.getSelectedItem().toString();
                    showValidatePopup(R.layout.visualisation_popup, "Custom", text,point);
                }
            });
        }
        else {
            //bouton Next après avoir tappé la phrase

            sentenceEditNextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //on change la visibilité des éléments nécessaire
                    EditText sentenceEditText = findViewById(R.id.sentence_edit_text);
                    String text = sentenceEditText.getText().toString();
                    if (!text.equals("")) {
                        sentenceEditLayout.setVisibility(View.GONE);
                        pointLayout.setVisibility(View.VISIBLE);
                        scrollableSentenceLayout.setVisibility(View.VISIBLE);
                        //on transfert ce qu'il a écrit dans la case answer dans la case qui est dans le scrollview
                        scrollableSentenceEditText.setText(text);
                        //on change le text qui guide
                        //questionTextView.setText(R.string.edit_buttons_and_select_correct_answer);
                        questionTextView.setText(R.string.point_case_right_answer);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    } else {
                        Toast.makeText(getApplicationContext(), "Veuillez remplir le champ", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }


        //bouton 1 point
        button1Point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managePointsSelection(1);
            }
        });

        //bouton 2 points
        button2Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managePointsSelection(2);
            }
        });

        //bouton 3 points
        button3Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managePointsSelection(3);
            }
        });

        //bouton 4 points
        button4Points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                managePointsSelection(4);
            }
        });
        //button pour visualiser la phrase
        visualizeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = scrollableSentenceEditText.getText().toString();
                if (numberOfOccurrences(text) == 0) {
                    text = getString(R.string.player_landmark) + " " + text;
                }
                text = getCleanText(text);

                String point = scrollablePointList.getSelectedItem().toString();
                showValidatePopup(R.layout.visualisation_popup, "Custom", text, point);
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


        //bouton ajouter un joueur a la phrase dans le scrollview
        ImageView scrollableAddPlayerToSentence = findViewById(R.id.scrollable_add_player_to_sentence);
        scrollableAddPlayerToSentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayerToSentence(findViewById(R.id.scrollable_sentence_edit_text));
            }
        });


    }

    private void setButtonBg(String gameMode,String color){

        if(gameMode.equals("ApeChill")){
            Drawable roundBtnDrawable = getRoundButtonBackground(color);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sentenceEditNextButton.setBackground(buttonDrawable);
                visualizeButton.setBackground(buttonDrawable);
                button1Point.setBackground(roundBtnDrawable);
                button2Points.setBackground(roundBtnDrawable);
                button3Points.setBackground(roundBtnDrawable);
                button4Points.setBackground(roundBtnDrawable);
            }
        }
        else{

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sentenceEditNextButton.setBackground(getDrawable(R.drawable.button_apepiment));
                visualizeButton.setBackground(getDrawable(R.drawable.button_apepiment));
                button1Point.setBackground(getDrawable(R.drawable.round_button_apepiment));
                button2Points.setBackground(getDrawable(R.drawable.round_button_apepiment));
                button3Points.setBackground(getDrawable(R.drawable.round_button_apepiment));
                button4Points.setBackground(getDrawable(R.drawable.round_button_apepiment));

            }
        }
    }

    private void managePointsSelection(int pointsAmount){
        pointLayout.setVisibility(View.GONE);
        scrollablePointLayout.setVisibility(View.VISIBLE);
        visualizeButton.setVisibility(View.VISIBLE);
        questionTextView.setText(R.string.check_informations);

        //setup du spinner dans le scrollView (ne pas oublier de lui donner la bonne valeur
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.pointList, R.layout.spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        scrollablePointList.setAdapter(adapter1);

        if(pointsAmount==2){
            scrollablePointList.setSelection(1);//permet que l'élément selectionné soit le deuxième élément de la liste
        }else if(pointsAmount==3){
            scrollablePointList.setSelection(2);
        }else if (pointsAmount==4){
            scrollablePointList.setSelection(3);
        }

    }

    private void showInfoPopup(int layout){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);

        Button okButton = layoutView.findViewById(R.id.ok_button);
        okButton.setBackground(buttonDrawable);
        TextView textInfo = layoutView.findViewById(R.id.text_info);
        TextView bonusText = layoutView.findViewById(R.id.bonus_text);
        ImageView imageInfo = layoutView.findViewById(R.id.image_info);
        ImageView nextArrow = layoutView.findViewById(R.id.right_popup_arrow);
        ImageView previousArrow = layoutView.findViewById(R.id.left_popup_arrow);
        final int[] pageCpt = {1};
        CheckBox checkBox = layoutView.findViewById(R.id.block_popup_checkBox);
        okButton.setVisibility(View.INVISIBLE);
        imageInfo.setVisibility(View.GONE);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        checkBox.setVisibility(View.INVISIBLE);
        imageInfo.setVisibility(View.GONE);
        previousArrow.setVisibility(View.INVISIBLE);
        String text1 = getString(R.string.first_text_info_addSentence);
        String text2 = getString(R.string.second_text_info_addSentence1);
        String text3 = getString(R.string.third_text_info_addSentence);
        textInfo.setText(text1);
        nextArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(pageCpt[0] ==1){// passage à la page 2
                    textInfo.setText(text2);
                    imageInfo.setVisibility(View.VISIBLE);
                    imageInfo.setImageResource(R.drawable.add_player);
                    bonusText.setVisibility(View.VISIBLE);
                    previousArrow.setVisibility(View.VISIBLE);
                    pageCpt[0]++;
                }
                else if(pageCpt[0] ==2){ // passage la page 3
                    textInfo.setText(text3);
                    imageInfo.setVisibility(View.GONE);
                    nextArrow.setImageResource(R.drawable.check);
                    bonusText.setVisibility(View.GONE);
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
                    textInfo.setText(text1);
                    previousArrow.setVisibility(View.INVISIBLE);
                    imageInfo.setVisibility(View.GONE);
                    bonusText.setVisibility(View.GONE);
                    pageCpt[0]--;
                }
                else{ //retour page 2
                    textInfo.setText(text2);
                    nextArrow.setImageResource(R.drawable.right_arrow);
                    imageInfo.setVisibility(View.VISIBLE);
                    imageInfo.setImageResource(R.drawable.add_player);
                    bonusText.setVisibility(View.VISIBLE);
                    pageCpt[0]--;
                }
            }
        });

    }

    private void addPlayerToSentence(EditText editText){
        String text = editText.getText().toString();
        int nbOccurrence = numberOfOccurrences(text);
        if(nbOccurrence<3) {
            String player = "{ "+getString(R.string.player_landmark)+" }";
            text += player;
            editText.setText(text);
            editText.setSelection(editText.getText().length());
            maxAddPlayerText ++;
        }
    }

    private void showValidatePopup(int layout,String title, String text,String point){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        // déclarer les élements de la popup
        TextView titleDisplay = layoutView.findViewById(R.id.current_title_display);
        TextView textDisplay = layoutView.findViewById(R.id.current_text_display);
        Button answerButton = layoutView.findViewById(R.id.answer_button);
        answerButton.setBackground(buttonDrawable);
        ImageView skip_button = layoutView.findViewById(R.id.skip_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        if(typeOfGame ==2){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                answerButton.setBackground(getDrawable(R.drawable.button_apepiment));
            }
        }
        titleDisplay.setText(title);
        textDisplay.setText(HtmlCompat.fromHtml(text,HtmlCompat.FROM_HTML_MODE_LEGACY));
        answerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String text=getString(R.string.player1_scores)+point+getString(R.string.point);
                showAnswerPopup(R.layout.game_answer_popup,text);
            }
        });
        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String text=getString(R.string.player1_drink_x_sips);
                showAnswerPopup(R.layout.game_answer_popup,text);
            }
        });
    }

    private void showAnswerPopup(int layout, String answer){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddSentenceActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        //recuperer les éléments de la popup
        Button yesButton = layoutView.findViewById(R.id.left_button);
        Button noButton = layoutView.findViewById(R.id.right_button);
        Button addButton = layoutView.findViewById(R.id.add_button);
        Button nextButton = layoutView.findViewById(R.id.next_button);
        Button modifySentenceButton = layoutView.findViewById(R.id.modify_sentence_button);
        yesButton.setBackground(buttonDrawable);
        noButton.setBackground(buttonDrawable);
        addButton.setBackground(buttonDrawable);
        nextButton.setBackground(buttonDrawable);
        modifySentenceButton.setBackground(buttonDrawable);
        ImageView xButton = layoutView.findViewById(R.id.x_answer_button);
        ImageView leftArrow = layoutView.findViewById(R.id.left_answer_arrow);
        TextView answerText = layoutView.findViewById(R.id.text_display);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        answerText.setText(HtmlCompat.fromHtml(answer,HtmlCompat.FROM_HTML_MODE_LEGACY));

        //on cache yes et no, on affiche next
        yesButton.setVisibility(View.GONE);
        noButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.VISIBLE);
        leftArrow.setVisibility(View.VISIBLE);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addButton.setVisibility(View.VISIBLE);
                answerText.setText(HtmlCompat.fromHtml(getString(R.string.confirm_add_sentence),HtmlCompat.FROM_HTML_MODE_LEGACY));
                nextButton.setVisibility(View.GONE);
                modifySentenceButton.setVisibility(View.VISIBLE);
                leftArrow.setVisibility(View.GONE);
                if(editSentence){
                    answerText.setText(R.string.confirm_or_keep_editing);
                    addButton.setText(R.string.modify);
                }
            }
        });

        modifySentenceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });

        leftArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                String text = scrollableSentenceEditText.getText().toString();
                if (numberOfOccurrences(text) == 0) {
                    text = getString(R.string.player_landmark) + " " + text;
                }
                text = getCleanText(text);
                String point = scrollablePointList.getSelectedItem().toString();
                showValidatePopup(R.layout.visualisation_popup, "Custom", text, point);
            }
        });


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int typeOfGame = 2;
                if(gameModeSpinner.getSelectedItem().toString().equals("ApeChill")){
                    typeOfGame=1;
                }
                String[] encoding = encoding(typeOfGame);
                addSentence(encoding);
                alertDialog.dismiss();
                Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });
        xButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }

    private int numberOfOccurrences(String source) {
        int occurrences = 0;

        if (source.contains(getString(R.string.player_landmark))) {
            int withSentenceLength    = source.length();
            int withoutSentenceLength = source.replace(getString(R.string.player_landmark), "").length();
            occurrences = (withSentenceLength - withoutSentenceLength) / getString(R.string.player_landmark).length();
        }

        return occurrences;
    }

    private String getCleanText(String text) {
        String res = "";
        if (numberOfOccurrences(text) == 1) {
            res = text.replaceFirst(getString(R.string.player_landmark), getString(R.string.player1_bold_italic));

        } else if (numberOfOccurrences(text) == 2) {
            res = text.replaceFirst(getString(R.string.player_landmark), getString(R.string.player1_bold_italic));
            res=res.replace(getString(R.string.player_landmark),getString(R.string.player2_bold_italic));
        }
        else{
            res = text.replaceFirst(getString(R.string.player_landmark), getString(R.string.player1_bold_italic));
            res=res.replace(getString(R.string.player_landmark),getString(R.string.player3_bold_italic));
            res = res.replaceFirst(getString(R.string.player3_bold_italic),getString(R.string.player2_bold_italic));
        }
        return res;
    }
    //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame
    private String[] encoding(int typeOfGame){
        String button1 = "skip";
        String button2 = "skip";
        String text = scrollableSentenceEditText.getText().toString().replace(getString(R.string.player_landmark),"§");
        String answer = "";
        String[] encodingSentence = new String[9];

        encodingSentence[0]=scrollablePointList.getSelectedItem().toString();
        encodingSentence[1]=answer;
        encodingSentence[2]=text;
        encodingSentence[3]="custom";
        if(rightAnswer==0){
            encodingSentence[4]="left";
        }
        else{
            encodingSentence[4]="right";
        }
        encodingSentence[5]=button1;
        encodingSentence[6]=button2;
        encodingSentence[7]=getString(R.string.default_custom_sentence_punition);//il faudrait donner une punition custom
        if(typeOfGame==1){
            encodingSentence[8]="ApeChill";
        }else{
            encodingSentence[8]="ApePiment";
        }
        return encodingSentence;
    }

    private void addSentence(String[] ligne){
        DataBaseManager dataBaseManager = new DataBaseManager();
        String language="CUSTOM";
        if(editSentence){
            dataBaseManager.updateSentenceInDB(language,ligne,decodedSentence[2].replace(getString(R.string.player_landmark),"§"),getApplicationContext());
        }else{
            dataBaseManager.addSentenceToDB(language,ligne,getApplicationContext());
        }
        SharedPreferences customSentences = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = customSentences.edit();
        editor.putBoolean("customSentences",true);
        editor.apply();
    }
    private Drawable getRoundButtonBackground(String color){
        Drawable drawable = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if(color.equals("blue"))
            drawable =getDrawable(R.drawable.round_button_blue);
        else if (color.equals("green"))
            drawable = getDrawable(R.drawable.roud_button_green);
        else
            drawable = getDrawable(R.drawable.round_button_red);
            }
        return drawable;
    }

    @Override
    public void onBackPressed() {
        Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
        startActivity(optionActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}