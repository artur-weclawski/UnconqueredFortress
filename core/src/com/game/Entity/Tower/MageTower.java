package com.game.Entity.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.Entity.Base;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

public class MageTower extends Tower{

    public MageTower(JSONObject turretLevels, Base base, int tileX, int tileY, float scale, GameScreen gameScreen)
    {
        super(turretLevels, base, true, "mageTower", "assets/game/towers/mageTower.png", "assets/game/towers/mageTowerBase.png", 64, new TextureRegion(new Texture(Gdx.files.internal("assets/game/bullets/mageBullet64.png"))), 64, 0, true, tileX, tileY, scale, gameScreen);
    }

}
