package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

public class RangedCharacter extends Character {
    private final Texture projectileTexture;
    private Character targetEnemy;
    private final int range;

    public RangedCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture walk1Texture,
                           Texture walk2Texture, Texture attack1Texture, Texture attack2Texture,
                           Texture projectileTexture, float x, float y, int lives,
                           int speed, EntityManager entityManager,
                           String type, int range, float attackCooldown, int damage, int energy, boolean canBeAttacked) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed,
            walk1Texture, walk2Texture, attack1Texture, attack2Texture, type, attackCooldown, damage, energy, canBeAttacked);
        this.projectileTexture = projectileTexture;
        this.range = range;
    }

    @Override
    public void attack() {
        float x;
        if (getType().equalsIgnoreCase("tower")) {
            x = image.getX() + image.getWidth();
        } else {
            x = image.getX() - image.getWidth();
        }
        Projectile projectile = new Projectile(projectileTexture, x,
            getHitboxCenter().y, entityManager, targetEnemy, getType(), getDamage());
        entityManager.addProjectile(projectile);
    }

    @Override
    public void checkForAttack(Array<Character> characters) {
        for (int i = 0; i < characters.size; i++) {
            Character enemy = characters.get(i);
            if (!enemy.getType().equalsIgnoreCase(getType())) {
                onEnemyInRange(enemy);
            }
        }

        if (targetEnemy != null && targetEnemy.getLives() <= 0) {
            if (getSpeed() != 0) {
                resumeMovement();
            }
            targetEnemy = null;
        }
    }

    public void onEnemyInRange(Character enemy) {
        if (enemy != this && isInSameRow(enemy) && isInRange(enemy) && isInFront(enemy) && enemy.getCanBeAttacked()) {
            targetEnemy = enemy;
            if (this.getSpeed() != 0) {
                stopMovementAndAttack();
            } else {
                tryAttack();
            }
        }
    }

    private boolean isInFront(Character enemy) {
        boolean isInFront = false;
        Rectangle thisHitbox = this.getHitbox();
        Rectangle enemyHitbox = enemy.getHitbox();

        if (enemy.getType().equalsIgnoreCase("enemy")) {
            if (thisHitbox.x < enemyHitbox.x) {
                isInFront = true;
            }
        } else {
            if (thisHitbox.x > enemyHitbox.x) {
                isInFront = true;
            }
        }

        return isInFront;
    }


    private boolean isInSameRow(Character enemy) {
        Rectangle thisHitbox = this.getHitbox();
        Rectangle enemyHitbox = enemy.getHitbox();

        // Verifica si los hitboxes se superponen en el eje Y
        return !(thisHitbox.y + thisHitbox.height < enemyHitbox.y || thisHitbox.y > enemyHitbox.y + enemyHitbox.height);
    }

    private boolean isInRange(Character enemy) {
        Rectangle thisHitbox = this.getHitbox();
        Rectangle enemyHitbox = enemy.getHitbox();

        float thisCenterX = thisHitbox.x + thisHitbox.width / 2;
        float thisCenterY = thisHitbox.y + thisHitbox.height / 2;

        float enemyCenterX = enemyHitbox.x + enemyHitbox.width / 2;
        float enemyCenterY = enemyHitbox.y + enemyHitbox.height / 2;

        float dx = enemyCenterX - thisCenterX;
        float dy = enemyCenterY - thisCenterY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        float attackRange = this.range * entityManager.getCellWidth();
        return distance <= attackRange;
    }

}
