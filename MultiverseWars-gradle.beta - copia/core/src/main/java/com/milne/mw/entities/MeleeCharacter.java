package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class MeleeCharacter extends Character {
    private Character targetEnemy;
    private int damage = 0;

    public MeleeCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture attack1Texture,
                          Texture attack2Texture, Texture walk1Texture, Texture walk2Texture,
                          float x, float y, int lives, int speed, EntityManager entityManager,
                          String type, float attackCooldown, int damage, int energy) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed, walk1Texture,
            walk2Texture, attack1Texture, attack2Texture, type, attackCooldown, damage, energy);
    }

    // Implementación del ataque cuerpo a cuerpo
    @Override
    public void attack() {
        boolean toRemove = false;
        if (targetEnemy != null && targetEnemy.getDamage() != 0) {
            if (getDamage() == 0) {
                this.damage = targetEnemy.getLives();
                toRemove = true;
            } else {
                this.damage = getDamage();
            }
            targetEnemy.takeDamage(this.damage);  // Aplica daño solo si `targetEnemy` está asignado
            targetEnemy = null;  // Restablece `targetEnemy` después del ataque
        }

        if (toRemove) {
            this.takeDamage(getLives());
        }
    }


    @Override
    public void checkForAttack(Array<Character> characters) {
        if (characters.size == 0) {
            return; // Sale del método si no hay personajes que verificar
        }
        int i = 0;
        boolean collisionDetected = false;

        // Usamos un do-while para recorrer los personajes
        do {
            Character character = characters.get(i);
            if (this != character && this.getHitbox().overlaps(character.getHitbox()) && !character.getType().equalsIgnoreCase(this.getType())) {
                collisionDetected = true;
                targetEnemy = character;
                stopMovementAndAttack();  // Detenemos y atacamos
            }
            i++;
        } while (i < characters.size && !collisionDetected);

        // Si no hay colisiones, continuar moviéndose
        if (!collisionDetected) {
            resumeMovement();
        }
    }
}
