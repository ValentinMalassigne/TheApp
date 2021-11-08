package fr.mapoe.appproject.tools;

import android.content.Context;
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

    public Drawable getDrawableRes() {
        color = "background_gradient_color_"+color;
        int file = context.getResources().getIdentifier(color,"drawable",context.getPackageName());
         backgroundDrawable = context.getResources().getDrawable(file);
        return backgroundDrawable;
    }

    public Drawable getButtonDrawable() {
        color = "button_"+color;
        int file = context.getResources().getIdentifier(color,"drawable",context.getPackageName());
        buttonDrawable = context.getResources().getDrawable(file);
        return buttonDrawable;
    }
}
