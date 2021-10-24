package fr.mapoe.appproject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.w3c.dom.Text;

public class SplashScreenActivity extends AppCompatActivity {
    private final int SPLASH_SCREEN_TIMEOUT = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        //redirect to main page after 3s
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                // start page
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
            // handler post delayed

            new Handler().postDelayed(runnable, SPLASH_SCREEN_TIMEOUT);


    }
}
