package com.milne.mw.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.milne.mw.difficulty.Difficulty;
import com.milne.mw.difficulty.Round;
import com.milne.mw.difficulty.RoundManager;
import com.milne.mw.entities.boss.BossCharacter;
import com.milne.mw.menu.VictoryMenu;
import com.milne.mw.player.Player;
import com.milne.mw.renders.BossAnimator;

import java.util.*;

import static com.milne.mw.Global.loadTexture;

public class EntityManager {
    private Stage stage;
    private Difficulty difficultyLevel;
    private Player player;
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
    private RoundManager roundManager;
    private int enemiesInGame = 0;
    private int currentRoundIndex = 0;
    private VictoryMenu victoryMenu;
    private boolean bossIsAlive = false;
    private Character bossFinal;

    public EntityManager(Stage stage, Difficulty difficultyLevel, Player player) {
        this.stage = stage;
        this.difficultyLevel = difficultyLevel;
        this.player = player;
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

        if (entityType.getEnergy() <= player.getEnergy()) {
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
            player.modifyEnergy(-entityType.getEnergy());
        }
    }

    public Character spawnEntity(EntityType entityType, float x, float y) {
        float adjustedX = x - (float) entityType.getHitboxWidth() / 2;
        float adjustedY = y - (float) entityType.getHitboxHeight() / 2;

        Character entity = entityType.getEntity(adjustedX, adjustedY, this);
        entity.scaleStats(difficultyLevel, currentRoundIndex);
        entity.getImage().setPosition(adjustedX, adjustedY);
        stage.addActor(entity.getImage());
        characters.add(entity);

        return entity;
    }

    public void spawnBossFinal(Character bossFinal, float x, float y) {
        bossFinal.getImage().setPosition(x, y);
        stage.addActor(bossFinal.getImage());
        characters.add(bossFinal);
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

        if (!currentRound.getEnemies().isEmpty()) {
            EntityType enemyType = currentRound.getEnemy(0);
            currentRound.getEnemies().remove(0);
            float y = currentRound.getyPosition(0);
            currentRound.getyPositions().remove(0);
            float x = stage.getViewport().getWorldWidth();
            spawnEntity(enemyType, x, y);
            spawnAccumulator = 0;
            enemiesInGame++;
        } else if (enemiesInGame == 0 && !bossIsAlive) {
            currentRoundIndex++;
            scaleStatsAllPlacedCharacters();
        }

        if (currentRoundIndex == difficultyLevel.getMaxRound() && !bossIsAlive) {
            bossIsAlive = true;
            BossAnimator animator = new BossAnimator(stage);
            int hitboxWidth = 50;
            int hitboxHeight = 50;
            float adjustedX = stage.getViewport().getWorldWidth() - (float) hitboxWidth / 2;
            float adjustedY = 200.0f - (float) hitboxHeight / 2;
            bossFinal = new BossCharacter(
                loadTexture("characters/boss/boss.png"),
                adjustedX,
                adjustedY,
                hitboxWidth, hitboxHeight,
                3000,
                this,
                25,
                loadTexture("characters/boss/boss.png"),
                loadTexture("characters/boss/boss.png"),
                loadTexture("characters/boss/force-aura.png"),
                "enemy", 1f, 30, 0, animator
            );
            spawnBossFinal(bossFinal, adjustedX, adjustedY);
        } else if(currentRoundIndex > difficultyLevel.getMaxRound()) {
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
            if (bossIsAlive) {
                if (bossFinal.getLives() <= 0) {
                    bossIsAlive = false;
                    bossFinal = null;
                    currentRoundIndex++;
                }
            }

            player.update(delta);

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

            for (Bomb bomb : bombs) {
                bomb.update(delta);
            }

        }
    }

    public void handleRightClick(float clickX, float clickY) {
        boolean sold = false;
        int i = 0;
        do {
            Rectangle hitbox = placementHitboxes.get(i);
            if (hitbox.contains(clickX, clickY) && positionMap.containsKey(i)) {
                Character character = positionMap.get(i);

                if (character.getType().equalsIgnoreCase("tower")) {
                    player.modifyEnergy(Math.round(character.getEnergy() * 0.6f));
                    positionMap.remove(i);
                    removeCharacter(character);
                    sold = true;
                }
            }
            i++;
        } while(i < placementHitboxes.size() && !sold);
    }

    public void removeOffScreenCharacters() {
        for (int i = 0; i < characters.size; i++) {
            Character character = characters.get(i);

            if (character.getImage().getX() < 0) {
                character.takeDamage(character.getLives());
                System.out.println(character.getLives());
            }
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

    public Difficulty getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setEnemiesInGame(int num) {
        enemiesInGame += num;
    }

    public int getRound() {
        return roundManager.getRound(currentRoundIndex).getRoundNumber();
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
            player.modifyEnergy(character.getEnergy());
            enemiesInGame--;
        }
        releasePosition(character);
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

    public Array<Bomb> getBombs() {
        return bombs;
    }

    public HashMap<Integer, Character> getPositionMap() {
        return positionMap;
    }
}
