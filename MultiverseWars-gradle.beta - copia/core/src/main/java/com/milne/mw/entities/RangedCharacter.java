package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class RangedCharacter extends Character implements RangeListener {
    private Texture projectileTexture;
    private Character targetEnemy;
    private int range;

    public RangedCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture walk1Texture,
                           Texture walk2Texture, Texture attack1Texture, Texture attack2Texture,
                           Texture projectileTexture, float x, float y, int lives,
                           int speed, EntityManager entityManager,
                           Stage stage, String type, int range, float attackCooldown) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed,
            walk1Texture, walk2Texture, attack1Texture, attack2Texture, stage, type, attackCooldown);
        this.projectileTexture = projectileTexture;
        this.range = range;
    }

    @Override
    public void attack() {
        if (targetEnemy == null || !isInRange(targetEnemy)) return;
        Projectile projectile = new Projectile(projectileTexture, image.getX() + image.getWidth(),
            image.getY() + image.getHeight() / 2, entityManager, getType());
        entityManager.addProjectile(projectile);
    }

    @Override
    public void checkForAttack(Array<Character> characters) {
        for (int i = 0; i < characters.size; i++) {
            Character enemy = characters.get(i);
            onEnemyInRange(enemy);
        }
    }

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
