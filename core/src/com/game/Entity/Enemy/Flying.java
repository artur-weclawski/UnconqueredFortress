package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Flying extends Enemy{

    public Flying(JSONObject enemy){
        super(enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"), "assets/game/enemies/flying.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }

}