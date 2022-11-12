package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Main;
import com.game.Manager.*;
import com.game.State.GameState;
import org.json.JSONObject;
import org.json.simple.JSONArray;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class ProfileLocalScreen implements Screen {
    private Main game;
    private FileReader fileReader;
    private ButtonStyleManager buttonStyleManager;
    private TextFieldStyleManager textFieldStyleManager;
    private List<User_save> profilesList;
    private Stage stage;
    private Texture background;
    private BitmapFont font, font_profile;
    private TextureAtlas taButtonsDefault, taEmptyTextfield, taButtonsProfile, taDialog;
    private Skin images_default, images_empty, image_profiles, images_settings;
    private TextButton bMigrateSaveDialogOk, bMigrateSaveDialogBack, bDeleteDialogDelete, bDeleteDialogCancel, bBack, bPlay, bOtherScreen, bNewProfile01, bNewProfile02, bNewProfile03, bDialogCancel, bDialogCreate, cDialogEasyDifficulty, cDialogNormalDifficulty, cDialogHardDifficulty;
    private Table table_profile_01, table_profile_02, table_profile_03, table_default, table_next, table_Dialog, table_deleteDialog;
    private Table delete1, delete2, delete3;
    private Table table_migrateSave, migrationSave1, migrationSave2, migrationSave3;
    private TextField tDialogEasyDifficulty, tDialogNormalDifficulty, tDialogHardDifficulty, tMigrateSaveText;
    private TextField tDialogSeed, tDialogSeedValue, tDialogDifficulty;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private TextButton.TextButtonStyle textButtonStyle_bBack,textButtonStyle_bSave, textButtonStyle_bNext, textButtonStyle_bNewProfile, textButtonStyle_cDialogDifficultyChecked, textButtonStyle_cDialogDifficultyUnchecked;
    private TextField.TextFieldStyle textFieldStyle, seedFieldStyle;
    private Music backgroundMusic;
    private LanguageManager languageManager;
    private ConnectionManager connectionManager;
    private JSONObject save1, save2, save3;
    private JSONObject loadResponse;
    private Dialog newGameDialog, deleteGameDialog, migrateSaveDialog;
    private String chosenDifficulty = null;
    private int chosenProfile;
    private boolean isDialog = false;
    private int saveToDelete;
    private ProfileManager profileManager;

    public ProfileLocalScreen(Main game, FileReader fileReader, LanguageManager languageManager){
        this.game = game;
        this.fileReader = fileReader;
        this.languageManager = languageManager;

        profileManager = new ProfileManager();

        initProfileLocalUI();

        profilesList = new ArrayList<>();
        buttonStyleManager = new ButtonStyleManager();
        textFieldStyleManager = new TextFieldStyleManager();

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bBack, images_default, font, "defaultButton", "defaultButton");
        bBack = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bBack"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bSave, images_default, font, "defaultButton", "defaultButton");
        bPlay = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bPlay"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        bDialogCreate = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCreate"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
        bDialogCancel = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCancel"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        bDeleteDialogDelete = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDelete"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
        bDeleteDialogCancel = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCancel"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogDifficultyChecked, images_settings, font, "checkbox_on", "checkbox_on");
        buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked, images_settings, font, "checkbox_off", "checkbox_off");

        cDialogEasyDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));
        cDialogNormalDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));
        cDialogHardDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bNext, image_profiles, font, "next_screen_button", "next_screen_button");
        bOtherScreen = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNext));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bNewProfile, image_profiles, font, "new_profile_button_up", "new_profile_button_down");
        bNewProfile01 = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        bNewProfile02 = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        bNewProfile03 = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        textFieldStyleManager.setTextFieldStyle(textFieldStyle, images_empty, font, "empty_background", Color.WHITE);
        textFieldStyleManager.setTextFieldStyleCursor(seedFieldStyle, images_settings, font, "textBar", Color.WHITE);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture profileBannerBackground = new Texture(new FileHandle("assets/backgrounds/profile_banner.png"));
        Texture loginRegisterDialogBackground = new Texture(new FileHandle("assets/dialog/loginregisterDialog.png"));
        Texture deleteDialogBackground = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));

        newGameDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(loginRegisterDialogBackground)))) {
            public void result(Object obj) {
                newGameDialog.cancel();
            }
        };

        tDialogEasyDifficulty = new TextField(languageManager.getValue(languageManager.getLanguage(), "tEasyDifficulty"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tDialogNormalDifficulty  = new TextField(languageManager.getValue(languageManager.getLanguage(), "tNormalDifficulty"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tDialogHardDifficulty = new TextField(languageManager.getValue(languageManager.getLanguage(), "tHardDifficulty"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));

        tDialogSeed = new TextField(languageManager.getValue(languageManager.getLanguage(), "seed"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tDialogSeed.setDisabled(true);
        tDialogSeed.setAlignment(Align.center);

        tDialogEasyDifficulty.setAlignment(Align.center);
        tDialogNormalDifficulty.setAlignment(Align.center);
        tDialogHardDifficulty.setAlignment(Align.center);

        tDialogSeedValue = new TextField("", textFieldStyleManager.returnTextFieldStyle(seedFieldStyle));
        tDialogSeedValue.setAlignment(Align.center);
        tDialogDifficulty = new TextField(languageManager.getValue(languageManager.getLanguage(), "difficulty_field"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tDialogDifficulty.setDisabled(true);
        deleteGameDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(deleteDialogBackground)))) {
            public void result(Object obj) {
                deleteGameDialog.cancel();
            }
        };

        table_Dialog.setWidth(360);
        table_Dialog.setHeight(420);
        table_Dialog.setX(0);
        table_Dialog.setY(0);
        table_Dialog.row().padBottom(8);
        table_Dialog.add(tDialogDifficulty).colspan(2);
        table_Dialog.row().padBottom(8);
        table_Dialog.add(cDialogEasyDifficulty);
        table_Dialog.add(tDialogEasyDifficulty);
        table_Dialog.row().padBottom(8);
        table_Dialog.add(cDialogNormalDifficulty);
        table_Dialog.add(tDialogNormalDifficulty);
        table_Dialog.row().padBottom(8);
        table_Dialog.add(cDialogHardDifficulty);
        table_Dialog.add(tDialogHardDifficulty);
        table_Dialog.row().padBottom(8);
        table_Dialog.add(tDialogSeed);
        table_Dialog.add(tDialogSeedValue);
        table_Dialog.row();
        table_Dialog.add(bDialogCancel).padTop(64);
        table_Dialog.add(bDialogCreate).padTop(64);

        table_deleteDialog.setWidth(350);
        table_deleteDialog.setX(200);
        table_deleteDialog.setY(300);

        newGameDialog.addActor(table_Dialog);

        deleteGameDialog.addActor(table_deleteDialog);
        deleteGameDialog.button(bDeleteDialogCancel).padBottom(80-bDeleteDialogDelete.getHeight()/2);
        deleteGameDialog.button(bDeleteDialogDelete).padBottom(80-bDeleteDialogDelete.getHeight()/2);

        cDialogEasyDifficulty.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                cDialogEasyDifficulty.setStyle(textButtonStyle_cDialogDifficultyChecked);
                cDialogNormalDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                cDialogHardDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                chosenDifficulty = "easy";
            }
        });

        cDialogNormalDifficulty.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                cDialogEasyDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                cDialogNormalDifficulty.setStyle(textButtonStyle_cDialogDifficultyChecked);
                cDialogHardDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                chosenDifficulty = "normal";
            }
        });

        cDialogHardDifficulty.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                cDialogEasyDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                cDialogNormalDifficulty.setStyle(textButtonStyle_cDialogDifficultyUnchecked);
                cDialogHardDifficulty.setStyle(textButtonStyle_cDialogDifficultyChecked);
                chosenDifficulty = "hard";
            }
        });

        bDialogCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                newGameDialog.hide();
            }
        });

        bDialogCreate.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (chosenDifficulty !=null) {
                    System.out.println("Stworzono gre na profilu " + chosenProfile + "o poziomie trudnosci " + chosenDifficulty);
                    GameState.setGameState(GameState.PLAYING);
                    game.setScreen(new GameScreen(game, profileManager.createEmptySave(chosenDifficulty, chosenProfile, tDialogSeedValue.getText()), true, fileReader, languageManager));
                }
            }
        });

        bDeleteDialogDelete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                deleteSave(saveToDelete);
            }
        });

        bDeleteDialogCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                deleteGameDialog.hide();
            }
        });

        table_profile_01.setBounds(Gdx.graphics.getWidth()/10*2, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_01.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));
        table_profile_02.setBounds(Gdx.graphics.getWidth()/10*4, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_02.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));
        table_profile_03.setBounds(Gdx.graphics.getWidth()/10*6, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_03.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));

        table_default.setBounds(Gdx.graphics.getWidth()/10*3, Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/10*4,50);

        if(fileReader.fileExists("save/save01l.json")) {
            save1 = fileReader.downloadSaveAsJSONObject("save/save01l.json");
            save1.put("profileNumber",1);
            table_profile_01 = profileManager.createProfileTable(save1, font_profile, languageManager, Gdx.graphics.getWidth()/10*2, "assets/icons/local.png");
            delete1 = profileManager.getDeleteTable((int) table_profile_01.getX(), (int) (table_profile_01.getY()+table_profile_01.getHeight())-32, (int) table_profile_01.getChild(0).getWidth(), (int) table_profile_01.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));
            delete1.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    saveToDelete=1;
                    deleteGameDialog.show(stage);
                }
            });

            migrationSave1 = profileManager.getMigrationSaveTable((int) table_profile_01.getX(), (int) (table_profile_01.getY() + table_profile_01.getHeight()) - 64, (int) table_profile_01.getChild(0).getWidth(), (int) table_profile_01.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), true);
            migrationSave1.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    migrateSave(1);
                }
            });

            table_profile_01.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("zostałem wybrany");
                    GameState.setGameState(GameState.PLAYING);
                    game.setScreen(new GameScreen(game, save1, true, fileReader, languageManager));

                }
            });
        } else {
            table_profile_01 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*2);
            table_profile_01.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 1;
                    newGameDialog.show(stage);
                }
            });
        }

        if(fileReader.fileExists("save/save02l.json")) {
            save2 = fileReader.downloadSaveAsJSONObject("save/save02l.json");
            save2.put("profileNumber",2);
            table_profile_02 = profileManager.createProfileTable(save2, font_profile, languageManager, Gdx.graphics.getWidth()/10*4, "assets/icons/local.png");
            delete2 = profileManager.getDeleteTable((int) table_profile_02.getX(), (int) (table_profile_02.getY()+table_profile_02.getHeight())-32, (int) table_profile_02.getChild(0).getWidth(), (int) table_profile_02.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));

            delete2.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    saveToDelete=2;
                    deleteGameDialog.show(stage);
                }
            });

            migrationSave2 = profileManager.getMigrationSaveTable((int) table_profile_02.getX(), (int) (table_profile_02.getY() + table_profile_02.getHeight()) - 64, (int) table_profile_02.getChild(0).getWidth(), (int) table_profile_02.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), true);
            migrationSave2.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    migrateSave(2);
                }
            });

            table_profile_02.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameState.setGameState(GameState.PLAYING);
                    game.setScreen(new GameScreen(game, save2, true, fileReader, languageManager));
                }
            });
        } else {
            table_profile_02 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*4);
            table_profile_02.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 2;
                    newGameDialog.show(stage);
                }
            });
        }

        if(fileReader.fileExists("save/save03l.json")) {
            save3 = fileReader.downloadSaveAsJSONObject("save/save03l.json");
            save3.put("profileNumber",3);
            table_profile_03 = profileManager.createProfileTable(save3, font_profile, languageManager, Gdx.graphics.getWidth()/10*6, "assets/icons/local.png");
            delete3 = profileManager.getDeleteTable((int) table_profile_03.getX(), (int) (table_profile_03.getY()+table_profile_03.getHeight())-32, (int) table_profile_03.getChild(0).getWidth(), (int) table_profile_03.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));
            delete3.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    saveToDelete=3;
                    deleteGameDialog.show(stage);
                }
            });

            migrationSave3 = profileManager.getMigrationSaveTable((int) table_profile_03.getX(), (int) (table_profile_03.getY() + table_profile_03.getHeight()) - 64, (int) table_profile_03.getChild(0).getWidth(), (int) table_profile_03.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), true);
            migrationSave3.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    migrateSave(3);
                }
            });

            table_profile_03.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    GameState.setGameState(GameState.PLAYING);
                    game.setScreen(new GameScreen(game, save3, true, fileReader, languageManager));
                }
            });
        } else {
            table_profile_03 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*6);
            table_profile_03.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 3;
                    newGameDialog.show(stage);
                }
            });
        }

        table_default.add(bBack).padRight(200);
        table_default.add(bPlay).padLeft(90);

        stage.addActor(table_profile_01);
        stage.addActor(table_profile_02);
        stage.addActor(table_profile_03);
        stage.addActor(delete1);
        stage.addActor(delete2);
        stage.addActor(delete3);
        stage.addActor(table_default);

        bBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, fileReader, languageManager));
            }
        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game, fileReader, languageManager));
                    dispose();
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });

        //cloud connection
        if(game.getIsLogged()) {
            table_next.setBounds(Gdx.graphics.getWidth()/10*9, Gdx.graphics.getWidth()/10*2,Gdx.graphics.getHeight()/10, Gdx.graphics.getWidth()/10*2);
            table_next.add(bOtherScreen);

            JSONObject loadSaves = new JSONObject().put("login", game.getLogin());

            new Thread(() -> {
                loadResponse = connectionManager.requestSend(loadSaves, "api/downloadSaves");

                if (!loadResponse.has("loadedData"))
                    loadResponse.put("loadedData",new JSONArray());

                System.out.println(loadResponse.getInt("status"));

                if (loadResponse.getInt("status") == 200 || loadResponse.getInt("status") == 201) {
                    stage.addActor(table_next);
                    if (fileReader.fileExists("save/save01l.json")) {
                        stage.addActor(migrationSave1);
                    }
                    if (fileReader.fileExists("save/save02l.json")) {
                        stage.addActor(migrationSave2);
                    }
                    if(fileReader.fileExists("save/save03l.json")) {
                        stage.addActor(migrationSave3);
                    }
                } else {
                    System.out.println(languageManager.getValue(languageManager.getLanguage(), loadResponse.getString("message")));
                }
            }).start();

            bOtherScreen.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {

                    game.setScreen(new ProfileCloudScreen(game, loadResponse, fileReader, languageManager));

                }
            });
        }

        tMigrateSaveText = new TextField(null, textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tMigrateSaveText.setAlignment(Align.center);
        bMigrateSaveDialogOk = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bSend"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
        bMigrateSaveDialogBack = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bBack"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        table_migrateSave.setWidth(512);
        table_migrateSave.setHeight(160);
        table_migrateSave.setX(0);
        table_migrateSave.setY(0);
        table_migrateSave.add(tMigrateSaveText).width(256).colspan(2);
        table_migrateSave.row().padBottom(8);
        table_migrateSave.add(bMigrateSaveDialogBack);
        table_migrateSave.add(bMigrateSaveDialogOk);
        migrateSaveDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(deleteDialogBackground)))) {
            public void result(Object obj) {
                deleteGameDialog.cancel();
            }
        };
        migrateSaveDialog.addActor(table_migrateSave);
    }

    private void migrateSave(int saveNumber) {

        int numberOfLoadedSaves = loadResponse.getJSONArray("loadedData").length();

        if (numberOfLoadedSaves==3) {
            System.out.println("zajete");
            tMigrateSaveText.setText(languageManager.getValue(languageManager.getLanguage(),"noAvailableSlots"));
            table_migrateSave.removeActor(bMigrateSaveDialogOk);
            table_migrateSave.removeActor(bMigrateSaveDialogBack);
            table_migrateSave.row().padBottom(8);
            table_migrateSave.add(bMigrateSaveDialogBack).colspan(2);
            bMigrateSaveDialogBack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    migrateSaveDialog.hide();
                }
            });

            migrateSaveDialog.show(stage);
            return;
        }

        List<Integer> existSave = new ArrayList<>();

        for (int i = 0; i < numberOfLoadedSaves; i++) {
            existSave.add(loadResponse.getJSONArray("loadedData").getJSONObject(i).getInt("profileNumber"));
        }
        Collections.sort(existSave);
        for(int i=1 ; i<=3; i++) {
            if (!existSave.contains(i)) {
                tMigrateSaveText.setText(languageManager.getValue(languageManager.getLanguage(), "tDoYouWantSend") + i+"?");
                int finalI = i;
                bMigrateSaveDialogOk.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        uploadAndDelete(saveNumber, finalI);
                        game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager));
                    }
                });
                bMigrateSaveDialogBack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y){
                        migrateSaveDialog.hide();
                    }
                });
                migrateSaveDialog.show(stage);
                return;
            }
        }
    }

    private void uploadAndDelete(int saveNumber, int emptyProfileNumber) {
        JSONObject saveToUpload = new JSONObject();

        switch (saveNumber) {
            case 1-> saveToUpload = save1;
            case 2-> saveToUpload = save2;
            case 3-> saveToUpload = save3;
        }
        saveToUpload.put("login",game.getLogin());
        saveToUpload.put("profileNumber",emptyProfileNumber);

        connectionManager.requestSend(saveToUpload, "api/uploadSave");

        fileReader.deleteSave(saveNumber);
    }

    public void deleteSave(int saveNumber){
        fileReader.deleteSave(saveNumber);
        game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();

        if(isDialog) {
            Gdx.gl.glClearColor(0,0,0,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            table_profile_01.setVisible(false);
            table_profile_02.setVisible(false);
            table_profile_03.setVisible(false);
            delete1.setVisible(false);
            delete2.setVisible(false);
            delete3.setVisible(false);
            migrationSave1.setVisible(false);
            migrationSave2.setVisible(false);
            migrationSave3.setVisible(false);

            table_default.setVisible(false);
            table_next.setVisible(false);
        } else {
            game.batch.draw(background, 0,0);
            table_profile_01.setVisible(true);
            table_profile_02.setVisible(true);
            table_profile_03.setVisible(true);
            delete1.setVisible(true);
            delete2.setVisible(true);
            delete3.setVisible(true);
            migrationSave1.setVisible(true);
            migrationSave2.setVisible(true);
            migrationSave3.setVisible(true);
            table_default.setVisible(true);
            table_next.setVisible(true);
        }

        game.batch.end();
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}

    private void initProfileLocalUI(){
        stage = new Stage();

        background = new Texture("backgrounds/tempBackground.png");

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 13;
        parameter.color = Color.WHITE;
        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        font = new BitmapFont();
        font = generator.generateFont(parameter);

        if(Gdx.graphics.getWidth() < 1281) {
            parameter.size = 9;
        } else if(Gdx.graphics.getWidth() < 1601){
            parameter.size = 11;
        } else {
            parameter.size = 13;
        }

        font_profile = new BitmapFont();
        font_profile = generator.generateFont(parameter);

        taButtonsDefault = new TextureAtlas("assets/buttons/buttons_default.pack");

        taEmptyTextfield = new TextureAtlas("assets/buttons/buttons_settings.pack");
        taButtonsProfile = new TextureAtlas("assets/buttons/buttons_profile.pack");
        taDialog = new TextureAtlas("assets/dialog/skin_dialog.pack");//<- to delete

        connectionManager = new ConnectionManager();

        images_default = new Skin(taButtonsDefault);
        images_empty = new Skin(taEmptyTextfield);
        image_profiles = new Skin(taButtonsProfile);
        images_settings = new Skin(new TextureAtlas("assets/buttons/buttons_settings.pack"));

        table_default = new Table();
        table_next = new Table();
        table_deleteDialog = new Table();
        table_Dialog = new Table();
        table_profile_01 = new Table();
        table_profile_02 = new Table();
        table_profile_03 = new Table();
        delete1 = new Table();
        delete2 = new Table();
        delete3 = new Table();
        migrationSave1 = new Table();
        migrationSave2 = new Table();
        migrationSave3 = new Table();
        table_migrateSave = new Table();

        textButtonStyle_bBack = new TextButton.TextButtonStyle();
        textButtonStyle_bSave = new TextButton.TextButtonStyle();
        textButtonStyle_bNext = new TextButton.TextButtonStyle();
        textButtonStyle_bNewProfile = new TextButton.TextButtonStyle();

        textFieldStyle = new TextField.TextFieldStyle();
        seedFieldStyle = new TextField.TextFieldStyle();

        backgroundMusic = game.getMusic();

        textButtonStyle_cDialogDifficultyChecked = new TextButton.TextButtonStyle();
        textButtonStyle_cDialogDifficultyUnchecked  = new TextButton.TextButtonStyle();
    }


}
