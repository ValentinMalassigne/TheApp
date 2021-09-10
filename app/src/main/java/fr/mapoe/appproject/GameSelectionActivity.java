package fr.mapoe.appproject;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
public class GameSelectionActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        AnimationBg.startBackgroundAnimation(findViewById(R.id.main_layout));
        // go to menu
        ImageButton backButton = (ImageButton) findViewById(R.id.menu_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        // go to character choose by ApeChill
        LinearLayout goToCharacter = (LinearLayout) findViewById(R.id.character_choose_image);
        goToCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChooseActivity.putExtra("typeOfGame",1);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // go to character choose by ApePiment
        LinearLayout goToApePiment = (LinearLayout) findViewById(R.id.ape_piment) ;
        goToApePiment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                characterChooseActivity.putExtra("typeOfGame",2);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
        // go to Simple Wheel
        LinearLayout goToSimpleWheel = (LinearLayout) findViewById(R.id.wheel_image);
        goToSimpleWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent simpleWheelActivity = new Intent(getApplicationContext(), SimpleWheelActivity.class);
                startActivity(simpleWheelActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // go to card game
        LinearLayout goToCardGame = (LinearLayout) findViewById(R.id.card_image);
        goToCardGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent cardGameActivity = new Intent(getApplicationContext(), CardGameActivity.class);
                startActivity(cardGameActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(mainActivity);
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        finish();
    }
}