package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Bomb {
    private Image image;
    private Circle explosionRange;
    private int damage;
    private float detonationTime;
    private float timeElapsed;
    private EntityManager entityManager;

    public Bomb(Texture texture, float x, float y, float rangeRadius, int damage, float detonationTime, EntityManager entityManager) {
        this.image = new Image(texture);
        this.image.setSize(30, 30);
        this.image.setPosition(x, y);
        this.explosionRange = new Circle(x + image.getWidth() / 2, y + image.getHeight() / 2, rangeRadius);
        this.damage = damage;
        this.detonationTime = detonationTime;
        this.entityManager = entityManager;

        moveDownward();
    }

    // Configura la acción de movimiento (caída de la bomba)
    private void moveDownward() {
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(image.getX(), 0);
        moveAction.setDuration(detonationTime);
        image.addAction(moveAction);
    }

    // Actualiza el tiempo y verifica la detonación
    public void update(float delta) {
        timeElapsed += delta;

        if (timeElapsed >= detonationTime) {
            detonate();
        }
    }

    // Maneja la lógica de explosión
    private void detonate() {
        // Daño a todos los enemigos en el rango
        for (Character character : entityManager.getCharacters()) {
            if (!character.getType().equalsIgnoreCase("tower") && explosionRange.contains(character.getImage().getX(), character.getImage().getY())) {
                character.takeDamage(damage);
            }
        }

        dispose(); // Elimina la bomba después de la explosión
    }

    public void dispose() {
        image.clearActions();
        image.remove();
    }

    public Image getImage() {
        return image;
    }

    public boolean isDetonated() {
        return timeElapsed >= detonationTime;
    }
}
