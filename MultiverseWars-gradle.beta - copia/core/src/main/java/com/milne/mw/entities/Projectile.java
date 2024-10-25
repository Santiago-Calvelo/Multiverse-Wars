package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.milne.mw.entities.EntityManager;

public class Projectile {
    private Image image;
    private Rectangle hitbox;
    private int damage = 1;
    private Stage stage;
    private EntityManager entityManager;
    private String type;
    private Timer.Task collisionTask;  // Referencia al Timer.Task específico para la colisión

    public Projectile(Texture texture, float x, float y, Stage stage, EntityManager entityManager, String type) {
        this.image = new Image(texture);
        this.image.setSize(20, 20);
        this.image.setPosition(x, y);
        this.stage = stage;
        this.hitbox = new Rectangle(x, y, 20, 20);
        this.entityManager = entityManager;
        this.type = type;
        moveAction();
    }

    private void moveAction() {
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(stage.getWidth(), image.getY());  // Mover el proyectil fuera del escenario
        moveAction.setDuration(2);  // El tiempo que toma el proyectil en recorrer la pantalla
        image.addAction(moveAction);

        // Programar el Timer.Task para la colisión y asignarlo a collisionTask
        collisionTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                updateHitbox();
                checkForCollision();
            }
        }, 0, 0.1f);  // Revisamos las colisiones cada 0.1 segundos
    }

    private void updateHitbox() {
        hitbox.setPosition(image.getX(), image.getY());
    }

    private void checkForCollision() {
        for (int i = 0; i < entityManager.getCharacters().size; i++) {
            Character character = entityManager.getCharacters().get(i);

            if (hitbox.overlaps(character.getHitbox()) && !this.getType().equalsIgnoreCase(character.getType())) {
                character.takeDamage(damage);
                Gdx.app.log("Projectile", "Character hit! Damage dealt: " + damage);
                image.remove();  // Eliminar el proyectil visualmente
                stage.getActors().removeValue(image, true);

                // Cancelar el Timer.Task específico para la colisión
                if (collisionTask != null) {
                    collisionTask.cancel();
                }
                break;
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public String getType() {
        return type;
    }
}

