package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.JsonWriter;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;

public class FileReader {
    private String tokenValue;
    private String resolutionValue;
    private int volumeValue;
    private int volumeEffectsValue;
    private String languageValue;

    private int seed;
    private String difficulty;
    private int finishedMaps;
    private int wave;
    private int gold;
    private int diamonds;

    public FileReader() {

    }

    public int getVolumeEffectsValue() {
        return volumeEffectsValue;
    }

    public String getTokenValue() {
        return tokenValue;
    }

    public String getResolutionValue() {
        return resolutionValue;
    }

    public float getVolumeValue() {
        return volumeValue;
    }

    public String getLanguageValue() {
        return languageValue;
    }

    public int getSeed() {
        return seed;
    }

    public int getGold() {
        return gold;
    }

    public int getDiamonds() {
        return diamonds;
    }

    public String getLoginFromToken() {
        String[] chunks = this.getTokenValue().split("\\.");
        Base64.Decoder decoder = Base64.getUrlDecoder();
        return (new JSONObject(new String(decoder.decode(chunks[1]))).getString("login"));
    }

    public void downloadUserInfo() {
        String jsonPath = "save/user.json";
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal(jsonPath));

        tokenValue = base.getString("token");
    }

    public void setUserInfo(String token) {
        String jsonPath = "save/user.json";
        User_info user_info = new User_info();
        FileHandle file = Gdx.files.local(jsonPath);
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);
        user_info.token = token;

        String txt = json.toJson(user_info);
        file.writeString(json.prettyPrint(txt), false);
    }

    public void downloadSettings() {
        String jsonPath = "save/settings.json";
        JsonReader json = new JsonReader();
        JsonValue base = json.parse(Gdx.files.internal(jsonPath));

        resolutionValue = base.getString("resolution");
        volumeValue = base.getInt("volume");
        volumeEffectsValue = base.getInt("volumeEffects");
        languageValue = (base.getString("language"));

    }

    public void setSettings(String resolution, float volume, float volume2, String language) {
        String jsonPath = "save/settings.json";
        User_settings user_settings = new User_settings();
        FileHandle file = Gdx.files.local(jsonPath);
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);
        user_settings.resolution = resolution;
        user_settings.volume = volume;
        user_settings.volumeEffects = volume2;
        user_settings.language = language;
        String txt = json.toJson(user_settings);
        file.writeString(json.prettyPrint(txt), false);
    }

    public boolean fileExists(String savePath) {
        File file = new File(savePath);
        return file.exists();
    }

    public JSONObject downloadSaveAsJSONObject(String jsonPath) {
        JSONObject j = new JSONObject();
        try {
            return new JSONObject(new String(Files.readAllBytes(Paths.get(jsonPath))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return j;
    }

    public JSONObject downloadFileAsJSONObject(String jsonPath) {

        JSONObject j = new JSONObject();
        try {
            return new JSONObject(new String(Files.readAllBytes(Paths.get(jsonPath))));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return j;
    }

    public void setSave(JSONObject save) {
        String jsonPath = "save/save0"+ save.getInt("profileNumber") +"l.json";

        FileHandle file = Gdx.files.local(jsonPath);
        Json json = new Json();
        json.setTypeName(null);
        json.setUsePrototypes(false);
        json.setIgnoreUnknownFields(true);
        json.setOutputType(JsonWriter.OutputType.json);

        file.writeString(json.prettyPrint(save.toString()), false);
    }


    public void deleteSave(int saveNumber) {
        Path path = FileSystems.getDefault().getPath("save/save0" + saveNumber + "l.json");
        try {
            Files.deleteIfExists(path);
        } catch (IOException x) {
            System.err.println(x);
        }
    }
}
