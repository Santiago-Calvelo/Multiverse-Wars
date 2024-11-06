package com.milne.mw.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.*;

public class EntityManager {
    private Stage stage;
    private Array<Character> characters;
    private Viewport viewport;
    private List<Rectangle> placementHitboxes;
    private HashMap<Integer, Character> positionMap;
    private final float INITIAL_X = 33;
    private final float INITIAL_Y = 42;
    private final float CELL_WIDTH = 62;
    private final float CELL_HEIGHT = 79;
    private final int COLS = 10;
    private final int ROWS = 5;
    private final float HITBOX_SIZE = 55;
    private float spawnInterval;
    private float spawnAccumulator;  // Acumulador para controlar el spawn de enemigos
    private boolean isPaused = false;
    private Array<Projectile> projectiles;

    public EntityManager(Stage stage, Viewport viewport) {
        this.stage = stage;
        this.viewport = viewport;
        characters = new Array<>();
        projectiles = new Array<>();
        this.placementHitboxes = new ArrayList<>();
        initPlacementPoints();
    }

    // Inicializar los puntos de colocación y sus hitboxes
    public void initPlacementPoints() {
        placementHitboxes.clear();
        positionMap = new HashMap<>();
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                float centerX = INITIAL_X + i * CELL_WIDTH;
                float centerY = INITIAL_Y + j * CELL_HEIGHT;

                Rectangle hitbox = new Rectangle(
                    centerX - HITBOX_SIZE / 2,
                    centerY - HITBOX_SIZE / 2,
                    HITBOX_SIZE,
                    HITBOX_SIZE
                );

                placementHitboxes.add(hitbox);
            }
        }
    }

    // Método para colocar una entidad en el mapa
    public void handleEntityPlacement(EntityType entityType, float x, float y, float cardWidth, float cardHeight) {
        boolean placed = false;
        int i = 0;
        Rectangle cardArea = new Rectangle(x, y, cardWidth, cardHeight);

        do {
            Rectangle hitbox = placementHitboxes.get(i);
            if (!positionMap.containsKey(i) && hitbox.overlaps(cardArea)) {
                float centerX = hitbox.x + hitbox.width / 2;
                float centerY = hitbox.y + hitbox.height / 2;
                Character entity = spawnEntity(entityType, centerX, centerY);
                positionMap.put(i, entity);
                placed = true;
            }
            i++;
        } while (!placed && i < placementHitboxes.size());

        if (!placed) {
            System.out.println("No se encontró un punto válido cerca de x = " + x + ", y = " + y);
        }
    }

    // Colocar la entidad, centrando la hitbox sobre el punto
    public Character spawnEntity(EntityType entityType, float x, float y) {
        float adjustedX = x - (float) entityType.getHitboxWidth() / 2;
        float adjustedY = y - (float) entityType.getHitboxHeight() / 2;

        Character entity = entityType.getEntity(adjustedX, adjustedY, stage, this);

        entity.getImage().setPosition(adjustedX, adjustedY);
        stage.addActor(entity.getImage());
        characters.add(entity);

        return entity;
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        stage.addActor(projectile.getImage());
    }

    public void removeProjectile(Projectile projectile) {
        projectiles.removeValue(projectile, true);
        stage.getActors().removeValue(projectile.getImage(), true);
        projectile.dispose();
    }

    // Obtener las hitboxes de colocación para poder dibujarlas
    public List<Rectangle> getPlacementHitboxes() {
        return placementHitboxes;
    }

    // Inicia el temporizador interno de spawn usando un acumulador
    public void startEnemySpawner(float spawnInterval) {
        this.spawnInterval = spawnInterval;
        this.spawnAccumulator = 0;  // Reiniciar acumulador de spawn
    }

    private void spawnRandomEnemy() {
        Random random = new Random();
        int randomRow = random.nextInt(ROWS);
        Rectangle rowHitbox = placementHitboxes.get(randomRow);
        float spawnY = rowHitbox.y + (rowHitbox.height / 2);
        float spawnX = viewport.getWorldWidth();
        spawnEntity(EntityType.SKELETON, spawnX, spawnY);
    }

    // Actualización por frame
    public void update(float delta) {
        if (!isPaused) {
            // Acumulador para el spawn de enemigos
            spawnAccumulator += delta;
            if (spawnAccumulator >= spawnInterval) {
                spawnRandomEnemy();
                spawnAccumulator = 0;  // Reiniciar el acumulador
            }

            for (Character character : characters) {
                character.update(delta);
                character.checkForAttack(characters);
            }

            for (Projectile projectile : projectiles) {
                projectile.update(delta);
            }
        }
    }


    public void removeOffScreenCharacters() {
        Array<Character> charactersToRemove = new Array<>();  // Lista de personajes para eliminar

        for (Character character : characters) {
            // Verifica si el personaje ha salido de la pantalla para marcarlo como eliminado
            if (character.getImage().getX() < 0) {
                System.out.println("Personaje fuera de la pantalla. Marcando para eliminar...");
                charactersToRemove.add(character);  // Añadir a la lista de personajes a eliminar
            }
        }

        // Eliminar los personajes fuera de pantalla de manera controlada
        for (Character character : charactersToRemove) {
            removeCharacter(character);
        }
    }

    public void releasePosition(Character character) {
        Integer index = null;

        // Buscar la posición ocupada por el personaje
        for (Map.Entry<Integer, Character> entry : positionMap.entrySet()) {
            if (entry.getValue() == character) {
                index = entry.getKey();
            }
        }

        if (index != null) {
            positionMap.remove(index);  // Liberar la casilla del HashMap
        }
    }

    // Método para pausar el juego
    public void pause() {
        isPaused = true;
        for (Character character : characters) {
            character.pause();
        }
    }

    // Método para reanudar el juego
    public void resume() {
        isPaused = false;
        for (Character character : characters) {
            character.resumeMovement();
        }
    }

    public Array<Character> getCharacters() {
        return characters;
    }

    public float getCellWidth() {
        return CELL_WIDTH;
    }

    public void removeCharacter(Character character) {
        character.getImage().remove();
        stage.getActors().removeValue(character.getImage(), true);
        characters.removeValue(character, true);
        character.dispose();
    }

    public void dispose() {
        for (Character character : characters) {
            removeCharacter(character);
        }
        characters.clear();
    }
}
