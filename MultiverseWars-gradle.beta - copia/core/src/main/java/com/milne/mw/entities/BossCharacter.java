package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.Random;

public class BossCharacter extends Character {
    private float forceSmashAccumulator = 0f;
    private float moveForceAccumulator = 0f;
    private boolean isExecutingSpecial = false;
    private float specialDuration = 0f;
    private final float forceSmashDuration = 3f;
    private final float moveForceDuration = 1f;

    private BossAttacks currentSpecialAttack = null;

    public BossCharacter(Texture texture, float x, float y, int hitboxWidth, int hitboxHeight, int lives,
                         EntityManager entityManager, int speed, Texture walkTexture, Texture attackTexture, Texture forceSmash, String type, float attackCooldown, int damage, int energy) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed,
            walkTexture, walkTexture, attackTexture, attackTexture, type, attackCooldown, damage, energy);
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (getLives() > 0) {
            if (isExecutingSpecial) {
                // Continúa ejecutando el ataque especial actual
                specialDuration += delta;
                if (specialDuration >= getCurrentSpecialDuration()) {
                    finishSpecial(); // Finaliza el ataque especial
                }
            } else {
                // Acumula tiempo para activar el siguiente ataque especial
                forceSmashAccumulator += delta;
                moveForceAccumulator += delta;

                if (forceSmashAccumulator >= 10f) { // Cooldown de Force Smash
                    startSpecialAttack(BossAttacks.FORCE_SMASH);
                } else if (moveForceAccumulator >= 1f) { // Cooldown de Move Force
                    startSpecialAttack(BossAttacks.MOVE_FORCE);
                }
            }
        }
    }

    private void startSpecialAttack(BossAttacks attack) {
        isExecutingSpecial = true;
        specialDuration = 0f; // Reinicia la duración del ataque
        currentSpecialAttack = attack; // Establece el ataque actual

        switch (attack) {
            case FORCE_SMASH:
                startForceSmash();
                forceSmashAccumulator = 0f; // Reinicia el acumulador
                break;
            case MOVE_FORCE:
                startMoveForce();
                moveForceAccumulator = 0f; // Reinicia el acumulador
                break;
        }
    }

    private float getCurrentSpecialDuration() {
        // Retorna la duración del ataque especial actual
        switch (currentSpecialAttack) {
            case FORCE_SMASH:
                return forceSmashDuration;
            case MOVE_FORCE:
                return moveForceDuration;
            default:
                return 0f; // Fallback
        }
    }

    private void startForceSmash() {
        System.out.println("Force Smash activado!");
        float forceSmashRadius = (float) Math.sqrt(entityManager.getCellWidth() * entityManager.getCellWidth() +
            entityManager.getCellHeight() * entityManager.getCellHeight());

        switch (entityManager.getDifficultyLevel()) {
            case EASY:
                forceSmashRadius *= 1;
                break;
            case MEDIUM:
                forceSmashRadius *= 2;
                break;
            case HARD:
                forceSmashRadius *= 2.5f;
                break;
        }

        Circle forceSmashRange = new Circle(image.getX() + image.getWidth() / 2, image.getY() + image.getHeight() / 2, forceSmashRadius);

        for (int i = 0; i < entityManager.getCharacters().size; i++) {
            Character enemy = entityManager.getCharacters().get(i);
            float characterX = enemy.getImage().getX() + enemy.getImage().getWidth() / 2;
            float characterY = enemy.getImage().getY() + enemy.getImage().getHeight() / 2;
            if (enemy.getType().equalsIgnoreCase("tower") && forceSmashRange.contains(characterX, characterY)) {
                enemy.takeDamage(30);
            }
        }
    }

    private void startMoveForce() {
        System.out.println("Move Force activado!");
        Character closestCharacter = findClosestTower();
        if (closestCharacter != null) {
            moveTowerToRandomPosition(closestCharacter);
        } else {
            finishSpecial(); // Si no hay torre, termina el especial
        }
    }

    private Character findClosestTower() {
        Character closestCharacter = null;
        float closestDistance = Float.MAX_VALUE;

        for (int i = 0; i < entityManager.getCharacters().size; i++) {
            Character character = entityManager.getCharacters().get(i);
            if (character.getType().equalsIgnoreCase("tower")) {
                float distance = (float) Math.sqrt(
                    Math.pow(character.getImage().getX() - this.getImage().getX(), 2) +
                        Math.pow(character.getImage().getY() - this.getImage().getY(), 2)
                );
                if (distance < closestDistance) {
                    closestDistance = distance;
                    closestCharacter = character;
                }
            }
        }

        return closestCharacter;
    }

    private void moveTowerToRandomPosition(Character tower) {
        Random random = new Random();
        Rectangle targetHitbox;
        int newIndex;

        // Determina una nueva posición válida
        do {
            newIndex = random.nextInt(entityManager.getPlacementHitboxes().size());
            targetHitbox = entityManager.getPlacementHitboxes().get(newIndex);
        } while (entityManager.getPositionMap().containsKey(newIndex));

        // Calcula las coordenadas de destino
        float targetX = targetHitbox.x + targetHitbox.width / 2 - tower.getImage().getWidth() / 2;
        float targetY = targetHitbox.y + targetHitbox.height / 2 - tower.getImage().getHeight() / 2;

        // Libera la posición actual
        entityManager.releasePosition(tower);

        // Pausa el movimiento del personaje
        tower.pause();

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(targetX, targetY);
        moveAction.setDuration(moveForceDuration);

        // Define la acción a ejecutar al completar el movimiento
        RunnableAction onCompleteAction = new RunnableAction();
        int finalNewIndex = newIndex;
        onCompleteAction.setRunnable(() -> {
            // Ocupa la nueva posición
            entityManager.getPositionMap().put(finalNewIndex, tower);

            // Reanuda el movimiento de la torre y finaliza el ataque especial
            tower.resumeMovement();
            finishSpecial();
        });

        // Añade las acciones al personaje
        tower.getImage().addAction(Actions.sequence(moveAction, onCompleteAction));
    }


    private void finishSpecial() {
        isExecutingSpecial = false;
        currentSpecialAttack = null; // Limpia el ataque especial actual
        System.out.println("Especial terminado.");
    }

    @Override
    public void attack() {
        System.out.println("Ataque básico de sable láser.");
    }

    @Override
    public void checkForAttack(Array<Character> characters) {
    }
}
