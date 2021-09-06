package fr.mapoe.appproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;


public class CharacterChooseActivity extends AppCompatActivity {

    private int nbJoueurs = 0; // stock egalement l'id de chaque joueur
    private LinearLayout scrollViewLayout;
    private LinearLayout containerLayout;
    public String[] tempTab = new String[10]; // liste temporaire pour stocker si le joueur bois ou pas
    public static String[][] tabJoueurs;
    private int idLayouts=100;
    private int idImageButtons=200;
    private int idDeletePlayerButton=300;
    private CharacterChooseActivity characterChooseActivity = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choose);

        Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);


        Arrays.fill(tempTab, "drink2");
        this.scrollViewLayout =(LinearLayout) findViewById(R.id.myDynamicLayout);

        Button addPlayer = (Button) findViewById(R.id.add_player_button);


        // ajout des TextView à chaque clique
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText addPlayerEditText = findViewById(R.id.add_player_edit_text);
                if (nbJoueurs < 10 & !addPlayerEditText.getText().toString().equals("")) {
                    nbJoueurs++;
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
                    TextView playerName = new TextView(getApplicationContext());
                    playerName.setText(addPlayerEditText.getText().toString());
                    addPlayerEditText.setText("");
                    playerName.setLayoutParams(new LinearLayout.LayoutParams(150, 150,8));
                    playerName.setId(nbJoueurs); //id du premier TextView : 1
                    playerName.setGravity(Gravity.CENTER);
                    playerName.setTextSize(25);
                    playerName.setTextColor(Color.WHITE);
                    playerName.setFilters(new InputFilter[] {new InputFilter.LengthFilter(12) {
                    }});
                    playerName.setTypeface(typeface);

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
                            String editContents = playerName.getText().toString();
                            if(editContents.equals("")){ // si le champ est vide
                                Toast.makeText(getApplicationContext(),getString(R.string.error_image),Toast.LENGTH_SHORT).show();
                            }
                            else{
                                showAlcoholPopup(R.layout.activity_popup_drink_selection,editContents,playerName.getId());
                            }
                        }
                    });
                    characterChooseActivity.containerLayout =(LinearLayout) findViewById(idLayouts);
                    containerLayout.addView(deletePlayerButton);
                    containerLayout.addView(playerName);
                    containerLayout.addView(imageButton);
                }
                else{
                    Toast.makeText(getApplicationContext(), getString(R.string.maximum_player_error), Toast.LENGTH_SHORT).show();
                }

            }
        });



        // go to game selection
        Button goToMenu = (Button) findViewById(R.id.menu_button);

        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameSelectionActivity = new Intent(getApplicationContext(),GameSelectionActivity.class);
                startActivity(gameSelectionActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        // go to game

        Button goToGame = (Button) findViewById(R.id.game_button);
        goToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int errors=0;
                TextView verifPlayerNames;
                for (int i=1;i<=nbJoueurs;i++){
                    verifPlayerNames=findViewById(i);
                    if(verifPlayerNames.getText().toString().equals("")){
                        errors+=1;
                    }
                }
                String[] playerTab = new String[nbJoueurs], alcoholTab = new String[nbJoueurs];

                if (errors==0) {
                    tabJoueurs = new String[nbJoueurs][2];
                    for (int i = 0; i < nbJoueurs; i++) {
                        int temp = i + 1;
                        TextView playerNameTextView = findViewById(temp);
                        String playerName = playerNameTextView.getText().toString();
                        playerTab[i] = playerName;
                        alcoholTab[i] = tempTab[i];
                        tabJoueurs[i][0] = playerName;
                        tabJoueurs[i][1] = tempTab[i];
                    }

                    Intent gameActivity = new Intent(getApplicationContext(), GameActivity.class);
                    gameActivity.putExtra("playerTab", playerTab);
                    gameActivity.putExtra("alcoholTab", alcoholTab);
                    startActivity(gameActivity);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();


                }
                else
                    Toast.makeText(getApplicationContext(), getString(R.string.missing_names_error), Toast.LENGTH_SHORT).show();

            }
        });
        showInfoDialog(R.layout.info_popup);
    }

    private void showInfoDialog(int layout){// créer la popup info
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CharacterChooseActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        Button okButton = layoutView.findViewById(R.id.ok_button);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
    }
    private void showAlcoholPopup(int layout, String name, int currentTextViewID){
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CharacterChooseActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        // déclaration des éléments
        TextView currentPlayerNameDisplay = layoutView.findViewById(R.id.title);
        ImageView drink0 = (ImageView) layoutView.findViewById(R.id.drink0);
        ImageView drink1 = layoutView.findViewById(R.id.drink1);
        ImageView drink2 = layoutView.findViewById(R.id.drink2);
        ImageView drink3 = layoutView.findViewById(R.id.drink3);
        currentPlayerNameDisplay.setText(name);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        drink0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),name+" "+getString(R.string.drink_0_message),Toast.LENGTH_SHORT).show();
                addTemporaryTab(currentTextViewID,"drink0");
                ImageButton imageButton = (ImageButton) findViewById(currentTextViewID+200);
                imageButton.setImageResource(R.drawable.drink_0);
                alertDialog.dismiss();

            }
        });
        drink1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),name+" "+getString(R.string.drink_1_message),Toast.LENGTH_SHORT).show();
                addTemporaryTab(currentTextViewID,"drink1");
                ImageButton imageButton = (ImageButton) findViewById(currentTextViewID+200);
                imageButton.setImageResource(R.drawable.drink_1);
                alertDialog.dismiss();

            }
        });
        drink2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),name+" "+getString(R.string.drink_2_message),Toast.LENGTH_SHORT).show();
                addTemporaryTab(currentTextViewID,"drink2");
                ImageButton imageButton = (ImageButton) findViewById(currentTextViewID+200);
                imageButton.setImageResource(R.drawable.drink_2);
                alertDialog.dismiss();

            }
        });
        drink3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),name+" "+getString(R.string.drink_3_message),Toast.LENGTH_SHORT).show();
                addTemporaryTab(currentTextViewID,"drink3");
                ImageButton imageButton = (ImageButton) findViewById(currentTextViewID+200);
                imageButton.setImageResource(R.drawable.drink_3);
                alertDialog.dismiss();

            }
        });
        alertDialog.show();
    }
    private void deletePlayer(int id){
            id = id - 300;
            //on supprime la ligne du joueur supprimé
            containerLayout =(LinearLayout) findViewById(100+id);
            containerLayout.removeView(findViewById(300+id));
            containerLayout.removeView(findViewById(200+id));
            containerLayout.removeView(findViewById(id));
            scrollViewLayout =(LinearLayout) findViewById(R.id.myDynamicLayout);
            scrollViewLayout.removeView(findViewById(100+id));

            //mettre a jour l'id des élements des lignes après la suppression
            while(id<nbJoueurs){
                TextView playerName= findViewById(id+1);
                playerName.setHint(getString(R.string.playe_name_hint)+id);
                playerName.setId(id);
                containerLayout =(LinearLayout) findViewById(100+id+1);
                containerLayout.setId(100+id);
                ImageButton selectAlcoholButton=findViewById(200+id+1);
                selectAlcoholButton.setId(200+id);
                ImageButton deletePlayerButton=findViewById(300+id+1);
                deletePlayerButton.setId(300+id);
                id++;
            }

            nbJoueurs--;
            idLayouts--;
            idImageButtons--;
            idDeletePlayerButton--;
    }

    public void addTemporaryTab(int position, String drink){ tempTab[position-1] = drink ; } // ajout à la liste temporaire
    // methode pour verifier si on peut lancer la game

    @Override
    public void onBackPressed() {
        Intent gameSelectionActivity = new Intent(getApplicationContext(), GameSelectionActivity.class);
        startActivity(gameSelectionActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}
