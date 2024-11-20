package com.milne.mw.difficulty;

public enum Difficulty {
    EASY(0.01f,0,0.5f, 1000, 7500, 3f),
    MEDIUM(0.03f,15,6f, 75, 50, 2f),
    HARD(0.05f,20,4f, 50, 25, 2f);

    private final float scalingFactor;
    private final int maxRound;
    private final float intervalSpawn;
    private final int initalLives;
    private final int initialEnergy;
    private final float energyGenerationRate;

    Difficulty(float scalingFactor, int maxRound, float intervalSpawn, int initialLives, int initialEnergy, float energyGenerationRate) {
        this.scalingFactor = scalingFactor;
        this.maxRound = maxRound;
        this.intervalSpawn = intervalSpawn;
        this.initalLives = initialLives;
        this.initialEnergy = initialEnergy;
        this.energyGenerationRate = energyGenerationRate;
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

    public int getInitalLives() {
        return initalLives;
    }

    public int getInitialEnergy() {
        return initialEnergy;
    }

    public float getEnergyGenerationRate() {
        return energyGenerationRate;
    }
}
