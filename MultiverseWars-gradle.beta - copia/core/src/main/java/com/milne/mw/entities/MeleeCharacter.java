package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;

public class MeleeCharacter extends Character {
    private Character targetEnemy;

    public MeleeCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture attack1Texture, Texture attack2Texture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, int speed, EntityManager entityManager, Stage stage, String type, float attackCooldown) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed, walk1Texture, walk2Texture, attack1Texture, attack2Texture, stage,type, attackCooldown);
    }

    // Implementación del ataque cuerpo a cuerpo
    @Override
    public void attack() {
        if (targetEnemy != null) {
            targetEnemy.takeDamage(15);  // Aplica daño solo si `targetEnemy` está asignado
            targetEnemy = null;  // Restablece `targetEnemy` después del ataque
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
