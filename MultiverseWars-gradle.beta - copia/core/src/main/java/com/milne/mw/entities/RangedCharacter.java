package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class RangedCharacter extends Character {

    protected Texture projectileTexture;
    protected Texture attack1Texture;
    protected Texture attack2Texture;

    public RangedCharacter(Texture texture, Texture attack1Texture, Texture attack2Texture, Texture projectileTexture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, boolean canMove, Stage stage, String type) {
        super(texture, x, y, lives, entityType, entityManager, canMove, walk1Texture, walk2Texture, stage, type);
        this.attack1Texture = attack1Texture;
        this.attack2Texture = attack2Texture;
        this.projectileTexture = projectileTexture;

        // Iniciar el ciclo de ataque (atacar cada 1 segundo)
        scheduleAttack();
    }

    // Programar el ataque cada cierto intervalo
    private void scheduleAttack() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                if (lives > 0) {  // Solo atacamos si seguimos vivos
                    attack();
                }
            }
        }, 0, 1);  // Atacar cada 1 segundo
    }

    // Implementación del ataque a distancia (disparar proyectil)
    @Override
    public void attack() {
        // Crear el proyectil y añadirlo al stage
        Projectile projectile = new Projectile(projectileTexture, image.getX() + image.getWidth(),
            image.getY() + image.getHeight() / 2, stage, entityManager, this.getType());
        stage.addActor(projectile.getImage());

        // Alternar las texturas de ataque para simular la animación
        image.setDrawable(new TextureRegionDrawable(attack1Texture));
        Timer.schedule(new Task() {
            @Override
            public void run() {
                image.setDrawable(new TextureRegionDrawable(attack2Texture));
            }
        }, 0.5f);  // Cambiar la textura después de 0.5 segundos
    }

    @Override
    public void checkForAttack() {

    }
}
