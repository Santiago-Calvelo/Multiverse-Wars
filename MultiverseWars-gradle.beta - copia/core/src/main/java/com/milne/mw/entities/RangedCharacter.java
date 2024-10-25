package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class RangedCharacter extends Character implements RangeListener {

    private Texture projectileTexture;
    private Stage stage;
    private Character targetEnemy; // Referencia al enemigo objetivo
    private int range;

    public RangedCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture attack1Texture, Texture attack2Texture, Texture projectileTexture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, int speed, Stage stage, String type, int range, float attackCooldown) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityType, entityManager, speed, walk1Texture, walk2Texture, attack1Texture, attack2Texture, stage, type, attackCooldown);
        this.projectileTexture = projectileTexture;
        this.stage = stage;
        this.range = range;
    }

    // Implementación del ataque a distancia (disparar proyectil)
    @Override
    public void attack() {
        if (targetEnemy == null || !isInRange(targetEnemy)) {
            return;  // Si el enemigo está fuera de rango o no hay enemigo, no atacar
        }

        System.out.println("Disparando proyectil");
        // Crear el proyectil y añadirlo al stage
        Projectile projectile = new Projectile(
            projectileTexture,
            image.getX() + image.getWidth(),
            image.getY() + image.getHeight() / 2,
            stage,
            entityManager,
            this.getType()
        );
        stage.addActor(projectile.getImage());
    }

    @Override
    public void dispose() {
        super.dispose();  // Llamamos al dispose de la clase padre
    }

    @Override
    public void checkForAttack() {
    }

    // Implementación del listener para detectar enemigos en rango
    @Override
    public void onEnemyInRange(Character enemy) {
        if (enemy != this && isInSameRow(enemy) && isInRange(enemy)) {
            targetEnemy = enemy;  // Establecer al enemigo como objetivo
            tryAttack();  // Usar la lógica de ataque directamente desde el padre
        }
    }

    // Verifica si el enemigo está en la misma fila
    private boolean isInSameRow(Character enemy) {
        return this.getImage().getY() == enemy.getImage().getY();  // Ajustar la tolerancia si es necesario
    }

    // Verifica si el enemigo está dentro de 3 casillas de distancia
    private boolean isInRange(Character enemy) {
        float dx = enemy.getImage().getX() - this.getImage().getX();
        float dy = enemy.getImage().getY() - this.getImage().getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        float attackRange = this.range * entityManager.getCellWidth();
        return distance <= attackRange;
    }
}

