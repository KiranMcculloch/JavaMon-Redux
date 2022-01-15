package com.mcculloch.pokemon.model;

import com.mcculloch.pokemon.Templates.TemplateItem;

public class Item {
    final public int id;
    final public String name, description;
    private int quantity;

    public void add(int inc){
        this.quantity += inc;
    }

    public Item(int id, int quantity){
        this.id = id;
        this.name = TemplateItem.itemList[id].name;
        this.description = TemplateItem.itemList[id].description;
        this.quantity = quantity;
    }

    public static void useItem (Pokemon pokemon, Item item){
        Pokemon.checkEvolutionRequirements(pokemon, item);
    }

}
