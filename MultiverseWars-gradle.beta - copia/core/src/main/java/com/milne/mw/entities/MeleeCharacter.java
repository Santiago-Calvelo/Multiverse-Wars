package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;

public class MeleeCharacter extends Character {

    private Texture attack1Texture;
    private Texture attack2Texture;
    private Character targetEnemy;

    public MeleeCharacter(Texture texture, int hitboxWidth, int hitboxHeight,Texture attack1Texture, Texture attack2Texture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, int speed, Stage stage, String type, float attackCooldown) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityType, entityManager, speed, walk1Texture, walk2Texture, attack1Texture, attack2Texture, stage,type, attackCooldown);
    }

    // Implementación del ataque cuerpo a cuerpo
    @Override
    public void attack() {
        if (targetEnemy != null) {
            targetEnemy.takeDamage(1);  // Aplica daño solo si `targetEnemy` está asignado
            targetEnemy = null;  // Restablece `targetEnemy` después del ataque
        }
    }


    @Override
    public void checkForAttack() {
        int i = 0;
        boolean collisionDetected = false;
        int characterCount = entityManager.getCharacters().size;

        // Usamos un do-while para recorrer los personajes
        do {
            Character character = entityManager.getCharacters().get(i);
            if (this != character && this.getHitbox().overlaps(character.getHitbox()) && !character.getType().equalsIgnoreCase(this.getType())) {
                collisionDetected = true;
                targetEnemy = character;
                System.out.println(targetEnemy);
                tryAttack();
                stopMovementAndAttack();  // Detenemos y atacamos
            }
            i++;
        } while (i < characterCount && !collisionDetected);

        // Si no hay colisiones, continuar moviéndose
        if (!collisionDetected) {
            resumeMovement();
        }
    }
}
