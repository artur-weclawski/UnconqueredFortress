package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Entity.Base;

public class TipsManager {
    private LanguageManager languageManager;
    private BitmapFont font, bigFont;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    private Base base;
    private float scale;
    private TextureAtlas taTips, taTips2;
    private Skin images_tips;
    private ButtonStyleManager buttonStyleManager;
    private TextButton bMechanics, bEnemies, bTowers, bBack;
    private TextButton.TextButtonStyle textButtonStyleMechanics, textButtonStyleEnemies, textButtonStyleTowers, textButtonStyleBack, textButtonStyleMechanicsChosen, textButtonStyleEnemiesChosen, textButtonStyleTowersChosen;
    private Table table_mechanics, table_enemies, table_towers, table_buttons;
    private Dialog tipsDialog;
    private Pixmap scaledPixmap, pixmap;
    private Label.LabelStyle labelStyle, bigLabelStyle;
    private String language;

    public TipsManager(LanguageManager languageManager, BitmapFont font, Base base, float scale){
        this.languageManager = languageManager;
        this.font = font;
        this.base = base;
        this.scale = scale;
        this.language = languageManager.getLanguage();
        buttonStyleManager = new ButtonStyleManager();
        initSettingUI();

        createTipsButtonTable();
        initListeners();
        createTipsMechanicsTable();
        createTipsEnemiesTable();
        createTipsTowersTable();
    }
    public void createTipsDialog(){

        pixmap = new Pixmap(new FileHandle("assets/backgrounds/tempBackground.png"));
        scaledPixmap = new Pixmap((int) (1920*scale/1.5f), (int) (1080*scale/1.5f), pixmap.getFormat());
        scaledPixmap.drawPixmap(
                pixmap,
                0, 0, pixmap.getWidth(), pixmap.getHeight(),
                0, 0, scaledPixmap.getWidth(), scaledPixmap.getHeight()
        );
        Texture dialogBg = new Texture(scaledPixmap);
        this.tipsDialog = new Dialog("", new Window.WindowStyle(font, Color.WHITE, new TextureRegionDrawable(new TextureRegion(dialogBg))));
        tipsDialog.setBounds(0,0,720*scale, 1280*scale);
        tipsDialog.clear();
        tipsDialog.row().colspan(2);
        tipsDialog.clear();
        tipsDialog.add(returnTipsButtonsTable());
        tipsDialog.row();
        tipsDialog.add(returnTipsMechanicsTable());
        setChosenButton("bMechanics");
    }
    public Dialog returnTipsDialog(){
        return  tipsDialog;
    };
    public void createTipsButtonTable(){
        this.table_buttons = new Table();

        bMechanics = new TextButton(languageManager.getValue(language, "bMechanics"), buttonStyleManager.returnTextButtonStyle(textButtonStyleMechanics));
        bEnemies = new TextButton(languageManager.getValue(language, "bEnemies"), buttonStyleManager.returnTextButtonStyle(textButtonStyleEnemies));
        bTowers = new TextButton(languageManager.getValue(language, "bTowers"), buttonStyleManager.returnTextButtonStyle(textButtonStyleTowers));
        bBack = new TextButton(languageManager.getValue(language, "bBackTips"), buttonStyleManager.returnTextButtonStyle(textButtonStyleBack));
        bMechanics.getLabel();
        table_buttons.setBounds(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/10+28*scale);
        table_buttons.add(bMechanics).height(Gdx.graphics.getHeight()/10+28*scale);
        table_buttons.add(bEnemies).height(Gdx.graphics.getHeight()/10+28*scale);
        table_buttons.add(bTowers).height(Gdx.graphics.getHeight()/10+28*scale);
        table_buttons.add(bBack).height(Gdx.graphics.getHeight()/10+28*scale);

    }
    public void createTipsMechanicsTable(){
        this.table_mechanics = new Table();
        table_mechanics.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/10*8);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tDifficulty"), bigLabelStyle)).width(Gdx.graphics.getWidth()).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tDifficultyDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).height(4*scale).padLeft(4*scale).padBottom(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tCurrency"), bigLabelStyle)).width(Gdx.graphics.getWidth()).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tGoldDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tDiamondsDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).height(4*scale).padLeft(4*scale).padBottom(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tShop"), bigLabelStyle)).width(Gdx.graphics.getWidth()).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tShopDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).height(4*scale).padLeft(4*scale).padBottom(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tLuck"), bigLabelStyle)).width(Gdx.graphics.getWidth()).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tLuckDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).height(4*scale).padLeft(4*scale).padBottom(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tEvent"), bigLabelStyle)).width(Gdx.graphics.getWidth()).padLeft(4*scale);
        table_mechanics.row();
        table_mechanics.add(new Label(languageManager.getValue(language, "tEventDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).padLeft(4*scale);
        if(Gdx.graphics.getHeight()==900) {
            table_mechanics.padBottom(32);
        } else if(Gdx.graphics.getHeight()==1080) {
            table_mechanics.padBottom(40);
        }
    }

    public void createTipsEnemiesTable(){
        this.table_enemies = new Table();

        table_enemies.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/10*8);
        table_enemies.columnDefaults(6);
        table_enemies.row();
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_enemies.row();
        table_enemies.add(new Label(languageManager.getValue(language, "tEnemies"), bigLabelStyle)).width(Gdx.graphics.getWidth()).colspan(6).padLeft(8*scale);
        table_enemies.row();
        table_enemies.add(new Label(languageManager.getValue(language, "tEnemiesDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).colspan(6).padLeft(8*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "separator")).width(Gdx.graphics.getWidth()).colspan(6).height(18*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "warrior")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_enemies.add(new Label(languageManager.getValue(language, "tWarrior"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_enemies.row();
        table_enemies.add(new Label(languageManager.getValue(language, "tAssassin"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_enemies.add(new Image(images_tips, "assassin")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "objectSeparatorRight")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "incubus")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_enemies.add(new Label(languageManager.getValue(language, "tIncubus"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_enemies.row();
        table_enemies.add(new Label(languageManager.getValue(language, "tBlob"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_enemies.add(new Image(images_tips, "blob")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "objectSeparatorRight")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "summoner")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_enemies.add(new Label(languageManager.getValue(language, "tSummoner"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_enemies.row();
        table_enemies.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_enemies.row();
        table_enemies.add(new Label(languageManager.getValue(language, "tGladiator"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_enemies.add(new Image(images_tips, "gladiator")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
    }

    public void createTipsTowersTable(){
        this.table_towers = new Table();

        table_towers.setBounds(0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight()/10*8);
        table_towers.columnDefaults(6);
        table_towers.row();
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.add(new Label("", bigLabelStyle)).height(0).colspan(1);
        table_towers.row();
        table_towers.add(new Label(languageManager.getValue(language, "tTowers"), bigLabelStyle)).width(Gdx.graphics.getWidth()).colspan(6).padLeft(8*scale);
        table_towers.row();
        table_towers.add(new Label(languageManager.getValue(language, "tTowersDescription"), labelStyle)).width(Gdx.graphics.getWidth()).height(Gdx.graphics.getHeight()/10*2-64*scale).colspan(6).padLeft(8*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "separator")).width(Gdx.graphics.getWidth()).colspan(6).height(18*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "melee")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_towers.add(new Label(languageManager.getValue(language, "tMelee"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_towers.row();
        table_towers.add(new Label(languageManager.getValue(language, "tCrossbow"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_towers.add(new Image(images_tips, "crossbow")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
        table_towers.row();
        table_towers.add(new Image(images_tips, "objectSeparatorRight")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "mage")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_towers.add(new Label(languageManager.getValue(language, "tMage"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_towers.row();
        table_towers.add(new Label(languageManager.getValue(language, "tCannon"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_towers.add(new Image(images_tips, "cannon")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
        table_towers.row();
        table_towers.add(new Image(images_tips, "objectSeparatorRight")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "sticky")).width(80*scale).height(80*scale).align(Align.left).colspan(1).padLeft(16);
        table_towers.add(new Label(languageManager.getValue(language, "tSticky"), labelStyle)).height(80*scale).align(Align.left).colspan(5).expandX().padLeft(30*scale);
        table_towers.row();
        table_towers.add(new Image(images_tips, "objectSeparatorLeft")).width(Gdx.graphics.getWidth()).colspan(6).height(4*scale);
        table_towers.row();
        table_towers.add(new Label(languageManager.getValue(language, "tNeedles"), labelStyle)).height(80*scale).align(Align.right).colspan(5).expandX().padRight(-24*scale);
        table_towers.add(new Image(images_tips, "needles")).width(80*scale).height(80*scale).align(Align.right).colspan(1).padLeft(-512*scale).padRight(16);
    }

    public Table returnTipsButtonsTable(){
        return  table_buttons;
    }
    public Table returnTipsMechanicsTable(){
        return  table_mechanics;
    }
    public Table returnTipsEnemiesTable(){
        return  table_enemies;
    }
    public Table returnTipsTowersTable(){
        return  table_towers;
    }

    public void setChosenButton(String name){
        switch (name){
            case "bMechanics":{
                bMechanics.setStyle(textButtonStyleMechanicsChosen);
                bEnemies.setStyle(textButtonStyleEnemies);
                bTowers.setStyle(textButtonStyleTowers);
            }break;
            case "bEnemies":{
                bMechanics.setStyle(textButtonStyleMechanics);
                bEnemies.setStyle(textButtonStyleEnemiesChosen);
                bTowers.setStyle(textButtonStyleTowers);
            }break;
            case "bTowers":{
                bMechanics.setStyle(textButtonStyleMechanics);
                bEnemies.setStyle(textButtonStyleEnemies);
                bTowers.setStyle(textButtonStyleTowersChosen);
            }break;
        }
    }

    public void initSettingUI(){
        taTips = new TextureAtlas("assets/buttons/buttons_default.pack");
        images_tips = new Skin(taTips);

        taTips2 = new TextureAtlas("assets/icons/tipsImages.pack");
        images_tips = new Skin(taTips2);

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = 20;
        parameter.color = Color.WHITE;
        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        bigFont = new BitmapFont();
        bigFont = generator.generateFont(parameter);

        textButtonStyleMechanics = new TextButton.TextButtonStyle();
        textButtonStyleBack = new TextButton.TextButtonStyle();
        textButtonStyleEnemies = new TextButton.TextButtonStyle();
        textButtonStyleTowers = new TextButton.TextButtonStyle();
        textButtonStyleMechanicsChosen = new TextButton.TextButtonStyle();
        textButtonStyleEnemiesChosen = new TextButton.TextButtonStyle();
        textButtonStyleTowersChosen = new TextButton.TextButtonStyle();

        buttonStyleManager.setTextButtonStyle(textButtonStyleMechanics, images_tips, font, "mechanics_up", "mechanics_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleEnemies, images_tips, font, "enemies_up", "enemies_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleTowers, images_tips, font, "towers_up", "towers_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleBack, images_tips, font, "back_up", "back_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleMechanicsChosen, images_tips, font, "mechanics_down", "mechanics_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleEnemiesChosen, images_tips, font, "enemies_down", "enemies_down");
        buttonStyleManager.setTextButtonStyle(textButtonStyleTowersChosen, images_tips, font, "towers_down", "towers_down");

        labelStyle = new Label.LabelStyle(font, Color.WHITE);
        bigLabelStyle = new Label.LabelStyle(bigFont, Color.WHITE);
    }

    public void initListeners(){
        bMechanics.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tipsDialog.clear();
                tipsDialog.add(returnTipsButtonsTable());
                tipsDialog.row();
                tipsDialog.add(returnTipsMechanicsTable());
                setChosenButton("bMechanics");
            }
        });
        bEnemies.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tipsDialog.clear();
                tipsDialog.add(returnTipsButtonsTable());
                tipsDialog.row();
                tipsDialog.add(returnTipsEnemiesTable());
                setChosenButton("bEnemies");
            }
        });
        bTowers.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                tipsDialog.clear();
                tipsDialog.add(returnTipsButtonsTable());
                tipsDialog.row();
                tipsDialog.add(returnTipsTowersTable());
                setChosenButton("bTowers");
            }
        });
        bBack.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pixmap.dispose();
                scaledPixmap.dispose();
                tipsDialog.hide();
                tipsDialog.remove();
                base.setState(Base.State.Resumed);
            }
        });
    }
}
