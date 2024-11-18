package com.milne.mw.difficulty;

public enum Difficulty {
    EASY(0.01f,10,0.5f),
    MEDIUM(0.03f,15,6f),
    HARD(0.05f,20,4f);

    private final float scalingFactor;
    private final int maxRound;
    private float intervalSpawn;

    Difficulty(float scalingFactor, int maxRound, float intervalSpawn) {
        this.scalingFactor = scalingFactor;
        this.maxRound = maxRound;
        this.intervalSpawn = intervalSpawn;
    }

    public float getScalingFactor() {
        return scalingFactor;
    }

    public float getIntervalSpawn() {
        return intervalSpawn;
    }

    public int getMaxRound() {
        return maxRound;
    }
}
