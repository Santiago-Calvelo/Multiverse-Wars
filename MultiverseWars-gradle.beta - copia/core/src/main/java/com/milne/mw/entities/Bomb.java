package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

import static com.milne.mw.Global.loadTexture;

public class Bomb {
    private Image image;
    private boolean isDetonated = false;
    private Circle explosionRange;
    private int damage;
    private EntityManager entityManager;
    private float targetY; // Posición más baja del globo
    private float explosionDisplayTime = 0.5f; // Tiempo para mostrar la explosión (en segundos)
    private float explosionTimer = 0;

    public Bomb(float x, float y, int damage, EntityManager entityManager, float targetY) {
        this.image = new Image(loadTexture("characters/projectile/bomba.png"));
        this.image.setSize(30, 30); // Tamaño inicial de la bomba
        this.image.setPosition(x, y);
        this.damage = damage;
        this.entityManager = entityManager;
        this.targetY = targetY;

        float explosionRadius = (float) Math.sqrt(entityManager.getCellWidth() * entityManager.getCellWidth() +
            entityManager.getCellHeight() * entityManager.getCellHeight());
        this.explosionRange = new Circle(x + image.getWidth() / 2, y + image.getHeight() / 2, explosionRadius);

        moveDownward(targetY);
        entityManager.addBomb(this);
    }

    private void moveDownward(float targetY) {
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(image.getX(), targetY);
        moveAction.setDuration(0.5f);
        image.addAction(moveAction);
    }

    private void updateExplosionRange() {
        explosionRange.setPosition(image.getX() + image.getWidth() / 2, image.getY() + image.getHeight() / 2);
    }

    public void update(float delta) {
        if (!isDetonated) {
            updateExplosionRange();
            if (Math.abs(image.getY() - targetY) < 1) {
                detonate();
            }
        } else {
            explosionTimer += delta;
            if (explosionTimer >= explosionDisplayTime) {
                entityManager.removeBomb(this);
                dispose();
            }
        }
    }

    private void detonate() {
        updateExplosionRange();
        image.clearActions();

        image.setDrawable(new TextureRegionDrawable(loadTexture("characters/projectile/explosion.png")));
        image.setSize(explosionRange.radius * 2, explosionRange.radius * 2);
        image.setPosition(explosionRange.x - explosionRange.radius, explosionRange.y - explosionRange.radius);

        // Aplica daño a los enemigos dentro del rango
        for (Character character : entityManager.getCharacters()) {
            float characterX = character.getImage().getX() + character.getImage().getWidth() / 2;
            float characterY = character.getImage().getY() + character.getImage().getHeight() / 2;

            if (!character.getType().equalsIgnoreCase("tower") && explosionRange.contains(characterX, characterY)) {
                character.takeDamage(damage);
            }
        }

        isDetonated = true;
    }



    public void dispose() {
        image.clearActions();
        image.remove();
    }

    public Circle getExplosionRange() {
        return explosionRange;
    }

    public Image getImage() {
        return image;
    }

}

