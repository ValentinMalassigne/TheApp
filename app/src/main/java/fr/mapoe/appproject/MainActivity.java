package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        //animation du bg
        AnimationBg.startBackgroundAnimation(findViewById(R.id.menu_layout));

        //go to option
        Button goToOption = (Button) findViewById(R.id.option_button);
        goToOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionActivity);
                finish();
            }
        });

        // go to GameSelection
        Button gotToGameSelection = (Button) findViewById(R.id.game_selection_button);
        gotToGameSelection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gameSelectionActivity = new Intent(getApplicationContext(), GameSelectionActivity.class);
                startActivity(gameSelectionActivity);
                finish();
            }
        });

    }

}