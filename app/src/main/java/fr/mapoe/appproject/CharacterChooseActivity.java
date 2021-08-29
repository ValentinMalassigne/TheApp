package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;



public class CharacterChooseActivity extends AppCompatActivity {

    private Button addPlayer;
    private int nbJoueurs = 0; // stock egalement l'id de chaque joueur
    private LinearLayout scrollViewLayout;
    private LinearLayout containerLayout;
    private Button goToMenu;
    private Button goToGame;
    private Activity activity;
    public String[] tempTab = new String[10]; // liste temporaire pour stocker si le joueur bois ou pas
    public static String[][] tabJoueurs;
    private int idLayouts=100;
    private int idImageButtons=200;
    private boolean[] onLoseFocusHistory = new boolean[10];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choose);
        
        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);

        for(int i=0;i<tempTab.length;i++){
            tempTab[i] = "drink2";
            onLoseFocusHistory[i]= false;
        }
        this.scrollViewLayout =(LinearLayout) findViewById(R.id.myDynamicLayout);

        this.addPlayer= (Button) findViewById(R.id.add_player_button);

        this.activity = this;

        CharacterChooseActivity characterChooseActivity = this;



        while(nbJoueurs<2){
            nbJoueurs++;
            idLayouts++;
            idImageButtons++;

            //ajout d'un horizontal layout au linear layout du scrollView
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setId(idLayouts);//id du premier layout : 101
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            scrollViewLayout.addView(linearLayout);

            //ajout du text et du bouton au horizontal layout
            EditText editText = new EditText(getApplicationContext());
            editText.setHint("Nom joueur "+nbJoueurs);
            editText.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,8));
            editText.setId(nbJoueurs); //id du premier editText : 1
            editText.setTypeface(typeface);
            createPopupOnLoseFocus(editText,nbJoueurs);
            ImageButton imageButton = new ImageButton(getApplicationContext());
            imageButton.setId(idImageButtons);//id du premier bouton : 201
            imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            imageButton.setBackgroundColor(Color.TRANSPARENT);
            imageButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150,2));
            imageButton.setImageResource(R.drawable.drink_2);
            //rendre l'image clickable
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    createPopup(editText,editText.getId());
                }
            });
            characterChooseActivity.containerLayout =(LinearLayout) findViewById(idLayouts);
            containerLayout.addView(editText);
            containerLayout.addView(imageButton);
        }

        // ajout des EditText à chaque clique
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (nbJoueurs <= 10) {

                    EditText previousEditText=findViewById(nbJoueurs);
                    int one = 1;
                    EditText editText1 = findViewById(one);
                    if (previousEditText.getText().toString().equals("") || editText1.getText().toString().equals("")){ // le champ est vide
                        Toast.makeText(getApplicationContext(), "Veuillez remplir le champ precedent", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        nbJoueurs++;
                        idLayouts++;
                        idImageButtons++;
                        //ajout d'un horizontal layout au linear layout du scrollView
                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                        linearLayout.setId(idLayouts);//id du premier layout : 101
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        scrollViewLayout.addView(linearLayout);

                        //ajout du text et du bouton au horizontal layout
                        EditText editText = new EditText(getApplicationContext());
                        editText.setHint("Nom joueur "+nbJoueurs);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,8));
                        editText.setId(nbJoueurs); //id du premier editText : 1
                        editText.setTypeface(typeface);
                        createPopupOnLoseFocus(editText,nbJoueurs);
                        ImageButton imageButton = new ImageButton(getApplicationContext());
                        imageButton.setId(idImageButtons);//id du premier bouton : 201
                        imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        imageButton.setBackgroundColor(Color.TRANSPARENT);
                        imageButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150,2));
                        imageButton.setImageResource(R.drawable.drink_2);
                        // image qui onvre une popup
                        imageButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                createPopup(editText,editText.getId());
                            }
                        });
                        characterChooseActivity.containerLayout =(LinearLayout) findViewById(idLayouts);
                        containerLayout.addView(editText);
                        containerLayout.addView(imageButton);
                    }


                }
                else{
                    Toast.makeText(getApplicationContext(), "Vous avez atteint le nombre maximum de joueur", Toast.LENGTH_SHORT).show();
                }

            }
        });



        // go to menu
        this.goToMenu= (Button) findViewById(R.id.menu_button);

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        // go to game

        this.goToGame = (Button) findViewById(R.id.game_button);
        goToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EditText LastEditText = findViewById(nbJoueurs);

                if (!LastEditText.getText().toString().equals("")) {
                    tabJoueurs = new String[nbJoueurs][2];
                    for (int i = 0; i < nbJoueurs; i++) {
                        int temp = i + 1;
                        EditText editText = findViewById(temp);
                        String playerName = editText.getText().toString();
                        tabJoueurs[i][0] = playerName;
                        tabJoueurs[i][1] = tempTab[i];

                    }

                        Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
                        startActivity(gameActivity);
                        finish();

                }
                else
                    Toast.makeText(getApplicationContext(), "Veuillez remplir le champ precedent", Toast.LENGTH_SHORT).show();


            }
        });
    }

    // fonction pour creer une popup quand on perd le focus
    private void createPopupOnLoseFocus(EditText editText, int currentEditTextID){
        // si on perd le focus
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {

                if(!hasFocus && !editText.getText().toString().equals("") && !onLoseFocusHistory[currentEditTextID-1]  ){
                    createPopup(editText,currentEditTextID);
                    onLoseFocusHistory[currentEditTextID-1] = true;

                }
            }
        });
    }
    // créer la popup
    public void createPopup(EditText editText, int currentEditTextID){

        PopupDrinkSelection popupDrinkSelection = new PopupDrinkSelection(activity);
        popupDrinkSelection.setPlayerName(editText.getText().toString());

        // quand on click sur l'image0
        popupDrinkSelection.getDrinkImage0().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), editText.getText() +" à pas soif", Toast.LENGTH_SHORT).show();
                addTemporaryTab(currentEditTextID,"drink0");
                popupDrinkSelection.dismiss();
                ImageButton imageButton = (ImageButton) findViewById(currentEditTextID+200);
                imageButton.setImageResource(R.drawable.drink_0);

            }
        });
        // quand on click sur l'image1
        popupDrinkSelection.getDrinkImage1().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), editText.getText() +" à pas très soif", Toast.LENGTH_SHORT).show();
                popupDrinkSelection.dismiss();
                addTemporaryTab(currentEditTextID,"drink1");
                ImageButton imageButton = (ImageButton) findViewById(currentEditTextID+200);
                imageButton.setImageResource(R.drawable.drink_1);
            }
        });
        //quand on click sur l'image2
        popupDrinkSelection.getDrinkImage2().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), editText.getText() +" à soif", Toast.LENGTH_SHORT).show();
                popupDrinkSelection.dismiss();
                addTemporaryTab(currentEditTextID,"drink2");
                ImageButton imageButton = (ImageButton) findViewById(currentEditTextID+200);
                imageButton.setImageResource(R.drawable.drink_2);
            }
        });
        // quand on click sur l'image3
        popupDrinkSelection.getDrinkImage3().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), editText.getText() +" est déglingo", Toast.LENGTH_SHORT).show();
                popupDrinkSelection.dismiss();
                addTemporaryTab(currentEditTextID,"drink3");
                ImageButton imageButton = (ImageButton) findViewById(currentEditTextID+200);
                imageButton.setImageResource(R.drawable.drink_3);
            }
        });

        popupDrinkSelection.build();
    }

    public void addTemporaryTab(int position, String drink){ tempTab[position-1] = drink ; } // ajout à la liste temporaire
    // methode pour verifier si on peut lancer la game

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
    public static String[][] getPlayersFromCharacterChoose() { return tabJoueurs; }

    public static void resetCharacterChooseActivity(){
        CharacterChooseActivity characterChooseActivity=new CharacterChooseActivity();
    }

}

