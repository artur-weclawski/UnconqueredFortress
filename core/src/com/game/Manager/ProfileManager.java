package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Entity.Base;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.Random;

public class ProfileManager {

    public ProfileManager() {

    }

    public Table createProfileTable(JSONObject save, BitmapFont font, LanguageManager languageManager, int x, String icon) {
        String language = languageManager.getLanguage();
        Table t = new Table();
        t.setBounds(x, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10 * 3, Gdx.graphics.getWidth() / 10 * 3);
        t.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("assets/backgrounds/profile_banner.png")))));

        TextField.TextFieldStyle tFS = new TextField.TextFieldStyle();
        TextFieldStyleManager tSM = new TextFieldStyleManager();
        tSM.setTextFieldStyle(tFS, new Skin(new TextureAtlas("assets/buttons/buttons_settings.pack")), font, "empty_background", Color.WHITE);

        TextField tDifficulty = new TextField(languageManager.getValue(language, "difficulty_field") + ": " + languageManager.getValue(language, save.getString("difficulty")), tSM.returnTextFieldStyle(tFS));
        tDifficulty.setAlignment(Align.left);
        TextField tFinishedMaps = new TextField(languageManager.getValue(language, "finishedmaps_field") + ": " + save.getInt("finishedMaps"), tSM.returnTextFieldStyle(tFS));
        tFinishedMaps.setAlignment(Align.left);
        TextField tWave = new TextField(languageManager.getValue(language, "wave_field") + ": " + save.getInt("wave"), tSM.returnTextFieldStyle(tFS));
        tWave.setAlignment(Align.left);
        TextField tGold = new TextField(languageManager.getValue(language, "gold_field") + ": " + save.getInt("gold"), tSM.returnTextFieldStyle(tFS));
        tGold.setAlignment(Align.left);
        TextField tDiamonds = new TextField(languageManager.getValue(language, "diamonds_field") + ": " + save.getInt("diamonds"), tSM.returnTextFieldStyle(tFS));
        tDiamonds.setAlignment(Align.left);
        Image local = new Image(new Texture(new FileHandle(icon)));
        local.setHeight(32);
        local.setWidth(32);
        t.add(local).align(Align.right).padRight(1);
        t.row();
        t.add(tDifficulty).width(t.getWidth() / 10 * 9).height(t.getHeight() / 10).padLeft(t.getWidth() / 10 * 2);
        t.row();
        t.add(tFinishedMaps).width(t.getWidth() / 10 * 9).height(t.getHeight() / 10).padLeft(t.getWidth() / 10 * 2);
        t.row();
        t.add(tWave).width(t.getWidth() / 10 * 9).height(t.getHeight() / 10).padLeft(t.getWidth() / 10 * 2);
        t.row();
        t.add(tGold).width(t.getWidth() / 10 * 9).height(t.getHeight() / 10).padLeft(t.getWidth() / 10 * 2);
        t.row();
        t.add(tDiamonds).width(t.getWidth() / 10 * 9).height(t.getHeight() / 10).padBottom(t.getHeight() / 2 - t.getHeight() / 10 - 20).padLeft(t.getWidth() / 10 * 2);
        return t;
    }

    public Table createEmptyProfileTable(BitmapFont font, int x) {

        Table t = new Table();
        t.setBounds(x, Gdx.graphics.getWidth() / 10, Gdx.graphics.getHeight() / 10 * 3, Gdx.graphics.getWidth() / 10 * 3);
        t.setBackground(new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("assets/backgrounds/profile_banner.png")))));

        ButtonStyleManager bSM = new ButtonStyleManager();
        TextButton.TextButtonStyle tBS = new TextButton.TextButtonStyle();
        bSM.setTextButtonStyle(tBS, new Skin(new TextureAtlas("assets/buttons/buttons_profile.pack")), font, "new_profile_button_up", "new_profile_button_down");
        TextButton tB = new TextButton(null, bSM.returnTextButtonStyle(tBS));

        t.add(tB).height(t.getHeight() / 4).width(t.getHeight() / 4).padBottom(t.getHeight() / 4);

        return t;
    }

    public long stringToSeed(String s) {
        final ThreadLocal<Random> RANDOM_THREAD_LOCAL = ThreadLocal.withInitial(Random::new);
        Random random = RANDOM_THREAD_LOCAL.get();
        long hash = 0;
        if (s.length() == 0) {
            return random.nextInt();
        }
        if (s.matches("-?[0-9]+")) {
            return Integer.parseInt(s);
        } else {
            for (char c : s.toCharArray()) {
                hash = 31L * hash + c;
            }
        }
        if (hash > Integer.MAX_VALUE) {
            return random.nextInt();
        } else {
            return hash;
        }
    }

    public JSONObject createEmptySave(String difficulty, int chosenProfile, String seedInput) {
        JSONObject j = new JSONObject();
        int seed = (int) stringToSeed(seedInput);
        j.put("seed", seed);//
        j.put("profileNumber", chosenProfile);
        j.put("difficulty", difficulty);
        j.put("finishedMaps", 0);
        j.put("wave", 0);
        j.put("maxHealth", 100);
        j.put("health", 100);
        j.put("gold", 200);
        j.put("diamonds", 0);
        j.put("terrainModifications", new JSONArray());
        j.put("buildings", new JSONArray());
        j.put("unlockedUpgrades", new JSONArray());
        j.put("roadObstacles", new JSONArray());

        return j;
    }

    public JSONObject getNewSave(JSONObject actualGame, Base base) {
        Random random = new Random(base.getSeed());
        actualGame.put("seed", random.nextInt());
        actualGame.put("finishedMaps", actualGame.getInt("finishedMaps") + 1);
        actualGame.put("diamonds", base.getDiamonds());
        actualGame.put("wave", 0);
        actualGame.put("maxHealth", base.getMaxHealth());
        actualGame.put("health", base.getMaxHealth());
        actualGame.put("gold", 200);
        actualGame.put("terrainModifications", new JSONArray());
        actualGame.put("buildings", new JSONArray());
        actualGame.put("roadObstacles", new JSONArray());

        return actualGame;
    }

    public JSONObject getReplaySave(JSONObject actualGame, Base base) {
        actualGame.put("finishedMaps", actualGame.getInt("finishedMaps") + 1);
        actualGame.put("diamonds", base.getDiamonds());
        actualGame.put("wave", 0);
        actualGame.put("maxHealth", base.getMaxHealth());
        actualGame.put("health", base.getMaxHealth());
        actualGame.put("gold", 200);
        actualGame.put("terrainModifications", new JSONArray());
        actualGame.put("buildings", new JSONArray());
        actualGame.put("roadObstacles", new JSONArray());

        return actualGame;
    }

    public Table getDeleteTable(int x, int y, int width, int height, float scale) {
        Image bin = new Image(new Texture("assets/icons/delete.png"));
        bin.setWidth(width);
        bin.setHeight(height);
        Table binTable = new Table();
        binTable.setBounds(x - 9 * scale, y - 12 * scale, bin.getWidth(), bin.getHeight());

        binTable.add(bin);

        return binTable;
    }

    public Table getMigrationSaveTable(int x, int y, int width, int height, float scale, boolean upload) {
        Image icon;
        if (upload)
            icon = new Image(new Texture("assets/icons/upload.png"));
        else
            icon = new Image(new Texture("assets/icons/download.png"));

        icon.setWidth(width);
        icon.setHeight(height);
        Table migrationTable = new Table();
        migrationTable.setBounds(x - 9 * scale, y - 12 * scale, icon.getWidth(), icon.getHeight());
        migrationTable.add(icon);

        return migrationTable;
    }
}