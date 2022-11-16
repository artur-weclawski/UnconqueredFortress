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
        Graphics.DisplayMode[] modes = Gdx.graphics.getDisplayModes();
        Graphics.DisplayMode currentMode = Gdx.graphics.getDisplayMode();

        for (int i = 0; i < modes.length; i++) {
            if (modes[i].width == 1280 && modes[i].height == 720) {
                if (modes[i].width != currentMode.width) {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
                } else {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
                }
            } else if (modes[i].width == 1600 && modes[i].height == 900) {
                if (modes[i].width != currentMode.width) {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
                } else {
                    resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
                }
            } else if (modes[i].width == 1920 && modes[i].height == 1080) {
                resolutions.add(modes[i].width + " X " + modes[i].height + " Fullscreen");
            }
            if (modes[i].width > 1920 && modes[i].height > 1080) {
                resolutions.add(modes[i].width + " X " + modes[i].height + " Windowed");
            }
        }
        return resolutions;
    }

    private ArrayList<String> hashToArray(HashSet<String> resolutions) {
        resolutionsArrayList = new ArrayList<>(resolutions);
        return resolutionsArrayList;
    }

    public ArrayList<String> getResolutionsArrayList() {
        return resolutionsArrayList;
    }
}
