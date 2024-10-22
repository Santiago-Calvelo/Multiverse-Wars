package com.milne.mw.renders;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.entities.Character;
import com.badlogic.gdx.math.Rectangle;
import com.milne.mw.Global;

public class RenderManager {
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;  // Añadimos ShapeRenderer para dibujar las zonas y hitboxes
    private Texture backgroundTexture;
    private Stage stage;

    public RenderManager(Texture backgroundTexture, Stage stage) {
        this.backgroundTexture = backgroundTexture;
        this.stage = stage;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();  // Inicializamos ShapeRenderer
    }

    public void render(Viewport viewport, boolean isPaused, EntityManager entityManager, float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Sincronizar la proyección del SpriteBatch con el viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        // Dibujamos el fondo
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        batch.end();

        // Dibujar todas las entidades
        drawEntities(entityManager);

        // Dibujar las zonas de colocación de personajes (los círculos)


        // Si no está pausado, dibujar las hitboxes si está en modo debug
        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager, viewport);
            drawPlacementZones(viewport, entityManager);
        }

        entityManager.update(delta);

        // Actualizar el stage
        stage.act();
        stage.draw();
    }

    // Método para dibujar las zonas de colocación (celdas con círculos)
    private void drawPlacementZones(Viewport viewport, EntityManager entityManager) {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);  // Dibujo de líneas para las hitboxes
        shapeRenderer.setColor(Color.GREEN);  // Color para las hitboxes

        for (Rectangle hitbox : entityManager.getPlacementHitboxes()) {
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);  // Dibujamos la hitbox
        }

        shapeRenderer.end();
    }


    // Método para dibujar todas las entidades
    private void drawEntities(EntityManager entityManager) {
        batch.begin();
        for (Character character : entityManager.getCharacters()) {
            character.getImage().draw(batch, 1);  // Dibujar cada personaje
        }
        batch.end();
    }

    // Método para dibujar las hitboxes de los personajes (si está en modo debug)
    private void drawHitboxes(EntityManager entityManager, Viewport viewport) {
        // Sincronizar ShapeRenderer con la misma proyección que el SpriteBatch
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);  // Modo línea para hitboxes
        shapeRenderer.setColor(Color.RED);  // Color rojo para las hitboxes

        for (Character character : entityManager.getCharacters()) {
            Rectangle hitbox = character.getHitbox();
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        batch.dispose();
        shapeRenderer.dispose();  // Liberar recursos del ShapeRenderer
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
