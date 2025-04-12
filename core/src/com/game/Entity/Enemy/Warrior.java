package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Warrior extends Enemy{

    public Warrior(JSONObject enemy){
        super(enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"), "assets/game/enemies/warrior.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }

}
