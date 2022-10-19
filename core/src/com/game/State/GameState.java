package com.game.State;

public enum GameState {
    PLAYING,
    MENU;
    private static GameState gameState = MENU;
    private static GameState previousGameState = MENU;
    public static void setGameState(GameState state){
        gameState = state;
    }
    public static void setPreviousGameState(GameState state){previousGameState = state;}
}
