package com.example.simplespacedrepetition;

import java.io.Serializable;
import java.util.ArrayList;

public class Word implements Serializable {
    String word;
    Integer lastDate;
    Integer curInterval;
    Integer quantityCorrectAnswers = 0;
    boolean inUsage = false;
    String[] keys;

    Word(String word, Integer lastDate, Integer curInterval, String[] keys) {
        this.word = firstUpperCase(word);
        this.lastDate = lastDate;
        this.curInterval = curInterval;

        ArrayList<String> temp= new ArrayList<>();

        for(int i=0;i<keys.length;i++){
            if (keys[i].length() > 0){
                temp.add(firstUpperCase(keys[i]));
            }
        }

        this.keys = new String[temp.size()];
        temp.toArray(this.keys);
    }

    // сделать первую букву заглавной
    public String firstUpperCase(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return word.substring(0, 1).toUpperCase() + word.substring(1);
    }

    public String getWord() {
        return word;
    }

    public Integer getLastDate() {
        return lastDate;
    }

    public Integer getCurInterval() {
        return curInterval;
    }

    public Integer getNumberCorrectAnswers() {
        return quantityCorrectAnswers;
    }

    public boolean getUsageNow() {
        return inUsage;
    }

    public String[] getKeysArray() {
        return keys;
    }

    public String getKeysSeparatedLineBreak() {
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < keys.length; i++) {
            str.append(keys[i]);
            if (i + 1 < keys.length) {
                str.append("\n");
            }
        }
        return str.toString();
    }

    public String getAllToSaveDB() {
        StringBuffer str = new StringBuffer(word + ";" + lastDate + ";" + curInterval + ";");
        for (int i = 0; i < keys.length; i++) {
            str.append(keys[i] + ";");
        }
        str.append("\n");

        return str.toString();
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setLastDate(Integer lastDate) {
        this.lastDate = lastDate;
    }

    public void setQuantityCorrectAnswers(Integer quantityCorrectAnswers) {
        this.quantityCorrectAnswers = quantityCorrectAnswers;
    }

    public void setCurInterval(Integer curInterval) {
        this.curInterval = curInterval;
    }

    public void setKeys(String[] keys) {
        ArrayList<String> temp= new ArrayList<>();

        for(int i=0;i<keys.length;i++){
            if (keys[i].length() > 0){
                temp.add(firstUpperCase(keys[i]));
            }
        }

        this.keys = new String[temp.size()];
        temp.toArray(this.keys);
    }


}
