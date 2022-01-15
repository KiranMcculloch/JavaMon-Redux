package com.mcculloch.pokemon.model;

import com.mcculloch.pokemon.Templates.TemplateMove;

public class Move {
    final public String name;
    final public boolean physical;
    //accuracy 0-100
    final public int id, power, type, maxPP, accuracy;
    private int pp;

    //type index in first position has output effectiveness against type index in second position
    public final static float[][] typeChart = {
            {1,1,1,1,1,0.5f,1,0,0.5f,1,1,1,1,1,1,1,1,1},
            {2,1,0.5f,0.5f,1,2,0.5f,0,2,1,1,1,1,0.5f,2,1,2,0.5f},
            {1,2,1,1,1,0.5f,2,1,0.5f,1,1,2,0.5f,1,1,1,1,1},
            {1,1,1,0.5f,0.5f,0.5f,1,0.5f,0,1,1,2,1,1,1,1,1,2},
            {1,1,0,2,1,2,0.5f,1,2,2,1,0.5f,2,1,1,1,1,1},
            {1,0.5f,2,1,0.5f,1,2,1,0.5f,2,1,1,1,1,2,1,1,1},
            {1,0.5f,0.5f,0.5f,1,1,1,0.5f,0.5f,0.5f,1,2,1,2,1,1,2,0.5f},
            {0,1,1,1,1,1,1,2,1,1,1,1,1,2,1,1,0.5f,1},
            {1,1,1,1,1,2,1,1,0.5f,0.5f,0.5f,1,0.5f,1,2,1,1,2},
            {1,1,1,1,1,0.5f,2,1,2,0.5f,0.5f,2,1,1,2,0.5f,1,1},
            {1,1,1,1,2,2,1,1,1,2,0.5f,0.5f,1,1,1,0.5f,1,1},
            {1,1,0.5f,0.5f,2,2,0.5f,1,0.5f,0.5f,2,0.5f,1,1,1,0.5f,1,1},
            {1,1,2,1,0,1,1,1,1,1,2,0.5f,0.5f,1,1,0.5f,1,1},
            {1,2,1,2,1,1,1,1,0.5f,1,1,1,1,0.5f,1,1,0,1},
            {1,1,2,1,2,1,1,1,0.5f,0.5f,0.5f,2,1,1,0.5f,2,1,1},
            {1,1,1,1,1,1,1,1,0.5f,1,1,1,1,1,1,2,1,0},
            {1,0.5f,1,1,1,1,1,2,1,1,1,1,1,2,1,1,0.5f,0.5f},
            {1,2,1,0.5f,1,1,1,1,0.5f,0.5f,1,1,1,1,1,2,2,1},
    };

    public Move(int index){
        this.id = index;
        this.name = TemplateMove.moveList[index].name;
        this.type = TemplateMove.moveList[index].type;
        this.power = TemplateMove.moveList[index].power;
        this.physical = TemplateMove.moveList[index].physical;
        this.pp = TemplateMove.moveList[index].maxPP;
        this.maxPP = TemplateMove.moveList[index].maxPP;
        this.accuracy = TemplateMove.moveList[index].accuracy;
    }

    public String getName() {
        return name;
    }

    public int getPP(){
        return this.pp;
    }

    public void addPP(int x){
        this.pp += x;
    }

    public static float effectiveness(int type, Pokemon target){
        if (target.getTypeOne() == target.getTypeTwo()){
            return typeChart[type][target.getTypeOne()];
        } else {
            return typeChart[type][target.getTypeOne()] * typeChart[type][target.getTypeTwo()];
        }
    }

    public int getAccuracy(){
        return accuracy;
    }

}
