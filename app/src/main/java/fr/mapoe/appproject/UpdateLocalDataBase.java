package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class UpdateLocalDataBase extends AppCompatActivity {
    private static final String FILE_NAME = "custom_sentences.txt";

    public void checkForUpdate(Context context){
        int onlineVersion=1; // normalement il faudra récup la version de la base de donnée online;
        //on charge le langage enregistrée dans les shared preferences
        SharedPreferences localDBVersion = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        int currentLocalDBVersion = localDBVersion.getInt("localDBVersion",0);

        //si on voit que la version local n'est pas la bonne alors on met a jour la db, et normalement on met aussi le shared preferences a jour
        if(currentLocalDBVersion!=onlineVersion){
            try {
                setUpList(context);
                SharedPreferences.Editor editor = localDBVersion.edit();
                editor.putInt("localDBVersion",onlineVersion);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void setUpList(Context context) throws IOException {

        DataBaseManager dataBaseManager = new DataBaseManager();

        //on récup la langue acctuelement utilisé par l'appli
        String language = context.getResources().getConfiguration().locale.getLanguage();
        language ="fr"; //bug avec l'anglais
        InputStream inputStream = null;
        if (language.equals("fr")) {
            inputStream = context.getResources().openRawResource(R.raw.fr_sentences);
            language="FR";
        } else {
            inputStream = context.getResources().openRawResource(R.raw.en_sentences);
            language="EN";
        }
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

            //on passe toute les lignes tant que l'on est pas aux lignes du mode génant
            /*if (typeOfGame == 2) {
                while (!reader.readLine().equals("End")) {
                }
            }*/
        reader.readLine();//on passe la ligne "gages"
        String tempLine = reader.readLine();//on lit le premier gage
        while (!tempLine.equals("minigames")) {
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"gages","ApeTime"),context);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("questions")) {
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"minigames","ApeTime"),context);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("End")) {
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"questions","ApeTime"),context);
            tempLine = reader.readLine();
        }
        inputStream.close();
    }

    private String[] transformSentenceIntoTab(String ligne,String SentenceType,String typeOfGame) {
        String[] currentChallenge = new String[9];
        boolean result = false;
        boolean temp = false;
        int i = 0;
        int j;
        if (ligne.equals("Spinning Wheel")) {
            //setUpWheel();
            currentChallenge[2]=ligne;
        } else if (ligne.equals("Red or Black")) {
            //startCardGame();
            currentChallenge[2]=ligne;
        } else {
            //currentChallenge[3] = tempTab[0]; on vérifie pas le type de la phrase c'est useless
            String points;
            String answers;
            String sentence;
            String rightAnswer;
            String button1Text;
            String button2Text;
            String punition;

            //on lit tant que l'on est pas a / pour savoir quelle est le premier bouton
            while (!temp) {
                if (ligne.substring(i, i + 1).equals("/")) {
                    temp = true;
                } else {
                    i++;
                }
            }
            button1Text = ligne.substring(0, i);
            j = i + 1;//on sauvergarde à quelle caractère il faut reprendre la lecture

            //on lit tant que l'on est pas a + ou - pour savoir quelle est le deuxième bouton
            temp = false;
            while (!temp) {
                if (ligne.substring(i, i + 1).equals("+") || ligne.substring(i, i + 1).equals("-")) {
                    temp = true;
                } else {
                    i++;
                }
            }

            button2Text = ligne.substring(j, i);

            rightAnswer = ligne.substring(i, i + 1);//on recup le + ou - qui se trouve juste après la deuxième rep
            points = ligne.substring(i + 1, i + 2);//on recup le nb de points qui est juste après le + ou -

            j = i + 1;//on sauvergarde à quelle caractère il faut reprendre la lecture

            //on lit tant que l'on a est pas a ¤ pour avoir la réponse
            temp = false;
            while (!temp) {
                if (ligne.substring(i, i + 1).equals("¤")) {
                    temp = true;
                } else {
                    i++;
                }
            }
            answers = ligne.substring(j + 1, i); //la phrase qui demande quelle est la bonne réponse

            j = i + 1;
            temp = false;
            while (!temp) {
                if (ligne.substring(j, j + 1).equals("¤")) {
                    temp = true;
                } else {
                    j++;
                }
            }

            sentence = ligne.substring(i + 1, j);//on lit la phrase
            punition = ligne.substring(j + 1);

            currentChallenge[0] = points;
            currentChallenge[1]=answers;
            currentChallenge[2]=sentence;
            currentChallenge[3]=SentenceType;
            currentChallenge[4]=rightAnswer;
            if(rightAnswer.equals("+")){
                currentChallenge[5] = button1Text;
                currentChallenge[6] = button2Text;
            }else{
                currentChallenge[6] = button1Text;
                currentChallenge[5] = button2Text;
            }
            currentChallenge[7] = punition;
            currentChallenge[8] = typeOfGame;
            result = true;
        }
        return currentChallenge;
    }

}
