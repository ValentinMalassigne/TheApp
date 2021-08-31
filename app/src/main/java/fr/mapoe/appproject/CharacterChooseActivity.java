package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Arrays;


public class CharacterChooseActivity extends AppCompatActivity {

    private Button addPlayer;
    private int playerNb = 0; // stock egalement l'id de chaque joueur
    private LinearLayout scrollViewLayout;
    private LinearLayout containerLayout;
    private Button goToMenu;
    private Button goToGame;
    private Activity activity;
    public String[] tempTab = new String[10]; // liste temporaire pour stocker si le joueur bois ou pas
    private int idLayouts=100;
    private int idImageButtons=200;
    private int idDeletePlayerButton=300;
    private boolean onLoseFocusHistory = false;
    private CharacterChooseActivity characterChooseActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choose);

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);


        Arrays.fill(tempTab, "drink2");
        this.scrollViewLayout =(LinearLayout) findViewById(R.id.myDynamicLayout);

        this.addPlayer= (Button) findViewById(R.id.add_player_button);

        this.activity = this;

        while(playerNb<2){
            playerNb++;//commence a 1
            idLayouts++;//commence a 101
            idImageButtons++;//commence a 201
            idDeletePlayerButton++;//commence a 301

            //ajout d'un horizontal layout au vertical layout du scrollView
            LinearLayout linearLayout = new LinearLayout(getApplicationContext());
            linearLayout.setId(idLayouts);//id du premier layout : 101
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            scrollViewLayout.addView(linearLayout);

            //ajout du text, du bouton deletePlayer et du bouton selection alcool au horizontal layout
            ImageButton deletePlayerButton = new ImageButton(getApplicationContext());
            deletePlayerButton.setId(idDeletePlayerButton);//id du premier deletePlayerBouton : 301
            deletePlayerButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
            deletePlayerButton.setBackgroundColor(Color.TRANSPARENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,80,2);
            params.gravity = Gravity.CENTER;
            deletePlayerButton.setLayoutParams(params);
            deletePlayerButton.setImageResource(R.drawable.x_icon);
            deletePlayerButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deletePlayer(deletePlayerButton.getId());
                }
            });
            EditText editText = new EditText(getApplicationContext());
            editText.setHint(getString(R.string.playe_name_hint)+" "+playerNb);
            editText.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,8));
            editText.setId(playerNb); //id du premier editText : 1
            editText.setInputType(InputType.TYPE_CLASS_TEXT);
            editText.setTypeface(typeface);

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
            containerLayout.addView(deletePlayerButton);
            containerLayout.addView(editText);
            containerLayout.addView(imageButton);
        }
        int firstEditTextID=1;
        EditText editText = (EditText) findViewById(firstEditTextID);
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(!hasFocus && !editText.getText().toString().equals("") && !onLoseFocusHistory ){
                    InfoPopup infoPopup = new InfoPopup(activity);
                    infoPopup.getOkButton().setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            infoPopup.dismiss();
                        }
                    });
                    infoPopup.build();
                    onLoseFocusHistory = true;
                }
            }
        });

        // ajout des EditText à chaque clique
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (playerNb <= 10) {

                    EditText previousEditText=findViewById(playerNb);
                    int one = 1;
                    EditText editText1 = findViewById(one);
                    if (previousEditText.getText().toString().equals("") || editText1.getText().toString().equals("")){ // le champ est vide
                        Toast.makeText(getApplicationContext(), "Veuillez remplir le champ precedent", Toast.LENGTH_SHORT).show();

                    }
                    else {
                        playerNb++;
                        idLayouts++;
                        idImageButtons++;
                        idDeletePlayerButton++;
                        //ajout du text, du bouton deletePlayer et du bouton selection alcool au horizontal layout
                        ImageButton deletePlayerButton = new ImageButton(getApplicationContext());
                        deletePlayerButton.setId(idDeletePlayerButton);//id du premier deletePlayerBouton : 301
                        deletePlayerButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                        deletePlayerButton.setBackgroundColor(Color.TRANSPARENT);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(80,80,2);
                        params.gravity = Gravity.CENTER;
                        deletePlayerButton.setLayoutParams(params);
                        deletePlayerButton.setImageResource(R.drawable.x_icon);
                        deletePlayerButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                deletePlayer(deletePlayerButton.getId());
                            }
                        });
                        LinearLayout linearLayout = new LinearLayout(getApplicationContext());
                        linearLayout.setId(idLayouts);//id du premier layout : 101
                        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                        scrollViewLayout.addView(linearLayout);

                        //ajout du text et du bouton au horizontal layout
                        EditText editText = new EditText(getApplicationContext());
                        editText.setHint(getString(R.string.playe_name_hint)+" "+playerNb);
                        editText.setInputType(InputType.TYPE_CLASS_TEXT);
                        editText.setLayoutParams(new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT,8));
                        editText.setId(playerNb); //id du premier editText : 1
                        editText.setTypeface(typeface);

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
                        containerLayout.addView(deletePlayerButton);
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

                int errors=0;
                EditText verifEditText;
                for (int i=1;i<=playerNb;i++){
                    verifEditText=findViewById(i);
                    if(verifEditText.getText().toString().equals("")){
                        errors+=1;
                    }
                }
                String[] playerTab = new String[playerNb], alcoholTab = new String[playerNb];

                if (errors==0) {

                    for (int i = 0; i < playerNb; i++) {
                        int temp = i + 1;
                        EditText editText = findViewById(temp);
                        String playerName = editText.getText().toString();
                        playerTab[i] = playerName;
                        alcoholTab[i] = tempTab[i];

                    }

                    Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
                    gameActivity.putExtra("playerTab", playerTab);
                    gameActivity.putExtra("alcoholTab", alcoholTab);
                    startActivity(gameActivity);
                    finish();


                }
                else
                    Toast.makeText(getApplicationContext(), "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void deletePlayer(int id){
        if(playerNb>2) {
            id -= 300;
            //on supprime la ligne du joueur supprimé
            containerLayout =(LinearLayout) findViewById(100+id);
            containerLayout.removeView(findViewById(300+id));
            containerLayout.removeView(findViewById(200+id));
            containerLayout.removeView(findViewById(id));
            scrollViewLayout =(LinearLayout) findViewById(R.id.myDynamicLayout);
            scrollViewLayout.removeView(findViewById(100+id));

            //mettre a jour l'id des élements des lignes après la suppression
            while(id<playerNb){
                EditText editText= findViewById(id+1);
                editText.setHint("Nom joueur "+id);
                editText.setId(id);
                containerLayout =(LinearLayout) findViewById(100+id+1);
                containerLayout.setId(100+id);
                ImageButton selectAlcoholButton=findViewById(200+id+1);
                selectAlcoholButton.setId(200+id);
                ImageButton deletePlayerButton=findViewById(300+id+1);
                deletePlayerButton.setId(300+id);
                id++;
            }

            playerNb--;
            idLayouts--;
            idImageButtons--;
            idDeletePlayerButton--;
        }else{
            //dire qu'il ne peut pas supprimer un joueur quand ils ne sont que 2
            Toast.makeText(getApplicationContext(), "Impossible d'être moins que 2 joueurs.", Toast.LENGTH_SHORT).show();
        }
    }

    // créer la popup de selection d'alcool
    public void createPopup(EditText editText, int currentEditTextID){
        if(!editText.getText().toString().equals("")) {
            PopupDrinkSelection popupDrinkSelection = new PopupDrinkSelection(activity);
            popupDrinkSelection.setPlayerName(editText.getText().toString());

            // quand on click sur l'image0
            popupDrinkSelection.getDrinkImage0().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), editText.getText() + getString(R.string.drink_0_message), Toast.LENGTH_SHORT).show();
                    addTemporaryTab(currentEditTextID, "drink0");
                    popupDrinkSelection.dismiss();
                    ImageButton imageButton = (ImageButton) findViewById(currentEditTextID + 200);
                    imageButton.setImageResource(R.drawable.drink_0);

                }
            });
            // quand on click sur l'image1
            popupDrinkSelection.getDrinkImage1().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), editText.getText() + " à pas très soif", Toast.LENGTH_SHORT).show();
                    popupDrinkSelection.dismiss();
                    addTemporaryTab(currentEditTextID, "drink1");
                    ImageButton imageButton = (ImageButton) findViewById(currentEditTextID + 200);
                    imageButton.setImageResource(R.drawable.drink_1);
                }
            });
            //quand on click sur l'image2
            popupDrinkSelection.getDrinkImage2().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), editText.getText() + " à soif", Toast.LENGTH_SHORT).show();
                    popupDrinkSelection.dismiss();
                    addTemporaryTab(currentEditTextID, "drink2");
                    ImageButton imageButton = (ImageButton) findViewById(currentEditTextID + 200);
                    imageButton.setImageResource(R.drawable.drink_2);
                }
            });
            // quand on click sur l'image3
            popupDrinkSelection.getDrinkImage3().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(getApplicationContext(), editText.getText() + " est déglingo", Toast.LENGTH_SHORT).show();
                    popupDrinkSelection.dismiss();
                    addTemporaryTab(currentEditTextID, "drink3");
                    ImageButton imageButton = (ImageButton) findViewById(currentEditTextID + 200);
                    imageButton.setImageResource(R.drawable.drink_3);
                }
            });

            popupDrinkSelection.build();
        }
        else
            Toast.makeText(getApplicationContext(), "Veuillez d'abord remplir le champ", Toast.LENGTH_SHORT).show();
    }

    public void addTemporaryTab(int position, String drink){ tempTab[position-1] = drink ; } // ajout à la liste temporaire
    // methode pour verifier si on peut lancer la game

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
        startActivity(mainActivity);
        finish();
    }
}