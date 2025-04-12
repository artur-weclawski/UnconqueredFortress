package com.game.Screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.game.Main;
import com.game.Manager.FileReader;
import com.game.Manager.LanguageManager;
import com.game.Manager.TextFieldStyleManager;

import java.util.ArrayList;

public class CreditsScreen implements Screen {
    private Main game;
    private Stage stage;
    private Texture background;
    public BitmapFont fontTitle, fontText;
    private TextureAtlas text_credits;
    private Skin images;
    private Table table_credits;
    private TextField tCreditsTitleGame, tCreditsTitleMusic, tCreditsTitleGraphics, tCreditsTitleStack;
    private TextField tCreditsGame1, tCreditsGame2, tCreditsGame3, tCreditsGame4;
    private TextField tCreditsMusic1, tCreditsMusic2, tCreditsMusic3, tCreditsMusic4;
    private TextField tCreditsGraphics1, tCreditsGraphics2, tCreditsGraphics3, tCreditsGraphics4;
    private ArrayList<String> languagesList;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private TextField.TextFieldStyle textTitleFieldStyle, textFieldStyle;
    private Music backgroundMusic;
    private TextFieldStyleManager textFieldStyleManager;
    private FileReader fileReader;
    private LanguageManager languageManager;
    private String language;

    public CreditsScreen(Main game, FileReader fileReader, LanguageManager languageManager){
        this.game = game;
        textFieldStyleManager = new TextFieldStyleManager();
        this.fileReader = fileReader;
        this.languageManager = languageManager;
        this.language = languageManager.getLanguage();

        initSettingsUI();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MenuScreen(game,fileReader, languageManager));
                dispose();
            }


        });

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    game.setScreen(new MenuScreen(game,fileReader, languageManager));
                    dispose();
                    return true;
                }
                return super.keyDown(event, keycode);
            }
        });

        textFieldStyleManager.setTextFieldStyle(textFieldStyle, images, fontText, "empty_text", Color.WHITE);
        textFieldStyleManager.setTextFieldStyle(textTitleFieldStyle, images, fontTitle, "title_text", Color.WHITE);

        tCreditsTitleGame = new TextField(languageManager.getValue(language, "creditsGame"), textFieldStyleManager.returnTextFieldStyle(textTitleFieldStyle));
        tCreditsTitleMusic = new TextField(languageManager.getValue(language, "creditsMusic"), textFieldStyleManager.returnTextFieldStyle(textTitleFieldStyle));
        tCreditsTitleGraphics = new TextField(languageManager.getValue(language, "creditsGraphics"), textFieldStyleManager.returnTextFieldStyle(textTitleFieldStyle));
        tCreditsTitleStack = new TextField(languageManager.getValue(language, "creditsStack"), textFieldStyleManager.returnTextFieldStyle(textTitleFieldStyle));

        tCreditsGame1 = new TextField("Artur Węcławski", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGame2 = new TextField("Tomasz Wiejak", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGame3 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGame4 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));

        tCreditsMusic1 = new TextField("The Sleeping Prophet - Jesse Gallagher", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsMusic2 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsMusic3 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsMusic4 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));

        tCreditsGraphics1 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGraphics2 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGraphics3 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tCreditsGraphics4 = new TextField("", textFieldStyleManager.returnTextFieldStyle(textFieldStyle));

        tCreditsTitleGame.setAlignment(Align.center);
        tCreditsGame1.setAlignment(Align.center);
        tCreditsGame2.setAlignment(Align.center);
        tCreditsGame3.setAlignment(Align.center);
        tCreditsGame4.setAlignment(Align.center);

        tCreditsTitleMusic.setAlignment(Align.center);
        tCreditsMusic1.setAlignment(Align.center);
        tCreditsMusic2.setAlignment(Align.center);
        tCreditsMusic3.setAlignment(Align.center);
        tCreditsMusic4.setAlignment(Align.center);

        tCreditsTitleGraphics.setAlignment(Align.center);
        tCreditsGraphics1.setAlignment(Align.center);
        tCreditsGraphics2.setAlignment(Align.center);
        tCreditsGraphics3.setAlignment(Align.center);
        tCreditsGraphics4.setAlignment(Align.center);

        tCreditsTitleStack.setAlignment(Align.center);

        table_credits.setHeight(200);
        table_credits.setBounds(0, (0-table_credits.getHeight()*2), Gdx.graphics.getWidth(), table_credits.getHeight());

        table_credits.align(Align.center);

        table_credits.add(tCreditsTitleGame).expand().fillX().pad(100,100,20,100);
        table_credits.row();
        table_credits.add(tCreditsGame1).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGame2).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGame3).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGame4).expandX().fillX();

        table_credits.row();
        table_credits.add(tCreditsTitleMusic).expand().fillX().pad(100,100,20,100);
        table_credits.row();
        table_credits.add(tCreditsMusic1).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsMusic2).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsMusic3).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsMusic4).expandX().fillX();

        table_credits.row();
        table_credits.add(tCreditsTitleGraphics).expand().fillX().pad(100,100,20,100);
        table_credits.row();
        table_credits.add(tCreditsGraphics1).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGraphics2).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGraphics3).expandX().fillX();
        table_credits.row();
        table_credits.add(tCreditsGraphics4).expandX().fillX();

        table_credits.row();
        table_credits.add(tCreditsTitleStack).expand().fillX().pad(100,100,0,100);

        stage.addActor(table_credits);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        game.batch.begin();
        game.batch.draw(background, 0,0);
        game.batch.end();
        stage.act(delta);
        stage.draw();

        for(int i=0; i<Math.round(Gdx.graphics.getHeight()); i++) {
            if (i%200==0) {
                table_credits.setBounds(0, table_credits.getY()+1, Gdx.graphics.getWidth(), Gdx.graphics.getWidth() / 10 * 3);

                if (table_credits.getY() > Gdx.graphics.getHeight()) {
                    game.setScreen(new MenuScreen(game,fileReader, languageManager));
                    dispose();
                }
            }
        }
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

        languagesList = new ArrayList<>();
        languagesList.add("English");
        languagesList.add("Polski");

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        stage = new Stage();

        fontTitle = new BitmapFont();
        fontText = new BitmapFont();

        parameter.size = 32;
        parameter.color = Color.BLACK;
        parameter.characters = "ąćęłóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        fontTitle = generator.generateFont(parameter);

        parameter.size = 15;
        parameter.color = Color.WHITE;

        fontText = generator.generateFont(parameter);

        textFieldStyle = new TextField.TextFieldStyle();
        textTitleFieldStyle = new TextField.TextFieldStyle();

        text_credits = new TextureAtlas("assets/buttons/text_credits.pack");

        images = new Skin(text_credits);

        table_credits = new Table();

        backgroundMusic = game.getMusic();
    }
}
