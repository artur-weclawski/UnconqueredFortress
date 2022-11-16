package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Blob extends Enemy{

    public Blob(JSONObject enemy){
        super(enemy.getFloat("regenTime"), enemy.getFloat("healthRegen"), enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"),"assets/game/enemies/blob.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }

}