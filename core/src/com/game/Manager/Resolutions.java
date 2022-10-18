package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;

import java.util.ArrayList;
import java.util.HashSet;

public class Resolutions {
    private HashSet<String> resolutions;
    private ArrayList<String> resolutionsArrayList;

    public Resolutions() {
        resolutions = new HashSet<>();
        setResolutions(resolutions);
        hashToArray(resolutions);
    }

    private HashSet<String> setResolutions(HashSet<String> resolutions) {
        //Utworzenie tablicy wszystkich możliwych konfiguracji
        Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();
        //Utworzenie zmiennej przechowującej aktualną konfigurację
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();
        //Utworzenie Hashsetu konfiguracji o maxymalnie trzech wartościach (1280x720, 1600x900, 1920x1080)
        //Eliminacja duplikatów jeżeli jedna z konfiguracji jest konfiguracją domyślną
        for (int i = 0; i < modes.length; i++) {
            if (modes[i].width == 1280 && modes[i].height == 720) {
                //Jeśli aktualna konfiguracja jest konfiguracją domyślną ustaw Fullscreen
                if (modes[i].width != currentMode.width) {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
                } else {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
                }
            } else if (modes[i].width == 1600 && modes[i].height == 900) {
                //Jeśli aktualna konfiguracja jest konfiguracją domyślną ustaw Fullscreen
                if (modes[i].width != currentMode.width) {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
                } else {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
                }
            }
            //Jeśli aktualna konfiguracja jest konfiguracją domyślną ustaw Fullscreen, brak obsługi dla większych rozdzielczości, może powinna być i wtedy 1920x1080 w oknie
            else if (modes[i].width == 1920 && modes[i].height == 1080) {
                resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
            }
            if (modes[i].width > 1920 && modes[i].height > 1080) {
                resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
            }
        }
        return resolutions;
    }

    private ArrayList<String> hashToArray(HashSet<String> resolutions) {
        //Utworzenie ArrayListy z HashSetu
        resolutionsArrayList = new ArrayList<>(resolutions);
        return resolutionsArrayList;
    }

    public ArrayList<String> getResolutionsArrayList() {
        return resolutionsArrayList;
    }
}
