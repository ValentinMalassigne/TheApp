package fr.mapoe.appproject.sqlite;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.appcompat.app.AppCompatActivity;

import fr.mapoe.appproject.sqlite.MySQLiteOpenHelper;

public class DataBaseManager extends AppCompatActivity {

    private MySQLiteOpenHelper accesDB;

    //0: point    1: réponse    2: phrase    3:type  4:rightAnswer (+ = oui) 5:boutonrep1 6: boutonrep2 7: la punition 8:typeOfGame
    public void addSentenceToDB(String language, String[] sentenceData,Context context){ //language doit valoir FR,EN,ES,...
        SQLiteDatabase db;
        String typeOfGame,sentenceType,sentence,popUp,punition,rightButton,leftButton,signe,points,minimumPlayers;
        points=sentenceData[0];
        popUp=sentenceData[1];
        sentence=sentenceData[2];
        sentenceType=sentenceData[3];
        signe=sentenceData[4];
        rightButton=sentenceData[5];
        leftButton=sentenceData[6];
        punition=sentenceData[7];
        typeOfGame=sentenceData[8];
        minimumPlayers="2";

        //on créé un accès à la BD
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getWritableDatabase();
        //on créé/ouvre la DB et on crée la table si elle n'existe pas

        db.execSQL("CREATE TABLE if not exists "+language+"SENTENCES (id INTEGER PRIMARY KEY,points TEXT,popUp TEXT,sentence TEXT,sentenceType TEXT,rightButton TEXT,leftButton TEXT,signe TEXT,punition TEXT,typeOfGame TEXT,minimumPlayers TEXT)");

        //on insert la phrase comme il faut
        db.execSQL("INSERT INTO "+language+"SENTENCES (points,popUp,sentence,sentenceType,rightButton,leftButton,signe,punition,typeOfGame,minimumPlayers) values (\""+points+"\",\""+popUp+"\",\""+sentence+"\",\""+sentenceType+"\",\""+rightButton+"\",\""+leftButton+"\",\""+signe+"\",\""+punition+"\",\""+typeOfGame+"\",\""+minimumPlayers+"\")");
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
        res[4]=cursor.getString(7);
        res[5]=cursor.getString(6);
        res[6]=cursor.getString(5);
        res[7]=cursor.getString(8);
        res[8]=cursor.getString(9);
        //res[9]=cursor.getString(9); il faudra implémenter le fait que certaines phrases ne se jouent que à partir d'un certain nombre de joueur

        return res;
    }

    public int getNumberOfSentences(String language,Context context){
        SQLiteDatabase db;
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getReadableDatabase();
        String req = "select * from "+language+"SENTENCES";
        Cursor cursor = db.rawQuery(req,null);
        //cursor.moveToLast();
        return cursor.getCount();//on renvoit le nb de lignes
    }

    public void updateSentenceInDB(String language,String[] sentenceData,String index,Context context){
        SQLiteDatabase db;
        String typeOfGame,sentenceType,sentence,popUp,punition,rightButton,leftButton,signe,points,minimumPlayers;
        points=sentenceData[0];
        popUp=sentenceData[1];
        sentence=sentenceData[2];
        sentenceType=sentenceData[3];
        signe=sentenceData[4];
        rightButton=sentenceData[5];
        leftButton=sentenceData[6];
        punition=sentenceData[7];
        typeOfGame=sentenceData[8];
        minimumPlayers="2";
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getWritableDatabase();
        //db.execSQL("UPDATE "+language+"SENTENCES SET (points,popUp,sentence,sentenceType,rightAnswer,otherAnswers,punition,typeOfGame,minimumPlayers) values (\""+points+"\",\""+popUp+"\",\""+sentence+"\",\""+sentenceType+"\",\""+rightAnswer+"\",\""+otherAnswers+"\",\""+punition+"\",\""+typeOfGame+"\",\""+minimumPlayers+"\") WHERE sentence = "+"\""+index+"\"");
        db.execSQL("UPDATE "+language+"SENTENCES SET points = \""+points+"\", popUp = \""+popUp+"\", sentence = \""+sentence+"\", sentenceType = \""+sentenceType+"\", rightButton = \""+rightButton+"\", leftButton = \""+leftButton+"\", signe = \""+signe+"\", punition = \""+punition+"\", typeOfGame = \""+typeOfGame+"\", minimumPlayers = \""+minimumPlayers+"\" WHERE sentence = \""+index+"\"");
    }

    public void deleteSentenceInDB(String language,String index,Context context){
        SQLiteDatabase db;
        accesDB = new MySQLiteOpenHelper(context,"GamesDataBase",null,1);
        db = accesDB.getWritableDatabase();
        db.execSQL("DELETE FROM "+language+"SENTENCES WHERE sentence = \""+index+"\"");
    }

    public void updateFromOnlineDB(String[][] sentenceTab,String language,Context context){
        for(int i=0;i<sentenceTab.length;i++){
            addSentenceToDB(language,adaptToLocalBase(sentenceTab[i]),context);
        }
    }

    private String[] adaptToLocalBase(String[] sentence){
        String[] adaptedSentence=new String[10];
        adaptedSentence[0]=sentence[8];
        adaptedSentence[1]=sentence[3];
        adaptedSentence[2]=sentence[2];
        adaptedSentence[3]=sentence[1];
        adaptedSentence[4]=sentence[6];
        adaptedSentence[5]=sentence[4];
        adaptedSentence[6]=sentence[5];
        adaptedSentence[7]=sentence[7];
        adaptedSentence[8]=sentence[0];
        return adaptedSentence;
    }
}
