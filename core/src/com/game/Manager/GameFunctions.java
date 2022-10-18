package com.game.Manager;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;
import com.game.Screens.GameScreen;
import org.json.JSONObject;

import java.util.*;

public class GameFunctions {

    public static Image[][] getOperationsArr(GameScreen gameScreen) {
        Image[][] arr = new Image[4][2];
        Skin images_buildings = new Skin(new TextureAtlas("assets/icons/buildings.pack"));

        arr[0][0] = new Image(images_buildings, "sword");
        arr[0][1] = new Image(images_buildings, "locked");
        arr[1][0] = new Image(images_buildings, "locked");
        arr[1][1] = new Image(images_buildings, "locked");
        arr[2][0] = new Image(images_buildings, "stickyRoad");
        arr[2][1] = new Image(images_buildings, "roadNeedles");
        arr[3][0] = new Image(images_buildings, "clean");
        arr[3][1] = new Image(images_buildings, "sell");

        arr[0][1].setTouchable(Touchable.disabled);
        arr[1][0].setTouchable(Touchable.disabled);
        arr[1][1].setTouchable(Touchable.disabled);

        arr[0][0].setName("melee");
        arr[0][1].setName("crossbow");
        arr[1][0].setName("mage");
        arr[1][1].setName("cannon");
        arr[2][0].setName("stickyRoad");
        arr[2][1].setName("roadNeedles");
        arr[3][0].setName("clean");
        arr[3][1].setName("sell");

        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 2; j++) {
                arr[i][j].addListener(new ImageClickListener(j, i, arr[i][j].getName()) {
                    public void clicked(InputEvent event, float x, float y) {
                        this.setLastClickedTile(gameScreen.lastClickedOperationTile);
                        gameScreen.mouseClickOperation();
                    }

                    public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        this.setLastClickedTile(gameScreen.lastClickedOperationTile);
                        gameScreen.mouseEnterOperation();
                    }

                    public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                        gameScreen.mouseExitOperation();
                    }
                });
            }

        return arr;
    }

    public static Image[][] getOperationsSelectedArr() {
        Image[][] arr = new Image[4][2];
        Skin images_buildings = new Skin(new TextureAtlas("assets/icons/buildings.pack"));

        arr[0][0] = new Image(images_buildings, "sword");
        arr[0][1] = new Image(images_buildings, "crossbow");
        arr[1][0] = new Image(images_buildings, "mage");
        arr[1][1] = new Image(images_buildings, "cannon");
        arr[2][0] = new Image(images_buildings, "stickyRoad");
        arr[2][1] = new Image(images_buildings, "roadNeedles");
        arr[3][0] = new Image(images_buildings, "clean");
        arr[3][1] = new Image(images_buildings, "sell");

        arr[0][0].setName("melee");
        arr[0][1].setName("crossbow");
        arr[1][0].setName("mage");
        arr[1][1].setName("cannon");
        arr[2][0].setName("stickyRoad");
        arr[2][1].setName("roadNeedles");
        arr[3][0].setName("clean");
        arr[3][1].setName("sell");

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 2; j++) {
                arr[i][j] = new Image(images_buildings, "empty");
                arr[i][j].setTouchable(Touchable.disabled);
            }
        }

        return arr;
    }

    public static Table getOperationsTable(Image[][] arr, float scale, TextButton bNextWave) {
        Table t = new Table();
        t.setBounds(Gdx.graphics.getWidth() - 224 * scale, (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2 + 48 * scale + 16 * scale, 224, 350);
        t.setTransform(true);

        Skin images_buildings = new Skin(new TextureAtlas("assets/icons/buildings.pack"));
        t.row().padBottom(4);
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 2; j++) {
                t.add(arr[i][j]);
            }
            t.row().padBottom(4);
        }
        t.add(new Image(images_buildings, "separator")).colspan(2).align(Align.left);
        t.row();
        t.add(arr[3][0]).padTop(4);
        t.add(arr[3][1]).padTop(4);
        t.row();
        t.add(bNextWave).align(Align.center).height(32).width(128).colspan(2).padTop(6);
        t.setScale(scale);
        return t;
    }

    public static ArrayList<Vector2> calculatePath(List<int[]> path, float scale) {
        ArrayList<Vector2> v = new ArrayList<>();
        for (int[] point : path) {
            System.out.println("x:" + point[1] + ", y:" + point[0]);
            v.add(new Vector2(point[1] * scale * 64 + Gdx.graphics.getWidth() / 20, (9 - point[0]) * scale * 64 + (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2));
        }
        Collections.reverse(v);

        return v;
    }

    public static void renderPreviewBuilding(SpriteBatch spritebatch, ShapeRenderer shapeRenderer, LastClickedTile tile, JSONObject turretLevels, String chosenOperation, float scale) {
        TextureRegion preview = new TextureRegion(new Texture(Gdx.files.internal("assets/game/towers/" + chosenOperation + "Preview.png")));
        float textureSize = 64;
        Vector2 position = new Vector2(tile.getX() * scale * 64 + Gdx.graphics.getWidth() / 20, (9 - tile.getY()) * scale * 64 + (Gdx.graphics.getHeight() - Gdx.graphics.getWidth() / 30 * 16) / 2);
        spritebatch.draw(preview, position.x, position.y, textureSize * scale, textureSize * scale);
        if ((!Objects.equals(chosenOperation, "roadNeedles") && !Objects.equals(chosenOperation, "stickyRoad"))) {
            float range = turretLevels.getJSONArray(chosenOperation + "Tower").getJSONObject(0).getFloat("range") * scale;
            spritebatch.end();
            shapeRenderer.setColor(Color.GREEN);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            shapeRenderer.circle(position.x + scale * textureSize / 2, position.y + scale * textureSize / 2, range);
            shapeRenderer.end();
            spritebatch.begin();
        }
    }
}