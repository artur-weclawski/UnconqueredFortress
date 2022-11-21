package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.game.Entity.Base;
import com.game.Entity.Upgrade;
import com.game.Main;
import com.game.Manager.*;
import com.game.Screens.*;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;

import java.awt.*;
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
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        ProfileLocalScreen profileLocalScreen = new ProfileLocalScreen(game, fileReader, languageManager);
                        game.setScreen(profileLocalScreen);

                        try {
                            Field field = game.getScreen().getClass().getDeclaredField("bOtherScreen");
                            field.setAccessible(true);
                            TextButton button = (TextButton) field.get(profileLocalScreen);

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
                    Thread.sleep(300);
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

                            Method render = game.getScreen().getClass().getDeclaredMethod("render", float.class);
                            render.setAccessible(true);

                            //Before
                            Assertions.assertTrue(base.getHealth()>0);
                            Assertions.assertEquals("Running",base.getState().toString());

                            base.setHealth(0);
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

    @Test
    public void WaveBlocksSavingGameTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen gameScreen = new GameScreen(game, newSave,true, fileReader, languageManager);
                        game.setScreen(gameScreen);

                        try {
                            Field fieldBase = game.getScreen().getClass().getDeclaredField("base");
                            fieldBase.setAccessible(true);
                            Base base = (Base) fieldBase.get(gameScreen);

                            Field fieldEnemyManager = game.getScreen().getClass().getDeclaredField("enemyManager");
                            fieldEnemyManager.setAccessible(true);
                            EnemyManager enemyManager = (EnemyManager) fieldEnemyManager.get(gameScreen);

                            Method render = game.getScreen().getClass().getDeclaredMethod("render", float.class);
                            render.setAccessible(true);

                            Method tryToSave = game.getScreen().getClass().getDeclaredMethod("tryToSave");
                            tryToSave.setAccessible(true);

                            Assertions.assertTrue((Boolean) tryToSave.invoke(gameScreen));

                            base.increaseWave(1);
                            enemyManager.createRandomEnemyWave();

                            render.invoke(gameScreen, Gdx.graphics.getDeltaTime());
                            Assertions.assertFalse((Boolean) tryToSave.invoke(gameScreen));

                        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e ) {
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
    public void IsMailValidatorWorkingCorrectlyTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        MenuScreen menuScreen = new MenuScreen(game, fileReader, languageManager);
                        game.setScreen(menuScreen);

                        try {
                            Method validateMail = game.getScreen().getClass().getDeclaredMethod("isValidEmailAddress", String.class);
                            validateMail.setAccessible(true);

                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"simple@example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"very.common@example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"disposable.style.email.with+symbol@example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"test/test@test.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"x@example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"user.name+tag+sorting@example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"example-indeed@strange-example.com"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"example@s.example"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"postmaster@[123.123.123.123]"));
                            Assertions.assertTrue((Boolean) validateMail.invoke(menuScreen,"mailhost!username@example.org"));

                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"Abc.example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"A@b@c@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"a\"b(c)d,e:f;g<h>i[j\\k]l@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"just\"not\"right@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"this is\"not\\allowed@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"this\\ still\\\"not\\\\allowed@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"1234567890123456789012345678901234567890123456789012345678901234+x@example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"i_like_underscore@but_its_not_allowed_in_this_part.example.com"));
                            Assertions.assertFalse((Boolean) validateMail.invoke(menuScreen,"QA[icon]CHOCOLATE[icon]@test.com"));



                        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
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
    public void CorrectlyClickableTowerInShopTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        GameScreen gameScreen = new GameScreen(game, newSave,true, fileReader, languageManager);
                        game.setScreen(gameScreen);

                        try {
                            Field fieldBase = game.getScreen().getClass().getDeclaredField("base");
                            fieldBase.setAccessible(true);
                            Base base = (Base) fieldBase.get(gameScreen);

                            Field fieldOperationsArr = game.getScreen().getClass().getDeclaredField("operationsArr");
                            fieldOperationsArr.setAccessible(true);
                            Image[][] operationsArr = (Image[][]) fieldOperationsArr.get(gameScreen);

                            Field fieldUpgradeManager = game.getScreen().getClass().getDeclaredField("upgradeManager");
                            fieldUpgradeManager.setAccessible(true);
                            UpgradeManager upgradeManager = (UpgradeManager) fieldUpgradeManager.get(gameScreen);

                            Method render = game.getScreen().getClass().getDeclaredMethod("render", float.class);
                            render.setAccessible(true);

                            Field fieldCannon = upgradeManager.getClass().getDeclaredField("uCannon");
                            fieldCannon.setAccessible(true);
                            Upgrade cannon = (Upgrade) fieldCannon.get(upgradeManager);

                            Assertions.assertEquals("cannon",operationsArr[1][1].getName());
                            Assertions.assertFalse(operationsArr[1][1].isTouchable());

                            cannon.unlock(null);
                            base.setPassiveUpgrade(cannon.getUpgrade(), false);
                            cannon.levelUp();

                            Assertions.assertTrue(operationsArr[1][1].isTouchable());


                        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException e ) {
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



}
