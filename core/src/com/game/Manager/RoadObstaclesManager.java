package com.game.Manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.game.Entity.Enemy.Enemy;
import com.game.Entity.RoadObstacle.RoadObstacle;
import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Iterator;

public class RoadObstaclesManager {
    private ArrayList<Enemy> enemies;
    private ArrayList<RoadObstacle> roadObstacles;
    private int[][] buildArr;

    private boolean isDisabledListeners;

    public RoadObstaclesManager(ArrayList<Enemy> enemies, int[][] buildArr) {
        this.enemies = enemies;
        this.roadObstacles = new ArrayList<>();
        this.buildArr = buildArr;
        this.isDisabledListeners = false;
    }

    public JSONArray getRoadObstacles() {
        JSONArray j = new JSONArray();

        for (RoadObstacle roadObstacle : roadObstacles) {
            JSONObject t = new JSONObject();
            t.put("name", roadObstacle.getName());
            t.put("x", roadObstacle.getTileX());
            t.put("y", roadObstacle.getTileY());
            t.put("usesLeft", roadObstacle.getUsesLeft());
            j.put(t);
        }
        return j;
    }


    public void buyObstacle(RoadObstacle r) {
        roadObstacles.add(r);
    }

    public void update() {
        Iterator<RoadObstacle> rIterator = roadObstacles.iterator();
        while (rIterator.hasNext()) {
            RoadObstacle r = rIterator.next();
            r.update(enemies);
            if (r.getUsesLeft() <= 0) {
                if (r.getIsOnMap()) {
                    r.setIsOnMap(false);
                    buildArr[r.getTileX()][r.getTileY()] = 0;
                    r.setBounds(0, 0, 0, 0);
                    r.clearImage();
                }
                if (r.getSlowedEnemies().size() == 0)
                    rIterator.remove();
            }
        }
    }

    public void render(SpriteBatch spritebatch) {
        for (RoadObstacle r : roadObstacles) {
            r.render(spritebatch);
        }
    }

    public void initObstacles(ArrayList<RoadObstacle> roadObstaclesLoad) {
        this.roadObstacles = roadObstaclesLoad;
    }

    public int[][] getArr() {
        return buildArr;
    }

    public void sellRoadObstacle(int x, int y) {
        roadObstacles.removeIf(r -> r.getTileX() == x && r.getTileY() == y);
    }

    public int getSellWorth(int x, int y) {
        for (RoadObstacle r : roadObstacles)
            if (r.getTileX() == x && r.getTileY() == y)
                return r.getSellWorth();
        return 0;
    }

    public void disableListeners() {
        isDisabledListeners = true;
        for (RoadObstacle r : roadObstacles) {
            r.disableListeners();
        }
    }

    public void enableListeners() {
        isDisabledListeners = false;
        for (RoadObstacle r : roadObstacles) {
            r.enableListeners();
        }
    }

    public boolean getIsDisabledListeners() {
        return isDisabledListeners;
    }
}