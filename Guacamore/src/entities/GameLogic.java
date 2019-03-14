/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Random;

/**
 *
 * @author planzagom
 */
public class GameLogic implements Serializable {

    public boolean winner = false;
    final int NUM_MOSTER = 5;
    private int correctButton = 0;
    public Hashtable<String, Integer> scoreData = new Hashtable<String, Integer>();
    

    public boolean exists(String username) {
        return scoreData.get(username) != null;
    }

    public int getRandomButton() {
        Random rand = new Random();
        this.correctButton = rand.nextInt(9);
        return correctButton;
    }

    public boolean insert(String username) {
        if (exists(username)) {
            return false;
        }
        updateScore(username, 0);
        return true;
    }

    public void updateScore(String username, int score) {
        scoreData.put(username, score);
    }

    public void incScore(String username) {
        if (!exists(username)) {
            scoreData.put(username, 1);
        } else {
            int currentScore = scoreData.get(username);
            scoreData.put(username, currentScore + 1);
        }

    }

    public String getWinnerUsername() {
        for (String key : scoreData.keySet()) {
            if(scoreData.get(key)>=this.NUM_MOSTER){
                return key;
            }
        }
        return null;
    }

    public boolean isWinner() {
        return winner;
    }

    public void setWinner(boolean winner) {
        this.winner = winner;
    }

    public int getCorrectButton() {
        return correctButton;
    }

    public void setCorrectButton(int correctButton) {
        this.correctButton = correctButton;
    }
    
    public void clearScores(){
        for (String key : scoreData.keySet()) {
            scoreData.put(key,0);
        }
    }

}
