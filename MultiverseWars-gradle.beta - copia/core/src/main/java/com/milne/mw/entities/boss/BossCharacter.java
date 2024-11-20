package com.milne.mw.entities.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.utils.Array;
import com.milne.mw.entities.Character;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.renders.BossAnimator;

import java.util.HashMap;
import java.util.Random;

public class BossCharacter extends Character {
    private float forceSmashAccumulator = 0f;
    private float moveForceAccumulator = 0f;
    private boolean isExecutingSpecial = false;
    private float switchLaneAccumulator = 0f;
    private float specialDuration = 0f;
    private BossAttacks lastAttack;
    private BossAnimator animator;
    private BossAttacks currentSpecialAttack = null;
    private HashMap<BossAttacks, BossAttack> attackMap = new HashMap<>();
    private float hitboxWidth, hitboxHeight;


    public BossCharacter(Texture texture, float x, float y, int hitboxWidth, int hitboxHeight, int lives,
                         EntityManager entityManager, int speed, Texture walkTexture, Texture attackTexture, Texture forceSmash, String type, float attackCooldown, int damage, int energy, BossAnimator animator) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed,
            walkTexture, walkTexture, attackTexture, attackTexture, type, attackCooldown, damage, energy);
        this.animator = animator;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        attackMap.put(BossAttacks.FORCE_SMASH, new ForceSmashAttack(forceSmash));
        attackMap.put(BossAttacks.MOVE_FORCE, new MoveForceAttack());
    }

    @Override
    public void update(float delta) {
        super.update(delta);

        if (getLives() > 0) {
            switchLaneAccumulator += delta;
            if (switchLaneAccumulator >= 10f) {
                switchLane();
            }

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

                if (forceSmashAccumulator >= 5f && lastAttack != BossAttacks.FORCE_SMASH) { // Cooldown de Force Smash
                    startSpecialAttack(BossAttacks.FORCE_SMASH);
                } else if (moveForceAccumulator >= 5f && lastAttack != BossAttacks.MOVE_FORCE) { // Cooldown de Move Force
                    startSpecialAttack(BossAttacks.MOVE_FORCE);
                }
            }
        }
    }

    private void startSpecialAttack(BossAttacks attack) {
        BossAttack bossAttack = attackMap.get(attack);

        if (bossAttack != null) {
            isExecutingSpecial = true;
            specialDuration = 0f;
            currentSpecialAttack = attack;
            animator.reset();
            bossAttack.execute(this, entityManager, animator);
            lastAttack = attack;
            resetAccumulatorForAttack(attack);
        }
    }

    private void resetAccumulatorForAttack(BossAttacks attack) {
        switch (attack) {
            case FORCE_SMASH:
                forceSmashAccumulator = 0f;
                break;
            case MOVE_FORCE:
                moveForceAccumulator = 0f;
                break;
        }
    }

    private float getCurrentSpecialDuration() {
        return attackMap.get(currentSpecialAttack).getDuration();
    }

    protected Circle getRange() {
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

        return new Circle(image.getX() + image.getWidth() / 2, image.getY() + image.getHeight() / 2, forceSmashRadius);
    }

    private void switchLane() {
        switchLaneAccumulator = 0f;
        float[] validYlanes = { 358.0f, 279.0f, 200.0f, 121.0f, 42.0f };
        float targetY;
        Random random = new Random();
        do {
            targetY = validYlanes[random.nextInt(validYlanes.length)] - hitboxHeight / 2;
        } while (targetY == image.getY());

        pause();

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(image.getX(), targetY);
        moveAction.setDuration(1f);

        RunnableAction onCompleteAction = new RunnableAction();
        onCompleteAction.setRunnable(this::resumeMovement);

        getImage().addAction(Actions.sequence(moveAction, onCompleteAction));
    }

    protected Character findClosestTower() {
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


    public void finishSpecial() {
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
