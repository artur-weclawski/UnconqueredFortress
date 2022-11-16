package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Assassin extends Enemy{

    public Assassin(JSONObject enemy){
        super(enemy.getInt("dodgeChance"), enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"), "assets/game/enemies/assassin.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }

}
