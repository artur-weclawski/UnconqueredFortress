package com.game.Screens;

import com.badlogic.gdx.Gdx;
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
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.game.Main;
import com.game.Manager.*;
import com.game.State.GameState;
import org.json.JSONObject;

import java.util.Objects;

public class MenuScreen implements Screen  {
    private Main game;
    private Texture front, clouds, clouds_front, castle, background;
    public TextButton bExit, bLogin ,bPlay, bSettings, bCredits;
    public TextButton bDialogLogin, bDialogLoginRegister, cDialogStayLogged, bDialogExit, bDialogExit2;
    public TextField tDialogLoginTextTitle, tDialogLoginTextLogin, tDialogLoginTextPassword, tDialogLoginStayLogged, tDialogLoginErrors;
    public TextField fDialogLoginLogin, fDialogLoginPassword;
    public TextButton bDialogRegister, bDialogRegisterLogin;
    public TextField tDialogRegisterTextTitle, tDialogRegisterTextLogin, tDialogRegisterTextMail, tDialogRegisterTextPassword, tDialogRegisterTextRepeatPassword, tDialogRegisterErrors;
    public TextField fDialogRegisterLogin, fDialogRegisterMail, fDialogRegisterPassword, fDialogRegisterRepeatPassword;
    public BitmapFont fontTitle, fontText, fontText_error;
    private FreeTypeFontGenerator generator;
    private FreeTypeFontGenerator.FreeTypeFontParameter parameter;
    public TextureAtlas taButtonsAtlas, taButtonsSettings, taButtonsDefault, taDialogBack;
    public Skin images, images_settings, images_default;
    public Stage stage;
    public Table table_bExit, table_bPlay, table_bSettings, table_bLogin, table_bCredits, table_dialogLogin, table_dialogRegister;
    private TextButton.TextButtonStyle textButtonStyle_bDialogExit, textButtonStyle_bExit, textButtonStyle_bPlay, textButtonStyle_bSettings, textButtonStyle_bCredits, textButtonStyle_bLogin, textButtonStyle_bDialogLogin, textButtonStyle_cDialogStayLogged;
    private TextField.TextFieldStyle textFieldStyle, formTextFieldStyle, titleTextFieldStyle, errorTextFieldStyle, textTitleTextFieldStyle;
    private ConnectionManager connectionManager;
    private ButtonStyleManager buttonStyleManager;
    private TextFieldStyleManager textFieldStyleManager;
    private LanguageManager languageManager;
    private FileReader fileReader;
    private Music backgroundMusic;
    private Dialog menuDialog;
    private float cloudsPosition, cloudsPosition_front;
    private float cloudsSpeed, cloudsSpeed_front;
    public boolean stayLogged = false;
    public boolean isDialog = false;
    private String language;

    public MenuScreen(Main game, FileReader fileReader, LanguageManager languageManager){
        this.game = game;

        this.fileReader = fileReader;
        this.languageManager = languageManager;
        this.language = languageManager.getLanguage();
        initSettingsUI();
        buttonStyleManager = new ButtonStyleManager();
        textFieldStyleManager = new TextFieldStyleManager();


        fileReader.downloadUserInfo();
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bLogin, images, fontTitle, "tempmainlog", "tempmainlog");
        if (fileReader.getTokenValue()!=null || game.getIsLogged()) {
            game.setIsLogged(true);
            if (fileReader.getTokenValue()!=null)
                game.setLogin(fileReader.getLoginFromToken());

            bLogin = new TextButton(languageManager.getValue(language, "bLogout"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bLogin));
        } else {
            bLogin = new TextButton(languageManager.getValue(language, "bLogin"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bLogin));
        }

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bExit, images, fontTitle, "tempmain", "tempmain");
        bExit = new TextButton(languageManager.getValue(language, "bExit"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bExit));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bPlay, images, fontTitle, "tempmain", "tempmain");
        bPlay = new TextButton(languageManager.getValue(language, "bPlay"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bPlay));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bSettings, images, fontTitle, "tempmain", "tempmain");
        bSettings = new TextButton(languageManager.getValue(language, "bSettings"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bSettings));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_bCredits, images, fontTitle, "tempmain", "tempmain");
        bCredits = new TextButton(languageManager.getValue(language, "bCredits"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bCredits));

        backgroundMusic.setVolume(fileReader.getVolumeValue()/100);
        backgroundMusic.setLooping(true);
        backgroundMusic.play();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        bExit.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*8+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/10*2, Gdx.graphics.getHeight()/100*7+8);
        //Ustawienie pozycji przycisków, skalowane z wymiarami okna
        table_bLogin.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*2+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/100*16, Gdx.graphics.getHeight()/100*7+8);
        table_bCredits.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*7+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/10*2, Gdx.graphics.getHeight()/100*7+8);
        table_bExit.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*8+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/10*2, Gdx.graphics.getHeight()/100*7+8);
        table_bPlay.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*5+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/10*2, Gdx.graphics.getHeight()/100*7+8);
        table_bSettings.setBounds(Gdx.graphics.getWidth()/10,Gdx.graphics.getHeight() - (Gdx.graphics.getHeight()/10*6+Gdx.graphics.getHeight()/100*7+8),Gdx.graphics.getWidth()/10*2, Gdx.graphics.getHeight()/100*7+8);

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bDialogLogin, images_default, fontText, "defaultButton", "defaultButton");
        bDialogLogin = new TextButton(languageManager.getValue(language, "bLogin"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogLogin));
        bDialogLoginRegister = new TextButton(languageManager.getValue(language, "bRegister"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogLogin));
        bDialogRegister = new TextButton(languageManager.getValue(language, "bRegister"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogLogin));
        bDialogRegisterLogin = new TextButton(languageManager.getValue(language, "bLogin"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogLogin));

        buttonStyleManager.setTextButtonStyle(textButtonStyle_bDialogExit, images_default, fontText, "defaultButton", "defaultButton");
        bDialogExit = new TextButton(languageManager.getValue(language, "bExit"),buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogExit));
        bDialogExit2 = new TextButton(languageManager.getValue(language, "bExit"),buttonStyleManager.returnTextButtonStyle(textButtonStyle_bDialogExit));
        buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogStayLogged, images_settings, fontText, "checkbox_off", "checkbox_off");
        cDialogStayLogged = new TextButton(null, buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogStayLogged));

        textFieldStyleManager.setTextFieldStyleCursor(formTextFieldStyle, images_settings, fontText, "textBar", Color.WHITE);
        textFieldStyleManager.setTextFieldStyle(textFieldStyle, images_settings, fontText, "textBar", Color.WHITE);
        textFieldStyleManager.setTextFieldStyle(titleTextFieldStyle, images_settings, fontTitle, "empty_background", Color.BLACK);
        textFieldStyleManager.setTextFieldStyle(errorTextFieldStyle, images_settings, fontText_error, "textBar", Color.valueOf("b77c7c"));
        textFieldStyleManager.setTextFieldStyle(textTitleTextFieldStyle, images_settings, fontText, "empty_background", Color.WHITE);

        tDialogLoginTextTitle= new TextField(languageManager.getValue(language, "tDialogLoginTitle"), textFieldStyleManager.returnTextFieldStyle(titleTextFieldStyle));
        tDialogLoginStayLogged = new TextField(languageManager.getValue(language, "tDialogLoginStayLogged"), textFieldStyleManager.returnTextFieldStyle(textFieldStyle));
        tDialogLoginStayLogged.setAlignment(Align.center);

        tDialogLoginTextLogin = new TextField(languageManager.getValue(language, "tDialogLoginTextLogin"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));
        tDialogLoginTextPassword = new TextField(languageManager.getValue(language, "tDialogLoginTextPassword"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));

        fDialogLoginLogin = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogLoginLogin.setAlignment(Align.center);
        fDialogLoginPassword = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogLoginPassword.setAlignment(Align.center);
        tDialogLoginErrors = new TextField(null,textFieldStyleManager.returnTextFieldStyle(errorTextFieldStyle));

        tDialogLoginTextTitle.setDisabled(true);
        tDialogLoginTextTitle.setAlignment(Align.center);
        tDialogLoginTextTitle.setSize(256,32);

        tDialogLoginTextLogin.setDisabled(true);
        tDialogLoginTextLogin.setAlignment(Align.center);
        tDialogLoginTextPassword.setDisabled(true);
        tDialogLoginTextPassword.setAlignment(Align.center);

        fDialogLoginPassword.setPasswordMode(true);
        fDialogLoginPassword.setPasswordCharacter('*');

        tDialogLoginErrors.setDisabled(true);
        tDialogLoginErrors.setAlignment(Align.center);

        tDialogRegisterTextTitle = new TextField(languageManager.getValue(language, "tDialogRegisterTitle"), textFieldStyleManager.returnTextFieldStyle(titleTextFieldStyle));
        tDialogRegisterTextLogin = new TextField(languageManager.getValue(language, "tDialogRegisterTextLogin"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));
        tDialogRegisterTextMail = new TextField(languageManager.getValue(language, "tDialogRegisterTextMail"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));
        tDialogRegisterTextPassword = new TextField(languageManager.getValue(language, "tDialogRegisterTextPassword"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));
        tDialogRegisterTextRepeatPassword = new TextField(languageManager.getValue(language, "tDialogRegisterTextRepeatPassword"), textFieldStyleManager.returnTextFieldStyle(textTitleTextFieldStyle));

        fDialogRegisterLogin = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogRegisterLogin.setAlignment(Align.center);
        fDialogRegisterMail = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogRegisterMail.setAlignment(Align.center);
        fDialogRegisterPassword = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogRegisterPassword.setAlignment(Align.center);
        fDialogRegisterRepeatPassword  = new TextField(null, textFieldStyleManager.returnTextFieldStyle(formTextFieldStyle));
        fDialogRegisterRepeatPassword.setAlignment(Align.center);

        tDialogRegisterErrors = new TextField(null,textFieldStyleManager.returnTextFieldStyle(errorTextFieldStyle));
        tDialogRegisterErrors.setAlignment(Align.center);

        tDialogRegisterTextTitle.setDisabled(true);
        tDialogRegisterTextTitle.setAlignment(Align.center);
        tDialogRegisterTextTitle.setHeight(30);
        tDialogRegisterTextLogin.setDisabled(true);
        tDialogRegisterTextLogin.setAlignment(Align.center);
        tDialogRegisterTextMail.setDisabled(true);
        tDialogRegisterTextMail.setAlignment(Align.center);
        tDialogRegisterTextPassword.setDisabled(true);
        tDialogRegisterTextPassword.setAlignment(Align.center);
        tDialogRegisterTextRepeatPassword.setDisabled(true);
        tDialogRegisterTextRepeatPassword.setAlignment(Align.center);

        fDialogRegisterPassword.setPasswordMode(true);
        fDialogRegisterPassword.setPasswordCharacter('*');
        fDialogRegisterRepeatPassword.setPasswordMode(true);
        fDialogRegisterRepeatPassword.setPasswordCharacter('*');

        tDialogRegisterErrors.setDisabled(true);

        Texture loginRegisterDialogBackground = new Texture(new FileHandle("assets/dialog/loginregisterDialog.png"));
        menuDialog = new Dialog("", new Window.WindowStyle(fontTitle, Color.WHITE, new TextureRegionDrawable(new TextureRegion(loginRegisterDialogBackground)))) {
            public void result(Object obj) {
                menuDialog.cancel();
            }
        };

        tDialogLoginErrors.setVisible(false);
        tDialogRegisterErrors.setVisible(false);

        // Login Dialog
        table_dialogLogin.setBounds(0,0,360,420);
        table_dialogLogin.row().colspan(2).width(256).align(Align.center).padBottom(12);
        table_dialogLogin.add(tDialogLoginTextTitle).align(Align.top|Align.center).width(256);
        table_dialogLogin.row().padBottom(12);
        table_dialogLogin.add(tDialogLoginTextLogin).align(Align.center);
        table_dialogLogin.add(fDialogLoginLogin).align(Align.center);
        table_dialogLogin.row().padBottom(12);
        table_dialogLogin.add(tDialogLoginTextPassword).align(Align.center);
        table_dialogLogin.add(fDialogLoginPassword).align(Align.center);
        table_dialogLogin.row().padBottom(12);
        table_dialogLogin.add(cDialogStayLogged);
        table_dialogLogin.add(tDialogLoginStayLogged);
        table_dialogLogin.row().padBottom(12);
        table_dialogLogin.add(tDialogLoginErrors).colspan(2).width(256);
        table_dialogLogin.row().padBottom(12);
        table_dialogLogin.add(bDialogLogin).colspan(2).padBottom(78);
        table_dialogLogin.row();
        table_dialogLogin.add(bDialogExit).width(128).height(32).align(Align.right);
        table_dialogLogin.add(bDialogLoginRegister).width(128).height(32).align(Align.left);

        // Register Dialog
        table_dialogRegister.setBounds(0,0,360,420);
        table_dialogRegister.row().colspan(2).width(320).align(Align.center).padBottom(12);
        table_dialogRegister.add(tDialogRegisterTextTitle).align(Align.center).expandX();
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(tDialogRegisterTextLogin).align(Align.center);
        table_dialogRegister.add(fDialogRegisterLogin).align(Align.center);
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(tDialogRegisterTextMail).align(Align.center);
        table_dialogRegister.add(fDialogRegisterMail).align(Align.center);
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(tDialogRegisterTextPassword).align(Align.center);
        table_dialogRegister.add(fDialogRegisterPassword).align(Align.center);
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(tDialogRegisterTextRepeatPassword).align(Align.center);
        table_dialogRegister.add(fDialogRegisterRepeatPassword).align(Align.center);
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(tDialogRegisterErrors).width(256).colspan(2);
        table_dialogRegister.row().padBottom(12);
        table_dialogRegister.add(bDialogRegister).colspan(2).padBottom(34);
        table_dialogRegister.row();
        table_dialogRegister.add(bDialogExit2).width(128).height(32).align(Align.right);
        table_dialogRegister.add(bDialogRegisterLogin).width(128).height(32).align(Align.left);

        bLogin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){

                if (game.getIsLogged()) {
                    if (fileReader.getTokenValue()!=null){
                        fDialogLoginLogin.setText(fileReader.getLoginFromToken());
                    }

                    fileReader.setUserInfo(null);
                    bLogin.setText(languageManager.getValue(language, "bLogin"));
                    game.setIsLogged(false);
                    return;
                }
                isDialog = true;

                tDialogLoginErrors.setText(null);
                tDialogLoginErrors.setVisible(false);
                menuDialog.removeActor(table_dialogRegister);
                menuDialog.addActor(table_dialogLogin);
                menuDialog.show(stage);
            }
        });

        bExit.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                Gdx.app.exit();
            }
        });

        cDialogStayLogged.addListener(new ClickListener(){

            @Override
            //public void touchUp (InputEvent event, float x, float y, int pointer, int button) {
            public void clicked(InputEvent event, float x, float y){
                stayLogged = !stayLogged;
                if (!stayLogged) {
                    buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogStayLogged, images_settings, fontText, "checkbox_off", "checkbox_off");
                    cDialogStayLogged = new TextButton(languageManager.getValue(language, "tDialogLoginStayLogged"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogStayLogged));
                } else {
                    buttonStyleManager.setTextButtonStyle(textButtonStyle_cDialogStayLogged, images_settings, fontText, "checkbox_on", "checkbox_on");
                    cDialogStayLogged = new TextButton(languageManager.getValue(language, "tDialogLoginStayLogged"), buttonStyleManager.returnTextButtonStyle(textButtonStyle_cDialogStayLogged));
                }
            }
        });

        bDialogLogin.addListener(new ClickListener() {
            public void clicked(InputEvent event, float x, float y) {

                if (fDialogLoginLogin.getText().isEmpty() || fDialogLoginPassword.getText().isEmpty()) {
                    tDialogLoginErrors.setVisible(true);
                    tDialogLoginErrors.setText(languageManager.getValue(language, "ErrorEmptyFields"));
                    return;
                }
                if (fDialogLoginLogin.getText().length() < 5 || fDialogLoginLogin.getText().length() > 20 ) {
                    tDialogLoginErrors.setVisible(true);
                    tDialogLoginErrors.setText(languageManager.getValue(language, "ErrorLengthOfLogin"));
                    return;
                }
                if (fDialogLoginPassword.getText().length() < 5 || fDialogLoginPassword.getText().length() > 20 ) {
                    tDialogLoginErrors.setVisible(true);
                    tDialogLoginErrors.setText(languageManager.getValue(language, "ErrorLengthOfPassword"));
                    return;
                }

                JSONObject loginData = new JSONObject();

                loginData.put("login",fDialogLoginLogin.getText());
                loginData.put("password", fDialogLoginPassword.getText());

                new Thread(() -> {
                    JSONObject response = connectionManager.requestSend(loginData, "api/login");

                    if (response.getInt("status") == 200) {
                        if (stayLogged) {
                            fileReader.setUserInfo(response.getString("token"));
                        } else {
                            fDialogLoginPassword.setText(null);
                        }

                        tDialogLoginErrors.setText(null);
                        tDialogLoginErrors.setVisible(false);
                        bLogin.setText(languageManager.getValue(language, "bLogout"));
                        game.setLogin(fDialogLoginLogin.getText());
                        game.setIsLogged(true);
                        isDialog = false;
                        menuDialog.hide();
                    } else {
                        tDialogLoginErrors.setVisible(true);
                        tDialogLoginErrors.setText(languageManager.getValue(language, response.getString("message")));
                    }

                }).start();
            }
        });

        bDialogExit.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                isDialog = false;
                menuDialog.hide();
            }
        });

        bDialogExit2.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y){
                isDialog = false;
                menuDialog.hide();
            }
        });

        bDialogLoginRegister.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                tDialogLoginErrors.setText(null);
                tDialogLoginErrors.setVisible(false);
                menuDialog.removeActor(table_dialogLogin);
                menuDialog.addActor(table_dialogRegister);
                menuDialog.getButtonTable().clearChildren();
                tDialogRegisterErrors.setVisible(false);
            }
        });

        bDialogRegister.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                if (fDialogRegisterLogin.getText().isEmpty() || fDialogRegisterMail.getText().isEmpty() || fDialogRegisterPassword.getText().isEmpty() || fDialogRegisterRepeatPassword.getText().isEmpty()) {
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, "ErrorEmptyFields"));
                    return;
                }
                if (fDialogRegisterLogin.getText().length() < 5 || fDialogRegisterLogin.getText().length() > 20 ) {
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, "ErrorLengthOfLogin"));
                    return;
                }
                if (!isValidEmailAddress(fDialogRegisterMail.getText())) {
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, "ErrorInvalidMail"));
                    return;
                }
                if (fDialogRegisterPassword.getText().length() < 5 || fDialogRegisterPassword.getText().length() > 20 ) {
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, "ErrorLengthOfPassword"));
                    return;
                }
                if (!Objects.equals(fDialogRegisterPassword.getText(), fDialogRegisterRepeatPassword.getText())) {
                    System.out.println(fDialogRegisterPassword.getText());
                    System.out.println(fDialogRegisterRepeatPassword.getText());
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, "ErrorRepeatPasswordDifferent"));
                    return;
                }

                JSONObject RegisterData = new JSONObject();

                RegisterData.put("login", fDialogRegisterLogin.getText());
                RegisterData.put("mail", fDialogRegisterMail.getText());
                RegisterData.put("password",fDialogRegisterPassword.getText());

                new Thread(() -> {
                    JSONObject response = connectionManager.requestSend(RegisterData, "api/register");

                if (response.getInt("status") == 200) {
                    fDialogRegisterLogin.setText(null);
                    fDialogRegisterMail.setText(null);
                    fDialogRegisterPassword.setText(null);
                    fDialogRegisterRepeatPassword.setText(null);
                    tDialogRegisterErrors.setText(null);
                    tDialogRegisterErrors.setVisible(false);
                    isDialog = false;
                    menuDialog.hide();
                } else {
                    tDialogRegisterErrors.setVisible(true);
                    tDialogRegisterErrors.setText(languageManager.getValue(language, response.getString("message")));
                }
                }).start();
            }
        });

        bDialogRegisterLogin.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                tDialogRegisterErrors.setText(null);
                tDialogRegisterErrors.setVisible(false);
                menuDialog.removeActor(table_dialogRegister);
                menuDialog.addActor(table_dialogLogin);
                menuDialog.getButtonTable().clearChildren();
                tDialogLoginErrors.setVisible(false);
            }
        });

        bPlay.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                GameState.setPreviousGameState(GameState.MENU);
                game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager));
                dispose();
            }
        });

        bSettings.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                GameState.setPreviousGameState(GameState.MENU);
                game.setScreen(new SettingsScreen(game, fileReader, languageManager));
                dispose();
            }
        });


        bCredits.addListener(new ClickListener(){
            @Override
            public void clicked(InputEvent event, float x, float y){
                GameState.setPreviousGameState(GameState.MENU);
                game.setScreen(new CreditsScreen(game, fileReader, languageManager));
                backgroundMusic.dispose();
                dispose();
            }
        });

        table_bExit.add(bExit).width(Gdx.graphics.getWidth()/10*2).height(Gdx.graphics.getHeight()/100*7+8);
        table_bPlay.add(bPlay).width(Gdx.graphics.getWidth()/10*2).height(Gdx.graphics.getHeight()/100*7+8);
        table_bLogin.add(bLogin).width(Gdx.graphics.getWidth()/100*16).height(Gdx.graphics.getHeight()/100*7+8);
        table_bSettings.add(bSettings).width(Gdx.graphics.getWidth()/10*2).height(Gdx.graphics.getHeight()/100*7+8);
        table_bCredits.add(bCredits).width(Gdx.graphics.getWidth()/10*2).height(Gdx.graphics.getHeight()/100*7+8);

        stage.addActor(table_bCredits);
        stage.addActor(table_bLogin);
        stage.addActor(table_bExit);
        stage.addActor(table_bLogin);
        stage.addActor(table_bSettings);
        stage.addActor(table_bPlay);
    }

    private boolean isValidEmailAddress(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
        java.util.regex.Matcher m = p.matcher(email);
        return m.matches();
    }

    @Override
    public void render(float delta) {
            Gdx.gl.glClearColor(1,1,1,1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            cloudsPosition -= delta*cloudsSpeed;
            cloudsPosition_front -= delta*cloudsSpeed_front;

            if (cloudsPosition <= -3840)
                cloudsPosition = 0;

            if(cloudsPosition_front <= -3840)
                cloudsPosition_front = 0;

            game.batch.begin();
            game.batch.draw(background, 0,0);
            game.batch.draw(clouds, cloudsPosition,0);
            game.batch.draw(castle, 0,0);
            game.batch.draw(clouds_front, cloudsPosition_front, 0);
            game.batch.draw(front, 0,0);

            if(isDialog) {
                bPlay.setTouchable(Touchable.disabled);
                bLogin.setTouchable(Touchable.disabled);
                bSettings.setTouchable(Touchable.disabled);
                bCredits.setTouchable(Touchable.disabled);
                bExit.setTouchable(Touchable.disabled);
            } else {
                bPlay.setTouchable(Touchable.enabled);
                bLogin.setTouchable(Touchable.enabled);
                bSettings.setTouchable(Touchable.enabled);
                bCredits.setTouchable(Touchable.enabled);
                bExit.setTouchable(Touchable.enabled);
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

    private void initSettingsUI(){
        cloudsPosition = 0;
        cloudsSpeed = 30;
        cloudsSpeed_front = 60;

        background = new Texture("backgrounds/menu/back.png");
        front = new Texture("backgrounds/menu/front.png");
        clouds = new Texture("backgrounds/menu/clouds.png");
        clouds_front = new Texture("backgrounds/menu/clouds_front.png");
        castle = new Texture("backgrounds/menu/castle.png");

        generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
        parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        stage = new Stage();

        fontTitle = new BitmapFont();
        fontText = new BitmapFont();

        parameter.size = (int) (20 * ((Gdx.graphics.getWidth() / 1280.0)));
        parameter.color = Color.valueOf("938672");
        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

        fontTitle = generator.generateFont(parameter);

        parameter.size = (int) (10* ((Gdx.graphics.getWidth() / 1280.0)));
        parameter.color = Color.WHITE;

        fontText = generator.generateFont(parameter);

        parameter.size = 10;
        parameter.color = Color.WHITE;

        fontText_error = generator.generateFont(parameter);

        connectionManager = new ConnectionManager();

        taButtonsAtlas = new TextureAtlas("assets/buttons/buttons_menu.pack");
        taButtonsDefault = new TextureAtlas("assets/buttons/buttons_default.pack");
        taButtonsSettings = new TextureAtlas("assets/buttons/buttons_settings.pack");
        taDialogBack = new TextureAtlas("assets/dialog/skin_dialog.pack");

        images = new Skin(taButtonsAtlas);
        images_settings = new Skin(taButtonsSettings);
        images_default = new Skin(taButtonsDefault);

        table_bExit = new Table();
        table_bPlay = new Table();
        table_bLogin = new Table();
        table_bCredits = new Table();
        table_bSettings = new Table();
        table_dialogLogin = new Table();
        table_dialogRegister = new Table();

        textFieldStyle = new TextField.TextFieldStyle();
        formTextFieldStyle = new TextField.TextFieldStyle();
        errorTextFieldStyle = new TextField.TextFieldStyle();
        titleTextFieldStyle = new TextField.TextFieldStyle();
        textTitleTextFieldStyle = new TextField.TextFieldStyle();

        textButtonStyle_bExit = new TextButton.TextButtonStyle();
        textButtonStyle_bPlay = new TextButton.TextButtonStyle();
        textButtonStyle_bSettings = new TextButton.TextButtonStyle();
        textButtonStyle_bCredits = new TextButton.TextButtonStyle();
        textButtonStyle_bLogin = new TextButton.TextButtonStyle();
        textButtonStyle_bDialogLogin = new TextButton.TextButtonStyle();
        textButtonStyle_cDialogStayLogged = new TextButton.TextButtonStyle();
        textButtonStyle_bDialogExit =  new TextButton.TextButtonStyle();

        backgroundMusic = game.getMusic();
    }
}
