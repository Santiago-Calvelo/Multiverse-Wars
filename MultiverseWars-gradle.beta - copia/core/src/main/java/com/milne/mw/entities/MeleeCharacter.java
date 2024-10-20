package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;

public class MeleeCharacter extends Character {

    private Texture attack1Texture;
    private Texture attack2Texture;

    public MeleeCharacter(Texture texture, Texture attack1Texture, Texture attack2Texture, Texture walk1Texture, Texture walk2Texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, boolean canMove, Stage stage) {
        super(texture, x, y, lives, entityType, entityManager, canMove, walk1Texture, walk2Texture, stage);
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
}
