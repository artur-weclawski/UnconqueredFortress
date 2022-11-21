package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.Entity.Base;
import com.game.Entity.Enemy.Enemy;
import com.game.Main;
import com.game.Manager.*;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@RunWith(HeadlessLauncher.class)
public class Tests {


    @Test
    public void EqualWorldsWithSameSeedTest()
    {
        WorldManager worldManager;

        Random random = new Random();
        int seed = random.nextInt();

        worldManager = new WorldManager();
        Image[][] a = worldManager.createWorld(null, seed, 46);
        worldManager = new WorldManager();
        Image[][] b = worldManager.createWorld(null, seed, 46);

        for (int i=0; i<10; i++)
            for (int j = 0; j < 15; j++)
                if (!Objects.equals(a[i][j].getName(), b[i][j].getName()))
                    Assertions.fail("Swiaty nie są takie same.");

    }



    @Test
    public void BossWaveEveryTenWavesTest()
    {
        ProfileManager profileManager = new ProfileManager();
        Base base = new Base(profileManager.createEmptySave("normal",1,""), null);

        FileReader fileReader = new FileReader();
        JSONObject enemiesJSONObject = fileReader.downloadFileAsJSONObject("assets/data/enemies.json");

        EnemyManager enemyManager = new EnemyManager(base, 1, null, enemiesJSONObject);

        for (int i=0; i<10; i++) {
            boolean hasBoss = false;

            base.increaseWave(10);
            enemyManager.createRandomEnemyWave();
            ArrayList<Enemy> enemies = enemyManager.getLastCreatedWave();

            for (Enemy e: enemies)
            {
                if (Objects.equals(e.getName(), "boss"))
                {

                 hasBoss=true;
                 break;
                }
            }

            if (!hasBoss)
            {
                Assertions.fail("Fala nie posiada bossa.");
            }
        }
    }


    @Test
    public void SeedInputTest(){
        ProfileManager profileManager = new ProfileManager();
        Assertions.assertEquals(10, profileManager.stringToSeed("10"));
        Assertions.assertEquals(-10, profileManager.stringToSeed("-10"));
        Assertions.assertEquals(1954875433, profileManager.stringToSeed("Abs209"));
        Assertions.assertEquals(1507332, profileManager.stringToSeed("10-2"));
    }

    @Test
    public void CreateAndDeleteSaveTest() {
        FileReader fileReader = new FileReader();
        ProfileManager profileManager = new ProfileManager();

        fileReader.setSave(profileManager.createEmptySave("normal", 99, ""));
        if (!fileReader.fileExists("save/save099l.json")) {
            Assertions.fail("Plik nie został zapisany.");
        }

        fileReader.deleteSave(99);
        if (fileReader.fileExists("save/save099l.json")) {
            Assertions.fail("Plik nie został usunięty.");
        }
    }


    /*


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
    public void EqualWorldsWithSameSeedTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        WorldManager worldManager;

                        Random random = new Random();
                        int seed = random.nextInt();

                        worldManager = new WorldManager();
                        Image[][] a = worldManager.createWorld(null, seed, 46);
                        worldManager = new WorldManager();
                        Image[][] b = worldManager.createWorld(null, seed, 46);

                        for (int i=0; i<10; i++)
                            for (int j = 0; j < 15; j++)
                                if (!Objects.equals(a[i][j].getName(), b[i][j].getName()))
                                    Assertions.fail("Swiaty nie są takie same.");

                        Gdx.app.exit();

                    }
                });
            }
        }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }

    @Test
    public void BossWaveEveryTenWavesTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        ProfileManager profileManager = new ProfileManager();
                        Base base = new Base(profileManager.createEmptySave("normal",1,""), null);

                        FileReader fileReader = new FileReader();
                        JSONObject enemiesJSONObject = fileReader.downloadFileAsJSONObject("assets/data/enemies.json");

                        EnemyManager enemyManager = new EnemyManager(base, 1, null, enemiesJSONObject);

                        for (int i=0; i<10; i++) {
                            boolean hasBoss = false;

                            base.increaseWave(10);
                            enemyManager.createRandomEnemyWave();
                            ArrayList<Enemy> enemies = enemyManager.getLastCreatedWave();

                            for (Enemy e: enemies)
                            {
                                if (Objects.equals(e.getName(), "boss"))
                                {

                                    hasBoss=true;
                                    break;
                                }
                            }

                            if (!hasBoss)
                            {
                                Assertions.fail("Fala nie posiada bossa.");
                            }
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
    public void SeedInputTest(){
        ProfileManager profileManager = new ProfileManager();
        Assertions.assertEquals(10, profileManager.stringToSeed("10"));
        Assertions.assertEquals(-10, profileManager.stringToSeed("-10"));
        Assertions.assertEquals(1954875433, profileManager.stringToSeed("Abs209"));
        Assertions.assertEquals(1507332, profileManager.stringToSeed("10-2"));
    }

    @Test
    public void CreateAndDeleteSaveTest()  {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                Gdx.app.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        FileReader fileReader = new FileReader();
                        ProfileManager profileManager = new ProfileManager();

                        fileReader.setSave(profileManager.createEmptySave("normal", 99, ""));
                        if (!fileReader.fileExists("save/save099l.json")) {
                            Assertions.fail("Plik nie został zapisany.");
                        }

                        fileReader.deleteSave(99);
                        if (fileReader.fileExists("save/save099l.json")) {
                            Assertions.fail("Plik nie został usunięty.");
                        }

                        Gdx.app.exit();

                    }
                });
            }
        }).start();

        new Lwjgl3Application(game, config);
        fileReader.downloadSettings();
    }

    */
}
