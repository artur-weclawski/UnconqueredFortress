package game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonWriter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestFileReader {
    public TestFileReader(){

    }

    public void deleteSave(int saveNumber) {

        //Wymagane podanie innej ścieżki
        Path path = FileSystems.getDefault().getPath("../save/save0"+saveNumber+"l.json");
        try {
            Files.deleteIfExists(path);
        } catch (IOException x) {
            System.err.println(x);
        }

    }

    public boolean fileExists(String savePath){
        File file = new File(savePath);
        return file.exists();
    }
    public void setSave(JSONObject save){
        String jsonPath = "../save/save0"+ save.getInt("profileNumber") +"l.json";

        FileHandle file = Gdx.files.local(jsonPath);
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        //if (save.has("profileNumber"))
        //    file.writeString(json.prettyPrint(save.remove("profileNumber").toString()), false);

        file.writeString(json.prettyPrint(save.toString()), false);
    }
}
