package game;

import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.game.Entity.Base;
import com.game.Entity.Enemy.Enemy;
import com.game.Manager.EnemyManager;
import com.game.Manager.FileReader;
import com.game.Manager.ProfileManager;
import com.game.Manager.WorldManager;
import org.json.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.jupiter.api.Assertions;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

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

        for (int i=0; i<10; i++) {
            for (int j = 0; j < 15; j++) {
                if (!Objects.equals(a[i][j].getName(), b[i][j].getName()))
                    fail("Swiaty nie są takie same.");
            }
        }
        assertTrue(true);
    }



    @Test
    public void BossWaveEveryTenWavesTest()
    {
        ProfileManager profileManager = new ProfileManager();
        Base base = new Base(profileManager.createEmptySave("normal",1,""), null);

        FileReader fileReader = new FileReader();
        JSONObject enemiesJSONObject = fileReader.downloadFileAsJSONObject("assets/enemies.json");

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
                fail("Fala nie posiada bossa.");
            }

        }
        assertTrue(true);
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

        testFileReader.setSave(profileManager.createEmptySave("normal",99,""));
        if(!testFileReader.fileExists("../save/save099l.json")){
            fail("Plik nie został zapisany.");
        }

        testFileReader.deleteSave(99);
        if(testFileReader.fileExists("../save/save099l.json")){
            fail("Plik nie został usunięty.");
        }


    }

    //@Test
    //public void multiplierTest() {
        //FileReader fileReader = new FileReader();
        //Assertions.assertEquals(50, fileReader.downloadFileAsJSONObject("assets/towers.json").getJSONArray("meleeTower").getJSONObject(0).getFloat("dmg"));
    //}

    @Test
    public void alwaysPassesTest()
    {
        assertTrue(true);
    }

}
