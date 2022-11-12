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
import org.json.JSONArray;
import org.json.JSONObject;

public class ProfileCloudScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture background;
    private FileReader fileReader;
    private ConnectionManager connectionManager;
    private LanguageManager languageManager;
    private ButtonStyleManager buttonStyleManager;
    private TextFieldStyleManager textFieldStyleManager;
    private JSONObject save1, save2, save3;
    private BitmapFont font, font_profile;
    private TextureAtlas taButtonsDefault, taEmptyTextfield, taButtonsProfile;
    private Skin images_default, images_empty, image_profiles, images_settings;
    private TextButton bMigrateSaveDialogOk, bMigrateSaveDialogBack, bDeleteDialogDelete, bDeleteDialogCancel;
    private TextButton bBack, bPlay, bOtherScreen, bNewProfile01, bNewProfile02, bNewProfile03, bDialogCancel, bDialogCreate, cDialogEasyDifficulty, cDialogNormalDifficulty, cDialogHardDifficulty;
    private Table table_profile_01, table_profile_02, table_profile_03, table_default, table_previous, table_Dialog, table_deleteDialog;
    private Table delete1, delete2, delete3;
    private Table table_migrateSave, migrationSave1, migrationSave2, migrationSave3;
    private TextField tDialogEasyDifficulty, tDialogNormalDifficulty, tDialogHardDifficulty, tDialogSeed, tDialogSeedValue, tDialogDifficulty, tMigrateSaveText;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private TextButton.TextButtonStyle textButtonStyle_bBack,textButtonStyle_bSave, textButtonStyle_bPrevious, textButtonStyle_bNewProfile, textButtonStyle_cDialogDifficultyChecked, textButtonStyle_cDialogDifficultyUnchecked;
    private TextField.TextFieldStyle textFieldStyle, seedFieldStyle;
    private Music backgroundMusic;
    private boolean isDialog = false;
    private JSONObject loadResponse;
    private Dialog newGameDialog, deleteGameDialog, migrateSaveDialog;;
    private String chosenDifficulty = null;
    private int chosenProfile;
    private int saveToDelete;
    private ProfileManager profileManager;

    public ProfileCloudScreen(Main game, JSONObject loadResponse, FileReader fileReader, LanguageManager languageManager){
        this.game = game;
        stage = new Stage();
        this.fileReader = fileReader;
        this.languageManager = languageManager;

        profileManager = new ProfileManager();

        this.loadResponse = loadResponse;

        initProfileCloudUI();

        buttonStyleManager = new ButtonStyleManager();
        textFieldStyleManager = new TextFieldStyleManager();

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bBack, images_default, font, "defaultButton", "defaultButton");
        bBack = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bBack"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bSave, images_default, font, "defaultButton", "defaultButton");
        bPlay = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bPlay"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        bDialogCreate = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCreate"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
        bDialogCancel = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCancel"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogDifficultyChecked, images_settings, font, "checkbox_on", "checkbox_on");
        buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked, images_settings, font, "checkbox_off", "checkbox_off");

        cDialogEasyDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));
        cDialogNormalDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));
        cDialogHardDifficulty = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogDifficultyUnchecked));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bPrevious, image_profiles, font, "previous_screen_button", "previous_screen_button");
        bOtherScreen = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_bPrevious));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bNewProfile, image_profiles, font, "new_profile_button_up", "new_profile_button_down");
        bNewProfile01 = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        bNewProfile02 = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        bNewProfile03 = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNewProfile));
        textFieldStyleManager.setTextFieldStyle(textFieldStyle, images_empty, font, "empty_background", Color.WHITE);
        textFieldStyleManager.setTextFieldStyleCursor(seedFieldStyle, images_settings, font, "textBar", Color.WHITE);

        background = new Texture("background.png");
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        Texture profileBannerBackground = new Texture(new FileHandle("assets/backgrounds/profile_banner.png"));
        Texture loginRegisterDialogBackground = new Texture(new FileHandle("assets/dialog/loginregisterDialog.png"));
        Texture deleteDialogBackground = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));

        table_profile_01.setBounds(Gdx.graphics.getWidth()/10*2, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_01.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));
        table_profile_02.setBounds(Gdx.graphics.getWidth()/10*4, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_02.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));
        table_profile_03.setBounds(Gdx.graphics.getWidth()/10*6, Gdx.graphics.getWidth()/10, Gdx.graphics.getHeight()/10*3, Gdx.graphics.getWidth()/10*3);
        table_profile_03.setBackground(new TextureRegionDrawable(new TextureRegion(profileBannerBackground)));

        if (loadResponse.getInt("status") == 200) {
            int numberOfLoadedSaves = loadResponse.getJSONArray("loadedData").length();
            JSONObject save;
            for (int i =0; i<numberOfLoadedSaves; i++) {
                save =  loadResponse.getJSONArray("loadedData").getJSONObject(i);

                if( save.getInt("profileNumber")==1) {
                    save1 = save;
                    table_profile_01 = profileManager.createProfileTable(save, font_profile, languageManager, Gdx.graphics.getWidth()/10*2, "assets/icons/cloud.png");
                    table_profile_01.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            System.out.println("zostałem wybrany");
                            GameState.setGameState(GameState.PLAYING);
                            game.setScreen(new GameScreen(game, save1, false, fileReader, languageManager));
                        }
                    });

                    delete1 = profileManager.getDeleteTable((int) table_profile_01.getX(), (int) (table_profile_01.getY()+table_profile_01.getHeight())-32, (int) table_profile_01.getChild(0).getWidth(), (int) table_profile_01.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));
                    delete1.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            saveToDelete=1;
                            deleteGameDialog.show(stage);
                        }
                    });

                    migrationSave1 = profileManager.getMigrationSaveTable((int) table_profile_01.getX(), (int) (table_profile_01.getY() + table_profile_01.getHeight()) - 64, (int) table_profile_01.getChild(0).getWidth(), (int) table_profile_01.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), false);
                    migrationSave1.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            migrateSave(1, save1);
                        }
                    });

                    stage.addActor(delete1);
                    stage.addActor(migrationSave1);
                } else if( save.getInt("profileNumber")==2) {
                    save2 = save;
                    table_profile_02 = profileManager.createProfileTable(save, font_profile, languageManager, Gdx.graphics.getWidth()/10*4,"assets/icons/cloud.png");
                    table_profile_02.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            GameState.setGameState(GameState.PLAYING);
                            game.setScreen(new GameScreen(game, save2, false, fileReader, languageManager));
                        }
                    });

                    delete2 = profileManager.getDeleteTable((int) table_profile_02.getX(), (int) (table_profile_02.getY()+table_profile_02.getHeight())-32, (int) table_profile_02.getChild(0).getWidth(), (int) table_profile_02.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));
                    delete2.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            saveToDelete=2;
                            deleteGameDialog.show(stage);
                        }
                    });

                    migrationSave2 = profileManager.getMigrationSaveTable((int) table_profile_02.getX(), (int) (table_profile_02.getY() + table_profile_02.getHeight()) - 64, (int) table_profile_02.getChild(0).getWidth(), (int) table_profile_02.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), false);
                    migrationSave2.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            migrateSave(2, save2);
                        }
                    });

                    stage.addActor(delete2);
                    stage.addActor(migrationSave2);
                } else if( save.getInt("profileNumber")==3) {
                    save3 = save;
                    table_profile_03 = profileManager.createProfileTable(save, font_profile, languageManager, Gdx.graphics.getWidth()/10*6, "assets/icons/cloud.png");
                    table_profile_03.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            GameState.setGameState(GameState.PLAYING);
                            game.setScreen(new GameScreen(game, save3, false, fileReader, languageManager));
                        }
                    });

                    delete3 = profileManager.getDeleteTable((int) table_profile_03.getX(), (int) (table_profile_03.getY()+table_profile_03.getHeight())-32, (int) table_profile_03.getChild(0).getWidth(), (int) table_profile_03.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0));
                    delete3.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            saveToDelete=3;
                            deleteGameDialog.show(stage);
                        }
                    });

                    migrationSave3 = profileManager.getMigrationSaveTable((int) table_profile_03.getX(), (int) (table_profile_03.getY() + table_profile_03.getHeight()) - 64, (int) table_profile_03.getChild(0).getWidth(), (int) table_profile_03.getChild(0).getHeight(), (float) (Gdx.graphics.getWidth() / 1280.0), false);
                    migrationSave3.addListener(new ClickListener(){
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            migrateSave(3, save3);
                        }
                    });

                    stage.addActor(delete3);
                    stage.addActor(migrationSave3);
                }
            }
        }

        //no saves -> make +
        if (table_profile_01.getChildren().size==0) {
            table_profile_01 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*2);
            table_profile_01.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 1;
                    newGameDialog.show(stage);
                }
            });
        }
        if (table_profile_02.getChildren().size==0) {
            table_profile_02 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*4);
            table_profile_02.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 2;
                    newGameDialog.show(stage);
                }
            });
        }
        if (table_profile_03.getChildren().size==0) {
            table_profile_03 = profileManager.createEmptyProfileTable(font, Gdx.graphics.getWidth()/10*6);
            table_profile_03.addListener(new ClickListener(){
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    chosenProfile = 3;
                    newGameDialog.show(stage);
                }
            });
        }

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

        //to calibrate
        table_previous.setBounds((Gdx.graphics.getWidth()-Gdx.graphics.getWidth()/10*9 - Gdx.graphics.getHeight()/10), Gdx.graphics.getWidth()/10*2,Gdx.graphics.getHeight()/10, Gdx.graphics.getWidth()/10*2);
        table_previous.add(bOtherScreen);

        stage.addActor(table_previous);

        bOtherScreen.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager));
            }
        });
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


        table_deleteDialog.setWidth(350);
        table_deleteDialog.setX(200);
        table_deleteDialog.setY(300);

        bDeleteDialogDelete = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDelete"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
        bDeleteDialogCancel = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDialogCancel"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));



        deleteGameDialog.addActor(table_deleteDialog);
        deleteGameDialog.button(bDeleteDialogCancel).padBottom(80-bDeleteDialogDelete.getHeight()/2);
        deleteGameDialog.button(bDeleteDialogDelete).padBottom(80-bDeleteDialogDelete.getHeight()/2);

        bDeleteDialogDelete.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                connectionManager.requestSend(new JSONObject().put("login", game.getLogin()).put("profileNumber", saveToDelete), "api/deleteSave");
                removeDeletedFromResponse();
                game.setScreen(new ProfileCloudScreen(game,loadResponse, fileReader, languageManager));
            }
        });

        bDeleteDialogCancel.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                deleteGameDialog.hide();
            }
        });


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

        newGameDialog.addActor(table_Dialog);

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
                    game.setScreen(new GameScreen(game,profileManager.createEmptySave(chosenDifficulty, chosenProfile, tDialogSeedValue.getText()), false, fileReader, languageManager));
                }
            }
        });

        table_default.setBounds(Gdx.graphics.getWidth()/10*3, Gdx.graphics.getHeight()/20, Gdx.graphics.getWidth()/10*4,50);
        table_default.add(bBack).padRight(200);
        table_default.add(bPlay).padLeft(90);

        stage.addActor(table_profile_01);
        stage.addActor(table_profile_02);
        stage.addActor(table_profile_03);
        stage.addActor(table_default);

        bBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game, fileReader, languageManager));
            }
        });

        tMigrateSaveText = new TextField(null, textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tMigrateSaveText.setAlignment(Align.center);
        bMigrateSaveDialogOk = new TextButton(languageManager.getValue(languageManager.getLanguage(), "bDownload"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));
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
    }

    private void migrateSave(int saveNumber, JSONObject saveToMigrate) {
        migrateSaveDialog.addActor(table_migrateSave);

        if (fileReader.fileExists("save/save01l.json") && fileReader.fileExists("save/save02l.json") && fileReader.fileExists("save/save03l.json")) {
            table_migrateSave.removeActor(bMigrateSaveDialogOk);
            table_migrateSave.removeActor(bMigrateSaveDialogBack);
            table_migrateSave.row().padBottom(8);
            table_migrateSave.add(bMigrateSaveDialogBack).colspan(2);
            System.out.println("zajete");
            tMigrateSaveText.setText(languageManager.getValue(languageManager.getLanguage(),"noAvailableSlots"));
            table_migrateSave.removeActor(bMigrateSaveDialogOk);
            bMigrateSaveDialogBack.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y){
                    migrateSaveDialog.hide();
                }
            });
            migrateSaveDialog.show(stage);
            return;
        }

        for(int i=1 ; i<=3; i++) {
            if(!fileReader.fileExists("save/save0"+i+"l.json")) {
                tMigrateSaveText.setText(languageManager.getValue(languageManager.getLanguage(), "tDoYouWantDownload") + i+"?");
                int finalI = i;
                bMigrateSaveDialogOk.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        uploadAndDelete(saveNumber, finalI, saveToMigrate);
                        game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager));
                    }
                });
                bMigrateSaveDialogBack.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        migrateSaveDialog.hide();
                    }
                });
                migrateSaveDialog.show(stage);
                return;
            }
        }

        tMigrateSaveText.setText("brak pustych");
    }

    private void uploadAndDelete(int saveNumber, int emptyProfileNumber, JSONObject saveToMigrate) {
        saveToMigrate.put("profileNumber", emptyProfileNumber);
        saveToMigrate.remove("login");
        saveToMigrate.remove("_id");
        saveToMigrate.remove("__v");

        fileReader.setSave(saveToMigrate);

        connectionManager.requestSend(new JSONObject().put("login", game.getLogin()).put("profileNumber", saveNumber), "api/deleteSave");
    }

    private void removeDeletedFromResponse() {
        for(int i=0; i<loadResponse.getJSONArray("loadedData").length(); i++) {
            if( loadResponse.getJSONArray("loadedData").getJSONObject(i).getInt("profileNumber")==saveToDelete) {
                JSONArray j = loadResponse.getJSONArray("loadedData");
                j.remove(i);
                loadResponse.put("loadedData", j);
                break;
            }
        }
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
            table_previous.setVisible(false);
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
            table_previous.setVisible(true);
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

    private void initProfileCloudUI() {
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
        } else if(Gdx.graphics.getWidth() < 1601) {
            parameter.size = 11;
        } else {
            parameter.size = 13;
        }

        font_profile = new BitmapFont();
        font_profile = generator.generateFont(parameter);

        taButtonsDefault = new TextureAtlas("assets/buttons/buttons_default.pack");
        taEmptyTextfield = new TextureAtlas("assets/buttons/buttons_settings.pack");
        taButtonsProfile = new TextureAtlas("assets/buttons/buttons_profile.pack");

        connectionManager = new ConnectionManager();

        images_default = new Skin(taButtonsDefault);
        images_empty = new Skin(taEmptyTextfield);
        image_profiles = new Skin(taButtonsProfile);
        images_settings = new Skin(new TextureAtlas("assets/buttons/buttons_settings.pack"));

        table_default = new Table();
        table_previous = new Table();
        table_Dialog = new Table();
        table_profile_01 = new Table();
        table_profile_02 = new Table();
        table_profile_03 = new Table();
        table_deleteDialog = new Table();
        delete1 = new Table();
        delete2 = new Table();
        delete3 = new Table();
        migrationSave1 = new Table();
        migrationSave2 = new Table();
        migrationSave3 = new Table();
        table_migrateSave = new Table();

        textButtonStyle_bBack = new TextButton.TextButtonStyle();
        textButtonStyle_bSave = new TextButton.TextButtonStyle();
        textButtonStyle_bPrevious = new TextButton.TextButtonStyle();
        textButtonStyle_bNewProfile = new TextButton.TextButtonStyle();

        textFieldStyle = new TextField.TextFieldStyle();
        seedFieldStyle = new TextField.TextFieldStyle();

        backgroundMusic = game.getMusic();

        textButtonStyle_cDialogDifficultyChecked = new TextButton.TextButtonStyle();
        textButtonStyle_cDialogDifficultyUnchecked  = new TextButton.TextButtonStyle();
    }
}
