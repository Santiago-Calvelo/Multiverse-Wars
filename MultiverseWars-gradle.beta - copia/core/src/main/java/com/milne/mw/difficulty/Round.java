package com.milne.mw.difficulty;

import com.milne.mw.entities.EntityType;
import java.util.ArrayList;
import java.util.List;

public class Round {
    private ArrayList<EntityType> enemies;

    // Constructor opcional para añadir enemigos de forma directa
    public Round(EntityType... enemies) {
        this.enemies = new ArrayList<>();
        for (EntityType enemy : enemies) {
            this.enemies.add(enemy);
        }
    }

    // Método para agregar enemigos de uno en uno
    public void addEnemy(EntityType enemy) {
        enemies.add(enemy);
    }

    // Método para obtener la lista de enemigos en la ronda
    public List<EntityType> getEnemies() {
        return enemies;
    }
}
