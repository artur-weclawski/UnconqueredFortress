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
    private String language;

    public Upgrade(String upgradeName, int level, JSONArray upgradeArray, Skin skin, String unlockedIcon, LanguageManager languageManager) {
        this.upgradeName = upgradeName;
        this.level = level;
        this.maxLevel = upgradeArray.length();
        this.image = new Image(skin, "upgradeIcons_lock");
        this.upgradeArray = upgradeArray;
        this.unlockedIcon = unlockedIcon;
        this.languageManager = languageManager;
        this.language = languageManager.getLanguage();
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
        if (images_upgrades!=null)
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
            return new Label(languageManager.getValue(language, "upgrade_First_Unlock"), labelStyle);
        }else if (level==maxLevel) {
            return new Label(languageManager.getValue(language, "upgrade_Max_level"), labelStyle);
        } else {
            String information3 = "";
            information3 += languageManager.getValue(language, upgradeName+"Description");
            String information = languageManager.getValue(language, "upgrade_Upgrade") + languageManager.getValue(language, this.upgradeName) + "\n"+ information3 + "\n" + languageManager.getValue(language, "upgrade_Level") + this.level + "/" + this.maxLevel + "\n";
            String information2 = "";
            JSONObject upgradesJSONObject = upgradeArray.getJSONObject(level);

            if (upgradesJSONObject.has("unlocks"))
                information2 += languageManager.getValue(language, "uInfoUnlocks")+ upgradeArray.getJSONObject(level).getString("unlocks")+"\n";

            if (upgradesJSONObject.has("maxHealth"))
                information2 += languageManager.getValue(language, "uInfoMaxHealth") + upgradeArray.getJSONObject(level).getInt("maxHealth")+"\n";

            if (upgradesJSONObject.has("multipliers")) {
                JSONObject upgradesNameMultipliers = upgradesJSONObject.getJSONObject("multipliers");

                if (upgradesNameMultipliers.has("healthRegeneration"))
                    information2 += languageManager.getValue(language, "uInfoHealthRegeneration") + upgradesNameMultipliers.getInt("healthRegeneration") + "\n";

                if (upgradesNameMultipliers.has("damageReduction"))
                    information2 += languageManager.getValue(language, "uInfoDamageReduction") + upgradesNameMultipliers.getInt("damageReduction") + "\n";

                if (upgradesNameMultipliers.has("goldMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoGoldMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("goldMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("diamondsMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoDiamondsMultiplier") + upgradesNameMultipliers.getInt("diamondsMultiplier") + "\n";

                if (upgradesNameMultipliers.has("costMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("costMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("upgradeCostMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoUpgradeCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("upgradeCostMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("cleaningCostMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoCleaningCostMultiplier") + (int) (-100 * upgradesNameMultipliers.getFloat("cleaningCostMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("luckMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoLuckMultiplier") + upgradesNameMultipliers.getInt("luckMultiplier") + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultiplier")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliermeleeTower"))
                    information2 += languageManager.getValue(language, "uInfoMeleeDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliermeleeTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliermageTower"))
                    information2 += languageManager.getValue(language, "uInfoMageDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliermageTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliercrossbowTower"))
                    information2 += languageManager.getValue(language, "uInfoCrossbowDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("damageMultipliercannonTower"))
                    information2 += languageManager.getValue(language, "uInfoCannonDamageMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("damageMultipliercannonTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("rangeMultipliercannonTower"))
                    information2 += languageManager.getValue(language, "uInfoRangeCannon") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliercannonTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("rangeMultipliercrossbowTower"))
                    information2 += languageManager.getValue(language, "uInfoRangeCrossbow") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("rangeMultipliermageTower"))
                    information2 += languageManager.getValue(language, "uInfoRangeMage") + (int) (100 * upgradesNameMultipliers.getFloat("rangeMultipliermageTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliermeleeTower"))
                    information2 += languageManager.getValue(language, "uInfoMeleeReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliermeleeTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliercrossbowTower"))
                    information2 += languageManager.getValue(language, "uInfoCrossbowReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliercrossbowTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliermageTower"))
                    information2 += languageManager.getValue(language, "uInfoMageReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliermageTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("reloadSpeedMultipliercannonTower"))
                    information2 += languageManager.getValue(language, "uInfoCannonReloadSpeedMultiplier") + + (int) (-100 * upgradesNameMultipliers.getFloat("reloadSpeedMultipliercannonTower")) + "%" + "\n";

                if (upgradesNameMultipliers.has("splashMultiplier"))
                    information2 += languageManager.getValue(language, "uInfoSplashMultiplier") + (int) (100 * upgradesNameMultipliers.getFloat("splashMultiplier")) + "%" + "\n";
            }

            if(information2.length()>0)
            {
                if (diamonds<getCostToUpgrade())
                    information2 += languageManager.getValue(language, "tPrice") +": "+ "[RED]"+getCostToUpgrade() +" "+ languageManager.getValue(language, "diamondsPrice")+"\n";
                else
                    information2 += languageManager.getValue(language, "tPrice") +": "+ "[GREEN]"+getCostToUpgrade() +" "+ languageManager.getValue(language, "diamondsPrice")+"\n";

                return new Label(information+information2,labelStyle);
            }

            return new Label( languageManager.getValue(language, "uInfoNotImplemented"),labelStyle);
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