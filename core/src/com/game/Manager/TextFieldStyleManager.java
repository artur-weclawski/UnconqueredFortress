package com.game.Manager;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;

public class TextFieldStyleManager {
    private Pixmap cursorColor;
    public TextFieldStyleManager(){

    }

    public void setTextFieldStyle(TextField.TextFieldStyle textFieldStyle, Skin images, BitmapFont font, String name, Color color){
        textFieldStyle.background = images.getDrawable(name);
        textFieldStyle.font = font;
        textFieldStyle.fontColor = color;
    }

    public void setTextFieldStyleCursor(TextField.TextFieldStyle textFieldStyle, Skin images, BitmapFont font, String name, Color color){
        textFieldStyle.background = images.getDrawable(name);
        textFieldStyle.font = font;
        textFieldStyle.fontColor = color;
        Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
        Label oneCharSizeCalibrationThrowAway = new Label("|", labelStyle);
        cursorColor = new Pixmap((int) oneCharSizeCalibrationThrowAway.getWidth(),
                (int) oneCharSizeCalibrationThrowAway.getHeight(),
                Pixmap.Format.RGB888);
        cursorColor.setColor(Color.WHITE);
        cursorColor.fill();
        textFieldStyle.cursor = new Image(new Texture(cursorColor)).getDrawable();

    }

    public TextField.TextFieldStyle returnTextFieldStyle(TextField.TextFieldStyle textFieldStyle){
        return textFieldStyle;
    }
}
