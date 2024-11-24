package com.milne.mw.entities.flycharacter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.milne.mw.entities.EntityManager;

import static com.milne.mw.Global.loadTexture;

public class Bomb {
    private final Texture bombTexture = loadTexture("characters/projectile/bomba.png");
    private final Texture explosionTexture = loadTexture("characters/projectile/explosion.png");
    private float x, y; // Coordenadas actuales
    private final float targetY; // Coordenada Y objetivo
    private boolean isDetonated = false;
    private Circle explosionRange;
    private final int damage;
    private final EntityManager entityManager;
    private final float explosionDisplayTime = 0.5f; // Tiempo para mostrar la explosión
    private final float speed = 100f; // Velocidad de caída
    private final float explosionRadius; // Radio de explosión calculado

    public Bomb(float x, float y, int damage, EntityManager entityManager, float targetY) {
        this.x = x;
        this.y = y;
        this.damage = damage;
        this.entityManager = entityManager;
        this.targetY = targetY;

        // Define el rango de la explosión basado en las dimensiones de las celdas
        this.explosionRadius = (float) Math.sqrt(
            entityManager.getCellWidth() * entityManager.getCellWidth() +
                entityManager.getCellHeight() * entityManager.getCellHeight()
        );
        this.explosionRange = new Circle(x, y, explosionRadius);

        // Registrar la bomba en el EntityManager
        entityManager.addBomb(this);
    }

    public void update(float delta) {
        if (isDetonated) return;

        // Mover hacia abajo
        if (y > targetY) {
            y -= speed * delta; // Actualizar posición
            if (y <= targetY) {
                detonate(); // Detonar al alcanzar la posición objetivo
            }
        }

        // Actualizar el rango de explosión
        explosionRange.setPosition(x, y);
    }

    private void detonate() {
        if (isDetonated) return; // Evitar detonaciones múltiples
        isDetonated = true;

        // Aplicar daño a los personajes dentro del rango de explosión
        entityManager.getCharacters().forEach(character -> {
            if (!character.getType().equalsIgnoreCase("tower")) {
                if (explosionRange.overlaps(new Circle(
                    character.getHitboxCenter().x,
                    character.getHitboxCenter().y,
                    0)) && character.getCanBeAttacked()) {
                    character.takeDamage(damage);
                }
            }
        });

    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public boolean isDetonated() {
        return isDetonated;
    }

    public Circle getExplosionRange() {
        return explosionRange;
    }

    public float getExplosionDisplayTime() {
        return explosionDisplayTime;
    }

    public float getExplosionRadius() {
        return explosionRadius;
    }

    public void dispose() {
        explosionRange = null; // Liberar memoria del rango
    }

    public Texture getBombTexture() {
        return bombTexture;
    }

    public Texture getExplosionTexture() {
        return explosionTexture;
    }
}
