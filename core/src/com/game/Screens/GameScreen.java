package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Entity.Base;
import com.game.Entity.RoadObstacle.*;
import com.game.Entity.Tower.*;
import com.game.Main;
import com.game.Manager.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class GameScreen implements Screen {
    private Main game;
    private Stage stage, pauseStage, gameOverStage;
    private Texture background, shopBackground;
    private BitmapFont font;
    private TextureAtlas taButtonsSettings, taButtonsDefault, taButtonsPause, taButtonTips;
    private Skin images, images_default, images_buildings, images_pause, images_tips;
    private TextButton bSaveDialog, bExitDialog, bSaveAndExitDialog, bNextWave, bResume, bPauseMenu, bUpgrade, bTips;
    private Table table_info, table_map, table_dialogPause, table_nextWave, table_operations, table_operationsSelected, table_buildings, table_dialogGameOver, table_menuPause, table_tips;
    private TextField GameOverTitle;
    private TextField.TextFieldStyle statsTextFieldStyle;
    private ArrayList<String> resolutionsList;
    private ArrayList<String> languages;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private TextButton.TextButtonStyle textButtonStyle_bBack, textButtonStyle_bNotAvailable, textButtonStyle_bPauseMenu, textButtonStyle_bTips;
    private Resolutions resolutions;
    private ButtonStyleManager buttonStyleManager;
    private TextFieldStyleManager textFieldStyleManager;
    private FileReader fileReader;
    private LanguageManager languageManager;
    private Dialog pauseDialog, gameOverDialog;
    private WorldManager worldManager;
    private JSONObject actualGame, enemies;
    private boolean isLocal;
    private ConnectionManager connectionManager;
    private Image[][] mapArr, operationsArr, operationsSelectedArr;
    private String chosenOperation = null;
    private int chosenOperationX, chosenOperationY;
    private SpriteBatch spritebatch = new SpriteBatch();
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private float scale;
    private boolean shouldRenderPreview = false;
    private int[][] buildArr;
    private EnemyManager enemyManager;
    private TowerManager towerManager;
    private UpgradeManager upgradeManager;
    private RoadObstaclesManager roadObstaclesManager;
    public LastClickedTile lastClickedMapTile, lastClickedOperationTile;
    private StatsTableManager statsTableManager;
    private Base base;
    OrthographicCamera hudCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    private Dialog eventDialog, infoDialog, upgradeDialog;
    private TextButton bBackEventDialog, bBackInfoDialog;
    private TextButton bGameOverExit, bGameOverReplay, bGameOverSaveExit, bGameOverNewMap;
    private TipsManager tipsManager;
    private JSONObject turretLevels;
    private Music cleanSound, sellSound, buySound;
    private ProfileManager profileManager;
    private String language;

    public GameScreen(Main game, JSONObject save, boolean isLocal, FileReader fileReader, LanguageManager languageManager) {
        this.game = game;
        this.isLocal = isLocal;
        scale = (float) (Gdx.graphics.getWidth() / 1280.0);
        profileManager = new ProfileManager();
        lastClickedMapTile = new LastClickedTile();
        lastClickedOperationTile = new LastClickedTile();
        resolutions = new Resolutions();
        this.fileReader = fileReader;
        this.languageManager = languageManager;
        this.language = languageManager.getLanguage();
        initSettingsUI();

        actualGame = save;
        if (!isLocal)
            actualGame.put("login", game.getLogin());

        enemies = fileReader.downloadFileAsJSONObject("assets/data/enemies.json");
        turretLevels = fileReader.downloadFileAsJSONObject("assets/data/towers.json");
        operationsArr = GameFunctions.getOperationsArr(this);
        base = new Base(actualGame, operationsArr);
        tipsManager = new TipsManager(languageManager, font, base, scale);
        if (actualGame.getString("difficulty").equals("normal")) {
            mapArr = worldManager.createWorld(this, actualGame.getInt("seed"), 46);
        } else if (actualGame.getString("difficulty").equals("hard")) {
            mapArr = worldManager.createWorld(this, actualGame.getInt("seed"), 36);
        } else if (actualGame.getString("difficulty").equals("easy")) {
            mapArr = worldManager.createWorld(this, actualGame.getInt("seed"), 51);
        }

        mapArr = worldManager.loadTerrainModifications(this, mapArr, actualGame.getJSONArray("terrainModifications"));

        this.buildArr = new int[15][10];

        enemyManager = new EnemyManager(base, scale, GameFunctions.calculatePath(worldManager.getPath(), scale), enemies);
        towerManager = new TowerManager(enemyManager.getEnemies());
        base.addTowerManager(towerManager);
        base.addEnemyManager(enemyManager);
        roadObstaclesManager = new RoadObstaclesManager(enemyManager.getEnemies(), buildArr);

        table_map = worldManager.drawWorld(mapArr, scale);

        hudCamera.position.set(hudCamera.viewportWidth / 2.0f, hudCamera.viewportHeight / 2.0f, 1.0f);

        statsTableManager = new StatsTableManager(base, scale, languageManager);
        table_info = statsTableManager.getStatsTable();

        textFieldStyleManager.setTextFieldStyle(statsTextFieldStyle, images, font, "empty_background", Color.WHITE);
        GameOverTitle = new TextField(languageManager.getValue(language, "Lose"), textFieldStyleManager.returnTextFieldStyle(statsTextFieldStyle));
        GameOverTitle.setAlignment(Align.center);

        if (Gdx.graphics.getHeight() == 900) {
            table_menuPause.setBounds(Gdx.graphics.getWidth() / 40 * 37, (Gdx.graphics.getHeight() / 40 * 38 - 2), Gdx.graphics.getWidth() / 50 * 3, Gdx.graphics.getHeight() / 40 * 3);
        } else {
            table_menuPause.setBounds(Gdx.graphics.getWidth() / 40 * 37, (Gdx.graphics.getHeight() / 40 * 37), Gdx.graphics.getWidth() / 50 * 3, Gdx.graphics.getHeight() / 40 * 3);
        }

        table_tips.setBounds(Gdx.graphics.getWidth() / 80 * 67 + 8 * scale, (Gdx.graphics.getHeight() - (Gdx.graphics.getWidth() / 50 * 3 - 12 * scale)) - 2, Gdx.graphics.getWidth() / 50 * 3, Gdx.graphics.getWidth() / 50 * 3 - 12 * scale);

        buttonStyleManager = new ButtonStyleManager();
        textFieldStyleManager = new TextFieldStyleManager();

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bBack, images_default, font, "defaultButton", "defaultButton");
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bTips, images_tips, font, "button_tips", "button_tips");
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bNotAvailable, images_default, font, "defaultButtonNotAvailable", "defaultButtonNotAvailable");

        bTips = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bTips));
        bSaveDialog = new TextButton(languageManager.getValue(language, "bSave"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bExitDialog = new TextButton(languageManager.getValue(language, "bExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bSaveAndExitDialog = new TextButton(languageManager.getValue(language, "bSaveAndExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bNextWave = new TextButton(languageManager.getValue(language, "bNextWave"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bResume = new TextButton(languageManager.getValue(language, "bResume"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bUpgrade = new TextButton(languageManager.getValue(language, "bUpgrade"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bPauseMenu, images_pause, font, "ButtonPauseUp", "ButtonPauseDown");
        bPauseMenu = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bPauseMenu));
        bBackEventDialog = new TextButton(languageManager.getValue(language, "bExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bBackInfoDialog = new TextButton(languageManager.getValue(language, "bExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bGameOverNewMap = new TextButton(languageManager.getValue(language, "bNewMap"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bGameOverReplay = new TextButton(languageManager.getValue(language, "bReplay"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bGameOverSaveExit = new TextButton(languageManager.getValue(language, "bSaveAndExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        bGameOverExit = new TextButton(languageManager.getValue(language, "bExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));

        table_operations = GameFunctions.getOperationsTable(operationsArr, scale, bUpgrade);
        table_operations.setBackground(new TextureRegionDrawable(new TextureRegion(shopBackground)));

        operationsSelectedArr = GameFunctions.getOperationsSelectedArr();

        table_operationsSelected = GameFunctions.getOperationsTable(operationsSelectedArr, scale, bUpgrade);

        table_nextWave.add(bNextWave).width(224 * scale).padBottom(8 * scale).height(48 * scale);
        table_nextWave.setBounds(Gdx.graphics.getWidth() - 224 * scale, (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2, 224 * scale, 48 * scale);

        table_menuPause.add(bPauseMenu).height(Gdx.graphics.getHeight() / 40 * 3).width(Gdx.graphics.getWidth() / 50 * 3);

        table_tips.add(bTips).width(64 * scale * 0.75f).height(64 * scale * 0.75f);
    }

    public void loadObstacles() {
        JSONArray roadObstacles = actualGame.getJSONArray("roadObstacles");
        ArrayList<RoadObstacle> roadObstaclesLoad = new ArrayList<>();
        for (int b = 0; b < roadObstacles.length(); b++) {
            buildArr[roadObstacles.getJSONObject(b).getInt("x")][roadObstacles.getJSONObject(b).getInt("y")] = 2;

            switch (roadObstacles.getJSONObject(b).getString("name")) {
                case "roadSticky" -> {
                    RoadObstacle t = new RoadSticky(turretLevels, base, roadObstacles.getJSONObject(b).getInt("x"), roadObstacles.getJSONObject(b).getInt("y"), scale, this);
                    t.setUsesLeft(roadObstacles.getJSONObject(b).getInt("usesLeft"));
                    roadObstaclesLoad.add(t);
                }
                case "roadNeedles" -> {
                    RoadObstacle t = new RoadNeedles(turretLevels, base, roadObstacles.getJSONObject(b).getInt("x"), roadObstacles.getJSONObject(b).getInt("y"), scale, this);
                    t.setUsesLeft(roadObstacles.getJSONObject(b).getInt("usesLeft"));
                    roadObstaclesLoad.add(t);
                }
            }
        }
        roadObstaclesManager.initObstacles(roadObstaclesLoad);
    }

    public void loadTowers() {
        JSONArray buildings = actualGame.getJSONArray("buildings");
        for (int b = 0; b < buildings.length(); b++) {
            switch (buildings.getJSONObject(b).getString("name")) {
                case "meleeTower" -> {
                    Tower t = new MeleeTower(turretLevels, base, buildings.getJSONObject(b).getInt("x"), buildings.getJSONObject(b).getInt("y"), scale, this);
                    t.setLevel(buildings.getJSONObject(b).getInt("level"));
                    stage.addActor(t);
                    towerManager.buyTower(t);
                }
                case "mageTower" -> {
                    Tower t = new MageTower(turretLevels, base, buildings.getJSONObject(b).getInt("x"), buildings.getJSONObject(b).getInt("y"), scale, this);
                    t.setLevel(buildings.getJSONObject(b).getInt("level"));
                    stage.addActor(t);
                    towerManager.buyTower(t);
                }
                case "crossbowTower" -> {
                    Tower t = new BowTower(turretLevels, base, buildings.getJSONObject(b).getInt("x"), buildings.getJSONObject(b).getInt("y"), scale, this);
                    t.setLevel(buildings.getJSONObject(b).getInt("level"));
                    stage.addActor(t);
                    towerManager.buyTower(t);
                }
                case "cannonTower" -> {
                    Tower t = new CannonTower(turretLevels, base, buildings.getJSONObject(b).getInt("x"), buildings.getJSONObject(b).getInt("y"), scale, this);
                    t.setLevel(buildings.getJSONObject(b).getInt("level"));
                    stage.addActor(t);
                    towerManager.buyTower(t);
                }
            }
            buildArr[buildings.getJSONObject(b).getInt("x")][buildings.getJSONObject(b).getInt("y")] = 1;
        }
    }


    public void mouseClickOperation() {
        if (Objects.equals(chosenOperation, lastClickedOperationTile.getName())) {
            operationsSelectedArr[chosenOperationY][chosenOperationX].setDrawable(images_buildings, "empty");
            chosenOperation = null;
            chosenOperationX = -1;
            chosenOperationY = -1;

            if (towerManager.getIsDisabledListeners() || roadObstaclesManager.getIsDisabledListeners()) {
                towerManager.enableListeners();
                roadObstaclesManager.enableListeners();
            }
        } else {
            if (chosenOperation != null)
                operationsSelectedArr[chosenOperationY][chosenOperationX].setDrawable(images_buildings, "empty");
            chosenOperation = lastClickedOperationTile.getName();
            chosenOperationX = lastClickedOperationTile.getX();
            chosenOperationY = lastClickedOperationTile.getY();
            operationsSelectedArr[chosenOperationY][chosenOperationX].setDrawable(images_buildings, "chosen");

            if (!towerManager.getIsDisabledListeners() || !roadObstaclesManager.getIsDisabledListeners()) {
                towerManager.disableListeners();
                roadObstaclesManager.disableListeners();
            }
        }
    }

    public void mouseEnterOperation() {
        if (Objects.equals(lastClickedOperationTile.getName(), "melee") || Objects.equals(lastClickedOperationTile.getName(), "crossbow") || Objects.equals(lastClickedOperationTile.getName(), "mage") || Objects.equals(lastClickedOperationTile.getName(), "cannon"))
            base.setInfoToDisplay(1, lastClickedOperationTile.getName(), turretLevels.getJSONArray(lastClickedOperationTile.getName() + "Tower").getJSONObject(0), null);
    }

    public void mouseExitOperation() {
        base.setInfoToDisplay(0);
    }


    public void mouseClickMapTile() {
        if (Objects.equals(chosenOperation, "sell")) {
            if (buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 1) {
                sellSound.dispose();
                sellSound.play();
                base.increaseMoney(towerManager.getSellWorth(lastClickedMapTile.getX(), lastClickedMapTile.getY()));
                towerManager.sellTower(lastClickedMapTile.getX(), lastClickedMapTile.getY());
                buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 0;
            }
            if (buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 2) {
                sellSound.dispose();
                sellSound.play();
                base.increaseMoney(roadObstaclesManager.getSellWorth(lastClickedMapTile.getX(), lastClickedMapTile.getY()));
                roadObstaclesManager.sellRoadObstacle(lastClickedMapTile.getX(), lastClickedMapTile.getY());
                buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 0;
            }
        }
        if (Objects.equals(chosenOperation, "stickyRoad")) {
            if (Objects.equals(lastClickedMapTile.getName(), "path") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                if (base.getMoney() >= turretLevels.getJSONObject("roadSticky").getInt("cost")) {
                    base.decreaseMoney(turretLevels.getJSONObject("roadSticky").getInt("cost"));
                    RoadObstacle r = new RoadSticky(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    roadObstaclesManager.buyObstacle(r);
                    stage.addActor(r);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 2;
                } else {
                    showInfoDialog();
                }
            }
        }
        if (Objects.equals(chosenOperation, "roadNeedles")) {
            if (Objects.equals(lastClickedMapTile.getName(), "path") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                if (base.getMoney() >= turretLevels.getJSONObject("roadNeedles").getInt("cost")) {
                    base.decreaseMoney(turretLevels.getJSONObject("roadNeedles").getInt("cost"));
                    RoadObstacle r = new RoadNeedles(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    roadObstaclesManager.buyObstacle(r);
                    stage.addActor(r);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 2;
                } else {
                    showInfoDialog();
                }
            }
        }


        if (Objects.equals(chosenOperation, "melee")) {
            if (Objects.equals(lastClickedMapTile.getName(), "grass") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                int cost = Math.round(turretLevels.getJSONArray("meleeTower").getJSONObject(0).getInt("cost") * base.getMultipliers().getFloat("costMultiplier"));

                if (base.getMoney() >= cost) {
                    base.decreaseMoney(cost);
                    Tower t = new MeleeTower(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    towerManager.buyTower(t);
                    stage.addActor(t);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 1;
                } else {
                    showInfoDialog();
                }
            }
        }
        if (Objects.equals(chosenOperation, "crossbow")) {
            if (Objects.equals(lastClickedMapTile.getName(), "grass") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                int cost = Math.round(turretLevels.getJSONArray("crossbowTower").getJSONObject(0).getInt("cost") * base.getMultipliers().getFloat("costMultiplier"));
                if (base.getMoney() >= cost) {
                    base.decreaseMoney(cost);
                    Tower t = new BowTower(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    towerManager.buyTower(t);
                    stage.addActor(t);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 1;
                } else {
                    showInfoDialog();
                }
            }
        }
        if (Objects.equals(chosenOperation, "mage")) {
            if (Objects.equals(lastClickedMapTile.getName(), "grass") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                int cost = Math.round(turretLevels.getJSONArray("mageTower").getJSONObject(0).getInt("cost") * base.getMultipliers().getFloat("costMultiplier"));
                if (base.getMoney() >= cost) {
                    base.decreaseMoney(cost);
                    Tower t = new MageTower(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    towerManager.buyTower(t);
                    stage.addActor(t);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 1;
                } else {
                    showInfoDialog();
                }
            }
        }
        if (Objects.equals(chosenOperation, "cannon")) {
            if (Objects.equals(lastClickedMapTile.getName(), "grass") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0) {
                int cost = Math.round(turretLevels.getJSONArray("cannonTower").getJSONObject(0).getInt("cost") * base.getMultipliers().getFloat("costMultiplier"));
                if (base.getMoney() >= cost) {
                    base.decreaseMoney(cost);
                    Tower t = new CannonTower(turretLevels, base, lastClickedMapTile.getX(), lastClickedMapTile.getY(), scale, this);
                    towerManager.buyTower(t);
                    stage.addActor(t);
                    buySound.dispose();
                    buySound.play();
                    buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] = 1;
                } else {
                    showInfoDialog();
                }
            }
        }
        if (Objects.equals(chosenOperation, "clean")) {
            if (Objects.equals(lastClickedMapTile.getName(), "obstacle")) {
                if (base.getMoney() >= base.getCleanPrice()) {
                    cleanSound.dispose();
                    cleanSound.play();
                    base.decreaseMoney(base.getCleanPrice());

                    JSONArray terr = actualGame.getJSONArray("terrainModifications");

                    terr.put(new JSONObject().put("tileName", "grass").put("x", lastClickedMapTile.getX()).put("y", lastClickedMapTile.getY()));
                    actualGame.put("terrainModifications", terr);

                    table_map = worldManager.changeTileAndRedrawWorld(this, mapArr, lastClickedMapTile.getX(), lastClickedMapTile.getY(), "grass", scale);

                    stage.addActor(table_map);
                    showEventDialog();
                } else {
                    showInfoDialog();
                }
            }
        }
    }

    public void mouseEnterMapTile() {
        if (Objects.equals(lastClickedMapTile.getName(), "grass") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0 && (Objects.equals(chosenOperation, "melee") || Objects.equals(chosenOperation, "crossbow") || Objects.equals(chosenOperation, "mage") || Objects.equals(chosenOperation, "cannon")))
            shouldRenderPreview = true;

        if (Objects.equals(lastClickedMapTile.getName(), "path") && buildArr[lastClickedMapTile.getX()][lastClickedMapTile.getY()] == 0 && (Objects.equals(chosenOperation, "roadNeedles") || Objects.equals(chosenOperation, "stickyRoad")))
            shouldRenderPreview = true;
    }

    public void showInfoDialog() {
        Texture infoDialogBackground = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));
        infoDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(infoDialogBackground)))) {
            public void result(Object obj) {
            }
        };
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        infoDialog.text(languageManager.getValue(language, "noGold"), labelStyle);
        infoDialog.button(bBackInfoDialog).padBottom(16);
        infoDialog.show(pauseStage);
        base.setState(Base.State.Paused);
    }

    public void showEventDialog() {
        Texture eventDialogBackground = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));
        eventDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(eventDialogBackground)))) {
            public void result(Object obj) {
            }
        };
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);

        Random eventRandom = new Random();
        int eventChance = eventRandom.nextInt(0, 3);
        int eventPriority = eventChance;

        if (base.getMultipliers().getFloat("luckMultiplier") >= eventRandom.nextInt(0, 101)) {
            eventChance = eventRandom.nextInt(0, 3);
            if (eventPriority > eventChance)
                eventChance = eventPriority;
        }

        if (eventChance == 0) {
            Label label = new Label(languageManager.getValue(language, "eventDamage"), labelStyle);
            label.setAlignment(Align.center);
            eventDialog.text(label);
            base.damageBase(5);
            eventDialog.button(bBackEventDialog).padBottom(16);
            eventDialog.show(pauseStage);
            base.setState(Base.State.Paused);
        } else if (eventChance == 1) {
            Label label = new Label(languageManager.getValue(language, "eventGold"), labelStyle);
            label.setAlignment(Align.center);
            eventDialog.text(label);
            base.increaseMoney(50);
            eventDialog.button(bBackEventDialog).padBottom(16);
            eventDialog.show(pauseStage);
            base.setState(Base.State.Paused);
        } else if (eventChance == 2) {
            Label label = new Label(languageManager.getValue(language, "eventDiamond"), labelStyle);
            label.setAlignment(Align.center);
            eventDialog.text(label);
            base.increaseDiamonds(1);
            eventDialog.button(bBackEventDialog).padBottom(16);
            eventDialog.show(pauseStage);
            base.setState(Base.State.Paused);
        } else { }
    }

    public void mouseExitMapTile() {
        if (shouldRenderPreview)
            shouldRenderPreview = false;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        upgradeManager = new UpgradeManager(languageManager, font, base, fileReader.downloadFileAsJSONObject("assets/data/upgrades.json"), actualGame.getJSONArray("unlockedUpgrades"), scale);

        Texture pauseDialogBanner = new Texture(new FileHandle("assets/backgrounds/profile_banner.png"));

        pauseDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(pauseDialogBanner)))) {
            public void result(Object obj) {
                pauseDialog.cancel();
            }
        };

        gameOverDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(pauseDialogBanner)))) {
            public void result(Object obj) {
                gameOverDialog.cancel();
            }
        };

        bUpgrade.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                base.setState(Base.State.Paused);
                statsTableManager.setMultipliersPage(0);
                statsTableManager.setButtonVisibility();
                base.setInfoToDisplay(4);

                upgradeDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(new Texture(new FileHandle("assets/dialog/upgrade_dialog_720.png"))))));
                upgradeDialog.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 8);
                upgradeDialog.setScale(scale);
                upgradeDialog.row();

                upgradeDialog.add(upgradeManager.returnUpgradeTable()).padLeft(200);
                upgradeDialog.add(statsTableManager.getButtonUp()).padRight(92).padBottom(610 - 50 * scale / 1.5f - 64 * (scale - 1));
                upgradeDialog.add(statsTableManager.getButtonDown()).padRight(32).padBottom(610 - 50 * scale / 1.5f - 64 * (scale - 1));

                upgradeDialog.show(pauseStage);
                upgradeDialog.setY(0);
                upgradeDialog.setX(0);
            }

        });

        bNextWave.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                base.increaseWave(1);
                enemyManager.createRandomEnemyWave();
            }
        });

        bSaveDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enemyManager.getEnemiesLeft() == 0)
                    saveGame();
            }


        });

        bSaveAndExitDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (enemyManager.getEnemiesLeft() == 0) {
                    saveGame();
                    game.setScreen(new MenuScreen(game,fileReader, languageManager));
                }}


        });

        bExitDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game,fileReader, languageManager));
            }
        });

        bGameOverNewMap.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, profileManager.getNewSave(actualGame, base), isLocal, fileReader, languageManager));
            }
        });

        bGameOverReplay.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new GameScreen(game, profileManager.getReplaySave(actualGame, base), isLocal, fileReader, languageManager));
            }
        });

        bGameOverSaveExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                saveGame();
                game.setScreen(new MenuScreen(game,fileReader, languageManager));
            }
        });

        bGameOverExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, fileReader, languageManager));
            }
        });

        bBackEventDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                eventDialog.hide();
                eventDialog.remove();
                base.setState(Base.State.Resumed);
            }
        });

        bBackInfoDialog.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                infoDialog.hide();
                infoDialog.remove();
                base.setState(Base.State.Resumed);
            }
        });

        table_dialogPause.setBounds(0, 0, 256, 460);
        table_dialogPause.add(bResume).width(table_dialogPause.getWidth() / 20 * 16).padRight(table_dialogPause.getWidth() / 10);
        table_dialogPause.row();
        table_dialogPause.add(bSaveDialog).width(table_dialogPause.getWidth() / 20 * 16).padRight(table_dialogPause.getWidth() / 10);
        table_dialogPause.row();
        table_dialogPause.add(bSaveAndExitDialog).width(table_dialogPause.getWidth() / 20 * 16).padRight(table_dialogPause.getWidth() / 10);
        table_dialogPause.row();
        table_dialogPause.add(bExitDialog).padBottom(table_dialogPause.getHeight() / 2 - 16).width(table_dialogPause.getWidth() / 20 * 16).padRight(table_dialogPause.getWidth() / 10);
        table_dialogPause.row();
        pauseDialog.add(table_dialogPause);

        table_dialogGameOver.setBounds(0, 0, 256, 460);
        table_dialogGameOver.row().padTop(32);
        table_dialogGameOver.add(GameOverTitle).width(table_dialogGameOver.getWidth() / 20 * 16).padRight(table_dialogGameOver.getWidth() / 10);
        table_dialogGameOver.row().padTop(32);
        table_dialogGameOver.add(bGameOverNewMap).width(table_dialogGameOver.getWidth() / 20 * 16).padRight(table_dialogGameOver.getWidth() / 10);
        table_dialogGameOver.row().padTop(32);
        table_dialogGameOver.add(bGameOverReplay).width(table_dialogGameOver.getWidth() / 20 * 16).padRight(table_dialogGameOver.getWidth() / 10);
        table_dialogGameOver.row().padTop(32);
        table_dialogGameOver.add(bGameOverSaveExit).width(table_dialogGameOver.getWidth() / 20 * 16).padRight(table_dialogGameOver.getWidth() / 10);
        table_dialogGameOver.row().padTop(32);
        table_dialogGameOver.add(bGameOverExit).width(table_dialogGameOver.getWidth() / 20 * 16).padRight(table_dialogGameOver.getWidth() / 10);
        gameOverDialog.add(table_dialogGameOver).padBottom(180);
        gameOverDialog.show(gameOverStage);

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if (chosenOperation != null) {
                        if (shouldRenderPreview)
                            shouldRenderPreview = false;

                        operationsSelectedArr[chosenOperationY][chosenOperationX].setDrawable(images_buildings, "empty");
                        chosenOperation = null;
                        chosenOperationX = -1;
                        chosenOperationY = -1;

                        if (towerManager.getIsDisabledListeners() || roadObstaclesManager.getIsDisabledListeners()) {
                            towerManager.enableListeners();
                            roadObstaclesManager.enableListeners();
                        }
                        return true;
                    }
                    pauseDialog.show(pauseStage);
                    base.setState(Base.State.Paused);
                    return true;
                }
                if (keycode == Input.Keys.F5) {
                    if (enemyManager.getEnemiesLeft() == 0)
                        saveGame();
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });

        pauseStage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    base.setState(Base.State.Resumed);
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });

        bPauseMenu.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseDialog.show(pauseStage);
                base.setState(Base.State.Paused);
            }
        });

        bTips.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tipsManager.createTipsDialog();
                Dialog tipsDialog = tipsManager.returnTipsDialog();
                tipsDialog.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                tipsDialog.show(pauseStage);
                base.setState(Base.State.Paused);
            }
        });
        bResume.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseDialog.hide();
                pauseDialog.remove();
                base.setState(Base.State.Resumed);
            }

        });
        stage.addActor(table_operations);
        stage.addActor(table_operationsSelected);
        stage.addActor(table_map);
        stage.addActor(table_buildings);
        stage.addActor(table_nextWave);
        stage.addActor(table_info);
        stage.addActor(table_menuPause);
        stage.addActor(table_tips);
        loadTowers();
        loadObstacles();
        towerManager.enableListeners();
        roadObstaclesManager.enableListeners();
    }

    public void saveGame() {
        actualGame.put("buildings", towerManager.getTowers());
        actualGame.put("gold", base.getMoney());
        actualGame.put("maxHealth", base.getMaxHealth());
        actualGame.put("health", base.getHealth());
        actualGame.put("wave", base.getWave());
        actualGame.put("roadObstacles", roadObstaclesManager.getRoadObstacles());

        if (isLocal) {
            fileReader.setSave(actualGame);
        } else {
            JSONObject saveResponse = connectionManager.requestSend(actualGame, "api/uploadSave");
        }
    }

    public void updateInfoDisplay() {
        statsTableManager.setInfoToDisplay(base.getInfoToDisplay(), base.getInfoToDisplayObjectNow(), base.getInfoToDisplayObjectUpgraded(), base.getInfoToDisplayName());
        table_info.remove();
        table_info = statsTableManager.getInfoTable();
        if (base.getState() == Base.State.Running || base.getState() == Base.State.Resumed)
            stage.addActor(table_info);
        else if (base.getState() == Base.State.Paused)
            pauseStage.addActor(table_info);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0, 0);
        game.batch.end();
        stage.act(delta);
        stage.draw();

        buildArr = roadObstaclesManager.getArr();

        if ((statsTableManager.getInfoToDisplay() != base.getInfoToDisplay()) || (base.getInfoToDisplay() == 1 && chosenOperation != base.getInfoToDisplayName()) || base.isShouldUpdateInfo()) {
            if (base.isShouldUpdateInfo())
                base.setShouldUpdateInfo(false);
            updateInfoDisplay();
        }
        if (enemyManager.getEnemiesLeft() != 0) {
            bSaveDialog.setTouchable(Touchable.disabled);
            bSaveAndExitDialog.setTouchable(Touchable.disabled);
            bSaveDialog.setStyle(textButtonStyle_bNotAvailable);
            bSaveAndExitDialog.setStyle(textButtonStyle_bNotAvailable);
        } else {
            bSaveDialog.setTouchable(Touchable.enabled);
            bSaveAndExitDialog.setTouchable(Touchable.enabled);
            bSaveDialog.setStyle(textButtonStyle_bBack);
            bSaveAndExitDialog.setStyle(textButtonStyle_bBack);
        }
        if (base.getHealth() <= 0) {
            base.setState(Base.State.GameOver);
        }

        switch (base.getState()) {
            case Running -> {
                roadObstaclesManager.update();

                statsTableManager.update();
                towerManager.update(delta);
                enemyManager.update(delta);
                spritebatch.begin();
                roadObstaclesManager.render(spritebatch);
                towerManager.render(spritebatch, shapeRenderer);
                enemyManager.render(spritebatch, shapeRenderer);
                font.draw(spritebatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, hudCamera.viewportHeight);
                if (shouldRenderPreview)
                    GameFunctions.renderPreviewBuilding(spritebatch, shapeRenderer, lastClickedMapTile, turretLevels, chosenOperation, scale);
                spritebatch.end();
            }
            case Paused -> {
                Gdx.input.setInputProcessor(pauseStage);
                table_info.toBack();
                statsTableManager.update();
                pauseStage.act(delta);
                spritebatch.begin();
                roadObstaclesManager.render(spritebatch);
                towerManager.render(spritebatch, shapeRenderer);
                enemyManager.render(spritebatch, shapeRenderer);
                spritebatch.end();
                pauseStage.draw();
            }
            case Resumed -> {
                resume();
                spritebatch.begin();
                roadObstaclesManager.render(spritebatch);
                towerManager.render(spritebatch, shapeRenderer);
                enemyManager.render(spritebatch, shapeRenderer);
                spritebatch.end();
                Gdx.input.setInputProcessor(stage);
                base.setState(Base.State.Running);
            }
            case GameOver -> {
                Gdx.input.setInputProcessor(gameOverStage);
                statsTableManager.update();
                spritebatch.begin();
                roadObstaclesManager.render(spritebatch);
                towerManager.render(spritebatch, shapeRenderer);
                enemyManager.render(spritebatch, shapeRenderer);
                spritebatch.end();
                gameOverStage.draw();
            }
        }
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {
        if (pauseDialog != null)
            if (pauseDialog.isVisible()) {
                pauseDialog.hide();
                pauseDialog.remove();
            }
        if (upgradeDialog != null)
            if (upgradeDialog.isVisible()) {
                upgradeDialog.hide();
                upgradeDialog.remove();
                upgradeDialog.clear();
            }
        if (eventDialog != null)
            if (eventDialog.isVisible()) {
                eventDialog.hide();
                eventDialog.remove();
            }
        if (infoDialog != null)
            if (infoDialog.isVisible()) {
                infoDialog.hide();
                infoDialog.remove();
            }
        if (tipsManager.returnTipsDialog() != null)
            if (tipsManager.returnTipsDialog().isVisible()) {
                tipsManager.returnTipsDialog().hide();
                tipsManager.returnTipsDialog().remove();
            }
        base.setInfoToDisplay(0);
        updateInfoDisplay();
    }

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    private void initSettingsUI() {
        worldManager = new WorldManager();

        background = new Texture("assets/backgrounds/tempBackground.png");
        shopBackground = new Texture(new FileHandle("assets/backgrounds/game/shopBackground.png"));

        resolutionsList = new ArrayList<>();
        resolutionsList = resolutions.getResolutionsArrayList();
        languages = new ArrayList<>();
        languages.add("English");
        languages.add("Polski");

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        stage = new Stage();
        pauseStage = new Stage();
        gameOverStage = new Stage();

        parameter.size = 15;
        parameter.color = Color.WHITE;
        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        font = new BitmapFont();
        font = generator.generateFont(parameter);

        connectionManager = new ConnectionManager(game);

        taButtonsSettings = new TextureAtlas("assets/buttons/buttons_settings.pack");
        taButtonsDefault = new TextureAtlas("assets/buttons/buttons_default.pack");
        taButtonsPause = new TextureAtlas("assets/buttons/buttons_pause.pack");
        taButtonTips = new TextureAtlas("assets/buttons/buttons_tips.pack");

        images = new Skin(taButtonsSettings);
        images_default = new Skin(taButtonsDefault);
        images_pause = new Skin(taButtonsPause);
        images_tips = new Skin(taButtonTips);
        images_buildings = new Skin(new TextureAtlas("assets/icons/buildings.pack"));

        table_dialogGameOver = new Table();
        table_dialogPause = new Table();
        table_nextWave = new Table();
        table_operations = new Table();
        table_buildings = new Table();
        table_operationsSelected = new Table();
        table_menuPause = new Table();
        table_info = new Table();
        table_tips = new Table();

        textFieldStyleManager = new TextFieldStyleManager();
        statsTextFieldStyle = new TextField.TextFieldStyle();

        textButtonStyle_bBack = new TextButton.TextButtonStyle();
        textButtonStyle_bNotAvailable = new TextButton.TextButtonStyle();
        textButtonStyle_bPauseMenu = new TextButton.TextButtonStyle();
        textButtonStyle_bTips = new TextButton.TextButtonStyle();

        cleanSound = game.getCleanSound();
        cleanSound.setVolume(fileReader.getVolumeEffectsValue() / 100f);
        cleanSound.setLooping(false);
        sellSound = game.getSellSound();
        sellSound.setVolume(fileReader.getVolumeEffectsValue() / 100f);
        sellSound.setLooping(false);
        buySound = game.getBuySound();
        buySound.setVolume(fileReader.getVolumeEffectsValue() / 100f);
        buySound.setLooping(false);

    }

    public Music getBuySound() {
        return buySound;
    }
}