package com.milne.mw.entities;

public interface ProximityListener {
    void onEnemyInRange(Character enemy);
    void onEnemyOutOfRange(Character enemy);
}

