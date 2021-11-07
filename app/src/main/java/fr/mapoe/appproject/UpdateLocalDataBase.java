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

import fr.mapoe.appproject.tools.AccesHTTP;

public class UpdateLocalDataBase extends AppCompatActivity {
    private static final String FILE_NAME = "custom_sentences.txt";
    private DataBaseManager dataBaseManager = new DataBaseManager();
    private Context mainActivityContext;
    public void checkForUpdate(Context context){
        mainActivityContext = context;
        AccesHTTP accesHTTP = new AccesHTTP(context);
        accesHTTP.execute();
        int onlineVersion=1; // normalement il faudra récup la version de la base de donnée online;
        //on charge la version de la db enregistrée dans les shared preferences
        SharedPreferences localDBVersion = context.getSharedPreferences("UserPreferences", Context.MODE_PRIVATE);
        int currentLocalDBVersion = localDBVersion.getInt("localDBVersion",0);

        //si on voit que la version local n'est pas la bonne alors on met a jour la db, et normalement on met aussi le shared preferences a jour
        if(currentLocalDBVersion!=onlineVersion) {
            try {
                setUpList(context);
                SharedPreferences.Editor editor = localDBVersion.edit();
                editor.putInt("localDBVersion", onlineVersion);
                editor.apply();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void updateFromOnlineDB(String[][] sentenceTab,Context context){
        String language="CUSTOM"; //pour les tests on enregistre la table online dans la table custom
        for(int i=0;i<sentenceTab.length;i++){
                dataBaseManager.addSentenceToDB(language,adaptToLocalBase(sentenceTab[i]),context);
        }
    }

    private String[] adaptToLocalBase(String[] sentence){
        String[] adaptedSentence=new String[10];
        adaptedSentence[0]=sentence[8];
        adaptedSentence[1]=sentence[3];
        adaptedSentence[2]=sentence[2];
        adaptedSentence[3]="custom";//sentence[1];
        adaptedSentence[4]=sentence[6];
        adaptedSentence[5]=sentence[4];
        adaptedSentence[6]=sentence[5];
        adaptedSentence[7]=sentence[7];
        adaptedSentence[8]=sentence[0];
        return adaptedSentence;
    }

    /* Ne marche pas et jsp pas pourquoi, il y a une erreur avec sentenceTab.length, car sentenceTab est null, ducoup le getter ne semble pas marcher
    public void updateFromOnlineDB(Context context){
        AccesHTTP accesHTTP = new AccesHTTP(context);
        accesHTTP.execute();

        String[][] sentenceTab=accesHTTP.getSentenceTab();

        for(int i=0;i<sentenceTab.length;i++){
            for(int j=0;j<10;j++){
                Log.d("anus",sentenceTab[i][j]);
            }
        }
    }*/

    //uniquement utile tant que l'on a des fichiers local
    private void setUpList(Context context) throws IOException {

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
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"gages","ApeChill"),context);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("questions")) {
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"minigames","ApeChill"),context);
            tempLine = reader.readLine();
        }
        tempLine = reader.readLine();
        while (!tempLine.equals("End")) {
            dataBaseManager.addSentenceToDB(language,transformSentenceIntoTab(tempLine,"questions","ApeChill"),context);
            tempLine = reader.readLine();
        }
        inputStream.close();
    }

    //uniquement utile tant que l'on a des fichiers local
    private String[] transformSentenceIntoTab(String ligne,String SentenceType,String typeOfGame) {
        String[] currentChallenge = new String[9];
        boolean result = false;
        boolean temp = false;
        int i = 0;
        int j;
        if (ligne.equals("Spinning Wheel")) {
            //setUpWheel();
            currentChallenge[2]=ligne;
            currentChallenge[3]="minigames";
        } else if (ligne.equals("Red or Black")) {
            //startCardGame();
            currentChallenge[2]=ligne;
            currentChallenge[3]="minigames";
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

            currentChallenge[0]=points;
            currentChallenge[1]=answers;
            currentChallenge[2]=sentence;
            currentChallenge[3]=SentenceType;
            currentChallenge[4]=rightAnswer;
            currentChallenge[6]=button1Text;//c'est inversé bouton rep1 est la réponse de gauche mais dans la table on commence par la réponse de droite
            currentChallenge[5]=button2Text;
            currentChallenge[7]=punition;
            currentChallenge[8]=typeOfGame;
            result = true;
        }
        return currentChallenge;
    }

}
