package fr.mapoe.appproject.tools;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *recupère un objet JSON
 * decode l'objet et le renvoie sous forme d'un tableau de string
 */
public class Parser {
    public String[] result; // 0: typeOfGame 1:sentenceType 2:sentence3: answer4:rightButton 5:leftButton 6: signe 7:punition 8: point 9: minimumPlayer
    private JSONObject jsonSentence;
    public double dbVersion;
    public Parser(JSONObject jsonObject) {
        this.jsonSentence = jsonObject;
    }

    // fonction pour Parse les phrases
    public void parseSentenceData() {
        try {
            String typeOfGame = jsonSentence.getString("typeOfGame");//0
            String sentenceType = jsonSentence.getString("sentenceType");//1
            String sentence = jsonSentence.getString("sentence");//2
            String answer = jsonSentence.getString("answer");//3
            String rightButton = jsonSentence.getString("rightButton");//4
            String leftButton = jsonSentence.getString("leftButton");//5
            String signe = jsonSentence.getString("signe");//6
            String punition = jsonSentence.getString("punition");//7
            String point = jsonSentence.getString("points");//8
            String minimunPlayer = jsonSentence.getString("minimumPlayer");
            result = new String[]{typeOfGame, sentenceType, sentence, answer, rightButton, leftButton, signe, punition, point,minimunPlayer};
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    // fonction pour parse le num de version
    public void parseVersionNumber(){
        try{
            dbVersion = jsonSentence.getDouble("dbVersion");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
    }

    public double getDbVersion() {
        return dbVersion;
    }

    public String[] getSentenceResult() {
        return result;
    }
}