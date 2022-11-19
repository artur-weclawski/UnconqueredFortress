package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.Entity.Base;
import com.game.Main;
import com.game.Manager.*;
import com.game.Screens.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ScreenTests {

    private LanguageManager languageManager;
    private FileReader fileReader;
    private Main game;
    private Lwjgl3ApplicationConfiguration config;
    private JSONObject newSave;
    private ProfileManager profileManager;
    @Before
    public void initTests()
    {
        config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        game = new Main();
        fileReader = new FileReader();
        languageManager = new LanguageManager("English");
        profileManager = new ProfileManager();
        newSave = profileManager.createEmptySave("normal",1,"");
    }


    @Test
    public void ButtonToCloudSavesCorrectlyVisibleTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager) );

                        try {
                            Field field = game.getScreen().getClass().getDeclaredField("bOtherScreen");
                            field.setAccessible(true);
                            TextButton button = (TextButton) field.get(new ProfileLocalScreen(game, fileReader, languageManager));

                            Assert.assertEquals(game.getIsLogged(), button.isVisible());

                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }

                        Gdx.app.exit();
                    }
                });
            }
        }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }


    @Test
    public void NonPositiveHpChangesStateToGameOverTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen gameScreen = new GameScreen(game, newSave,true, fileReader, languageManager);
                        game.setScreen(gameScreen);

                        try {
                            Field field = game.getScreen().getClass().getDeclaredField("base");
                            field.setAccessible(true);
                            Base base = (Base) field.get(gameScreen);

                            //Before
                            Assertions.assertTrue(base.getHealth()>0);
                            Assertions.assertEquals("Running",base.getState().toString());

                            base.setHealth(0);
                            Method render = game.getScreen().getClass().getDeclaredMethod("render", float.class);
                            render.setAccessible(true);
                            render.invoke(gameScreen, Gdx.graphics.getDeltaTime());

                            //After
                            Assertions.assertTrue(base.getHealth()<=0);
                            Assertions.assertEquals("GameOver",base.getState().toString());



                        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                            e.printStackTrace();
                        }

                        Gdx.app.exit();

                    }
                });
            }
        }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }


    /*
    @Test
    public void ButtonToCloudSavesCorrectlyVisibleTest()  {
    new Thread(new Runnable() {
        @Override
        public void run() {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Gdx.app.postRunnable(new Runnable() {
                @Override
                public void run() {
                    game.setScreen(new ProfileLocalScreen(game, fileReader, languageManager) );
                    System.out.println( game.getScreen());
                        //game.getScreen().getClass().getDeclaredFields();
                        //game.getScreen().getClass().getField("bOtherScreen");
                        //System.out.println(Arrays.toString(game.getScreen().getClass().getFields()));
                        //System.out.println("Pauza");

                    //TextButton s = FieldUtils.getFieldValue
                    //Field f= FieldUtils.getDeclaredField(game.getScreen().getClass(), "bOtherScreen", true);
                    //f.setAccessible(true);
                    //TextButton t = new TextButton();
                    try {
                        TextButton tt = (TextButton) f.getClass().cast(TextButton t);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                    Field field = null;
                    try {
                        field = game.getScreen().getClass().getDeclaredField("bOtherScreen");
                        field.setAccessible(true);
                        Skin image_profiles = new Skin( new TextureAtlas("assets/buttons/buttons_profile.pack"));
                        TextButton.TextButtonStyle textButtonStyle_bNext = new TextButton.TextButtonStyle();

                        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Silkscreen.ttf"));
                        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
                        parameter.size = 13;
                        parameter.color = Color.WHITE;
                        parameter.characters = "ąćęłńóśżźabcdefghijklmnopqrstuvwxyzĄĆĘÓŁŃŚŻŹABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;:,{}\"´`'<>";

                        BitmapFont font = new BitmapFont();
                        font = generator.generateFont(parameter);


                        ButtonStyleManager buttonStyleManager = new ButtonStyleManager();
                        buttonStyleManager.setTextButtonStyle(textButtonStyle_bNext, image_profiles, font, "next_screen_button", "next_screen_button");
                        TextButton t = new TextButton("", buttonStyleManager.returnTextButtonStyle(textButtonStyle_bNext));

                        Object value = field.get(t);
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                    }



                    //System.out.println(Arrays.toString(game.getScreen().getClass().getDeclaredFields()));
                    try {
                        //TextButton t =  game.getScreen().getClass().getDeclaredField("bOtherScreen");
                        System.out.println(game.getScreen().getClass().getDeclaredField("bOtherScreen"));
                        TextButton t = (TextButton) game.getScreen().getClass().getDeclaredField("bOtherScreen").;
                        System.out.println(t.isVisible());
                    } catch (NoSuchFieldException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    //System.out.println(Arrays.toString(game.getScreen().getClass().getDeclaredFields()));



                    Gdx.app.exit();
                }
            });
        }
    }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }

     */

/*
    @Test
    public void tt() throws InterruptedException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        game.setScreen(new CreditsScreen(game, fileReader, languageManager) );
                        System.out.println( game.getScreen());
                        Gdx.app.exit();
                    }
                });
            }
        }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }*/

}
