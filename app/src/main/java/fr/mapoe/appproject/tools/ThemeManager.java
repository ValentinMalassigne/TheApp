package fr.mapoe.appproject.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;

import fr.mapoe.appproject.R;


public class ThemeManager {

    public String color;
    public Drawable backgroundDrawable;
    public Drawable buttonDrawable;
    public Context context;

    public ThemeManager(Context context, String color){
        this.color = color;
        this.context = context;
    }

    public Drawable getBackgroundDrawable() {
        // obligé de définir en dur car ça crash de manière random suivant les tel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        if(color.equals("blue"))
                backgroundDrawable = context.getDrawable(R.drawable.background_gradient_color_blue);
        else if(color.equals("green"))
            backgroundDrawable = context.getDrawable(R.drawable.background_gradient_color_green);
        else if(color.equals("apepiment"))
            backgroundDrawable = context.getDrawable(R.drawable.background_gradient_color_apepiment);
        else
            backgroundDrawable = context.getDrawable(R.drawable.background_gradient_color_red);
        }

        return backgroundDrawable;
    }

    public Drawable getButtonDrawable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(color.equals("blue"))
                buttonDrawable = context.getDrawable(R.drawable.button_blue);
            else if(color.equals("green"))
                buttonDrawable = context.getDrawable(R.drawable.button_green);
            else if(color.equals("apepiment"))
                buttonDrawable = context.getDrawable(R.drawable.button_apepiment);
            else
                buttonDrawable = context.getDrawable(R.drawable.button_red);
        }
        return buttonDrawable;
    }
}
