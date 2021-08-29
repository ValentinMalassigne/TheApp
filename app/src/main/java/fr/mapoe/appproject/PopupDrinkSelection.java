package fr.mapoe.appproject;



import android.app.Activity;
import android.app.Dialog;
import android.widget.ImageView;
import android.widget.TextView;


public class PopupDrinkSelection extends Dialog {

    private String playerName;
    private TextView currentPlayerNameDisplay;
    private ImageView drink0;
    private ImageView drink1;
    private ImageView drink2;
    private ImageView drink3;

    //constructor
    public PopupDrinkSelection (Activity activity) {

        super(activity, R.style.Theme_AppCompat_DayNight_Dialog);
        setContentView(R.layout.activity_popup_drink_selection);

        this.playerName = "Current player Name";
        this.drink0 = findViewById(R.id.drink0);
        this.drink1 = findViewById(R.id.drink1);
        this.drink2 = findViewById(R.id.drink2);
        this.drink3 = findViewById(R.id.drink3);
        this.currentPlayerNameDisplay = findViewById(R.id.title);
    }
        // recuperation des elements de la popup
        public void setPlayerName (String playerName){this.playerName = playerName;}

        public ImageView getDrinkImage0 (){return drink0;}

        public ImageView getDrinkImage1 (){return drink1;}

        public ImageView getDrinkImage2 (){return drink2;}

        public ImageView getDrinkImage3 (){return drink3;}

        public void build(){
        show();
        currentPlayerNameDisplay.setText(playerName);
        }
}