package com.game.Entity.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.game.Entity.Base;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

public class MeleeTower extends Tower{


    public MeleeTower(JSONObject turretLevels, Base base, int tileX, int tileY, float scale, GameScreen gameScreen)
    {
        super(turretLevels, base, false, "meleeTower", "assets/game/towers/meleeTower.png", "assets/game/towers/meleeTowerBase.png",  64, new TextureRegion(new Texture(Gdx.files.internal("assets/game/bullets/meleeAttack64.png"))), 64, 0, false, tileX, tileY, scale, gameScreen);
    }

}
