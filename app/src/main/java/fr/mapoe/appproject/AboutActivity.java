package fr.mapoe.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {
    private Button goBackToOption;

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // go back to option
        this.goBackToOption = (Button) findViewById(R.id.option_button);
        goBackToOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent optionActivity = new Intent(getApplicationContext(), OptionActivity.class);
                startActivity(optionActivity);
                finish();
            }
        });
    }
}