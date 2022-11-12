package com.game.Entity.Tower;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.*;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Null;
import com.game.Entity.Base;
import com.game.Entity.Bullet;
import com.game.Entity.Enemy.Enemy;
import com.game.Screens.GameScreen;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
import java.util.Random;

public class Tower extends Actor {
    private JSONArray towerLevels;
    private String name;
    private float range;
    private float bulletDamage;
    private float scale;
    private Enemy enemyToFollow;
    private TextureRegion bulletTexture;
    private int towerTextureSize;
    private int bulletTextureSize;
    private Vector2 position;
    private int tileX, tileY;
    private float bulletSpeed;
    private Animation<TextureRegion> towerAnimation;
    private Animation<TextureRegion> towerAnimation2;
    private float timeToShoot;
    private float reloadTime;
    private float rotation;
    private int lvl;
    private int worth;
    private ArrayList<Bullet> towerBullets;
    private float stateTime;
    private Base base;
    private boolean canAttackFlying;
    private boolean isMouseEntered;
    private float bulletSplashRange;
    private GameScreen gameScreen;

    private float shootDelayLeft, shootDelay;
    private Bullet delayBullet;
    private boolean looping;
    private int framesDelay;

    private TextureRegion[] animationSprites, animationSprites2;

    public Tower(JSONObject towerLevelsAll, Base base, boolean canAttackFlying, String name, String path, @Null String path2, int towerTextureSize, TextureRegion bulletTexture, int bulletTextureSize, int framesDelay, boolean looping, int tileX, int tileY, float scale, GameScreen gameScreen) {
        this.name = name;
        this.canAttackFlying = canAttackFlying;
        this.towerLevels = towerLevelsAll.getJSONArray(name);
        this.gameScreen = gameScreen;
        JSONObject turretFirstLevel = this.towerLevels.getJSONObject(0);
        this.looping = looping;
        this.base = base;
        this.isMouseEntered = false;
        this.timeToShoot = 0;
        this.reloadTime = turretFirstLevel.getFloat("reload") * base.getMultipliers().getFloat("reloadSpeedMultiplier" + name);
        ;
        this.worth = turretFirstLevel.getInt("cost");
        this.scale = scale;
        this.towerBullets = new ArrayList<>();
        this.bulletSplashRange = turretFirstLevel.getInt("splash") * scale;
        this.tileX = tileX;
        this.tileY = tileY;
        this.framesDelay = framesDelay;

        this.stateTime = 100f;

        this.bulletTexture = bulletTexture;
        this.towerTextureSize = towerTextureSize;
        this.bulletTextureSize = bulletTextureSize;

        this.bulletSpeed = turretFirstLevel.getFloat("bulletSpeed") * scale;
        this.range = turretFirstLevel.getFloat("range") * scale;

        this.lvl = turretFirstLevel.getInt("lvl");
        this.enemyToFollow = null;


        Texture spriteMap = new Texture(Gdx.files.internal(path));
        TextureRegion[][] spritePosition = TextureRegion.split(spriteMap, towerTextureSize, towerTextureSize);
        animationSprites = new TextureRegion[spritePosition[0].length];

        for (int i = 0; i < animationSprites.length; i++) {
            animationSprites[i] = spritePosition[0][i];
        }

        this.towerAnimation = new Animation<>(turretFirstLevel.getFloat("reload") / animationSprites.length, animationSprites);


        shootDelay = turretFirstLevel.getFloat("reload") / animationSprites.length * framesDelay;

        Texture spriteMap2;
        TextureRegion[][] spritePosition2;

        if (path2 != null) {
            spriteMap2 = new Texture(Gdx.files.internal(path2));
            spritePosition2 = TextureRegion.split(spriteMap2, towerTextureSize, towerTextureSize);
            animationSprites2 = new TextureRegion[spritePosition2[0].length];

            for (int i = 0; i < animationSprites2.length; i++) {
                animationSprites2[i] = spritePosition2[0][i];
            }
            this.towerAnimation2 = new Animation<>(turretFirstLevel.getFloat("reload") / animationSprites.length, animationSprites2);

        }


        this.bulletDamage = turretFirstLevel.getFloat("dmg");

        this.position = new Vector2(tileX * scale * towerTextureSize + Gdx.graphics.getWidth() / 20, (9 - tileY) * scale * towerTextureSize + (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2);
        this.rotation = 0;

        this.addListener(new ClickListener() {
            private boolean isClicked = false;

            public void clicked(InputEvent event, float x, float y) {
                TowerLevelUp();
                gameScreen.getBuySound().dispose();
                gameScreen.getBuySound().play();
                isClicked = true;

            }

            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isMouseEntered = true;
                if (getLvl() < getTowerLevels().length())
                    getBase().setInfoToDisplay(2, getName(), getTowerLevels().getJSONObject(getLvl() - 1), getTowerLevels().getJSONObject(getLvl()));
                else
                    getBase().setInfoToDisplay(2, getName(), getTowerLevels().getJSONObject(getLvl() - 1), null);

            }

            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                if (!isClicked) {
                    isMouseEntered = false;
                    getBase().setInfoToDisplay(0);
                }
                isClicked = false;
            }
        });
        this.setBounds(position.x, position.y, towerTextureSize, towerTextureSize);
        this.disableListeners();
    }

    public void enableListeners() {
        this.setTouchable(Touchable.enabled);
    }

    public void disableListeners() {
        this.setTouchable(Touchable.disabled);
    }

    @Override
    public String getName() {
        return name;
    }

    public Base getBase() {
        return base;
    }

    public void setLevel(int lvl) {
        JSONObject lvlUp = towerLevels.getJSONObject(lvl - 1);
        this.reloadTime = lvlUp.getFloat("reload") * base.getMultipliers().getFloat("reloadSpeedMultiplier" + name);
        this.shootDelay = lvlUp.getFloat("reload") / animationSprites.length * framesDelay;
        this.bulletDamage = lvlUp.getFloat("dmg");
        this.bulletSpeed = lvlUp.getFloat("bulletSpeed") * scale;
        this.bulletSplashRange = lvlUp.getInt("splash") * scale;
        this.range = lvlUp.getFloat("range") * scale;
        this.lvl = lvlUp.getInt("lvl");

        this.worth = 0;
        for (int j = 0; j < lvl; j++)
            this.worth += towerLevels.getJSONObject(j).getInt("cost");
    }

    public void TowerLevelUp() {
        if (lvl < towerLevels.length()) {
            int cost = Math.round(towerLevels.getJSONObject(lvl).getInt("cost") * base.getMultipliers().getFloat("upgradeCostMultiplier"));

            if (base.getMoney() >= cost) {
                JSONObject lvlUp = towerLevels.getJSONObject(lvl);
                worth += lvlUp.getInt("cost");
                base.decreaseMoney(cost);
                reloadTime = lvlUp.getFloat("reload") * base.getMultipliers().getFloat("reloadSpeedMultiplier" + name);
                shootDelay = lvlUp.getFloat("reload") / animationSprites.length * framesDelay;
                bulletDamage = lvlUp.getFloat("dmg");
                bulletSpeed = lvlUp.getFloat("bulletSpeed");
                bulletSplashRange = lvlUp.getInt("splash") * scale;
                range = lvlUp.getFloat("range") * scale;
                lvl = lvlUp.getInt("lvl");
            } else {
                gameScreen.showInfoDialog();
            }
        }
    }

    public JSONArray getTowerLevels() {
        return towerLevels;
    }

    public int getLvl() {
        return lvl;
    }

    public void setLvl(int lvl) {
        this.lvl = lvl;
    }

    public int getTileX() {
        return tileX;
    }

    public int getTileY() {
        return tileY;
    }

    public void refreshReload() {
        float newReloadTime = towerLevels.getJSONObject(lvl - 1).getFloat("reload") * base.getMultipliers().getFloat("reloadSpeedMultiplier" + name);
        stateTime = stateTime * (newReloadTime / reloadTime);
        shootDelayLeft = shootDelayLeft * (newReloadTime / reloadTime);

        towerAnimation = null;
        towerAnimation = new Animation<>(newReloadTime / animationSprites.length, animationSprites);

        if (towerAnimation2 != null)
            towerAnimation2 = new Animation<>(newReloadTime / animationSprites2.length, animationSprites2);

        reloadTime = newReloadTime;
        shootDelay = reloadTime / animationSprites.length * framesDelay;
    }

    public void update(float deltaTime, ArrayList<Enemy> enemies) {
        //set positions, orientation
        // shoot if reloaded
        stateTime += deltaTime;

        Iterator<Bullet> bIterator = towerBullets.iterator();
        while (bIterator.hasNext()) {
            Bullet b = bIterator.next();

            b.update(deltaTime);
            if (b.isOnEnemy()) {

                if (Objects.equals(b.getEnemyToFollow().getName(), "assassin")) {
                    Random attackRandom = new Random();
                    int attackChance = attackRandom.nextInt(0, 101);
                    int attackPriority = attackChance;

                    if (b.getEnemyToFollow().getDodgeChance() >= attackPriority && base.getMultipliers().getFloat("luckMultiplier") >= attackRandom.nextInt(0, 101)) {
                        attackChance = attackRandom.nextInt(0, 101);
                        if (attackPriority > attackChance)
                            attackChance = attackPriority;
                    }

                    if (attackChance > b.getEnemyToFollow().getDodgeChance())
                        if (b.getEnemyToFollow().getCanBeAttacked())
                            b.getEnemyToFollow().dealDmg(b.getBulletDamage() * base.getMultipliers().getFloat("damageMultiplier") * base.getMultipliers().getFloat("damageMultiplier" + name));

                } else {
                    if (b.getEnemyToFollow().getCanBeAttacked())
                        b.getEnemyToFollow().dealDmg(b.getBulletDamage() * base.getMultipliers().getFloat("damageMultiplier") * base.getMultipliers().getFloat("damageMultiplier" + name));
                }

                if (bulletSplashRange > 0) {
                    for (Enemy e : enemies) {
                        if (e.getPosition().y <= b.getEnemyToFollow().getPosition().y + bulletSplashRange && e.getPosition().y >= b.getEnemyToFollow().getPosition().y - bulletSplashRange && e.getPosition().x <= b.getEnemyToFollow().getPosition().x + bulletSplashRange && e.getPosition().x >= b.getEnemyToFollow().getPosition().x - bulletSplashRange) {
                            if (b.getEnemyToFollow().getCanBeAttacked())
                                e.dealDmg(b.getBulletDamage() * base.getMultipliers().getFloat("damageMultiplier" + name) * base.getMultipliers().getFloat("splashMultiplier"));
                        }


                        if (Objects.equals(e.getName(), "summoner")) {
                            for (Enemy eS : e.getSummonedList()) {
                                if (eS.getPosition().y <= b.getEnemyToFollow().getPosition().y + bulletSplashRange && eS.getPosition().y >= b.getEnemyToFollow().getPosition().y - bulletSplashRange && eS.getPosition().x <= b.getEnemyToFollow().getPosition().x + bulletSplashRange && eS.getPosition().x >= b.getEnemyToFollow().getPosition().x - bulletSplashRange) {
                                    if (b.getEnemyToFollow().getCanBeAttacked())
                                        eS.dealDmg(b.getBulletDamage() * base.getMultipliers().getFloat("damageMultiplier" + name) * base.getMultipliers().getFloat("splashMultiplier"));
                                }
                            }
                        }
                    }
                }
                bIterator.remove();
            }
        }

        if (timeToShoot > 0)
            timeToShoot -= deltaTime;

        if (shootDelayLeft > 0) {
            shootDelayLeft -= deltaTime;

            //moving in direction to enemy
            Vector2 direction = new Vector2(position.x - delayBullet.getEnemyToFollow().getPosition().x, position.y - delayBullet.getEnemyToFollow().getPosition().y);
            double x = Math.abs(position.x - delayBullet.getEnemyToFollow().getPosition().x);
            double distance = Vector2.dst(position.x, position.y, delayBullet.getEnemyToFollow().getPosition().x, delayBullet.getEnemyToFollow().getPosition().y);
            double alfa = Math.acos(x / distance);
            rotation = (float) Math.toDegrees(alfa);
            if (direction.x <= 0 && direction.y <= 0)
                rotation = 270 + rotation;
            else if (direction.x <= 0 && direction.y > 0)
                rotation = 270 - rotation;
            else if (direction.x > 0 && direction.y > 0)
                rotation = 90 + rotation;
            else if (direction.x > 0 && direction.y <= 0)
                rotation = 90 - rotation;

            if (shootDelayLeft <= 0)
                towerBullets.add(delayBullet);
        }

        if (timeToShoot <= 0) {
            //if dst < range for last enemy then shoot last enemy
            // else find new enemy
            for (Enemy e : enemies) {
                if (e.getIsFlying() && !canAttackFlying)
                    continue;

                if (range * base.getMultipliers().getFloat("rangeMultiplier" + name) >= Vector2.dst(position.x + towerTextureSize * scale / 2, position.y + towerTextureSize * scale / 2, e.getPosition().x + e.getEnemySize() * scale / 2, e.getPosition().y + e.getEnemySize() * scale / 2)) {
                    if (shootDelay > 0) {
                        delayBullet = new Bullet(e, e.getEnemySize(), bulletDamage, bulletSpeed, bulletTexture, bulletTextureSize, position, scale);
                        shootDelayLeft = shootDelay;
                    } else
                        towerBullets.add(new Bullet(e, e.getEnemySize(), bulletDamage, bulletSpeed, bulletTexture, bulletTextureSize, position, scale));

                    stateTime = 0f;
                    timeToShoot = reloadTime;

                    Vector2 direction = new Vector2(position.x - e.getPosition().x, position.y - e.getPosition().y);
                    double x_Distance = Math.abs(position.x - e.getPosition().x);
                    double distance = Vector2.dst(position.x, position.y, e.getPosition().x, e.getPosition().y);
                    double alfa = Math.acos(x_Distance / distance);
                    rotation = (float) Math.toDegrees(alfa);

                    if (direction.x <= 0 && direction.y <= 0)
                        rotation = 270 + rotation;
                    else if (direction.x <= 0 && direction.y > 0)
                        rotation = 270 - rotation;
                    else if (direction.x > 0 && direction.y > 0)
                        rotation = 90 + rotation;
                    else if (direction.x > 0 && direction.y <= 0)
                        rotation = 90 - rotation;

                    break;
                }

                if (Objects.equals(e.getName(), "summoner")) {
                    for (Enemy eSummon : e.getSummonedList()) {
                        if (eSummon.getCanBeAttacked()) {
                            if (range * base.getMultipliers().getFloat("rangeMultiplier" + name) >= Vector2.dst(position.x + towerTextureSize * scale / 2, position.y + towerTextureSize * scale / 2, eSummon.getPosition().x + eSummon.getEnemySize() * scale / 2, eSummon.getPosition().y + eSummon.getEnemySize() * scale / 2)) {
                                towerBullets.add(new Bullet(eSummon, eSummon.getEnemySize(), bulletDamage, bulletSpeed, bulletTexture, bulletTextureSize, position, scale));
                                stateTime = 0f;
                                timeToShoot = reloadTime;

                                Vector2 direction = new Vector2(position.x - eSummon.getPosition().x, position.y - eSummon.getPosition().y);
                                double x = Math.abs(position.x - eSummon.getPosition().x);
                                double distance = Vector2.dst(position.x, position.y, eSummon.getPosition().x, eSummon.getPosition().y);
                                double alfa = Math.acos(x / distance);
                                rotation = (float) Math.toDegrees(alfa);

                                if (direction.x <= 0 && direction.y <= 0)
                                    rotation = 270 + rotation;
                                else if (direction.x <= 0 && direction.y > 0)
                                    rotation = 270 - rotation;
                                else if (direction.x > 0 && direction.y > 0)
                                    rotation = 90 + rotation;
                                else if (direction.x > 0 && direction.y <= 0)
                                    rotation = 90 - rotation;

                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        //draw tower
        if (isMouseEntered) {
            batch.end();
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(position.x + scale * towerTextureSize / 2, position.y + scale * towerTextureSize / 2, range * base.getMultipliers().getFloat("rangeMultiplier" + name));

            if (lvl < towerLevels.length()) {
                shapeRenderer.setColor(Color.BLUE);
                shapeRenderer.circle(position.x + scale * towerTextureSize / 2, position.y + scale * towerTextureSize / 2, scale * towerLevels.getJSONObject(lvl).getFloat("range") * base.getMultipliers().getFloat("rangeMultiplier" + name));
            }

            shapeRenderer.end();
            batch.begin();
        }

        if (towerAnimation2 != null)
            batch.draw(towerAnimation2.getKeyFrame(stateTime, looping), position.x + 32 * (scale - 1), position.y + 32 * (scale - 1), towerTextureSize / 2, towerTextureSize / 2, towerTextureSize, towerTextureSize, scale, scale, 0);

        batch.draw(towerAnimation.getKeyFrame(stateTime, false), position.x + 32 * (scale - 1), position.y + 32 * (scale - 1), towerTextureSize / 2, towerTextureSize / 2, towerTextureSize, towerTextureSize, scale, scale, rotation);

        for (Bullet b : towerBullets) {
            b.render(batch);
        }
    }

    public int getSellWorth() {
        return worth / 2;
    }
}
