package com.game.Entity.RoadObstacle;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.game.Entity.Base;
import com.game.Entity.Enemy.Enemy;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class RoadObstacle extends Actor {

    private String name;
    private Base base;
    private int tileX, tileY;
    private float scale;
    private TextureRegion texture;
    private int usesLeft;
    private Vector2 position;
    private int textureSize;
    private List<Integer> attackedEnemies;
    private ArrayList<Enemy> slowedEnemies;
    private boolean isOnMap;

    private int dmg;
    private float slow;

    private int buyPrice;


    public RoadObstacle(JSONObject obstacle, Base base, String path, int tileX, int tileY, float scale, GameScreen gameScreen) {
        this.name = obstacle.getString("name");
        this.base = base;

        this.buyPrice = obstacle.getInt("cost");

        this.usesLeft = obstacle.getInt("uses");
        if (Objects.equals(name, "roadSticky"))
            this.slow = obstacle.getFloat("slow");
        else if (Objects.equals(name, "roadNeedles"))
            this.dmg = obstacle.getInt("dmg");

        this.tileX = tileX;
        this.tileY = tileY;
        this.scale = scale;
        this.textureSize = 64;
        this.isOnMap = true;

        attackedEnemies = new ArrayList<>();
        slowedEnemies = new ArrayList<>();

        this.texture = new TextureRegion(new Texture(Gdx.files.internal(path)));

        this.position = new Vector2(tileX * scale * 64 + Gdx.graphics.getWidth() / 20, (9 - tileY) * scale * 64 + (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2);

        this.addListener(new ClickListener() {
            private boolean isClicked = false;
            public void clicked(InputEvent event, float x, float y) {
                isClicked = true;

            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                getBase().setUsesLeft(getUsesLeft());
                getBase().setRoadObstacleId(getThis().hashCode());

                if (getUsesLeft() > 0)
                    getBase().setInfoToDisplay(5, getName());
            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!isClicked) {
                    getBase().setRoadObstacleId(0);
                    getBase().setInfoToDisplay(0);
                }
                isClicked = false;
            }
        });

        this.setBounds(position.x, position.y, textureSize, textureSize);
        this.disableListeners();
    }

    public void clearImage() {
        this.texture.setTexture(new Texture(Gdx.files.internal("assets/game/towers/clearImage.png")));
    }

    public ArrayList<Enemy> getSlowedEnemies() {
        return slowedEnemies;
    }

    public boolean getIsOnMap() {
        return isOnMap;
    }

    public void setIsOnMap(boolean onMap) {
        isOnMap = onMap;
    }

    private RoadObstacle getThis() {
        return this;
    }

    public Base getBase() {
        return base;
    }

    public void setUsesLeft(int usesLeft) {
        this.usesLeft = usesLeft;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public void enableListeners() {
        this.setTouchable(Touchable.enabled);
    }

    public void disableListeners() {
        this.setTouchable(Touchable.disabled);
    }

    public int getSellWorth() {
        return Math.round(buyPrice / 20f * usesLeft);
    }

    public void update(ArrayList<Enemy> enemies) {

        for (Enemy e : enemies) {
            if (textureSize / 2 * scale >= Vector2.dst(position.x + textureSize * scale / 2, position.y + textureSize * scale / 2, e.getPosition().x + e.getEnemySize() * scale / 2, e.getPosition().y + e.getEnemySize() * scale / 2)) {

                if (!attackedEnemies.contains(e.hashCode())) {
                    attackedEnemies.add(e.hashCode());

                    if (Objects.equals(name, "roadNeedles")) {
                        attackedEnemies.add(e.hashCode());
                        e.dealDmg(dmg);
                    } else if (Objects.equals(name, "roadSticky")) {
                        e.changeSpeed(slow);
                        attackedEnemies.add(e.hashCode());
                        slowedEnemies.add(e);
                    }

                    usesLeft -= 1;
                    if (getBase().getRoadObstacleId() == this.hashCode()) {
                        getBase().setUsesLeft(usesLeft);
                    }

                    break;
                }
            }

            if (Objects.equals(e.getName(), "summoner")) {
                for (Enemy eS : e.getSummonedList()) {
                    if (textureSize / 2 * scale >= Vector2.dst(position.x + textureSize * scale / 2, position.y + textureSize * scale / 2, eS.getPosition().x + eS.getEnemySize() * scale / 2, eS.getPosition().y + eS.getEnemySize() * scale / 2)) {

                        if (!attackedEnemies.contains(eS.hashCode())) {
                            attackedEnemies.add(eS.hashCode());

                            if (Objects.equals(name, "roadNeedles")) {
                                attackedEnemies.add(eS.hashCode());
                                eS.dealDmg(dmg);
                            } else if (Objects.equals(name, "roadSticky")) {
                                eS.changeSpeed(slow);
                                attackedEnemies.add(eS.hashCode());
                                slowedEnemies.add(eS);
                            }

                            usesLeft -= 1;
                            if (getBase().getRoadObstacleId() == this.hashCode()) {
                                getBase().setUsesLeft(usesLeft);
                            }

                            break;
                        }
                    }
                }
            }
        }

        Iterator<Enemy> eIterator = slowedEnemies.iterator();
        while (eIterator.hasNext()) {
            Enemy e = eIterator.next();

            if (textureSize / 2 * scale < Vector2.dst(position.x + textureSize * scale / 2, position.y + textureSize * scale / 2, e.getPosition().x + e.getEnemySize() * scale / 2, e.getPosition().y + e.getEnemySize() * scale / 2)) {
                e.changeSpeed(1f / slow);
                eIterator.remove();
            }
        }
    }


    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x, position.y, textureSize * scale, textureSize * scale);
    }


}
