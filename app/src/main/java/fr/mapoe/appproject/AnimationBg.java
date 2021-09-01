package fr.mapoe.appproject;

import android.graphics.drawable.AnimationDrawable;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

public class AnimationBg {

    //fonction pour lancer l'animation du background

    public static void startBackgroundAnimation(ConstraintLayout constraintLayout){

        AnimationDrawable animationDrawable = (AnimationDrawable) constraintLayout.getBackground();
        animationDrawable.setEnterFadeDuration(2000);
        animationDrawable.setExitFadeDuration(4000);
        animationDrawable.start();
    }

}
