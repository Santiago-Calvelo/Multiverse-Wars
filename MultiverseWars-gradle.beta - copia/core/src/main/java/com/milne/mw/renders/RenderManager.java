package com.milne.mw.renders;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.milne.mw.entities.Character;
import com.milne.mw.entities.EntityManager;

public class RenderManager {
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Stage stage;

    public RenderManager(Texture backgroundTexture, Stage stage) {
        this.backgroundTexture = backgroundTexture;
        this.stage = stage;
        this.batch = new SpriteBatch();
    }

    public void render(Viewport viewport, boolean isPaused, EntityManager entityManager) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (!isPaused) {
            batch.setProjectionMatrix(viewport.getCamera().combined);
            batch.begin();
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
            batch.end();

            // Dibujar todas las entidades gestionadas por el EntityManager
            drawEntities(entityManager);

            // Actualizar las acciones del escenario
            stage.act();
        }

        stage.draw();  // Dibujar elementos del stage (UI)
    }

    // Método para dibujar todas las entidades
    private void drawEntities(EntityManager entityManager) {
        batch.begin();
        for (Character character : entityManager.getCharacters()) {
            if (character.getImage() != null) {
                character.getImage().draw(batch, 1);  // Dibujar cada personaje si su imagen no es null
            }
        }
        batch.end();
    }

    public void dispose() {
        batch.dispose();
        backgroundTexture.dispose();
    }
}
