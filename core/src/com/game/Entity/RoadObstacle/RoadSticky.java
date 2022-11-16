package com.game.Entity.RoadObstacle;

import com.game.Entity.Base;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

public class RoadSticky extends RoadObstacle {

    public RoadSticky(JSONObject obstacles, Base base, int tileX, int tileY, float scale, GameScreen gameScreen)
    {
        super(obstacles.getJSONObject("roadSticky"), base,"assets/game/towers/road_sticky.png", tileX, tileY, scale, gameScreen);
    }

}
