package com.milne.mw.renders;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.Gdx;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.entities.Character;
import com.badlogic.gdx.math.Rectangle;
import com.milne.mw.Global;
import com.milne.mw.maps.PauseButton;

public class RenderManager {
    private static RenderManager instance;
    private SpriteBatch batch;
    private ShapeRenderer shapeRenderer;  // Añadimos ShapeRenderer para dibujar las zonas y hitboxes
    private Texture backgroundTexture;
    private Stage stage;

    private RenderManager(Texture backgroundTexture, Stage stage) {
        this.backgroundTexture = backgroundTexture;
        this.stage = stage;
        this.batch = new SpriteBatch();
        this.shapeRenderer = new ShapeRenderer();  // Inicializamos ShapeRenderer
    }

    // Método para obtener la instancia única de RenderManager
    public static RenderManager getInstance(Texture backgroundTexture, Stage stage) {
        if (instance == null) {
            instance = new RenderManager(backgroundTexture, stage);
        }
        return instance;
    }

    // Método para obtener la instancia existente de RenderManager
    public static RenderManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RenderManager no ha sido inicializado. Llama a getInstance() con parámetros primero.");
        }
        return instance;
    }

    public void render(Viewport viewport, boolean isPaused, EntityManager entityManager, float delta, PauseButton pauseButton) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Sincronizar la proyección del SpriteBatch con el viewport
        batch.setProjectionMatrix(viewport.getCamera().combined);
        stage.getBatch().begin();

        // Dibujamos el fondo
        if (backgroundTexture != null) {
            stage.getBatch().draw(backgroundTexture, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        }

        drawEntities(entityManager);

        if (isPaused) {
            // Dibujar fondo de pausa y botones de manera fija encima de las entidades
            pauseButton.getPauseBackground().draw(stage.getBatch(), 1f);
            pauseButton.getResumeButton().draw(stage.getBatch(), 1f);
            pauseButton.getMainMenuButton().draw(stage.getBatch(), 1f);
        }
        stage.getBatch().end();

        if (!isPaused) {
            stage.act();
            entityManager.update(delta);
        }
        stage.draw();

        // Si no está pausado, dibujar las hitboxes si está en modo debug
        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager, viewport);
            drawPlacementZones(viewport, entityManager, pauseButton);
        }
    }

    // Método para dibujar las zonas de colocación (celdas con círculos)
    private void drawPlacementZones(Viewport viewport, EntityManager entityManager, PauseButton pauseButton) {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);  // Dibujo de líneas para las hitboxes
        shapeRenderer.setColor(Color.GREEN);  // Color para las hitboxes

        shapeRenderer.circle(pauseButton.pauseButtonHitbox.x, pauseButton.pauseButtonHitbox.y, pauseButton.pauseButtonHitbox.radius);
        for (Rectangle hitbox : entityManager.getPlacementHitboxes()) {
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);  // Dibujamos la hitbox
        }

        shapeRenderer.end();
    }


    // Método para dibujar todas las entidades
    private void drawEntities(EntityManager entityManager) {
        for (Character character : entityManager.getCharacters()) {
            character.getImage().draw(stage.getBatch(), 1);  // Dibujar cada personaje
        }
    }

    public void animateCharacterAttack(Character character, float cooldown) {
        character.getImage().setDrawable(new TextureRegionDrawable(new TextureRegion(character.getAttack1Texture())));
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                character.getImage().setDrawable(new TextureRegionDrawable(new TextureRegion(character.getAttack2Texture())));
            }
        }, cooldown);  // Cambiar a la siguiente textura después de 0.5 segundos
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
