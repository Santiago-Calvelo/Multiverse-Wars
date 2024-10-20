package com.milne.mw.entities;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

public class EntityManager {
    private Stage stage;
    private Array<Character> characters;
    private Viewport viewport;  // Necesitamos el viewport para los cálculos de la cuadrícula

    public EntityManager(Stage stage, Viewport viewport) {
        this.stage = stage;
        this.viewport = viewport;
        characters = new Array<>();
    }

    // Método que maneja la colocación de entidades en el mapa
    public void handleEntityPlacement(EntityType entityType, float x, float y) {
        int gridCols = 9;  // Número de columnas de la cuadrícula
        int gridRows = 6;  // Número de filas de la cuadrícula

        // Calcular la posición en la cuadrícula
        int col = (int) (x / (viewport.getWorldWidth() / gridCols));
        int row = (int) (y / (viewport.getWorldHeight() * 2 / 3 / gridRows));

        // Verificar si la posición está dentro de la cuadrícula
        if (col >= 0 && col < gridCols && row >= 0 && row < gridRows) {
            float cellX = col * (viewport.getWorldWidth() / gridCols);
            float cellY = row * ((viewport.getWorldHeight() * 2 / 3) / gridRows);

            // Crear la entidad y colocarla en el escenario
            spawnEntity(entityType, cellX, cellY);
        }
    }

    public void spawnEntity(EntityType entityType, float x, float y) {
        Character entity = entityType.getEntity(x, y, stage, this);  // Crear la entidad desde el enum
        stage.addActor(entity.getImage());  // Añadir la entidad al escenario
        characters.add(entity);  // Añadir la entidad a la lista de personajes para renderizado y gestión
    }

    public Array<Character> getCharacters() {
        return characters;
    }

    public void startEnemySpawner(float spawnInterval) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnRandomEnemy();
            }
        }, 0, spawnInterval);  // Spawnear enemigos regularmente
    }

    private void spawnRandomEnemy() {
        int gridRows = 6;
        float cellHeight = stage.getHeight() * 2 / 3 / gridRows;
        int randomRow = (int) (Math.random() * gridRows);
        float spawnY = randomRow * cellHeight;
        float spawnX = stage.getWidth();
        spawnEntity(EntityType.SKELETON, spawnX, spawnY);  // Spawnear un enemigo Skeleton
    }
}
