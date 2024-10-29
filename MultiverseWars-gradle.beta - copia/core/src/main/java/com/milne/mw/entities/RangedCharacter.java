package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class RangedCharacter extends Character implements RangeListener {
    private Texture projectileTexture;
    private Character targetEnemy;
    private int range;

    public RangedCharacter(Texture texture, int hitboxWidth, int hitboxHeight,
                           Texture attack1Texture, Texture attack2Texture, Texture projectileTexture,
                           Texture walk1Texture, Texture walk2Texture, float x, float y, int lives,
                           EntityType entityType, EntityManager entityManager, int speed,
                           Stage stage, String type, int range, float attackCooldown) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityType, entityManager, speed,
            walk1Texture, walk2Texture, attack1Texture, attack2Texture, stage, type, attackCooldown);
        this.projectileTexture = projectileTexture;
        this.range = range;
    }

    @Override
    public void attack() {
        if (targetEnemy == null || !isInRange(targetEnemy)) return;
        Projectile projectile = new Projectile(projectileTexture, image.getX() + image.getWidth(),
            image.getY() + image.getHeight() / 2, stage, entityManager, getType());
        stage.addActor(projectile.getImage());
    }

    @Override
    public void checkForAttack() {}

    @Override
    public void onEnemyInRange(Character enemy) {
        if (enemy != this && isInSameRow(enemy) && isInRange(enemy)) {
            targetEnemy = enemy;
            tryAttack();
        }
    }

    private boolean isInSameRow(Character enemy) {
        return this.getImage().getY() == enemy.getImage().getY();
    }

    private boolean isInRange(Character enemy) {
        float dx = enemy.getImage().getX() - this.getImage().getX();
        float dy = enemy.getImage().getY() - this.getImage().getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float attackRange = this.range * entityManager.getCellWidth();
        return distance <= attackRange;
    }
}
