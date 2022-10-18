package com.game.Manager;

import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;


public class ImageClickListener extends ClickListener {
    private int x;
    private int y;
    private String name;

    public ImageClickListener(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public void setLastClickedTile(LastClickedTile lastClickedTile) {
        lastClickedTile.setX(x);
        lastClickedTile.setY(y);
        lastClickedTile.setName(name);
    }
}
