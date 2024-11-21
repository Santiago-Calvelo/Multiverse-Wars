package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;
import com.milne.mw.entities.boss.BossCharacter;

public class MeleeCharacter extends Character {
    private Character targetEnemy;
    public MeleeCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture attack1Texture,
                          Texture attack2Texture, Texture walk1Texture, Texture walk2Texture,
                          float x, float y, int lives, int speed, EntityManager entityManager,
                          String type, float attackCooldown, int damage, int energy, boolean canBeAttacked) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed, walk1Texture,
            walk2Texture, attack1Texture, attack2Texture, type, attackCooldown, damage, energy, canBeAttacked);
    }

    // Implementación del ataque cuerpo a cuerpo
    @Override
    public void attack() {
        boolean toRemove = false;
        if (targetEnemy != null && targetEnemy.getCanBeAttacked()) {
            if (!this.getCanBeAttacked()) {
                toRemove = true;
            }
            targetEnemy.takeDamage(getDamage());
            targetEnemy = null;
        }

        if (toRemove) {
            entityManager.removeCharacter(this);
        }
    }


    @Override
    public void checkForAttack(Array<Character> characters) {
        int i = 0;
        boolean collisionDetected = false;

        // Usamos un do-while para recorrer los personajes
        do {
            Character character = characters.get(i);
            if (this != character && this.getHitbox().overlaps(character.getHitbox()) && !character.getType().equalsIgnoreCase(this.getType())) {
                collisionDetected = true;
                targetEnemy = character;
                if (!this.getCanBeAttacked() && !(targetEnemy instanceof BossCharacter)) {
                    targetEnemy.pause();
                }
                if (this.getSpeed() != 0) {
                    stopMovementAndAttack();
                } else {
                    tryAttack();
                }
            }
            i++;
        } while (i < characters.size && !collisionDetected);

        // Si no hay colisiones, continuar moviéndose
        if (!collisionDetected) {
            resumeMovement();
        }
    }
}
