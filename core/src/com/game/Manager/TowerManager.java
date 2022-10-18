package com.game.Manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.game.Entity.Enemy.Enemy;
import com.game.Entity.Tower.Tower;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;

public class TowerManager {

    private ArrayList<Tower> towers;
    private ArrayList<Enemy> enemies;

    private boolean isDisabledListeners;

    public TowerManager(ArrayList<Enemy> enemies) {
        this.enemies = enemies;
        towers = new ArrayList<>();
        isDisabledListeners = false;
    }

    public void buyTower(Tower t) {
        towers.add(t);
    }

    public void sellTower(int x, int y) {
        towers.removeIf(t -> t.getTileX() == x && t.getTileY() == y);
    }

    public int getSellWorth(int x, int y) {
        for (Tower t : towers) {
            if (t.getTileX() == x && t.getTileY() == y) {
                return t.getSellWorth();
            }
        }
        return 0;
    }

    public JSONArray getTowers() {
        JSONArray j = new JSONArray();
        for (Tower tower : towers) {
            JSONObject t = new JSONObject();
            t.put("name", tower.getName());
            t.put("x", tower.getTileX());
            t.put("y", tower.getTileY());
            t.put("level", tower.getLvl());
            j.put(t);
        }
        return j;
    }

    public void refreshReloads(String towerName) {
        for (Tower t : towers) {
            if (Objects.equals(t.getName(), towerName))
                t.refreshReload();
        }
    }

    public void update(float deltaTime) {
        for (Tower t : towers) {
            t.update(deltaTime, enemies);
        }
    }

    public void render(SpriteBatch spritebatch, ShapeRenderer shapeRenderer) {
        for (Tower t : towers) {
            t.render(spritebatch, shapeRenderer);
        }
    }

    public void disableListeners() {
        isDisabledListeners = true;
        for (Tower t : towers) {
            t.disableListeners();
        }
    }

    public void enableListeners() {
        isDisabledListeners = false;
        for (Tower t : towers) {
            t.enableListeners();
        }
    }

    public boolean getIsDisabledListeners() {
        return isDisabledListeners;
    }


}
