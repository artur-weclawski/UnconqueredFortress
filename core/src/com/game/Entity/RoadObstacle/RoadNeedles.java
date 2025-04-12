package com.game.Entity.RoadObstacle;

import com.game.Entity.Base;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

public class RoadNeedles extends RoadObstacle {

    public RoadNeedles(JSONObject obstacles, Base base, int tileX, int tileY, float scale, GameScreen gameScreen)
    {
        super(obstacles.getJSONObject("roadNeedles"), base,"assets/game/towers/road_needles.png", tileX, tileY, scale, gameScreen);
    }

}
