package com.game.Entity.Enemy;

import org.json.JSONObject;

public class Summoner extends Enemy{

    public Summoner(JSONObject enemy, JSONObject summon){
        super(summon, enemy.getFloat("delayBetweenSummonings"), enemy.getFloat("summoningTime"), "assets/game/enemies/summoner_summoning.png", enemy.getInt("health"), enemy.getInt("damage"), enemy.getInt("money"), enemy.getInt("diamonds"), enemy.getInt("speed"), "assets/game/enemies/summoner.png", enemy.getString("name"), 64, enemy.getBoolean("isFlying"));
    }

}
