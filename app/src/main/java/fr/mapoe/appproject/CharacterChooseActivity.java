package fr.mapoe.appproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
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
    private Button goToMenu;
    private Button addPlayer;
    private Button goToGame;
    private int typeOfGame = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_choose);

        this.scrollViewLayout = findViewById(R.id.myDynamicLayout);
        this.addPlayer = (Button) findViewById(R.id.add_player_button);
        this.goToGame = (Button) findViewById(R.id.game_button);
        this.goToMenu = (Button) findViewById(R.id.menu_button);
        // recupération des valeurs passé en param
        Bundle extras = getIntent().getExtras();
        boolean restart= false;
        String[] savePlayerTab=new String[0],saveAlcoholTab = new String[0];

        if (extras != null) {
            typeOfGame = extras.getInt("typeOfGame");
            savePlayerTab = extras.getStringArray("playerTab");
            saveAlcoholTab = extras.getStringArray("alcoholTab");
            restart = extras.getBoolean("restart");
        }
        if(restart){
            init(savePlayerTab,saveAlcoholTab);
            for(int i=0; i<saveAlcoholTab.length;i++){
                tempTab[i]=saveAlcoholTab[i];
            }
        }
        else{
            Arrays.fill(tempTab, "drink2");
        }
        // changer le bg des buttons si on ApePiment
        if (typeOfGame==2) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                addPlayer.setBackground(getDrawable(R.drawable.button2));
                goToGame.setBackground(getDrawable(R.drawable.button2));
                goToMenu.setBackground(getDrawable(R.drawable.button2));
            }
        }

        // ajout des TextView à chaque clique
        addPlayer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPlayers(null,null,false,0);
            }
        });


        // go to game selection
        goToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }

        });

        // go to game
        goToGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(nbJoueurs<2){
                    Toast.makeText(getApplicationContext(),getString(R.string.minimum_player_error),Toast.LENGTH_SHORT).show();
                }else {
                    String[] playerTab = new String[nbJoueurs];
                    String[] alcoholTab = new String[nbJoueurs];

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
                    gameActivity.putExtra("typeOfGame", typeOfGame);
                    startActivity(gameActivity);
                    overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                    finish();

                }
            }
        });
        if(!restart)
        {
            //on vérifie si l'utilisateur à bloqué la popup
            SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
            String alcohol_reminder_popup_blocked = "";
            if(sharedPreferences.contains("block_alcohol_reminder_popup")){
                alcohol_reminder_popup_blocked = sharedPreferences.getString("block_alcohol_reminder_popup","");
            }
            if(!alcohol_reminder_popup_blocked.equals("blocked"))
                showInfoDialog(R.layout.info_popup);

        }

    }

    private void addPlayers(String[] savePlayerTab, String[] saveAlcoholTab, boolean initialisation,int i){
        if (nbJoueurs < 10) {
            EditText addPlayerEditText = findViewById(R.id.add_player_edit_text);
            //on vérifie que le nom n'est pas celui d'un joueur déjà existant
            int error=0;
            if(!initialisation) { //pas besoins de vérifier si on réutilise des précédents pseudos puisqu'ils sont déjà tous différents
                for (i = 1; i <= nbJoueurs; i++) {
                    TextView tempTextView = findViewById(i);
                    if (addPlayerEditText.getText().toString().equals(tempTextView.getText().toString()))
                        error++;
                }
            }
            if(error==0){
                if (!addPlayerEditText.getText().toString().equals("") || initialisation) {
                    Typeface typeface = ResourcesCompat.getFont(getApplicationContext(), R.font.convergence);
                    nbJoueurs++;
                    idLayouts++;
                    idImageButtons++;
                    idDeletePlayerButton++;
                    //ajout du text, du bouton deletePlayer et du bouton selection alcool au horizontal layout
                    ImageButton deletePlayerButton = new ImageButton(getApplicationContext());
                    deletePlayerButton.setId(idDeletePlayerButton);//id du premier deletePlayerBouton : 301
                    deletePlayerButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    deletePlayerButton.setBackgroundColor(Color.TRANSPARENT);
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(120, 120, 2);
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
                    scrollViewLayout.addView(linearLayout, 0);

                    //ajout du text et du bouton au horizontal layout
                    TextView playerName = new TextView(getApplicationContext());
                    if (initialisation) {
                        playerName.setText(savePlayerTab[i]);
                    } else {
                        playerName.setText(addPlayerEditText.getText().toString());
                    }
                    addPlayerEditText.setText("");
                    playerName.setLayoutParams(new LinearLayout.LayoutParams(150, 150, 8));
                    playerName.setId(nbJoueurs); //id du premier TextView : 1
                    playerName.setGravity(Gravity.CENTER);
                    playerName.setTextSize(25);
                    playerName.setTextColor(Color.WHITE);
                    playerName.setTypeface(typeface);

                    ImageButton imageButton = new ImageButton(getApplicationContext());
                    imageButton.setId(idImageButtons);//id du premier bouton : 201
                    imageButton.setScaleType(ImageView.ScaleType.FIT_CENTER);
                    imageButton.setBackgroundColor(Color.TRANSPARENT);
                    imageButton.setLayoutParams(new LinearLayout.LayoutParams(150, 150, 2));
                    if (initialisation) {
                        if (saveAlcoholTab[i].equals("drink0")) {
                            imageButton.setImageResource(R.drawable.drink_0);
                        }
                        if (saveAlcoholTab[i].equals("drink1")) {
                            imageButton.setImageResource(R.drawable.drink_1);
                        }
                        if (saveAlcoholTab[i].equals("drink2")) {
                            imageButton.setImageResource(R.drawable.drink_2);
                        }
                        if (saveAlcoholTab[i].equals("drink3")) {
                            imageButton.setImageResource(R.drawable.drink_3);
                        }
                    } else {
                        imageButton.setImageResource(R.drawable.drink_2);
                    }
                    // image qui ouvre une popup
                    imageButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String editContents = playerName.getText().toString();
                            if (editContents.equals("")) { // si le champ est vide
                                Toast.makeText(getApplicationContext(), getString(R.string.error_image), Toast.LENGTH_SHORT).show();
                            } else {
                                showAlcoholPopup(R.layout.activity_popup_drink_selection, editContents, playerName.getId());
                            }
                        }
                    });
                    containerLayout = findViewById(idLayouts);
                    containerLayout.addView(deletePlayerButton);
                    containerLayout.addView(playerName);
                    containerLayout.addView(imageButton);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.empty_add_player_error), Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(getApplicationContext(), getString(R.string.duplicater_player_name_error), Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), getString(R.string.maximum_player_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void init(String[] savePlayerTab, String[] saveAlcoholTab){
        for(int i=0;i<savePlayerTab.length;i++){
            addPlayers(savePlayerTab,saveAlcoholTab,true,i);
        }
    }

    private void showInfoDialog(int layout){// créer la popup info
        AlertDialog alertDialog;
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(CharacterChooseActivity.this);
        View layoutView = getLayoutInflater().inflate(layout,null);
        Button okButton = layoutView.findViewById(R.id.ok_button);
        ImageView nextButton = layoutView.findViewById(R.id.right_popup_arrow);
        ImageView leftButton = layoutView.findViewById(R.id.left_popup_arrow);
        leftButton.setVisibility(View.GONE);
        nextButton.setVisibility(View.GONE);
        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.show();
        // chnagement du bg button on fonction du mode de jeu
        if(typeOfGame == 2) {
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

        CheckBox blockPopupCheckBox = layoutView.findViewById(R.id.block_popup_checkBox);
        blockPopupCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checked =  ((CheckBox) view).isChecked();
                //ici on enregistre dans les SharedPreferences si l'utilisateur bloque la popup
                SharedPreferences blockPopup;
                blockPopup = getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = blockPopup.edit();
                if(checked){
                    editor.putString("block_alcohol_reminder_popup","blocked");
                }else{
                    editor.putString("block_alcohol_reminder_popup","activated");
                }
                editor.apply();
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
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        finish();
    }
}