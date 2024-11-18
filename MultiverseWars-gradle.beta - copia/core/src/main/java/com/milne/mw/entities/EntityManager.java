package com.milne.mw.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.milne.mw.difficulty.Difficulty;
import com.milne.mw.difficulty.Round;
import com.milne.mw.difficulty.RoundManager;
import com.milne.mw.maps.VictoryMenu;

import java.util.*;

public class EntityManager {
    private Stage stage;
    private Array<Character> characters;
    private ArrayList<Rectangle> placementHitboxes;
    private HashMap<Integer, Character> positionMap;
    private final float INITIAL_X = 33;
    private final float INITIAL_Y = 42;
    private final float CELL_WIDTH = 62;
    private final float CELL_HEIGHT = 79;
    private final int COLS = 10;
    private final int ROWS = 5;
    private final float HITBOX_SIZE = 55;
    private float spawnInterval;
    private float spawnAccumulator;
    private boolean isPaused = false;
    private Array<Projectile> projectiles;
    private Array<Bomb> bombs;
    private Difficulty difficultyLevel;
    private RoundManager roundManager;
    private int enemiesInGame = 0;
    private int currentRoundIndex = 0;
    private VictoryMenu victoryMenu;

    public EntityManager(Stage stage, Difficulty difficultyLevel) {
        this.stage = stage;
        this.difficultyLevel = difficultyLevel;
        characters = new Array<>();
        projectiles = new Array<>();
        bombs = new Array<>();
        this.placementHitboxes = new ArrayList<>();
        initPlacementPoints();
        roundManager = new RoundManager();
    }

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
    }

    public Character spawnEntity(EntityType entityType, float x, float y) {
        float adjustedX = x - (float) entityType.getHitboxWidth() / 2;
        float adjustedY = y - (float) entityType.getHitboxHeight() / 2;

        Character entity = entityType.getEntity(adjustedX, adjustedY, this);

        entity.scaleStats(difficultyLevel, currentRoundIndex);
        entity.getImage().setPosition(adjustedX, adjustedY);
        stage.addActor(entity.getImage());
        characters.add(entity);

        System.out.println(y);
        return entity;
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
        stage.addActor(projectile.getImage());
    }

    public void addBomb(Bomb bomb) {
        bombs.add(bomb);
        stage.addActor(bomb.getImage()); // Añadir la imagen de la bomba al escenario
    }

    public void removeBomb(Bomb bomb) {
        bombs.removeValue(bomb, true);
        stage.getActors().removeValue(bomb.getImage(), true);
        bomb.dispose();
    }

    public void removeProjectile(Projectile projectile) {
        projectiles.removeValue(projectile, true);
        stage.getActors().removeValue(projectile.getImage(), true);
        projectile.dispose();
    }

    public List<Rectangle> getPlacementHitboxes() {
            return placementHitboxes;
    }

    public void startEnemySpawner() {
        this.spawnInterval = difficultyLevel.getIntervalSpawn();
        this.spawnAccumulator = 0;
    }

    private void spawnRoundEnemy() {
        Round currentRound = roundManager.getRound(currentRoundIndex);

        System.out.println(currentRound.getRoundNumber());
        int i = 0;
        if (!currentRound.getEnemies().isEmpty()) {
            EntityType enemyType = currentRound.getEnemy(i);
            currentRound.getEnemies().remove(i);
            float y = currentRound.getyPosition(i);
            currentRound.getyPositions().remove(i);
            float x = stage.getViewport().getWorldWidth();
            spawnEntity(enemyType, x, y);
            spawnAccumulator = 0;
            enemiesInGame++;
            i++;
        } else if (enemiesInGame == 0) {
            currentRoundIndex++;
            scaleStatsAllPlacedCharacters();
        }

        if (currentRoundIndex == difficultyLevel.getMaxRound()) {
            victoryMenu.createVictoryMenu();
        }
    }

    private void scaleStatsAllPlacedCharacters() {
        for (Character character : characters) {
            character.scaleStats(difficultyLevel, currentRoundIndex);
        }
    }

    public void update(float delta) {
        if (!isPaused) {
            removeOffScreenCharacters();

            spawnAccumulator += delta;
            if (spawnAccumulator >= spawnInterval) {
                spawnRoundEnemy();
                spawnAccumulator = 0;
            }

            for (Character character : characters) {
                character.update(delta);
                character.checkForAttack(characters);
            }

            for (Projectile projectile : projectiles) {
                projectile.update(delta);
            }

            Iterator<Bomb> bombIterator = bombs.iterator();
            while (bombIterator.hasNext()) {
                Bomb bomb = bombIterator.next();
                bomb.update(delta);
                if (bomb.getIsToRemove()) {  // Verifica si la bomba ha explotado
                    bombIterator.remove(); // Elimina la bomba de la lista
                }
            }
        }
    }

    public void removeOffScreenCharacters() {
        Array<Character> charactersToRemove = new Array<>();

        for (Character character : characters) {
            if (character.getImage().getX() < 0) {
                System.out.println("Personaje fuera de la pantalla. Marcando para eliminar...");
                charactersToRemove.add(character);
            }
        }

        for (Character character : charactersToRemove) {
            removeCharacter(character);
        }
    }

    public void releasePosition(Character character) {
        Integer index = null;

        for (Map.Entry<Integer, Character> entry : positionMap.entrySet()) {
            if (entry.getValue() == character) {
                index = entry.getKey();
            }
        }

        if (index != null) {
            positionMap.remove(index);
        }
    }

    public void pause() {
        isPaused = true;
        for (Character character : characters) {
            character.pause();
        }
    }

    public void resume() {
        isPaused = false;
        for (Character character : characters) {
            character.resumeMovement();
        }
    }

    public void setVictoryMenu(VictoryMenu victoryMenu) {
        this.victoryMenu = victoryMenu;
    }

    public void setEnemiesInGame(int num) {
        enemiesInGame += num;
    }

    public Array<Character> getCharacters() {
        return characters;
    }

    public float getCellWidth() {
        return CELL_WIDTH;
    }

    public float getCellHeight() {
        return CELL_HEIGHT;
    }

    public void removeCharacter(Character character) {
        if (character.getType().equalsIgnoreCase("enemy")) {
            enemiesInGame--;
        }
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
