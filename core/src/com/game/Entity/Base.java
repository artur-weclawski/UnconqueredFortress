package com.game.Entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.Manager.EnemyManager;
import com.game.Manager.TowerManager;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Objects;

public class Base {
    private int maxHealth;
    private int health;
    private int armor;
    private int money;
    private int diamonds;
    private int wave;
    private int seed;
    private int infoToDisplay;
    private String infoToDisplayName;
    private JSONObject infoToDisplayObjectNow, infoToDisplayObjectUpgraded;
    private String difficulty;
    private float difficultyMultiplier;
    private int cleanPrice;
    private JSONObject multipliers;
    private int usesLeft;
    private int roadObstacleId;
    private boolean shouldUpdateInfo;
    private TowerManager towerManager;
    private EnemyManager enemyManager;

    public enum State{
        Running, Paused, Resumed, GameOver
    }

    private State state;

    private Image[][] operationsArr;

    public Base(JSONObject actualGame, Image[][] operationsArr){
        this.infoToDisplay = 0;
        this.infoToDisplayName = "";
        this.infoToDisplayObjectNow = null;
        this.infoToDisplayObjectUpgraded = null;
        this.shouldUpdateInfo = false;
        this.operationsArr = operationsArr;
        state = State.Running;

        multipliers = new JSONObject();
        multipliers.put("damageMultiplier",1f); //done
        multipliers.put("damageMultipliermeleeTower",1f); //done
        multipliers.put("damageMultipliermageTower",1f); //done
        multipliers.put("damageMultipliercrossbowTower",1f); //done
        multipliers.put("damageMultipliercannonTower",1f);  //done
        multipliers.put("reloadSpeedMultipliermeleeTower",1f); //done
        multipliers.put("reloadSpeedMultipliermageTower",1f); //done
        multipliers.put("reloadSpeedMultipliercrossbowTower",1f); //done
        multipliers.put("reloadSpeedMultipliercannonTower",1f); //done
        multipliers.put("rangeMultipliermeleeTower",1f); //done
        multipliers.put("rangeMultipliermageTower",1f); //done
        multipliers.put("rangeMultipliercrossbowTower",1f); //done
        multipliers.put("rangeMultipliercannonTower",1f); //done
        multipliers.put("splashMultiplier",0f); //done
        multipliers.put("healthRegeneration",0f); //done
        multipliers.put("damageReduction",0f); //done
        multipliers.put("goldMultiplier",1f); //done
        multipliers.put("diamondsMultiplier",0f); //done
        multipliers.put("costMultiplier",1f); //done
        multipliers.put("upgradeCostMultiplier",1f); //done
        multipliers.put("luckMultiplier",0f); //done
        multipliers.put("cleaningCostMultiplier",1f); //done

        this.money = actualGame.getInt("gold");
        this.diamonds = actualGame.getInt("diamonds");
        this.maxHealth = actualGame.getInt("maxHealth");
        this.health = actualGame.getInt("health");
        this.armor = 100;
        this.difficulty = actualGame.getString("difficulty");
        this.seed = actualGame.getInt("seed");

        switch (this.difficulty)
        {
            case "easy" -> difficultyMultiplier = 1;
            case "normal" -> difficultyMultiplier = 2;
            case "hard" -> difficultyMultiplier = 3;
            default -> difficultyMultiplier = 1;
        }



        this.wave = actualGame.getInt("wave");
        this.cleanPrice = 200;
    }

    public Base(int maxHealth, int health, int armor){
        this.money = 0;
        this.maxHealth = maxHealth;
        this.health = health;
        this.armor = armor;

    }


    public void setPassiveUpgrade(JSONObject upgrade, boolean isLoaded) {
        if (!isLoaded)
            this.diamonds -= upgrade.getInt("cost");

        if (upgrade.has("unlocks"))
        {

            String unlock = upgrade.getString("unlocks");
            for(int i=0; i<4; i++)
                for(int j=0; j<2; j++)
                    if (Objects.equals(operationsArr[i][j].getName(), unlock))
                    {
                        operationsArr[i][j].setDrawable(new Skin(new TextureAtlas("assets/icons/buildings.pack")),unlock);
                        operationsArr[i][j].setTouchable(Touchable.enabled);
                        break;
                    }

        }

        if (upgrade.has("multipliers"))
        {

            JSONObject addMultipliers = upgrade.getJSONObject("multipliers");
            Iterator<?> keys = addMultipliers.keys();

            while( keys.hasNext() ) {
                String key = (String) keys.next();
                multipliers.put(key, multipliers.getFloat(key)+addMultipliers.getFloat(key));

                if (Objects.equals(key, "reloadSpeedMultipliermeleeTower") || Objects.equals(key, "reloadSpeedMultipliermageTower") || Objects.equals(key, "reloadSpeedMultipliercrossbowTower") || Objects.equals(key, "reloadSpeedMultipliercannonTower"))
                {
                    towerManager.refreshReloads(key.replace("reloadSpeedMultiplier",""));
                }

            }
        }

        if (upgrade.has("maxHealth") && !isLoaded)
        {

            int addingHealth = upgrade.getInt("maxHealth");
            maxHealth += addingHealth;
            health += addingHealth;
        }
    }

    public float getDifficultyMultiplier() {
        return difficultyMultiplier;
    }

    public boolean isShouldUpdateInfo() {
        return shouldUpdateInfo;
    }

    public void setShouldUpdateInfo(boolean shouldUpdateInfo) {
        this.shouldUpdateInfo = shouldUpdateInfo;
    }

    public int getUsesLeft() {
        return usesLeft;
    }

    public void setUsesLeft(int uses)
    {
        usesLeft = uses;
    }

    public void addTowerManager(TowerManager towerManager) {
        this.towerManager = towerManager;
    }

    public int getEnemiesLeft() {
        return enemyManager.getEnemiesLeft();
    }

    public void addEnemyManager(EnemyManager enemyManager) {
        this.enemyManager = enemyManager;
    }

    public int getSeed() {
        return seed;
    }

    public int getInfoToDisplay() {
        return infoToDisplay;
    }

    public String getInfoToDisplayName() {
        return infoToDisplayName;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setInfoToDisplay(int infoToDisplay) {
        this.infoToDisplay = infoToDisplay;
    }

    public void setInfoToDisplay(int infoToDisplay, String name, JSONObject towerNow, JSONObject towerUpgrade) {
        this.infoToDisplay = infoToDisplay;
        infoToDisplayName = name;
        infoToDisplayObjectNow = towerNow;
        infoToDisplayObjectUpgraded = towerUpgrade;
    }

    public void setInfoToDisplay(int infoToDisplay, String name) {
        this.infoToDisplay = infoToDisplay;
        infoToDisplayName = name;
    }

    public void setRoadObstacleId(int hashCode) {
        roadObstacleId = hashCode;
    }

    public int getRoadObstacleId() {
        return roadObstacleId;
    }

    public JSONObject getInfoToDisplayObjectNow() {
        return infoToDisplayObjectNow;
    }

    public JSONObject getInfoToDisplayObjectUpgraded() {
        return infoToDisplayObjectUpgraded;
    }

    public void damageBase(int dmg){
        this.health = this.health - dmg;
    }

    public void healBase(int heal){
        this.health = this.health + heal;
    }

    public JSONObject getMultipliers() {
        return multipliers;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public void setMaxHealth(int maxHealth) {
        this.maxHealth = maxHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getArmor() {
        return armor;
    }

    public void setArmor(int armor) {
        this.armor = armor;
    }

    public void increaseMoney(int money) {
        this.money += money;
    }

    public void decreaseMoney(int money) {
        this.money -= money;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
    public void increaseDiamonds(int diamonds) {
        this.diamonds += diamonds;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(int diamonds) {
        this.diamonds = diamonds;
    }
    public String getDifficulty() {
        return difficulty;
    }

    public void increaseWave(int wave) {
        this.wave += wave;
    }

    public int getWave() {
        return wave;
    }

    public void setWave(int wave) {
        this.wave = wave;
    }

    public int getCleanPrice() {
        return Math.round(cleanPrice*getMultipliers().getFloat("cleaningCostMultiplier"));
    }
}
