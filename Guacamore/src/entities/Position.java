/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.util.Random;

/**
 *
 * @author PLANZAGOM
 */
public class Position {
    private int x;
    private int y;
    public int MAX_X=2;
    public int MAX_Y=2;

    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }
    
    public Position(){
        this.random();
    }
    
    public Position(String strPos){
        if (checkStrPos(strPos)){
            strPos = strPos.replace("(","");
            strPos = strPos.replace(")","");
            String x = strPos.split(",")[0];
            String y = strPos.split(",")[1];
            this.x = Integer.valueOf(x);
            this.y = Integer.valueOf(y);
        }
    }
    
    public void random(){
        this.x = this.createRandom(0,this.MAX_X);
        this.y = this.createRandom(0,this.MAX_Y);
    }
    
    private int createRandom(int min, int max) {
        Random rand = new Random();
        return min + rand.nextInt(max - min + 1);
    }
    
    public boolean checkStrPos(String strPos){
        return strPos.matches("\\([0-9]+,[0-9]+\\)");
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
    
    @Override
    public String toString(){
        return "("+x+","+y+")";
    }
    
    
    
    
}
