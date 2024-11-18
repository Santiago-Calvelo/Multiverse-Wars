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
    private boolean toRemove = false;
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

        // Configurar el rango de explosión (3x3 casillas)
        float explosionRadius = (float) Math.sqrt(entityManager.getCellWidth() * entityManager.getCellWidth() +
            entityManager.getCellHeight() * entityManager.getCellHeight());
        this.explosionRange = new Circle(x + image.getWidth() / 2, y + image.getHeight() / 2, explosionRadius);

        // Inicia el movimiento hacia abajo
        moveDownward(targetY);
        entityManager.addBomb(this); // Registra la bomba en el EntityManager
    }

    private void moveDownward(float targetY) {
        MoveToAction moveAction = new MoveToAction();

        // Configura el movimiento hacia la posición más baja de la casilla
        moveAction.setPosition(image.getX(), targetY);
        moveAction.setDuration(0.5f); // Ajusta la velocidad (0.5 segundos para llegar al suelo)
        image.addAction(moveAction);
    }

    private void updateExplosionRange() {
        // Actualiza el rango dinámicamente basado en el centro de la bomba
        explosionRange.setPosition(image.getX() + image.getWidth() / 2, image.getY() + image.getHeight() / 2);
    }

    public void update(float delta) {
        if (!isDetonated) {
            // Actualiza el rango dinámicamente
            updateExplosionRange();

            // Detecta si la bomba ha alcanzado su destino y detona
            if (Math.abs(image.getY() - targetY) < 1) { // Tolerancia para precisión
                detonate();
            }
        } else {
            // Incrementa el temporizador para eliminar la bomba tras mostrar la explosión
            explosionTimer += delta;
            if (explosionTimer >= explosionDisplayTime) {
                toRemove = true;
                dispose(); // Limpia la bomba después del tiempo de explosión
            }
        }
    }

    private void detonate() {
        // Asegura que el rango esté correctamente actualizado antes de la explosión
        updateExplosionRange();

        // Cambia la textura a la imagen de explosión
        image.setDrawable(new TextureRegionDrawable(loadTexture("characters/projectile/explosion.png")));

        // Ajusta el tamaño de la textura de explosión para cubrir el rango de explosión
        image.setSize(explosionRange.radius * 2, explosionRange.radius * 2);

        // Asegura que la posición de la textura de explosión esté centrada en el rango
        image.setPosition(explosionRange.x - explosionRange.radius, explosionRange.y - explosionRange.radius);

        // Aplica daño a los enemigos dentro del rango
        for (Character character : entityManager.getCharacters()) {
            float characterX = character.getImage().getX() + character.getImage().getWidth() / 2;
            float characterY = character.getImage().getY() + character.getImage().getHeight() / 2;

            if (!character.getType().equalsIgnoreCase("tower") && explosionRange.contains(characterX, characterY)) {
                character.takeDamage(damage);
            }
        }

        isDetonated = true; // Marca la bomba como detonada
    }


    public void dispose() {
        image.clearActions();
        image.remove();
    }

    public Image getImage() {
        return image;
    }

    public boolean getIsToRemove() {
        return toRemove;
    }
}

