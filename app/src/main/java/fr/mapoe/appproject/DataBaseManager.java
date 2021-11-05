package fr.mapoe.appproject;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

public class DataBaseManager extends AppCompatActivity {

    private MySQLiteOpenHelper accesDB;

    //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame
    public void addSentenceToDB(String language, String[] sentenceData,Context context){ //language doit valoir FR,EN,ES,...
        SQLiteDatabase db;
        String typeOfGame,sentenceType,sentence,popUp,punition,rightAnswer,otherAnswers,points,minimumPlayers;
        points=sentenceData[0];
        popUp=sentenceData[1];
        sentence=sentenceData[2];
        sentenceType=sentenceData[3];
        rightAnswer=sentenceData[5];
        otherAnswers=sentenceData[6];
        punition=sentenceData[7];
        typeOfGame=sentenceData[8];
        minimumPlayers="2";

        //on créé un accès à la BD
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getWritableDatabase();
        //on créé/ouvre la DB et on crée la table si elle n'existe pas

        db.execSQL("CREATE TABLE if not exists "+language+"SENTENCES (id INTEGER PRIMARY KEY,typeOfGame TEXT,sentenceType TEXT,minimumPlayers TEXT,points TEXT,sentence TEXT,popUp TEXT,punition TEXT,rightAnswer TEXT,otherAnswers TEXT)");

        //on insert la phrase comme il faut
        db.execSQL("INSERT INTO "+language+"SENTENCES (typeOfGame,sentenceType,minimumPlayers,points,sentence,popUp,punition,rightAnswer,otherAnswers) values (\""+typeOfGame+"\",\""+sentenceType+"\",\""+minimumPlayers+"\",\""+points+"\",\""+sentence+"\",\""+popUp+"\",\""+punition+"\",\""+rightAnswer+"\",\""+otherAnswers+"\")");
    }

    public String[] getSentenceFromDB(String language,int index,Context context){
        SQLiteDatabase db;
        String[] res = new String[9];
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getReadableDatabase();
        String req = "select * from "+language+"SENTENCES";
        Cursor cursor = db.rawQuery(req,null);
        cursor.move(index);
        for(int i = 1;i<10;i++){
            res[i-1]=cursor.getString(i);
        }

        return res;
    }
}
