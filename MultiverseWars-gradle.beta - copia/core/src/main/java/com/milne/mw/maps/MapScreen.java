package com.milne.mw.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.milne.mw.difficulty.Difficulty;
import com.milne.mw.renders.RenderManager;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.entities.EntityType;
import com.milne.mw.MusicManager;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;

public class MapScreen implements Screen {
    private Game game;
    private Stage stage;
    private RenderManager renderManager;
    private EntityManager entityManager;
    private PauseMenu pauseMenu;
    private VictoryMenu victoryMenu;

    public MapScreen(Game game, Texture map, Difficulty difficultyLevel) {
        this.game = game;
        this.stage = new Stage(new FitViewport(800, 600));
        Gdx.input.setInputProcessor(stage);
        renderManager = RenderManager.getInstance(map, stage);

        entityManager = new EntityManager(stage, difficultyLevel);
        pauseMenu = new PauseMenu(stage, game, entityManager);
        victoryMenu = new VictoryMenu(stage, game, pauseMenu);

        addEntityCardsToPanel();
        entityManager.startEnemySpawner();
        entityManager.setVictoryMenu(victoryMenu);
    }

    private void addEntityCardsToPanel() {
        float xPos = 97;
        float yPos = stage.getViewport().getWorldHeight() - 82.5f;

        for (EntityType entityType : EntityType.values()) {
            if (entityType.getCardTexture() != null) {
                final Image cardImage = new Image(entityType.getCardTexture());
                cardImage.setSize(60, 80);
                cardImage.setPosition(xPos, yPos);

                cardImage.addListener(new InputListener() {
                    boolean playerSelected = false;
                    final float originalX = cardImage.getX();
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
                            float cardX = cardImage.getX();
                            float cardY = cardImage.getY();
                            entityManager.handleEntityPlacement(entityType, cardX, cardY, cardImage.getWidth(), cardImage.getHeight());
                            cardImage.setPosition(originalX, originalY);
                            playerSelected = false;
                        }
                    }
                });

                stage.addActor(cardImage);
                xPos += 70;
            }
        }
    }

    @Override
    public void show() {
        MusicManager.playMusic("tema d battala.mp3");
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.justTouched()) {
            float touchX = Gdx.input.getX();
            float touchY = Gdx.input.getY();
            Vector2 worldTouch = stage.getViewport().unproject(new Vector2(touchX, touchY));
            pauseMenu.handleInput(worldTouch.x, worldTouch.y);
        }
        renderManager.render(pauseMenu.getIsPaused(), entityManager, delta, pauseMenu);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        MusicManager.stopMusic();
        dispose();
    }

    @Override
    public void dispose() {
        if (entityManager != null) {
            entityManager.dispose();
        }
        if (renderManager != null) {
            RenderManager.resetInstance();
        }
        stage.clear();
        stage.dispose();
    }
}
