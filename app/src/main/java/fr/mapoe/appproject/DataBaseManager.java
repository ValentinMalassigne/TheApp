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

        db.execSQL("CREATE TABLE if not exists "+language+"SENTENCES (id INTEGER PRIMARY KEY,points TEXT,popUp TEXT,sentence TEXT,sentenceType TEXT,rightAnswer TEXT,otherAnswers TEXT,punition TEXT,typeOfGame TEXT,minimumPlayers TEXT)");

        //on insert la phrase comme il faut
        db.execSQL("INSERT INTO "+language+"SENTENCES (points,popUp,sentence,sentenceType,rightAnswer,otherAnswers,punition,typeOfGame,minimumPlayers) values (\""+points+"\",\""+popUp+"\",\""+sentence+"\",\""+sentenceType+"\",\""+rightAnswer+"\",\""+otherAnswers+"\",\""+punition+"\",\""+typeOfGame+"\",\""+minimumPlayers+"\")");
    }

    public String[] getSentenceFromDB(String language,int index,Context context){
        SQLiteDatabase db;
        String[] res = new String[9];
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getReadableDatabase();
        String req = "select * from "+language+"SENTENCES";
        Cursor cursor = db.rawQuery(req,null);
        cursor.move(index);

        //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame
        res[0]=cursor.getString(1);
        res[1]=cursor.getString(2);
        res[2]=cursor.getString(3);
        res[3]=cursor.getString(4);
        res[4]="+";
        res[5]=cursor.getString(5);
        res[6]=cursor.getString(6);
        res[7]=cursor.getString(7);
        res[8]=cursor.getString(8);
        //res[9]=cursor.getString(9); il faudra implémenter le fait que certaines phrases ne se jouent que à partir d'un certain nombre de joueur

        return res;
    }

    public int getNumberOfSentences(String language,Context context){
        SQLiteDatabase db;
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getReadableDatabase();
        String req = "select * from "+language+"SENTENCES";
        Cursor cursor = db.rawQuery(req,null);
        cursor.moveToLast();
        return cursor.getInt(0);//la première colonne de la dernière ligne est l'id de la dernière phrase (et donc c'est le nb de phrases vu que l'id commence a 1)
    }
}
