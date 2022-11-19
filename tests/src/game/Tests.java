package game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.Entity.Base;
import com.game.Entity.Enemy.Enemy;
import com.game.Manager.*;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

@RunWith(HeadlessLauncher.class)
public class Tests {

    @Test
    public void equalWorldsWithSameSeedTest()
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
    public void seedInputTest(){
        ProfileManager profileManager = new ProfileManager();
        Assertions.assertEquals(10, profileManager.stringToSeed("10"));
        Assertions.assertEquals(-10, profileManager.stringToSeed("-10"));
        Assertions.assertEquals(1954875433, profileManager.stringToSeed("Abs209"));
        Assertions.assertEquals(1507332, profileManager.stringToSeed("10-2"));
    }

    @Test
    public void createAndDeleteSaveTest() {
        TestFileReader testFileReader = new TestFileReader();
        ProfileManager profileManager = new ProfileManager();

        testFileReader.setSave(profileManager.createEmptySave("normal", 99, ""));
        if (!testFileReader.fileExists("../save/save099l.json")) {
            Assertions.fail("Plik nie został zapisany.");
        }

        testFileReader.deleteSave(99);
        if (testFileReader.fileExists("../save/save099l.json")) {
            Assertions.fail("Plik nie został usunięty.");
        }
    }


}
