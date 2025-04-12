package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;

public class LanguageManager {

    private String language;

    public LanguageManager(String language) {
        this.language = language;
    }

    public String getLanguage() {
        return language;
    }

    public String getValue(String language, String name) {
        if (language.equals("English")) {
            String jsonPath = "assets/languages/english.json";
            JsonReader json = new JsonReader();
            JsonValue base = json.parse(Gdx.files.internal(jsonPath));
            return base.getString(name);
        } else {
            String jsonPath = "assets/languages/polski.json";
            JsonReader json = new JsonReader();
            JsonValue base = json.parse(Gdx.files.internal(jsonPath));
            return base.getString(name);
        }
    }
}
