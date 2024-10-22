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

    public MeleeCharacter(Texture texture, Texture attack1Texture, Texture attack2Texture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, boolean canMove, Stage stage, String type) {
        super(texture, x, y, lives, entityType, entityManager, canMove, walk1Texture, walk2Texture, stage,type);
        this.attack1Texture = attack1Texture;
        this.attack2Texture = attack2Texture;
    }

    // Implementación del ataque cuerpo a cuerpo
    @Override
    public void attack() {
        // Cambiar la textura para reflejar el ataque
        image.setDrawable(new TextureRegionDrawable(new TextureRegion(attack1Texture)));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(attack2Texture)));
            }
        }, 0.5f);
    }

    @Override
    public void checkForAttack() {
        int i = 0;
        boolean collisionDetected = false;
        int characterCount = entityManager.getCharacters().size;

        // Usamos un do-while para recorrer los personajes
        do {
            Character character = entityManager.getCharacters().get(i);

            // Si colisionamos con un enemigo y no es el propio personaje
            if (this != character && super.hitbox.overlaps(character.getHitbox()) && !character.getType().equalsIgnoreCase(this.getType())) {
                collisionDetected = true;

                character.takeDamage(5);  // Aquí llamamos al método general takeDamage()
                Gdx.app.log("Skeleton", "Character hit! Damage dealt: " + 5);

                // Si hay una colisión, detenemos el movimiento
                stopMovementAndAttack();  // Detenemos y atacamos
            }
            i++;
        } while (i < characterCount && !collisionDetected);

        // Si no hay colisiones, continuar moviéndose
        if (!collisionDetected) {
            resumeMovement();
        }
    }

    // Método para detener el movimiento y atacar
    private void stopMovementAndAttack() {
        image.clearActions();  // Detener todas las acciones (incluyendo el movimiento)
        attack();  // Iniciar el ataque
    }

    // Método para reanudar el movimiento si no hay colisiones
    private void resumeMovement() {
        if (!image.hasActions() && super.moveAction != null) {
            image.addAction(super.moveAction);  // Reanudar el movimiento
        }
    }
}
