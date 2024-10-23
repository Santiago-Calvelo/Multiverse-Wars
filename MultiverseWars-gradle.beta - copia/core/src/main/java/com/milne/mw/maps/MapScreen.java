package com.milne.mw.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.renders.RenderManager;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.entities.EntityType;
import com.milne.mw.MusicManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import java.util.EnumSet;

public class MapScreen implements Screen {
    private Game game;
    private Stage stage;
    private Viewport viewport;
    private RenderManager renderManager;
    private EntityManager entityManager;
    private boolean isPaused = false;
    private SpriteBatch batch;
    private BitmapFont font;


    public MapScreen(Game game, Texture map) {
        this.game = game;
        this.viewport = new FitViewport(800, 600);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        RenderManager.getInstance(map, stage);
        entityManager = new EntityManager(stage, viewport);  // Pasamos viewport a EntityManager para los cálculos
        batch = new SpriteBatch();
        font = new BitmapFont();
        // Añadir todas las cartas de los EntityType dinámicamente al panel
        addEntityCardsToPanel();

        // Iniciar el spawner de enemigos
        entityManager.startEnemySpawner(20f);
    }

    // Método que añade las cartas al panel basado en el Enum EntityType
    private void addEntityCardsToPanel() {
        float xPos = 105;  // Posición inicial X para las cartas
        float yPos = viewport.getWorldHeight() - 85;  // Posición en el panel (Y)

        for (EntityType entityType : EntityType.values()) {
            // Verificamos si la entidad tiene una carta antes de crear la imagen
            if (entityType.getCardTexture() != null) {
                // Crear una instancia única para cada Image y listener
                final Image cardImage = new Image(entityType.getCardTexture());
                cardImage.setSize(60, 80);
                cardImage.setPosition(xPos, yPos);

                // Listener para manejar arrastrar y soltar
                cardImage.addListener(new InputListener() {
                    boolean playerSelected = false;
                    final float originalX = cardImage.getX();  // Guardamos la posición original
                    final float originalY = cardImage.getY();

                    @Override
                    public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                        playerSelected = true;
                        return true;
                    }

                    @Override
                    public void touchDragged(InputEvent event, float x, float y, int pointer) {
                        if (playerSelected) {
                            cardImage.moveBy(x - cardImage.getWidth() / 2, y - cardImage.getHeight() / 2);
                        }
                    }

                    @Override
                    public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                        if (playerSelected) {
                            // Obtener las coordenadas donde se suelta la carta
                            float cardX = cardImage.getX();
                            float cardY = cardImage.getY();

                            // Delegamos la lógica de colocación al EntityManager, pasando las coordenadas
                            entityManager.handleEntityPlacement(entityType, cardX, cardY, cardImage.getWidth(), cardImage.getHeight());

                            // Volver a colocar la carta en su posición original
                            cardImage.setPosition(originalX, originalY);
                            playerSelected = false;
                        }
                    }
                });

                stage.addActor(cardImage);  // Añadir la carta al panel
                xPos += 70;  // Incrementar la posición X para la siguiente carta
            }
        }
    }



    @Override
    public void show() {
        MusicManager.playMusic("tema d battala.mp3");
    }

    @Override
    public void render(float delta) {
        RenderManager renderManager = RenderManager.getInstance();
        renderManager.render(viewport, isPaused, entityManager, delta);  // Pasar el entityManager para renderizar las entidades
        batch.begin();
        font.draw(batch, "Personajes restantes: " + entityManager.getCharacters().size, 10, 20);
        batch.end();
        entityManager.removeOffScreenCharacters();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        MusicManager.stopMusic();
        MusicManager.playMusic("bye bye.mp3");
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        renderManager.dispose();
        batch.dispose();
        font.dispose();  // Liberar la fuente

        // No liberar texturas aquí si planeas usarlas después
        for (EntityType entityType : EnumSet.allOf(EntityType.class)) {
            if (entityType.getCardTexture() != null) {
                entityType.getCardTexture().dispose();
            }
            entityType.getTexture().dispose();
            if (entityType.getProjectileTexture() != null) {
                entityType.getProjectileTexture().dispose();
            }
        }
    }


    public void setDifficulty(int difficultyLevel) {

    }
}
