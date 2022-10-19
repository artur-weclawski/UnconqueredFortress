package com.game.Entity;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.game.Manager.LanguageManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Upgrade {
    private String upgradeName;
    private int level;
    private int maxLevel;
    private boolean isMaxLevel;
    private Image image;
    private JSONArray upgradeArray;
    private String unlockedIcon;

    private ArrayList<Upgrade> nextUpgrades;
    private List<Integer> levelToUnlockUpgrades;
    private LanguageManager languageManager;

    public Upgrade(String upgradeName, int level, JSONArray upgradeArray, Skin skin, String unlockedIcon, LanguageManager languageManager) {

        this.upgradeName = upgradeName;
        this.level = level;
        this.maxLevel = upgradeArray.length();
        this.image = new Image(skin, "upgradeIcons_lock");
        this.upgradeArray = upgradeArray;
        this.unlockedIcon = unlockedIcon;
        this.languageManager = languageManager;

        this.isMaxLevel = true;

        this.nextUpgrades = new ArrayList<>();
        this.levelToUnlockUpgrades = new ArrayList<>();

        if (level>=0) {
            this.isMaxLevel = false;
            this.image.setDrawable(skin, unlockedIcon);
        }

        image.setTouchable(Touchable.enabled);
    }


    public int getLevel() {
        return level;
    }

    public void unlock(Skin images_upgrades) {
        isMaxLevel=false;
        level = 0;
        image.setDrawable(images_upgrades, unlockedIcon);
    }


    public void levelUp() {
        if(!isMaxLevel) {
            level++;

            if (level == maxLevel) {
                isMaxLevel=true;
                image.setDrawable(new Skin(new TextureAtlas("assets/icons/upgrade_icons.pack")), "upgradeIcons_bought");
            }
        }
    }

    public String getUpgradeName() {
        return upgradeName;
    }

    public Label returnInformation(Label.LabelStyle labelStyle, int diamonds){

        if(this.level<0) {
            return new Label(languageManager.getValue(languageManager.getLanguage(), "upgrade_First_Unlock"), labelStyle);
        }else if (level==maxLevel) {
            return new Label(languageManager.getValue(languageManager.getLanguage(), "upgrade_Max_level"), labelStyle);
        }
        else {

            String information3 = "";

            information3 += languageManager.getValue(languageManager.getLanguage(), upgradeName+"Description");

            String information = languageManager.getValue(languageManager.getLanguage(), "upgrade_Upgrade") + languageManager.getValue(languageManager.getLanguage(), this.upgradeName) + "\n"+ information3 + "\n" +languageManager.getValue(languageManager.getLanguage(), "upgrade_Level") + this.level + "/" + this.maxLevel + "\n";

            String information2 = "";


            JSONObject upgradesJSONObject = upgradeArray.getJSONObject(level);
            // if unlocks tower or adds health
            if (upgradesJSONObject.has("unlocks"))
                information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoUnlocks")+ upgradeArray.getJSONObject(level).getString("unlocks")+"\n";

            if (upgradesJSONObject.has("maxHealth"))
                information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoMaxHealth") + upgradeArray.getJSONObject(level).getInt("maxHealth")+"\n";

            if (upgradesJSONObject.has("multipliers")) {

                JSONObject upgradesNameMultipliers = upgradesJSONObject.getJSONObject("multipliers");

                if (upgradesNameMultipliers.has("healthRegeneration"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoHealthRegeneration") + upgradesNameMultipliers.getInt("healthRegeneration") + "\n";

                if (upgradesNameMultipliers.has("damageReduction"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoDamageReduction") + upgradesNameMultipliers.getInt("damageReduction") + "\n";

                if (upgradesNameMultipliers.has("goldMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoGoldMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("goldMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("diamondsMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoDiamondsMultiplier") + upgradesNameMultipliers.getInt("diamondsMultiplier") + "\n";

                if (upgradesNameMultipliers.has("costMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("costMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("upgradeCostMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoUpgradeCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("upgradeCostMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("cleaningCostMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCleaningCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("cleaningCostMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("luckMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoLuckMultiplier") + upgradesNameMultipliers.getInt("luckMultiplier") + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliermeleeTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoMeleeDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliermeleeTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliermageTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoMageDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliermageTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliercrossbowTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCrossbowDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliercannonTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCannonDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliercannonTower")) + "%" + "\n";

                //range
                if (upgradesNameMultipliers.has("rangeMultipliercannonTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoRangeCannon") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliercannonTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("rangeMultipliercrossbowTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoRangeCrossbow") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("rangeMultipliermageTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoRangeMage") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliermageTower")) + "%" + "\n";


                //reload
                if (upgradesNameMultipliers.has("reloadSpeedMultipliermeleeTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoMeleeReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliermeleeTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliercrossbowTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCrossbowReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliermageTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoMageReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliermageTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliercannonTower"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoCannonReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliercannonTower")) + "%" + "\n";

                //splash
                if (upgradesNameMultipliers.has("splashMultiplier"))
                    information2 += languageManager.getValue(languageManager.getLanguage(), "uInfoSplashMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("splashMultiplier")) + "%" + "\n";

            }

            if(information2.length()>0)
            {

                if (diamonds<getCostToUpgrade())
                    information2 += languageManager.getValue(languageManager.getLanguage(), "tPrice") +": "+ "[RED]"+getCostToUpgrade() +" "+ languageManager.getValue(languageManager.getLanguage(), "diamondsPrice")+"\n";
                else
                    information2 += languageManager.getValue(languageManager.getLanguage(), "tPrice") +": "+ "[GREEN]"+getCostToUpgrade() +" "+ languageManager.getValue(languageManager.getLanguage(), "diamondsPrice")+"\n";

                return new Label(information+information2,labelStyle);
            }


            //else if not implemented
            return new Label( languageManager.getValue(languageManager.getLanguage(), "uInfoNotImplemented"),labelStyle);

        }

    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public boolean isMaxLevel() {
        return isMaxLevel;
    }

    public int getCostToUpgrade() {
        return upgradeArray.getJSONObject(level).getInt("cost");
    }

    public JSONObject getUpgrade() {
        return upgradeArray.getJSONObject(level);
    }

    public void addNextUpgrade(Upgrade nextUpgrade, int levelToUnlock) {
        nextUpgrades.add(nextUpgrade);
        levelToUnlockUpgrades.add(levelToUnlock);

    }

    public Image getImage() {
        return image;
    }

    public int getLevelToUnlock() {
        return levelToUnlockUpgrades.get(0);
    }

    public Upgrade getNextUpgrade() {
        return nextUpgrades.get(0);
    }

    public void removeUnlocked() {
        nextUpgrades.remove(0);
        levelToUnlockUpgrades.remove(0);

    }

    public int getUnlocksLeft() {
        return nextUpgrades.size();
    }

}
