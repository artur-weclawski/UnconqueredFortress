package com.game.Entity.Enemy;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.game.Entity.Base;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

public class Enemy extends Actor {
    private int health, maxHealth;
    private int dmg;

    private String name;

    private int money;
    private int diamonds;
    private float speed;

    private Animation<TextureRegion> currentAnimation;
    private Animation<TextureRegion>[] animationArr;
    private float enemySize;

    private Vector2 position;

    private float stateTime;
    private float scale;

    private boolean isAtEnd;
    private Base base;
    private boolean isFlying;

    private boolean canBeAttacked;

    private List<Vector2> path;

    private String moveDirection;

    private float summoningTime, timeOfActualSummoning;
    private float timeToSummonNextEnemy, delayBetweenSummonings;
    private ArrayList<Enemy> summonedEnemies;
    private boolean isSummoning;
    private Animation<TextureRegion>[] summoningAnimation;
    private JSONObject summonJSONObject;

    private float regenTime, regenTimeLeft;
    private float healthRegen;

    private int dodgeChance;

    public Enemy() {

    }

    //assassin constructor
    public Enemy(int dodgeChance, int health, int damage, int money, int diamonds, int speed, String path, String name, int enemySize, boolean isFlying) {
        this(health, damage, money, diamonds, speed, path, name, enemySize, isFlying);

        this.dodgeChance = dodgeChance;
    }

    //blob constructor
    public Enemy(float regenTime, float healthRegen, int health, int damage, int money, int diamonds, int speed, String path, String name, int enemySize, boolean isFlying) {
        this(health, damage, money, diamonds, speed, path, name, enemySize, isFlying);

        this.regenTime = regenTime;
        this.regenTimeLeft = regenTime;
        this.healthRegen = healthRegen;


    }

    //summoner constructor
    public Enemy(JSONObject summonJSONObject, float delayBetweenSummonings, float summoningTime, String summonPath, int health, int damage, int money, int diamonds, int speed, String path, String name, int enemySize, boolean isFlying) {
        this(health, damage, money, diamonds, speed, path, name, enemySize, isFlying);

        this.summonJSONObject = summonJSONObject;
        this.isSummoning = false;
        this.timeOfActualSummoning = 0;

        this.summoningTime = summoningTime;
        this.delayBetweenSummonings = delayBetweenSummonings;
        this.timeToSummonNextEnemy = this.delayBetweenSummonings;

        this.summonedEnemies = new ArrayList<>();

        summoningAnimation = new Animation[4];
        Texture spriteMap = new Texture(Gdx.files.internal(summonPath));
        TextureRegion[][] spritePosition = TextureRegion.split(spriteMap, 64, 64);
        TextureRegion[] animationSprites;

        for (int j = 0; j < 4; j++) {

            animationSprites = new TextureRegion[spritePosition[j].length];

            for (int i = 0; i < spritePosition[j].length; i++) {
                animationSprites[i] = spritePosition[j][i];

            }

            summoningAnimation[j] = new Animation<>(0.5f, animationSprites);

        }


    }

    public Enemy(int health, int damage, int money, int diamonds, int speed, String path, String name, int enemySize, boolean isFlying) {
        this.isFlying = isFlying;
        this.canBeAttacked = true;
        this.name = name;
        this.enemySize = enemySize;
        this.health = health;
        this.maxHealth = health;
        this.dmg = damage;
        this.money = money;
        this.diamonds = diamonds;

        moveDirection = "";
        isAtEnd = false;

        this.speed = speed;

        stateTime = 0f;

        animationArr = new Animation[4];
        Texture spriteMap = new Texture(Gdx.files.internal(path));
        TextureRegion[][] spritePosition = TextureRegion.split(spriteMap, enemySize, enemySize);
        TextureRegion[] animationSprites;


        for (int j = 0; j < 4; j++) {

            animationSprites = new TextureRegion[spritePosition[j].length];

            for (int i = 0; i < spritePosition[j].length; i++) {
                animationSprites[i] = spritePosition[j][i];

            }

            animationArr[j] = new Animation<>(0.125f, animationSprites);

        }

        currentAnimation = animationArr[1];


    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    public int getMoney() {
        return money;
    }


    public void dealDmg(float damage) {
        health -= damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public Vector2 getPosition() {
        return position;
    }


    public void initEnemy(java.util.List<Vector2> path, float scale) {
        this.scale = scale;
        this.path = new ArrayList<>();
        this.speed = speed * scale;

        for (Vector2 v : path) {
            this.path.add(new Vector2(v.x, v.y));
        }

        position = new Vector2(this.path.get(0).x, this.path.get(0).y);
        //this.path.remove(0);
    }

    public void initSummonerEnemy(Base base, java.util.List<Vector2> path, float scale) {
        this.base = base;
        this.scale = scale;
        this.path = new ArrayList<>();
        this.speed = speed * scale;


        for (Vector2 v : path) {
            this.path.add(new Vector2(v.x, v.y));
        }

        position = new Vector2(this.path.get(0).x, this.path.get(0).y);
        //this.path.remove(0);
    }

    public void initSummonedEnemy(float summoningTime, Base base, java.util.List<Vector2> path, float scale) {
        this.base = base;
        this.scale = scale;
        this.path = new ArrayList<>();
        this.speed = speed * scale;
        this.canBeAttacked = false;
        this.summoningTime = summoningTime;
        for (Vector2 v : path) {
            this.path.add(new Vector2(v.x, v.y));
        }

        position = new Vector2(this.path.get(0).x, this.path.get(0).y);
        //this.path.remove(0);

        summoningAnimation = new Animation[1];
        Texture spriteMap = new Texture(Gdx.files.internal("assets/game/enemies/summonStain.png")); // add summon textures
        TextureRegion[][] spritePosition = TextureRegion.split(spriteMap, 64, 64);
        TextureRegion[] animationSprites;

        for (int j = 0; j < 1; j++) {

            animationSprites = new TextureRegion[spritePosition[j].length];

            for (int i = 0; i < spritePosition[j].length; i++) {
                animationSprites[i] = spritePosition[j][i];

            }

            summoningAnimation[j] = new Animation<>(0.125f, animationSprites);
        }

        currentAnimation = summoningAnimation[0];

    }


    private boolean MoveToPoint(Vector2 finalPoint, float deltaTime) {
        if (position.equals(finalPoint))
            return true;


        // Find direction from current position to goal
        Vector2 direction = new Vector2(position.x - finalPoint.x, position.y - finalPoint.y);

        //System.out.println("x: "+ direction.x + ", y: "+ direction.y);

        // Move in that direction
        if (direction.x > 0)
            position.x = position.x - speed * deltaTime;
        else if (direction.x < 0)
            position.x = position.x + speed * deltaTime;
        else if (direction.y > 0)
            position.y = position.y - speed * deltaTime;
        else if (direction.y < 0)
            position.y = position.y + speed * deltaTime;


        // If we moved PAST the goal, move it back to the goal
        Vector2 directionAfterMove = new Vector2(position.x - finalPoint.x, position.y - finalPoint.y);
        if ((direction.x * directionAfterMove.x < 0) || (direction.y * directionAfterMove.y < 0))
            position = finalPoint;

        // Return whether we've reached the goal or not
        //return position == finalPoint;
        return position.equals(finalPoint);

    }

    public float getEnemySize() {
        return enemySize;
    }

    public boolean isAtEnd() {
        return isAtEnd;
    }

    public void update(float deltaTime) {
        stateTime += deltaTime;


        //Blob
        if (Objects.equals(name, "blob")) {
            regenTimeLeft -= deltaTime;
            if (regenTimeLeft < 0) {
                regenTimeLeft = regenTime;
                if (health < maxHealth) {
                    if (health + healthRegen < maxHealth)
                        health += healthRegen;
                    else
                        health = maxHealth;
                }
            }
        }

        //Summon
        if (Objects.equals(name, "summon")) {
            if (!canBeAttacked) {
                this.summoningTime -= deltaTime;
                if (summoningTime <= 0) {
                    canBeAttacked = true;
                    moveDirection = "";
                }
                return;
            }

        }

        //Summoner
        if (Objects.equals(name, "summoner")) {
            if (timeToSummonNextEnemy <= 0) {
                summonEnemy();
                isSummoning = true;
                canBeAttacked = false;
                timeToSummonNextEnemy = delayBetweenSummonings;

                switch (moveDirection) {
                    case "Right" -> currentAnimation = summoningAnimation[0];
                    case "Left" -> currentAnimation = summoningAnimation[1];
                    case "Down" -> currentAnimation = summoningAnimation[2];
                    case "Up" -> currentAnimation = summoningAnimation[3];
                }

                return;
            } else if (isSummoning) {
                timeOfActualSummoning += deltaTime;
                if (timeOfActualSummoning >= summoningTime) {
                    isSummoning = false;
                    canBeAttacked = true;
                    moveDirection = "";
                    timeOfActualSummoning = 0;
                }
                return;
            } else {
                timeToSummonNextEnemy -= deltaTime;
            }

        }


        //All enemies
        if (path.size() > 0 && MoveToPoint(path.get(0), deltaTime)) {
            path.remove(0);


            //chamge rotation if enemy changes direction
            if (path.size() > 0) {
                Vector2 direction = new Vector2(position.x - path.get(0).x, position.y - path.get(0).y);
                if (direction.x < 0 && direction.y == 0 && moveDirection != "Right") {
                    moveDirection = "Right";
                    currentAnimation = animationArr[0];
                } else if (direction.x > 0 && direction.y == 0 && moveDirection != "Left") {
                    moveDirection = "Left";
                    currentAnimation = animationArr[1];
                } else if (direction.x == 0 && direction.y > 0 && moveDirection != "Down") {
                    moveDirection = "Down";
                    currentAnimation = animationArr[2];
                } else if (direction.x == 0 && direction.y < 0 && moveDirection != "Up") {
                    moveDirection = "Up";
                    currentAnimation = animationArr[3];
                }
            } else if (path.size() == 0)
                isAtEnd = true;
        }
    }

    public void updateSummoned(float deltaTime) {
        Iterator<Enemy> eIterator = summonedEnemies.iterator();
        while (eIterator.hasNext()) {
            Enemy e = eIterator.next();

            e.update(deltaTime);


            //if reached end
            if (e.isAtEnd()) {
                base.damageBase(Math.round(base.getDifficultyMultiplier() * (e.getDmg() - base.getMultipliers().getFloat("damageReduction"))));
                eIterator.remove();
            }

            //if dead
            if (!e.isAlive()) {
                eIterator.remove();
            }

        }
    }

    public float getY() {
        return position.y;
    }

    public float getHeight() {
        return enemySize;
    }


    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {

        batch.draw(currentAnimation.getKeyFrame(stateTime, true), position.x, position.y + scale * enemySize / 2, scale * enemySize, scale * enemySize);

        if (canBeAttacked) {
            batch.end();

            shapeRenderer.setColor(Color.RED);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.rect(position.x, position.y + enemySize * scale * 3 / 2, enemySize * scale, 10);
            shapeRenderer.end();

            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.rect(position.x, position.y + enemySize * scale * 3 / 2, enemySize * health / maxHealth * scale, 10);
            shapeRenderer.end();

            batch.begin();
        }
    }


    public int getDmg() {
        return dmg;
    }

    public int getHealth() {
        return health;
    }

    public void changeSpeed(float v) {
        this.speed = speed * v;
    }


    public void summonEnemy() {
        Enemy summon = new Enemy(summonJSONObject.getInt("health"), summonJSONObject.getInt("damage"), summonJSONObject.getInt("money"), summonJSONObject.getInt("diamonds"), summonJSONObject.getInt("speed"), "assets/game/enemies/summon.png", summonJSONObject.getString("name"), 64, summonJSONObject.getBoolean("isFlying"));
        summon.initSummonedEnemy(this.summoningTime * 0.8f, this.base, this.path, this.scale);
        summonedEnemies.add(summon);

    }

    public ArrayList<Enemy> getSummonedList() {
        return summonedEnemies;
    }

    public boolean getCanBeAttacked() {
        return canBeAttacked;
    }

    public boolean getIsFlying() {
        return isFlying;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public int getDodgeChance() {
        return dodgeChance;
    }
}



