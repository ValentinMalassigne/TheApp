package fr.mapoe.appproject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;


public class InfoPopup extends Dialog {

    private Button okButton;

    public InfoPopup(Activity activity) {
        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.info_popup);
        this.okButton =  findViewById(R.id.ok_button);


    }
    //recuperer le button
    public Button getOkButton() {
        return okButton;
    }

    public void build(){
        show();
    }
}
