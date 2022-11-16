package com.game.Manager;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.game.Entity.Enemy.*;
import com.game.Entity.Base;
import org.json.JSONObject;

import java.util.*;

public class EnemyManager {
    private ArrayList<ArrayList<Enemy>> enemyWavesToSpawn;
    private ArrayList<Enemy> enemies;
    private ArrayList<Enemy> renderEnemiesList;
    private ArrayList<Float> spawningDelay;
    private ArrayList<Float> timeLeftToSpawn;
    private float scale;
    private List<Vector2> path;
    private Base base;
    private JSONObject enemiesJSONObject;
    private Comparator<Enemy> renderSorter = (a, b) -> {
        if (a.getY() < b.getY())
            return 1;
        else if (a.getY() > b.getY())
            return -1;
        else
            return 0;
    };

    public EnemyManager(Base base, float scale, List<Vector2> path, JSONObject enemiesJSONObject) {
        enemyWavesToSpawn = new ArrayList<>();
        spawningDelay = new ArrayList<>();
        timeLeftToSpawn = new ArrayList<>();
        enemies = new ArrayList<>();

        this.path = path;
        this.scale = scale;
        this.base = base;
        this.enemiesJSONObject = enemiesJSONObject;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public ArrayList<Enemy> getLastCreatedWave() {
        return enemyWavesToSpawn.get(enemyWavesToSpawn.size() - 1);
    }

    public void createRandomEnemyWave() {
        int wave = base.getWave();
        ArrayList<Enemy> enemies = new ArrayList<>();

        Random random = new Random(base.getSeed());
        int randomNumber = (Math.abs(base.getSeed())) / ((wave + 1) * 13);
        for (int i = 0; i < wave * 7; i++)
            random.nextInt(randomNumber);

        int enemyPoints = 50 + (wave / 10 + wave) * 5;

        if (wave % 10 == 5) {
            enemies.add(new Summoner(enemiesJSONObject.getJSONObject("summoner"), enemiesJSONObject.getJSONObject("summon")));
            enemyPoints -= 30;
        } else if (wave % 10 == 0 && wave >= 10) {
            enemies.add(new Boss(enemiesJSONObject.getJSONObject("boss")));
            enemyPoints -= 50;
        }

        JSONObject spawnRate = enemiesJSONObject.getJSONObject("spawnRate");
        JSONObject spawnWave = enemiesJSONObject.getJSONObject("spawnWave");

        while (enemyPoints >= 5) {
            int chosenEnemy = random.nextInt(0, spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") + spawnRate.getInt("flying") + spawnRate.getInt("summoner") + 1);
            if (chosenEnemy <= spawnRate.getInt("warrior") && wave >= spawnWave.getInt("warrior") && enemyPoints >= 5) {
                enemies.add(new Warrior(enemiesJSONObject.getJSONObject("warrior")));
                enemyPoints -= 5;
            } else if (spawnRate.getInt("warrior") < chosenEnemy && chosenEnemy <= spawnRate.getInt("warrior") + spawnRate.getInt("blob") && wave >= spawnWave.getInt("blob") && enemyPoints >= 7) {
                enemies.add(new Blob(enemiesJSONObject.getJSONObject("blob")));
                enemyPoints -= 7;
            } else if (spawnRate.getInt("warrior") + spawnRate.getInt("blob") < chosenEnemy && chosenEnemy <= spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") && wave >= spawnWave.getInt("assassin") && enemyPoints >= 10) {
                enemies.add(new Assassin(enemiesJSONObject.getJSONObject("assassin")));
                enemyPoints -= 10;
            } else if (spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") < chosenEnemy && chosenEnemy <= spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") + spawnRate.getInt("flying") && wave >= spawnWave.getInt("flying") && enemyPoints >= 15) {
                enemies.add(new Flying(enemiesJSONObject.getJSONObject("flying")));
                enemyPoints -= 15;
            } else if (spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") + spawnRate.getInt("flying") < chosenEnemy && chosenEnemy <= spawnRate.getInt("warrior") + spawnRate.getInt("blob") + spawnRate.getInt("assassin") + spawnRate.getInt("flying") + spawnRate.getInt("summoner") && wave >= spawnWave.getInt("summoner") && enemyPoints >= 30) {
                enemies.add(new Summoner(enemiesJSONObject.getJSONObject("summoner"), enemiesJSONObject.getJSONObject("summon")));
                enemyPoints -= 30;
            }
        }
        addWaveToSpawn(enemies);
    }

    public void addWaveToSpawn(ArrayList<Enemy> wave) {
        if (path != null)
            for (Enemy e : wave)
                if (!Objects.equals(e.getName(), "summoner"))
                    e.initEnemy(path, scale);
                else
                    e.initSummonerEnemy(base, path, scale);

        enemyWavesToSpawn.add(wave);
        if ((60 / (float) wave.size()) < 3)
            spawningDelay.add(60 / (float) wave.size());
        else
            spawningDelay.add(3f);

        timeLeftToSpawn.add(0f);
    }

    public void spawn(Enemy e) {
        enemies.add(e);
    }

    public int getEnemiesLeft() {
        int numberOfEnemies = enemies.size();
        for (ArrayList<Enemy> wave : enemyWavesToSpawn)
            numberOfEnemies += wave.size();

        return numberOfEnemies;
    }

    public void update(float deltaTime) {
        //spawning enemies
        if (enemyWavesToSpawn.size() > 0) {
            ArrayList<Integer> indexesToDelete = new ArrayList<>();

            for (int i = 0; i < enemyWavesToSpawn.size(); i++) {
                timeLeftToSpawn.set(i, timeLeftToSpawn.get(i) - deltaTime);

                if (timeLeftToSpawn.get(i) <= 0) {
                    spawn(enemyWavesToSpawn.get(i).get(0));
                    enemyWavesToSpawn.get(i).remove(0);
                    if (enemyWavesToSpawn.get(i).size() == 0) {
                        indexesToDelete.add(i);
                    } else {
                        timeLeftToSpawn.set(i, spawningDelay.get(i));
                    }
                }
            }
            if (indexesToDelete.size() > 0) {
                for (int index : indexesToDelete) {
                    timeLeftToSpawn.remove(index);
                    spawningDelay.remove(index);
                    enemyWavesToSpawn.remove(index);
                    //heal when whole wave is pushed on map
                    if (base.getHealth() < base.getMaxHealth()) {
                        int heal = Math.round(base.getMultipliers().getFloat("healthRegeneration"));
                        if (heal > 0) {
                            if ((base.getHealth() + heal) <= base.getMaxHealth())
                                base.setHealth(base.getHealth() + heal);
                            else
                                base.setHealth(base.getMaxHealth());
                        }
                    }
                }
            }
        }

        for (Enemy e : enemies) {
            if (Objects.equals(e.getName(), "summoner")) {
                e.updateSummoned(deltaTime);
            }
        }

        Iterator<Enemy> eIterator = enemies.iterator();
        while (eIterator.hasNext()) {
            Enemy e = eIterator.next();
            e.update(deltaTime);

            if (e.isAtEnd()) {
                base.damageBase(Math.round(base.getDifficultyMultiplier() * (e.getDmg() - base.getMultipliers().getFloat("damageReduction"))));
                eIterator.remove();
            }

            if (!e.isAlive()) {
                if (e.getDiamonds() > 0)
                    base.increaseDiamonds(e.getDiamonds() + (int) (base.getMultipliers().getFloat("diamondsMultiplier")));

                float baseEnemyMoney = e.getMoney() * base.getMultipliers().getFloat("goldMultiplier");

                Random moneyRandom = new Random();
                int moneyRandomlyGenerated = moneyRandom.nextInt(Math.round(baseEnemyMoney - 0.2f * baseEnemyMoney), Math.round(baseEnemyMoney + 0.2f * baseEnemyMoney));
                int moneyToGive = moneyRandomlyGenerated;
                if (base.getMultipliers().getFloat("luckMultiplier") >= moneyRandom.nextInt(0, 101)) {
                    moneyRandomlyGenerated = moneyRandom.nextInt(Math.round(baseEnemyMoney - 0.2f * baseEnemyMoney), Math.round(baseEnemyMoney + 0.2f * baseEnemyMoney));
                    if (moneyToGive < moneyRandomlyGenerated)
                        moneyToGive = moneyRandomlyGenerated;
                }
                base.increaseMoney(moneyToGive);
                eIterator.remove();
            }
        }
    }

    public void render(SpriteBatch spritebatch, ShapeRenderer shapeRenderer) {
        renderEnemiesList = new ArrayList<>(enemies);
        for (Enemy e : enemies)
            if (Objects.equals(e.getName(), "summoner"))
                if (e.getSummonedList().size() > 0)
                    renderEnemiesList.addAll(e.getSummonedList());

        renderEnemiesList.sort(renderSorter);
        for (Enemy e : renderEnemiesList)
            e.render(spritebatch, shapeRenderer);
    }
}