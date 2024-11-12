package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.milne.mw.renders.RenderManager;

public abstract class Character {
    protected Image image;
    private Rectangle hitbox;
    private int lives;
    private float x, y;
    private EntityType entityType;
    protected EntityManager entityManager;
    private int speed;
    private Texture walk1Texture, walk2Texture, attack1Texture, attack2Texture, deathTexture;
    private MoveToAction moveAction;
    private boolean isMoving, canAttack;
    private float attackCooldown, cooldownElapsed;
    private String type;
    private int damage, energy;
    private float deathDelay = 0.5f;
    private boolean isDying = false;

    public Character(Texture texture, float x, float y, int hitboxWidth, int hitboxHeight, int lives,
                     EntityManager entityManager, int speed,
                     Texture walk1Texture, Texture walk2Texture, Texture attack1Texture,
                     Texture attack2Texture, Texture deathTexture, String type,
                     float attackCooldown, int damage, int energy) {
        this.image = new Image(texture);
        this.image.setPosition(x, y);
        this.image.setSize(50, 50);
        this.hitbox = new Rectangle(x, y, hitboxWidth, hitboxHeight);
        this.lives = lives;
        this.x = x;
        this.y = y;
        this.entityManager = entityManager;
        this.walk1Texture = walk1Texture;
        this.walk2Texture = walk2Texture;
        this.attack1Texture = attack1Texture;
        this.attack2Texture = attack2Texture;
        this.deathTexture = deathTexture;
        this.isMoving = false;
        this.speed = speed;
        this.type = type;
        this.canAttack = true;
        this.attackCooldown = attackCooldown;
        this.cooldownElapsed = 0;
        this.damage = damage;
        this.energy = energy;

        if (speed != 0) {
            startMovement();
        }
    }

    public void startMovement() {
        if (!isMoving) {
            moveAction = new MoveToAction();
            float targetX = -image.getWidth();
            float distanceX = image.getX() - targetX;
            float duration = distanceX / speed;
            moveAction.setPosition(targetX, image.getY());
            moveAction.setDuration(duration);
            image.addAction(moveAction);
            isMoving = true;
        }
    }

    public void update(float delta) {
        // Actualización del cooldown de ataque
        if (cooldownElapsed < attackCooldown) {
            cooldownElapsed += delta;
        } else {
            canAttack = true;
            cooldownElapsed = 0;
        }
    }

    public void tryAttack() {
        if (canAttack) {
            attack();
            RenderManager.getInstance().animateCharacterAttack(this, attackCooldown);
            canAttack = false;
            cooldownElapsed = 0;
        }
    }

    public abstract void attack();
    public abstract void checkForAttack(Array<Character> characters);

    public void takeDamage(int damage) {
        this.lives -= damage;
        if (lives <= 0 && !isDying) {
            RenderManager.getInstance().animateDead(this);
        }
    }

    public void removeCharacter() {
        dispose();
        entityManager.releasePosition(this);
        entityManager.getCharacters().removeValue(this, true);
    }

    public void pause() {
        if (isMoving) {
            image.clearActions();
            isMoving = false;
        }
    }

    public void resumeMovement() {
        if (!isMoving && speed != 0) {
            startMovement();
        }
    }

    public void stopMovementAndAttack() {
        if (isMoving) {
            image.clearActions();
            isMoving = false;
        }
        tryAttack();
    }

    public Image getImage() {
        return image;
    }
    public String getType() {
        return type;
    }
    public Texture getWalk1Texture() {
        return walk1Texture;
    }
    public Texture getWalk2Texture() {
        return walk2Texture;
    }
    public Texture getAttack1Texture() {
        return attack1Texture;
    }
    public Texture getAttack2Texture() { return attack2Texture; }
    public Texture getDeathTexture() { return deathTexture; }

    public Rectangle getHitbox() {
        hitbox.setPosition(image.getX(), image.getY());
        return hitbox;
    }

    public int getDamage() { return damage; }
    public int getEnergy() { return energy; }
    public int getSpeed() {
        return speed;
    }
    public int getLives() {
        return lives;
    }
    public float getX() {
        return x;
    }

    public void dispose() {
        image.clearActions();
        image.remove();
    }
}
