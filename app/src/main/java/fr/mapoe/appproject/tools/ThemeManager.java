package fr.mapoe.appproject.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;


public class ThemeManager {

    public String color;
    public Drawable backgroundDrawable;
    public Drawable buttonDrawable;
    public Context context;

    public ThemeManager(Context context, String background){
        this.color = background;
        this.context = context;
    }

    public Drawable getBackgroundDrawable() {
        int file = context.getResources().getIdentifier("background_gradient_color_"+color,"drawable",context.getPackageName());
         backgroundDrawable = context.getResources().getDrawable(file);
        return backgroundDrawable;
    }

    public Drawable getButtonDrawable() {
        int file = context.getResources().getIdentifier("button_"+color,"drawable",context.getPackageName());
        buttonDrawable = context.getResources().getDrawable(file);
        return buttonDrawable;
    }
}
