package com.game.Entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.game.Entity.Enemy.Enemy;

public class Bullet {

    private Enemy enemyToFollow;
    private float velocity;
    private TextureRegion bulletTexture;
    private int bulletTextureSize;
    private Vector2 position;
    private float scale;
    private float rotation;
    private boolean isOnEnemy;
    private float bulletDamage;
    private float enemySize;

    public Bullet(Enemy enemyToFollow, float enemySize, float bulletDamage, float velocity, TextureRegion bulletTexture, int bulletTextureSize, Vector2 position, float scale)
    {
        this.enemySize = enemySize;
        this.isOnEnemy = false;
        this.enemyToFollow = enemyToFollow;
        this.velocity = velocity;
        this.bulletTexture = bulletTexture;
        this.bulletTextureSize = bulletTextureSize;
        this.position = new Vector2(position);
        this.scale = scale;
        this.bulletDamage = bulletDamage;
        Vector2 spawnDirection = new Vector2(position.x - enemyToFollow.getPosition().x, position.y - (enemyToFollow.getPosition().y+enemySize*scale/2));
        rotation = (float) Math.toDegrees(Math.acos(Math.abs(position.x - enemyToFollow.getPosition().x)/Vector2.dst(position.x,position.y,enemyToFollow.getPosition().x,enemyToFollow.getPosition().y+enemySize*scale/2)));

        if (spawnDirection.x <= 0 && spawnDirection.y <= 0)
            rotation = 270 + rotation;
        else if (spawnDirection.x <= 0 && spawnDirection.y > 0)
            rotation = 270 - rotation;
        else if (spawnDirection.x > 0 && spawnDirection.y > 0)
            rotation = 90 + rotation;
        else if (spawnDirection.x > 0 && spawnDirection.y <= 0)
            rotation = 90 - rotation;
    }

    public boolean isOnEnemy() {
        return isOnEnemy;
    }

    public Enemy getEnemyToFollow() {
        return enemyToFollow;
    }

    public float getBulletDamage() {
        return bulletDamage;
    }

    public void update(float deltaTime) {
        double x = Math.abs(position.x - enemyToFollow.getPosition().x);
        double distance = Vector2.dst(position.x,position.y,enemyToFollow.getPosition().x,enemyToFollow.getPosition().y+enemySize*scale/2);
        double alfa = Math.acos(x/distance);
        float velocityX = (float) (velocity * Math.cos(alfa));
        float velocityY = (float) (velocity * Math.sin(alfa));
        Vector2 direction = new Vector2(position.x - enemyToFollow.getPosition().x, position.y - (enemyToFollow.getPosition().y+enemySize*scale/2));
        rotation = (float) Math.toDegrees(alfa);

        if (direction.x <= 0 && direction.y <= 0)
            rotation = 270 + rotation;
        else if (direction.x <= 0 && direction.y > 0)
            rotation = 270 - rotation;
        else if (direction.x > 0 && direction.y > 0)
            rotation = 90 + rotation;
        else if (direction.x > 0 && direction.y <= 0)
            rotation = 90 - rotation;

        if (direction.x > 0) {
            position.x = position.x - velocityX * deltaTime;
        } else if (direction.x < 0) {
            position.x = position.x + velocityX * deltaTime;
        }

        if (direction.y > 0){
            position.y = position.y - velocityY * deltaTime;
        }
        else if (direction.y < 0)
            position.y = position.y + velocityY * deltaTime;

        Vector2 directionAfterMove = new Vector2(position.x - enemyToFollow.getPosition().x, position.y - (enemyToFollow.getPosition().y+enemySize*scale/2));

        if (direction.x * directionAfterMove.x < 0)
            position.x = enemyToFollow.getPosition().x;
        if (direction.y * directionAfterMove.y < 0)
            position.y = enemyToFollow.getPosition().y+enemySize*scale/2;

        if (position.x == enemyToFollow.getPosition().x && position.y == enemyToFollow.getPosition().y+enemySize*scale/2)
        {
            isOnEnemy=true;
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(bulletTexture,position.x, position.y, bulletTextureSize/2, bulletTextureSize/2,bulletTextureSize,bulletTextureSize,scale,scale,rotation);
    }
}