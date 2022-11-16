package com.game.Manager;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.game.Screens.GameScreen;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.List;


public class WorldManager {
    private int errorCounter = 0;
    public int getErrorCounter() {
        return errorCounter;
    }

    public void setErrorCounter(int errorCounter) {
        this.errorCounter = errorCounter;
    }
    public void increaseErrorCounter(int x) {
        this.errorCounter+=x;
    }
    private final int[][] directions = {{1, 0},{0, 1},{-1, 0},{0, -1}};
    private int[] playerBasePosition;
    private int[] enemyBasePosition;
    private List<int[]> pathToMove = new ArrayList<>();

    public WorldManager() {
    }

    private int[][] generateWater(int[][] map, int waterI, int waterJ, int waterSize, int chosenAxis, int chosenCorner, int waterDeep, int seed){
        Random random = new Random(seed);
        boolean isReturn = false;
        switch (chosenCorner) {
            case 1 -> {
                if (chosenAxis == 0) {
                    for (int i = 0; i < 10; i++) {
                        for (int j = 0; j < 15; j++) {
                            if ((j >= waterJ && j < waterJ + waterSize && i <= waterDeep)) {
                                map[i][j] = 2;
                            }
                        }
                        waterDeep--;
                        if (waterDeep < 0) {
                            isReturn = true;
                        }
                        int checker = 0;
                        int newPosition = random.nextInt(waterJ - 1, waterJ + waterSize + 2);
                        while (newPosition > 14) {
                            checker++;
                            newPosition = random.nextInt(waterJ - 1, waterJ + waterSize + 2);
                            if(checker > 100) {
                                newPosition = random.nextInt(0, 13);
                            }
                        }
                        waterJ = newPosition;
                        checker = 0;
                        int newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                        while (newWaterSize > waterSize || newWaterSize < 0) {
                            checker++;
                            newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                            if(checker > 100){
                                newWaterSize = 0;
                            }
                        }
                        waterSize = newWaterSize;
                    }
                } else {
                    for (int j = 0; j < 15; j++) {
                        for (int i = 0; i < 10; i++) {
                            if ((i >= waterI && i < waterI + waterSize && j < waterDeep)) {
                                map[i][j] = 2;
                            }
                        }

                        waterDeep--;
                        if (waterDeep <= 0) {
                            isReturn = true;
                        }
                        int checker = 0;
                        int newPosition = random.nextInt(waterI - 1, waterI + waterSize + 2);
                        while (newPosition > 9) {
                            checker++;
                            newPosition = random.nextInt(waterI - 1, waterI + waterSize + 2);
                            if(checker > 100) {
                                newPosition = random.nextInt(0, 9);
                            }
                        }
                        waterI = newPosition;
                        checker = 0;
                        int newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                        while (newWaterSize > waterSize || newWaterSize < 0) {
                            checker++;
                            newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                            if(checker > 100) {
                                newWaterSize = 0;
                            }
                        }
                        waterSize = newWaterSize;
                    }
                }
            }
            case 2 -> {
                if (chosenAxis == 0) {
                    for (int i = 9; i >=0; i--) {
                        for (int j = 0; j < 15; j++) {
                            if ((j >= waterJ && j < waterJ + waterSize && i > waterDeep)) {
                                map[i][j] = 2;
                            }
                        }
                        if (i == 0) {
                            isReturn = true;
                        }
                        int checker = 0;
                        int newPosition = random.nextInt(waterJ - 1, waterJ + waterSize + 2);
                        while (newPosition > 14) {
                            checker++;
                            newPosition = random.nextInt(waterJ - 1, waterJ + waterSize + 2);
                            if(checker > 100) {
                                newPosition = random.nextInt(0, 13);
                            }
                        }
                        waterJ = newPosition;
                        checker = 0;
                        int newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                        while (newWaterSize > waterSize || newWaterSize < 0) {
                            checker++;
                            newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                            if(checker > 100) {
                                newWaterSize = 0;
                            }
                        }
                        waterSize = newWaterSize;
                    }
                } else {
                    for (int j = 14; j >= 0; j--) {
                        for (int i = 0; i < 10; i++) {
                            if ((i >= waterI && i < waterI + waterSize && j > waterDeep)) {
                                map[i][j] = 2;
                            }
                        }
                        if (j == 0) {
                            isReturn = true;
                        }
                        int checker = 0;
                        int newPosition = random.nextInt(waterI - 1, waterI + waterSize + 2);
                        while (newPosition > 9) {
                            checker++;
                            newPosition = random.nextInt(waterI - 1, waterI + waterSize + 2);
                            if(checker > 100) {
                                newPosition = random.nextInt(0,9);
                            }
                        }
                        waterI = newPosition;
                        checker = 0;
                        int newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                        while (newWaterSize > waterSize || newWaterSize < 0) {
                            checker++;
                            newWaterSize = random.nextInt(waterSize - 1, waterSize + 2);
                            if(checker > 100) {
                                newWaterSize = 0;
                            }
                        }
                        waterSize = newWaterSize;
                    }
                }
            }
        }
        if(isReturn) {
            return map;
        } else {
            return generateWater(map, waterI, waterJ, waterSize, chosenAxis, chosenCorner, waterDeep, seed);
        }
    }
    private List<int[]> pathGenerating(int[][] arr, int[] start, int[] end, int seed){
        directionGenerating(seed);
        boolean[][] visited = new boolean[arr.length][arr[0].length];
        List<int[]> path = new ArrayList<>();
        dfs(arr, visited, start, end, path, seed);
        return path;
    }
    private void directionGenerating(int seed) {
        Random random = new Random(seed);
        for(int i = 0; i< directions.length; i++) {
            int j = random.nextInt(i+1);
            int[] t = directions[i];
            directions[i] = directions[j];
            directions[j] = t;
        }
    }
    private boolean dfs(int[][] map, boolean[][] visited, int[] currentPosition, int[] enemyBasePosition, List<int[]> path, int seed){
        increaseErrorCounter(1);
        if(getErrorCounter() > 150) {
            path.clear();
            setErrorCounter(0);
            return true;
        }
            if (currentPosition[0] == enemyBasePosition[0] && currentPosition[1] == enemyBasePosition[1]) {
                path.add(new int[]{currentPosition[0], currentPosition[1]});
                return true;
            }

            visited[currentPosition[0]][currentPosition[1]] = true;
            path.add(new int[]{currentPosition[0], currentPosition[1]});

            directionGenerating(seed);

            for (int[] direction : directions) {
                int newI = currentPosition[0] + direction[0];
                int newJ = currentPosition[1] + direction[1];
                if (newI >= 0 && newI < map.length && newJ >= 0 && newJ < map[0].length && !visited[newI][newJ] && visited.length<50)
                    if (dfs(map, visited, new int[]{newI, newJ}, enemyBasePosition, path, seed)) {
                        return true;
                    }
            }
            visited[currentPosition[0]][currentPosition[1]] = false;
            path.remove(path.size() - 1);
            return false;
    }

    private int[][] generatePath(int[][] map, List<int[]> path){
        for(int i=0; i<10; i++) {
            for(int j=0; j<15; j++) {
                for(int k=0; k<path.size(); k++) {
                    if(i == path.get(k)[0] && j == path.get(k)[1]){
                        map[i][j] = 1;
                    }
                }
            }
        }
        return map;
    }

    private int[][] generateObstacles(int[][] map, int seed, int difficulty){
        Random random = new Random(seed);
        switch (difficulty) {
            case 51:{
                for(int i=0; i<10; i++) {
                    for(int j=0; j<15; j++) {
                        int obstacleChance = random.nextInt(0,100);
                        if(obstacleChance > 6 && obstacleChance < 17 && map[i][j] == 0) {
                            map[i][j] = 5;
                        }if(obstacleChance < 6 && map[i][j]==0) {
                            map[i][j] = 6;
                        }
                    }
                }
            }break;
            case 46:{
                for(int i=0; i<10; i++) {
                    for(int j=0; j<15; j++) {
                        int obstacleChance = random.nextInt(0,100);
                        if(obstacleChance > 12 && obstacleChance < 25 && map[i][j] == 0) {
                            map[i][j] = 5;
                        }if(obstacleChance < 12 && map[i][j]==0) {
                            map[i][j] = 6;
                        }
                    }
                }
            }break;
            case 36:{
                for(int i=0; i<10; i++) {
                    for(int j=0; j<15; j++) {
                        int obstacleChance = random.nextInt(0,100);
                        if(obstacleChance > 21 && obstacleChance < 38 && map[i][j] == 0) {
                            map[i][j] = 5;
                        }if(obstacleChance < 21 && map[i][j]==0) {
                            map[i][j] = 6;
                        }
                    }
                }
            }break;
        }
        return map;
    }

    private int[][] overwritePath(int[][] map, List<int[]> path){
        for(int i = 0; i< path.size(); i++) {
            if(i== path.size()-1) {
                if((path.get(i-1)[0] < path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] > path.get(i-2)[1])) {
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 16;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] > path.get(i)[1]) && (path.get(i-1)[0] < path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 16;
                } else if((path.get(i-1)[0] > path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] > path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 14;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] > path.get(i)[1]) && (path.get(i-1)[0] > path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 14;
                } else if((path.get(i-1)[0] < path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] < path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 15;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] < path.get(i)[1]) && (path.get(i-1)[0] < path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 15;
                } else if((path.get(i-1)[0] > path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] < path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 13;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] < path.get(i)[1]) && (path.get(i-1)[0] > path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 13;
                } else if((path.get(i-1)[0] > path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] < path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 12;
                } else if((path.get(i-1)[0] < path.get(i)[0] && path.get(i-1)[1] == path.get(i)[1]) && (path.get(i-1)[0] > path.get(i-2)[0] && path.get(i-1)[1] == path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 12;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] > path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] < path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 11;
                } else if((path.get(i-1)[0] == path.get(i)[0] && path.get(i-1)[1] < path.get(i)[1]) && (path.get(i-1)[0] == path.get(i-2)[0] && path.get(i-1)[1] > path.get(i-2)[1])){
                    map[path.get(i-1)[0]][path.get(i-1)[1]] = 11;
                }
            } else if(i< path.size()-2) {
                if((path.get(i+1)[0] < path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] > path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])) {
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 12;
                } else if((path.get(i+1)[0] > path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] < path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 12;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] < path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] > path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 11;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] > path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] < path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 11;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] > path.get(i)[1]) && (path.get(i+1)[0] < path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 16;
                } else if((path.get(i+1)[0] < path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] > path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 16;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] < path.get(i)[1]) && (path.get(i+1)[0] < path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])){
                    map[path.get(i+1)[0]][path.get(i+1)[1]] = 15;
                } else if((path.get(i+1)[0] < path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] < path.get(i+2)[1])) {
                    map[path.get(i + 1)[0]][path.get(i + 1)[1]] = 15;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] > path.get(i)[1]) && (path.get(i+1)[0] > path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])) {
                    map[path.get(i + 1)[0]][path.get(i + 1)[1]] = 14;
                } else if((path.get(i+1)[0] > path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] > path.get(i+2)[1])) {
                    map[path.get(i + 1)[0]][path.get(i + 1)[1]] = 14;
                } else if((path.get(i+1)[0] > path.get(i)[0] && path.get(i+1)[1] == path.get(i)[1]) && (path.get(i+1)[0] == path.get(i+2)[0] && path.get(i+1)[1] < path.get(i+2)[1])) {
                    map[path.get(i + 1)[0]][path.get(i + 1)[1]] = 13;
                } else if((path.get(i+1)[0] == path.get(i)[0] && path.get(i+1)[1] < path.get(i)[1]) && (path.get(i+1)[0] > path.get(i+2)[0] && path.get(i+1)[1] == path.get(i+2)[1])) {
                    map[path.get(i + 1)[0]][path.get(i + 1)[1]] = 13;
                }
            }
        }
        return map;
    }

    public Image[][] createWorld(GameScreen gameScreen, int seed, int difficulty){
        Random random = new Random(seed);

        int[][] map = new int[10][15];

        for(int i=0; i<10; i++) {
            for(int j=0; j<15; j++) {
                map[i][j] = 0;
            }
        }

        int playerBaseI;
        int playerBaseJ;
        int enemyBaseI;
        int enemyBaseJ;

        int chosenCorner = random.nextInt(1, 3);

        int chosenAxis = random.nextInt(0, 2);

        int waterSize;
        if(chosenAxis == 1) {
            waterSize = random.nextInt(1, 10);
        } else {
            waterSize = random.nextInt(1, 8);
        }

        int waterDeep;
        if(chosenAxis == 0) {
            waterDeep = random.nextInt(3, 6);
        } else {
            waterDeep = random.nextInt(5, 8);
        }

        switch (chosenCorner) {
            case 1 -> {
                if (chosenAxis == 0) {
                    playerBaseI = 0;
                    playerBaseJ = random.nextInt(0, 15-waterSize);
                    generateWater(map, playerBaseI, playerBaseJ, waterSize, chosenAxis, chosenCorner, waterDeep, seed);
                    playerBaseI = random.nextInt((waterDeep + 2), 10);
                    if (playerBaseI < 9) {
                        playerBaseJ = random.nextInt(0, 2);
                        if (playerBaseJ == 1) {
                            playerBaseJ = 14;
                        }
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseI = random.nextInt(waterDeep + 1, 10);
                        if (playerBaseJ == 0) {
                            enemyBaseJ = 14;
                        } else {
                            enemyBaseJ = 0;
                        }
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    } else {
                        playerBaseJ = random.nextInt(0, 14);
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseI = 0;
                        enemyBaseJ = random.nextInt(0, 14);
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    }
                    List<int[]> path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    while (path.size() <5 || path.size() >100) {
                        seed+=1;
                        path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    }
                    generatePath(map, path);
                    map[playerBaseI][playerBaseJ] = 9;
                    map[enemyBaseI][enemyBaseJ] = 8;
                    generateObstacles(map, seed, difficulty);
                    pathToMove = path;
                    overwritePath(map, path);
                } else {
                    playerBaseI = random.nextInt(0, 10-waterSize);
                    playerBaseJ = 0;
                    generateWater(map, playerBaseI, playerBaseJ, waterSize, chosenAxis, chosenCorner, waterDeep, seed);
                    playerBaseJ = random.nextInt((waterDeep + 2), 15);
                    if (playerBaseJ < 14) {
                        playerBaseI = random.nextInt(0, 2);
                        if (playerBaseI == 1) {
                            playerBaseI = 9;
                        }
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseJ = random.nextInt(waterDeep + 1, 15);
                        if (playerBaseI == 0) {
                            enemyBaseI = 9;
                        } else {
                            enemyBaseI = 0;
                        }
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    } else {
                        playerBaseI = random.nextInt(0, 10);
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseJ = 0;
                        enemyBaseI = random.nextInt(0, 10);
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    }
                    List<int[]> path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    while (path.size() <5 || path.size() >100) {
                        seed+=1;
                        path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    }
                    generatePath(map, path);
                    map[playerBaseI][playerBaseJ] = 9;
                    map[enemyBaseI][enemyBaseJ] = 8;
                    generateObstacles(map, seed, difficulty);
                    pathToMove = path;
                    overwritePath(map, path);
                }
            }
            case 2 -> {
                if (chosenAxis == 0) {
                    playerBaseJ = random.nextInt(0, 15-waterSize);
                    playerBaseI = 9;
                    generateWater(map, playerBaseI, playerBaseJ, waterSize, chosenAxis, chosenCorner, waterDeep, seed);
                    playerBaseI = random.nextInt(0, 10 - (waterDeep + 1));
                    if (playerBaseI > 0) {
                        playerBaseJ = random.nextInt(0, 2);
                        if (playerBaseJ == 1) {
                            playerBaseJ = 14;
                        }
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseI = random.nextInt(0, 10 - (waterDeep + 1));
                        if (playerBaseJ == 0) {
                            enemyBaseJ = 14;
                        } else {
                            enemyBaseJ = 0;
                        }
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    } else {
                        playerBaseJ = random.nextInt(0, 14);
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseI = 9;
                        enemyBaseJ = random.nextInt(0, 14);
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    }
                    List<int[]> res = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    while (res.size() <5 || res.size() >100) {
                        seed+=1;
                        res = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    }

                    generatePath(map, res);
                    map[playerBaseI][playerBaseJ] = 9;
                    map[enemyBaseI][enemyBaseJ] = 8;
                    generateObstacles(map, seed, difficulty);
                    pathToMove = res;
                    overwritePath(map, res);
                } else {
                    playerBaseI = random.nextInt(0, 10-waterSize);
                    playerBaseJ = 14;
                    generateWater(map, playerBaseI, playerBaseJ, waterSize, chosenAxis, chosenCorner, waterDeep, seed);
                    playerBaseJ = random.nextInt(0, 15 - (waterDeep + 1));
                    if (playerBaseJ > 0) {
                        playerBaseI = random.nextInt(0, 2);
                        if (playerBaseI == 1) {
                            playerBaseI = 9;
                        }
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseJ = random.nextInt(0, 15 - (waterDeep + 1));
                        if (playerBaseI == 0) {
                            enemyBaseI = 9;
                        } else {
                            enemyBaseI = 0;
                        }
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};

                    } else {
                        playerBaseI = random.nextInt(1, 10);
                        playerBasePosition = new int[]{playerBaseI, playerBaseJ};
                        enemyBaseJ = 14;
                        enemyBaseI = random.nextInt(0, 10);
                        enemyBasePosition = new int[]{enemyBaseI, enemyBaseJ};
                    }
                    List<int[]> path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    while (path.size() <5 || path.size() >100) {
                        seed+=1;
                        path = pathGenerating(map, playerBasePosition, enemyBasePosition, seed);
                    }
                    generatePath(map, path);
                    map[playerBaseI][playerBaseJ] = 9;
                    map[enemyBaseI][enemyBaseJ] = 8;
                    generateObstacles(map, seed, difficulty);
                    pathToMove = path;
                    overwritePath(map, path);
                }
            }
        }

        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));
        Image[][] imageArr = new Image[10][15];
        int i = 0;
        for (int[] x : map) {
            int j = 0;
            for (int y : x) {
                if (y==0) {
                    if(random.nextInt(0,2) == 0) {
                        imageArr[i][j] = new Image(images_map, "grass");
                        imageArr[i][j].setName("grass");
                    } else {
                        imageArr[i][j] = new Image(images_map, "grass_flowers");
                        imageArr[i][j].setName("grass");
                    }
                }
                else if (y==2) {
                    imageArr[i][j] = getRotatedWater(map,i,j);
                    imageArr[i][j].setName("water");
                }
                else if (y==5) {
                    if(random.nextInt(0,2) == 0) {
                        imageArr[i][j] = new Image(images_map, "obstacle");
                        imageArr[i][j].setName("obstacle");
                    } else {
                        imageArr[i][j] = new Image(images_map, "obstacle_2");
                        imageArr[i][j].setName("obstacle");
                    }
                }
                else if (y==6) {
                    imageArr[i][j] = getRotatedMountain(map,i,j);
                    imageArr[i][j].setName("mountain");
                }
                else if (y==8) {
                    imageArr[i][j] = getRotatedEnemyBase(map,i,j);
                    imageArr[i][j].setName("enemy");
                }
                else if (y==9) {
                    imageArr[i][j] = getRotatedBase(map,i,j);
                    imageArr[i][j].setName("base");
                }
                else if (y==11) {
                    imageArr[i][j] = new Image(images_map, "pathLeftRight");
                    imageArr[i][j].setName("path");
                }
                else if (y==12) {
                    imageArr[i][j] = new Image(images_map, "pathUpDown");
                    imageArr[i][j].setName("path");
                }
                else if (y==13) {
                    imageArr[i][j] = new Image(images_map, "pathUpRight");
                    imageArr[i][j].setName("path");
                }
                else if (y==14) {
                    imageArr[i][j] = new Image(images_map, "pathLeftUp");
                    imageArr[i][j].setName("path");
                }
                else if (y==15) {
                    imageArr[i][j] = new Image(images_map, "pathDownRight");
                    imageArr[i][j].setName("path");
                }
                else if (y==16) {
                    imageArr[i][j] = new Image(images_map, "pathLeftDown");
                    imageArr[i][j].setName("path");
                }

                if (gameScreen!=null) {

                    imageArr[i][j].addListener(new ImageClickListener(j, i, imageArr[i][j].getName()) {
                        public void clicked(InputEvent event, float x, float y) {
                            this.setLastClickedTile(gameScreen.lastClickedMapTile);
                            gameScreen.mouseClickMapTile();
                        }

                        public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            this.setLastClickedTile(gameScreen.lastClickedMapTile);
                            gameScreen.mouseEnterMapTile();

                        }

                        public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                            gameScreen.mouseExitMapTile();
                        }
                    });
                }

                j++;
            }
            i++;

        }
        return imageArr;
    }

    private Image getRotatedWater(int[][] map, int i, int j) {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));

        boolean isWaterLeft = false;
        boolean isWaterRight = false;
        boolean isWaterUp = false;
        boolean isWaterDown = false;

        if (j+1<15) {
            if (map[i][j + 1] == 2)
                isWaterRight = true;
        } else {
            isWaterRight = true;
        }

        if (j-1>=0) {
            if (map[i][j - 1] == 2)
                isWaterLeft = true;
        } else {
            isWaterLeft = true;
        }

        if (i-1>=0) {
            if (map[i-1][j] == 2)
                isWaterUp = true;
        } else {
            isWaterUp = true;
        }

        if (i+1<10) {
            if (map[i+1][j] == 2)
                isWaterDown = true;
        } else {
            isWaterDown = true;
        }

        if(isWaterLeft && isWaterUp && isWaterRight && isWaterDown)
            return new Image(images_map, "waterFull");
        if(isWaterLeft && isWaterUp && isWaterRight)
            return new Image(images_map, "waterDown");
        if(isWaterLeft && isWaterUp && isWaterDown)
            return new Image(images_map, "waterRight");
        if(isWaterLeft && isWaterDown && isWaterRight)
            return new Image(images_map, "waterUp");
        if(isWaterDown && isWaterUp && isWaterRight)
            return new Image(images_map, "waterLeft");

        if(isWaterUp && isWaterRight)
            return new Image(images_map, "waterLeftDown");
        if(isWaterDown && isWaterRight)
            return new Image(images_map, "waterLeftUp");
        if(isWaterLeft && isWaterUp)
            return new Image(images_map, "waterRightDown");
        if(isWaterLeft && isWaterDown)
            return new Image(images_map, "waterRightUp");

        if(isWaterLeft && isWaterRight)
            return new Image(images_map, "waterUpDown");
        if(isWaterUp && isWaterDown)
            return new Image(images_map, "waterLeftRight");

        if(isWaterUp)
            return new Image(images_map, "waterLeftRightDown");
        if(isWaterDown)
            return new Image(images_map, "waterLeftRightUp");
        if(isWaterLeft)
            return new Image(images_map, "waterRightUpDown");
        if(isWaterRight)
            return new Image(images_map, "waterLeftUpDown");

        return new Image(images_map, "water");
    }

    private Image getRotatedMountain(int[][] map, int i, int j) {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));

        boolean isMountainLeft = false;
        boolean isMountainRight = false;

        if (j+1<15) {
            if (map[i][j + 1] == 6)
                isMountainRight = true;
        } else {
            isMountainRight = true;
        }

        if (j-1>=0) {
            if (map[i][j - 1] == 6)
                isMountainLeft = true;
        } else {
            isMountainLeft = true;
        }

        if(isMountainLeft && isMountainRight)
            return new Image(images_map, "mountainFull");
        if(isMountainRight)
            return new Image(images_map, "mountainLeft");
        if(isMountainLeft)
            return new Image(images_map, "mountainRight");


        return new Image(images_map, "mountain");
    }


    private Image getRotatedEnemyBase(int[][] map, int i, int j) {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));

        if(i==0) {
            if (j+1<15)
                if(map[i][j+1] == 11 || map[i][j+1] == 16)
                    return new Image(images_map, "enemyToRight");
            if (j-1>=0)
                if(map[i][j-1] == 11 || map[i][j-1] == 15)
                    return new Image(images_map, "enemyToLeft");
            if(map[i+1][j] == 12 || map[i+1][j] == 13 || map[i+1][j] == 14)
                return new Image(images_map, "enemyToDown");
        }
        else if(i==9) {
            if (j+1<15)
                if(map[i][j+1] == 11 || map[i][j+1] == 14)
                    return new Image(images_map, "enemyToRight");
            if (j-1>=0)
                if(map[i][j-1] == 11 || map[i][j-1] == 13)
                    return new Image(images_map, "enemyToLeft");

            if(map[i-1][j] == 12 || map[i-1][j] == 15 || map[i-1][j] == 16)
                return new Image(images_map, "enemyToUp");
        }
        else if(j==0) {
            if(map[i][j+1] == 11 || map[i][j+1] == 14 || map[i][j+1] == 16)
                return new Image(images_map, "enemyToRight");
            if (i-1>=0)
                if(map[i-1][j] == 12 || map[i-1][j] == 15)
                    return new Image(images_map, "enemyToUp");
            if (i+1<15)
                if(map[i+1][j] == 12 || map[i+1][j] == 13)
                    return new Image(images_map, "enemyToDown");
        }
        else if(j==14) {
            if(map[i][j-1] == 11 || map[i][j-1] == 13 || map[i][j-1] == 15)
                return new Image(images_map, "enemyToLeft");
            if (i-1>=0)
                if(map[i-1][j] == 12 || map[i-1][j] == 16)
                    return new Image(images_map, "enemyToUp");
            if (i+1<15)
                if(map[i+1][j] == 12 || map[i+1][j] == 14)
                    return new Image(images_map, "enemyToDown");
        }

        return new Image(images_map, "enemyToDown");
    }

    private Image getRotatedBase(int[][] map, int i, int j) {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));

        if(i==0) {
            if (j+1<15)
                if(map[i][j+1] == 11 || map[i][j+1] == 16)
                    return new Image(images_map, "baseToRight");
            if (j-1>=0)
                if(map[i][j-1] == 11 || map[i][j-1] == 15)
                    return new Image(images_map, "baseToLeft");
            if(map[i+1][j] == 12 || map[i+1][j] == 13 || map[i+1][j] == 14)
                return new Image(images_map, "baseToDown");
        }
        else if(i==9) {
            if (j+1<15)
                if(map[i][j+1] == 11 || map[i][j+1] == 14)
                    return new Image(images_map, "baseToRight");
            if (j-1>=0)
                if(map[i][j-1] == 11 || map[i][j-1] == 13)
                    return new Image(images_map, "baseToLeft");

            if(map[i-1][j] == 12 || map[i-1][j] == 15 || map[i-1][j] == 16)
                return new Image(images_map, "baseToUp");
        }
        else if(j==0) {
            if(map[i][j+1] == 11 || map[i][j+1] == 14 || map[i][j+1] == 16)
                return new Image(images_map, "baseToRight");
            if (i-1>=0)
                if(map[i-1][j] == 12 || map[i-1][j] == 15)
                    return new Image(images_map, "baseToUp");
            if (i+1<15)
                if(map[i+1][j] == 12 || map[i+1][j] == 13)
                    return new Image(images_map, "baseToDown");
        }
        else if(j==14) {
            if(map[i][j-1] == 11 || map[i][j-1] == 13 || map[i][j-1] == 15)
                return new Image(images_map, "baseToLeft");
            if (i-1>=0)
                if(map[i-1][j] == 12 || map[i-1][j] == 16)
                    return new Image(images_map, "baseToUp");
            if (i+1<15)
                if(map[i+1][j] == 12 || map[i+1][j] == 14)
                    return new Image(images_map, "baseToDown");
        }

        return new Image(images_map, "baseToDown");
    }


    public Image[][] loadTerrainModifications (GameScreen gameScreen, Image[][] mapArr, JSONArray terrArr)
    {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));
        for (int i = 0; i< terrArr.length(); i++) {
            JSONObject j = terrArr.getJSONObject(i);

            mapArr[j.getInt("y")][j.getInt("x")].setDrawable(images_map, j.getString("tileName"));
            mapArr[j.getInt("y")][j.getInt("x")].setName(j.getString("tileName"));
            mapArr[j.getInt("y")][j.getInt("x")].clearListeners();
            mapArr[j.getInt("y")][j.getInt("x")].addListener(new ImageClickListener(j.getInt("x"), j.getInt("y"),  mapArr[j.getInt("y")][j.getInt("x")].getName()) {
                public void clicked(InputEvent event, float x, float y) {
                    this.setLastClickedTile(gameScreen.lastClickedMapTile);
                    gameScreen.mouseClickMapTile();
                }
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    this.setLastClickedTile(gameScreen.lastClickedMapTile);
                    gameScreen.mouseEnterMapTile();
                }
                public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    gameScreen.mouseExitMapTile();
                }
            });
        }
        return mapArr;
    }


    public Table changeTileAndRedrawWorld (GameScreen gameScreen, Image[][] mapArr, int x, int y, String tileName, float scale)
    {
        Skin images_map = new Skin(new TextureAtlas("assets/icons/map_sprites.pack"));
        mapArr[y][x].setDrawable(images_map, tileName);
        mapArr[y][x].setName(tileName);
        mapArr[y][x].clearListeners();

        mapArr[y][x].addListener(new ImageClickListener(x,y,mapArr[y][x].getName()){
            public void clicked(InputEvent event, float x, float y) {
                this.setLastClickedTile(gameScreen.lastClickedMapTile);
                gameScreen.mouseClickMapTile();
            }
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                this.setLastClickedTile(gameScreen.lastClickedMapTile);
                gameScreen.mouseEnterMapTile();
            }
            public void exit(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                gameScreen.mouseExitMapTile();
            }
        });
        return drawWorld(mapArr, scale);
    }


    public Table drawWorld(Image[][] imageArr, float scale){
        Table t = new Table();
        t.setBounds(Gdx.graphics.getWidth()/20 , (Gdx.graphics.getHeight()-Gdx.graphics.getWidth()/30*16)/2 , 960 , 640);

        t.setTransform(true);
        for (int i = 0; i<10; i++) {
            for (int j = 0; j<15; j++) {
                t.add(imageArr[i][j]);
            }
            t.row();
        }
        t.setScale(scale);
        return t;
    }
    public List<int[]> getPath() {
        return pathToMove;
    }

}