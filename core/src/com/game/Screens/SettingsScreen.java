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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Main;
import com.game.Manager.*;

import java.util.ArrayList;
import java.util.Objects;

public class SettingsScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture background;
    private BitmapFont font;
    private TextureAtlas taButtonsSettings, taButtonsDefault;
    private Skin images, images_default;
    private TextButton bLeftResolution, bRightResolution, bBack, bSave, bLeftLanguage, bRightLanguage, bBackDialog;
    private Table table_resolution, table_default, table_volume, table_volume2, table_language;
    private TextField tResolutionField, tResolutionFieldText, tVolumeFieldText, tVolumeEffectsFieldText, tLanguageFieldText, tLanguageField;
    private ArrayList<String> resolutionsList;
    private ArrayList<String> languagesList;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private TextButton.TextButtonStyle textButtonStyle_bLeft, textButtonStyle_bRight, textButtonStyle_bBack, textButtonStyle_bSave;
    private TextField.TextFieldStyle textFieldStyle;
    private Music backgroundMusic;
    private Slider volumeMusicSlider, volumeEffectsSlider;
    private Slider.SliderStyle sliderStyle;
    private Resolutions resolutions;
    private ButtonStyleManager buttonStyleManager;
    private TextFieldStyleManager textFieldStyleManager;
    private FileReader fileReader;
    private LanguageManager languageManager;
    private Dialog backDialog;
    private boolean isDialog = false;
    private String language;
    public SettingsScreen (Main game, FileReader fileReader, LanguageManager languageManager){
        this.game = game;
        resolutions = new Resolutions();
        this.fileReader = fileReader;
        this.languageManager = languageManager;
        this.language = languageManager.getLanguage();

        initSettingsUI();
        buttonStyleManager = new ButtonStyleManager();
        textFieldStyleManager = new TextFieldStyleManager();

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bLeft, images, font, "resolutionButtonLeft_up", "resolutionButtonLeft_down");
        bLeftResolution = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bLeft));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bRight, images, font, "resolutionButtonRight_up","resolutionButtonRight_down" );
        bRightResolution = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bRight));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bBack, images_default, font, "defaultButton", "defaultButton");
        bBack = new TextButton(languageManager.getValue(language, "bBack"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bSave, images_default, font, "defaultButton", "defaultButton");
        bSave = new TextButton(languageManager.getValue(language, "bSave"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSave));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bBack, images_default, font, "defaultButton", "defaultButton");
        bBackDialog = new TextButton(languageManager.getValue(language, "bBackDialog"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bBack));

        textFieldStyleManager.setTextFieldStyle(textFieldStyle, images, font, "textBar", Color.WHITE);
        tResolutionFieldText = new TextField(languageManager.getValue(language, "resolution_field_text"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));

        tVolumeFieldText = new TextField(languageManager.getValue(language, "volume_field_text"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tVolumeEffectsFieldText = new TextField(languageManager.getValue(language, "volumeEffects_field_text"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        sliderStyle.disabledBackground = images.getDrawable("slider_background");
        sliderStyle.disabledKnob = images.getDrawable("slider_knob");
        sliderStyle.background = images.getDrawable("slider_background");
        sliderStyle.knob = images.getDrawable("slider_knob");
        volumeMusicSlider = new Slider(0,100,1,false, sliderStyle);
        volumeMusicSlider.setValue(fileReader.getVolumeValue());

        volumeEffectsSlider = new Slider(0,100,1,false, sliderStyle);
        volumeEffectsSlider.setValue(fileReader.getVolumeEffectsValue());

        tLanguageFieldText = new TextField(languageManager.getValue(language, "language_field_text"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        bLeftLanguage = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bLeft));
        bRightLanguage = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bRight));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        table_resolution.setBounds(Gdx.graphics.getWidth()/10*3,Gdx.graphics.getHeight()/10*8, Gdx.graphics.getWidth()/10*4,32);
        table_default.setBounds(Gdx.graphics.getWidth()/10*3, Gdx.graphics.getHeight()/10, Gdx.graphics.getWidth()/10*4,50);
        table_volume.setBounds(Gdx.graphics.getWidth()/10*3, Gdx.graphics.getHeight()/10*7, Gdx.graphics.getWidth()/10*4, 32);
        table_volume2.setBounds(Gdx.graphics.getWidth()/10*3, Gdx.graphics.getHeight()/10*6, Gdx.graphics.getWidth()/10*4, 32);
        table_language.setBounds(Gdx.graphics.getWidth()/10*3,Gdx.graphics.getHeight()/10*5, Gdx.graphics.getWidth()/10*4,32);

        bRightResolution.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(resolutionsList.size() == 3){
                    if(tResolutionField.getText().equals(resolutionsList.get(0))) {
                        tResolutionField.setText(resolutionsList.get(1));
                    } else if (tResolutionField.getText().equals(resolutionsList.get(1))) {
                        tResolutionField.setText(resolutionsList.get(2));
                    } else if (Objects.equals(tResolutionField.getText(), resolutionsList.get(2))) {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                } else if(resolutionsList.size() == 2){
                    if(tResolutionField.getText().equals(resolutionsList.get(0))) {
                        tResolutionField.setText(resolutionsList.get(1));
                    } else if (tResolutionField.getText().equals(resolutionsList.get(1))) {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                }
                else if(resolutionsList.size() == 1) {
                    if(tResolutionField.getText().equals(resolutionsList.get(0))) {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                }
            }
        });

        volumeMusicSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                backgroundMusic.setVolume(volumeMusicSlider.getValue()/100);
            }
        });

        bLeftResolution.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(resolutionsList.size() == 3) {
                    if(tResolutionField.getText().equals(resolutionsList.get(0))) {
                        tResolutionField.setText(resolutionsList.get(2));
                    } else if (tResolutionField.getText().equals(resolutionsList.get(2))) {
                        tResolutionField.setText(resolutionsList.get(1));
                    }else if (Objects.equals(tResolutionField.getText(), resolutionsList.get(1))) {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                } else if(resolutionsList.size() == 2) {
                    if(tResolutionField.getText().equals(resolutionsList.get(0))) {
                        tResolutionField.setText(resolutionsList.get(1));
                    } else if (Objects.equals(tResolutionField.getText(), resolutionsList.get(1))) {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                } else if(resolutionsList.size() == 1) {
                    if(tResolutionField.getText().equals(resolutionsList.get(0)))
                    {
                        tResolutionField.setText(resolutionsList.get(0));
                    }
                }
            }
        });

        bRightLanguage.addListener(new ClickListener(){
            @Override
            public  void clicked(InputEvent event, float x, float y){
                if(tLanguageField.getText().equals(languagesList.get(0))) {
                    tLanguageField.setText(languagesList.get(1));
                } else {
                    tLanguageField.setText(languagesList.get(0));
                }
            }
        });

        bLeftLanguage.addListener(new ClickListener(){
            @Override
            public  void clicked(InputEvent event, float x, float y){
                if(tLanguageField.getText().equals(languagesList.get(1))) {
                    tLanguageField.setText(languagesList.get(0));
                } else {
                    tLanguageField.setText(languagesList.get(1));
                }
            }
        });
        bBack.addListener(new ClickListener(){

            @Override
            public void clicked(InputEvent event, float x, float y){
                if(tLanguageField.getText().equals(fileReader.getLanguageValue()) && tResolutionField.getText().equals(fileReader.getResolutionValue()) && Math.round(volumeMusicSlider.getValue()) == fileReader.getVolumeValue() && Math.round(volumeEffectsSlider.getValue()) == fileReader.getVolumeEffectsValue()){
                    game.setScreen(new MenuScreen(game, fileReader, languageManager));
                    dispose();
                } else {
                    isDialog = true;
                    Texture bg = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));
                    backDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(bg)))) {
                        public void result(Object obj) {
                        }
                    };

                    Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
                    backDialog.text(languageManager.getValue(language, "dialog_field_text"), labelStyle);
                    backDialog.button(bBackDialog).padBottom(16);
                    backDialog.button(bSave).padBottom(16);
                    backDialog.show(stage);
                    table_default.remove();
                }
            }
        });
        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    if(tLanguageField.getText().equals(fileReader.getLanguageValue()) && tResolutionField.getText().equals(fileReader.getResolutionValue()) && Math.round(volumeMusicSlider.getValue()) == fileReader.getVolumeValue() && Math.round(volumeEffectsSlider.getValue()) == fileReader.getVolumeEffectsValue()){
                        game.setScreen(new MenuScreen(game, fileReader, languageManager));
                        dispose();
                    } else if(!isDialog) {
                        isDialog = true;
                        Texture bg = new Texture(new FileHandle("assets/dialog/settings_dialog.png"));
                        backDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(bg)))) {
                            public void result(Object obj) {
                            }
                        };

                        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
                        backDialog.text(languageManager.getValue(language, "dialog_field_text"), labelStyle);
                        backDialog.button(bBackDialog).padBottom(16);
                        backDialog.button(bSave).padBottom(16);
                        backDialog.show(stage);
                        table_default.remove();
                    }
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });
        bBackDialog.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                game.setScreen(new MenuScreen(game, fileReader, languageManager));
                dispose();
            }
        });

        bSave.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if(tResolutionField.getText().equals("1920 X 1080 Fullscreen") || tResolutionField.getText().equals("1280 X 720 Fullscreen") || tResolutionField.getText().equals("1600 X 900 Fullscreen"))
                 {
                    Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
                } else if (tResolutionField.getText().equals("1920 X 1080 Windowed")) {
                    Gdx.graphics.setWindowedMode(1920,1080);
                } else if (tResolutionField.getText().equals("1280 X 720 Windowed")) {
                    Gdx.graphics.setWindowedMode(1280,720);
                } else if (tResolutionField.getText().equals("1600 X 900 Windowed")) {
                    Gdx.graphics.setWindowedMode(1600,900);
                }

                fileReader.setSettings(tResolutionField.getText(), volumeMusicSlider.getValue(), volumeEffectsSlider.getValue(),tLanguageField.getText());
                languageManager = new LanguageManager(tLanguageField.getText());
                game.setScreen(new MenuScreen(game, fileReader, languageManager));
                dispose();
            }
        });
        if(fileReader.getResolutionValue() != null) {
            tResolutionField = new TextField(fileReader.getResolutionValue(), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        } else {
            if(Gdx.graphics.getWidth() == 1920) {
                if(resolutionsList.contains("1920 X 1080 Fullscreen")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1920 X 1080 Fullscreen")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                } else if(resolutionsList.contains("1920 X 1080 Windowed")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1920 X 1080 Windowed")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                }
            } else if(Gdx.graphics.getWidth() == 1280) {
                if(resolutionsList.contains("1280 X 720 Fullscreen")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1280 X 720 Fullscreen")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                } else if(resolutionsList.contains("1280 X 720 Windowed")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1280 X 720 Windowed")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                }
            } else {
                if(resolutionsList.contains("1600 X 900 Fullscreen")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1600 X 900 Fullscreen")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                } else if(resolutionsList.contains("1600 X 900 Windowed")) {
                    tResolutionField = new TextField(resolutionsList.get(resolutionsList.indexOf("1600 X 900 Windowed")), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
                }
            }
        }

        if(fileReader.getLanguageValue() != null) {
            tLanguageField = new TextField(fileReader.getLanguageValue(), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        } else {
            tLanguageField = new TextField(languagesList.get(0), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        }

        tResolutionFieldText.setAlignment(Align.center);
        tVolumeFieldText.setAlignment(Align.center);
        tVolumeEffectsFieldText.setAlignment(Align.center);
        tLanguageFieldText.setAlignment(Align.center);

        tResolutionField.setAlignment(Align.center);
        table_resolution.add(tResolutionFieldText).padRight(100).width(320);
        table_resolution.add(bLeftResolution).width(32).height(32);
        table_resolution.add(tResolutionField).width(320).height(32);
        table_resolution.add(bRightResolution).width(32).height(32);

        table_default.add(bBack).padRight(200);
        table_default.add(bSave).padLeft(90);

        table_volume.add(tVolumeFieldText).padRight(100).width(320);
        table_volume.add(volumeMusicSlider).width(384).height(32);

        table_volume2.add(tVolumeEffectsFieldText).padRight(100).width(320);
        table_volume2.add(volumeEffectsSlider).width(384).height(32);

        tLanguageField.setAlignment(Align.center);
        table_language.add(tLanguageFieldText).padRight(100).width(320);
        table_language.add(bLeftLanguage).width(32).height(32);
        table_language.add(tLanguageField).width(320).height(32);
        table_language.add(bRightLanguage).width(32).height(32);

        stage.addActor(table_resolution);
        stage.addActor(table_default);
        stage.addActor(table_volume);
        stage.addActor(table_volume2);
        stage.addActor(table_language);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.batch.begin();
        game.batch.draw(background, 0,0);
        if(isDialog) {
            table_language.setVisible(false);
            table_volume.setVisible(false);
            table_volume2.setVisible(false);
            table_resolution.setVisible(false);
        } else {
            table_language.setVisible(true);
            table_volume.setVisible(true);
            table_volume2.setVisible(true);
            table_resolution.setVisible(true);
        }

        game.batch.end();
        tVolumeFieldText.setText(languageManager.getValue(language, "volume_field_text") + Math.round(volumeMusicSlider.getValue()));
        tVolumeEffectsFieldText.setText(languageManager.getValue(language, "volumeEffects_field_text") + Math.round(volumeEffectsSlider.getValue()));
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
    private void initSettingsUI(){
        background = new Texture("backgrounds/tempBackground.png");

        resolutionsList = new ArrayList<>();
        resolutionsList = resolutions.getResolutionsArrayList();
        languagesList = new ArrayList<>();
        languagesList.add("English");
        languagesList.add("Polski");

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        stage = new Stage();

        parameter.size = 15;
        parameter.color = Color.WHITE;
        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        font = new BitmapFont();
        font = generator.generateFont(parameter);

        taButtonsSettings = new TextureAtlas("assets/buttons/buttons_settings.pack");
        taButtonsDefault = new TextureAtlas("assets/buttons/buttons_default.pack");

        images = new Skin(taButtonsSettings);
        images_default = new Skin(taButtonsDefault);

        table_resolution = new Table();
        table_default = new Table();
        table_volume = new Table();
        table_language = new Table();
        table_volume2 = new Table();

        textButtonStyle_bLeft = new TextButton.TextButtonStyle();
        textButtonStyle_bRight = new TextButton.TextButtonStyle();
        textButtonStyle_bBack = new TextButton.TextButtonStyle();
        textButtonStyle_bSave = new TextButton.TextButtonStyle();

        textFieldStyle = new TextField.TextFieldStyle();

        sliderStyle = new Slider.SliderStyle();

        backgroundMusic = game.getMusic();
    }
}