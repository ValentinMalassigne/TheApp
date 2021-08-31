package fr.mapoe.appproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Random;

public class SimpleWheelActivity extends AppCompatActivity {

    private static final String[] sectors = {"0","2","3","10","5","6","7","8","9","100","11","12"};
    private static final int[] sectorDegrees = new int[sectors.length];
    private static final Random random = new Random();
    private int degree=0;
    private boolean isSpinning=false;

    private ImageView wheel;
    private Button backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_wheel);

        final ImageView spinBtn = findViewById(R.id.spinBtn);
        wheel = findViewById(R.id.wheel);

        AnimationBg.startBackgroundAnimation(findViewById(R.id.simple_wheel_layout));

        getDegreeForSectors();

        spinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isSpinning){
                    spin();
                    isSpinning=true;
                }
            }
        });

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
    }

    private void spin(){
        degree= random.nextInt(sectors.length-1);
        RotateAnimation rotateAnimation = new RotateAnimation(0, (360* sectors.length)+sectorDegrees[degree],RotateAnimation.RELATIVE_TO_SELF,0.5f,RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setDuration(3600);
        rotateAnimation.setFillAfter(true);
        rotateAnimation.setInterpolator(new DecelerateInterpolator());
        rotateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Toast.makeText(SimpleWheelActivity.this,"Tu as "+sectors[sectors.length-(degree+1)]+" points.",Toast.LENGTH_SHORT).show();
                isSpinning=false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        wheel.startAnimation(rotateAnimation);
    }

    private void getDegreeForSectors(){
        int sectorDegree=360/sectors.length;

        for(int i=0;i<sectors.length;i++){
            sectorDegrees[i]=(i+1)*sectorDegree;
        }
    }
}