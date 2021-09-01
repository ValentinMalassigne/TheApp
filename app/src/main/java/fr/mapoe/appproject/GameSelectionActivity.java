package fr.mapoe.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.graphics.drawable.AnimationDrawable;

import androidx.appcompat.app.AppCompatActivity;

public class GameSelectionActivity extends AppCompatActivity {

    private AnimationDrawable animationDrawable;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        // go to menu
        Button backButton = (Button) findViewById(R.id.menu_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
                startActivity(mainActivity);
                overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
                finish();
            }
        });

        // go to character choose
        Button goToCharacter = (Button) findViewById(R.id.character_choose_button);
        goToCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                startActivity(characterChooseActivity);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        });

        // go to Simple Wheel
        Button goToSimpleWheel = (Button) findViewById(R.id.wheel_button);
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
        Button goToCardGame = (Button) findViewById(R.id.card_button);
        animationDrawable = (AnimationDrawable) goToCardGame.getBackground();
        animationDrawable.setEnterFadeDuration(1500);
        animationDrawable.setExitFadeDuration(1500);
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
    @Override
    protected void onResume() {
        super.onResume();
        if(animationDrawable!=null && !animationDrawable.isRunning()){
            animationDrawable.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(animationDrawable!=null && animationDrawable.isRunning()){
            animationDrawable.stop();
        }
    }
}