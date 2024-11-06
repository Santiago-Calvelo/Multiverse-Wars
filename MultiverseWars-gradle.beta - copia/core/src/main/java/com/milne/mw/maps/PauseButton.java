package com.milne.mw.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.MultiverseWars;
import com.milne.mw.MusicManager;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.screens.MainMenuScreen;

public class PauseButton {
    private TextButton resumeButton;
    private TextButton mainMenuButton;
    private boolean isPaused = false;
    private Viewport viewport;
    private Game game;
    private EntityManager entityManager;
    private Image pauseBackground;
    public Circle pauseButtonHitbox;
    private Stage stage;  // Referencia al Stage para añadir o eliminar actores

    public PauseButton(Viewport viewport, Stage stage, Game game, EntityManager entityManager) {
        this.viewport = viewport;
        this.game = game;
        this.entityManager = entityManager;
        this.stage = stage;

        pauseButtonHitbox = new Circle(viewport.getWorldWidth() - 257, viewport.getWorldHeight() - 40, 30);
        createPauseMenuButtons();
    }

    private void createPauseMenuButtons() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();
        textButtonStyle.fontColor = Color.WHITE;

        pauseBackground = new Image(new Texture("escena-pausa.png"));
        pauseBackground.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        resumeButton = new TextButton("Reanudar", textButtonStyle);
        resumeButton.setPosition(viewport.getWorldWidth() / 2f - resumeButton.getWidth() / 2, viewport.getWorldHeight() / 2f + 90);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        mainMenuButton = new TextButton("Volver al Menu", textButtonStyle);
        mainMenuButton.setPosition(viewport.getWorldWidth() / 2f - mainMenuButton.getWidth() / 2, viewport.getWorldHeight() / 2f - 55);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Gdx.app.postRunnable(() -> game.setScreen(new MainMenuScreen(game)));
                MusicManager.playMusic("bye bye.mp3");
            }
        });
    }

    public void handleInput(float touchX, float touchY) {
        if (pauseButtonHitbox.contains(touchX, touchY)) {
            togglePause();
        }
    }

    public void checkForEscapeKey() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            togglePause();  // Llama a togglePause si se presiona ESC
        }
    }

    public void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            Timer.instance().stop();
            entityManager.pause();
            MusicManager.pauseMusic();
            addPauseMenuToStage();
        } else {
            Timer.instance().start();
            entityManager.resume();
            MusicManager.resumeMusic();
            removePauseMenuFromStage();
        }
    }

    private void addPauseMenuToStage() {
        stage.addActor(pauseBackground);
        stage.addActor(resumeButton);
        stage.addActor(mainMenuButton);
    }

    private void removePauseMenuFromStage() {
        pauseBackground.remove();
        resumeButton.remove();
        mainMenuButton.remove();
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    public void dispose() {
        removePauseMenuFromStage();
        pauseBackground.remove();
        resumeButton.remove();
        mainMenuButton.remove();
    }
}
