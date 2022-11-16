package com.game.Entity.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.Entity.Base;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

public class BowTower extends Tower{

    public BowTower(JSONObject turretLevels, Base base, int tileX, int tileY, float scale, GameScreen gameScreen)
    {
        super(turretLevels, base, true, "crossbowTower", "assets/game/towers/crossbowTower.png", null, 64, new TextureRegion(new Texture(Gdx.files.internal("assets/game/bullets/crossbowArrow64.png"))), 64, 0, false, tileX, tileY, scale, gameScreen);
    }

}
