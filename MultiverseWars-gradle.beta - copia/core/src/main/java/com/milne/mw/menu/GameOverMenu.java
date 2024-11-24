package com.milne.mw.menu;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.milne.mw.entities.EntityManager;

import static com.milne.mw.Global.loadTexture;

public class GameOverMenu {
    private TextButton mainMenuButton;
    private Game game;
    private Image gameOverBackground;
    private Stage stage;
    private PauseMenu pauseMenu;
    private EntityManager entityManager;
    private Rectangle retryButton;

    public GameOverMenu(Stage stage, Game game, PauseMenu pauseMenu, EntityManager entityManager) {
        this.stage = stage;
        this.game = game;
        this.pauseMenu = pauseMenu;
        this.entityManager = entityManager;
    }

    public void createMenu() {
        pauseMenu.setEnable(false);
        pauseMenu.togglePause();
        retryButton = new Rectangle(250f,300f,100,50);

        gameOverBackground = new Image(loadTexture("multiverse-wars/game-over.jpg"));
        gameOverBackground.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());

        addGameOverMenuToStage();
    }

    public void handleInput(float touchX, float touchY) {
        if (retryButton.contains(touchX, touchY)) {
            retry();
        }
    }

    public void retry() {
        entityManager.dispose();
        dispose();
    }

    private void addGameOverMenuToStage() {
        stage.addActor(gameOverBackground);
    }

    private void removeGameOverMenuFromStage() {
        gameOverBackground.remove();
   }

    public void dispose() {
        removeGameOverMenuFromStage();
        gameOverBackground.remove();
        retryButton = null;
    }
}
