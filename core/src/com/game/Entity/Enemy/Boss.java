package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Boss extends Enemy{

    public Boss(JSONObject enemy){
        super(enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"), "assets/game/enemies/boss.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }
}
