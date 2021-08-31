package fr.mapoe.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GameSelectionActivity extends AppCompatActivity {

    private Button backButton;
    private Button goToAbout;
    private Button goToCharacter;
    private Button goToSimpleWheel;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);

        // go to menu
        this.backButton = (Button) findViewById(R.id.menu_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainActivity = new Intent(getApplicationContext(), fr.mapoe.appproject.MainActivity.class);
                startActivity(mainActivity);
                finish();
            }
        });

        // go to character choose
        this.goToCharacter = (Button) findViewById(R.id.game1_button);
        goToCharacter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent characterChooseActivity = new Intent(getApplicationContext(), CharacterChooseActivity.class);
                startActivity(characterChooseActivity);
                finish();
            }
        });

        // go to Simple Wheel
        this.goToSimpleWheel = (Button) findViewById(R.id.game2_button);
        goToSimpleWheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent simpleWheelActivity = new Intent(getApplicationContext(), SimpleWheelActivity.class);
                startActivity(simpleWheelActivity);
                finish();
            }
        });

    }
}