package com.game.Manager;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

public class ButtonStyleManager {
    public ButtonStyleManager() {

    }

    public void setTextButtonStyle(TextButton.TextButtonStyle textButtonStyle, Skin images, BitmapFont font, String up, String down) {
        textButtonStyle.up = images.getDrawable(up);
        textButtonStyle.down = images.getDrawable(down);
        textButtonStyle.pressedOffsetX = 1;
        textButtonStyle.pressedOffsetY = -1;
        textButtonStyle.font = font;

    }

    public TextButton.TextButtonStyle returnTextButtonStyle(TextButton.TextButtonStyle textButtonStyle) {
        return textButtonStyle;
    }
}
