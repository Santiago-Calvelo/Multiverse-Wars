package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;

public class RangedCharacter extends Character {

    private Texture projectileTexture;
    private Texture attack1Texture;
    private Texture attack2Texture;
    private Stage stage;
    private Task attackTask; // Guardar referencia al Task de ataque

    public RangedCharacter(Texture texture, int hitboxWidth, int hitboxHeight,Texture attack1Texture, Texture attack2Texture, Texture projectileTexture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, int speed, Stage stage, String type) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityType, entityManager, speed, walk1Texture, walk2Texture, stage, type);
        this.attack1Texture = attack1Texture;
        this.attack2Texture = attack2Texture;
        this.projectileTexture = projectileTexture;
        this.stage = stage;

        // Iniciar el ciclo de ataque (atacar cada 1 segundo)
        scheduleAttack();
    }

    // Programar el ataque cada cierto intervalo
    private void scheduleAttack() {
        attackTask = new Task() {
            @Override
            public void run() {
                if (getLives() > 0) {  // Solo atacamos si seguimos vivos
                    attack();
                }
            }
        };
        Timer.schedule(attackTask, 0, 1);  // Atacar cada 1 segundo
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
    public void dispose() {
        // Cancelar el ciclo de ataque antes de eliminar el personaje
        if (attackTask != null) {
            attackTask.cancel();  // Detener el Task de ataque
        }
        super.dispose();  // Llamamos al dispose de la clase padre para eliminar la imagen y demás
    }

    @Override
    public void checkForAttack() {
        // Lógica de chequeo de ataque (vacía en este caso)
    }
}
