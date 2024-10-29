package com.milne.mw.maps;

import com.badlogic.gdx.Game;
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

    public PauseButton(Viewport viewport, Game game, EntityManager entityManager) {
        this.viewport = viewport;
        this.game = game;
        this.entityManager = entityManager;

        pauseButtonHitbox = new Circle(viewport.getWorldWidth() - 35, viewport.getWorldHeight() - 35, 25);
        createPauseMenuButtons();
    }

    private void createPauseMenuButtons() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();  // Fuente por defecto
        textButtonStyle.fontColor = Color.WHITE;  // Color de fuente
        pauseBackground = new Image(new Texture("escena-pausa.png")); // Asegúrate de tener esta imagen en tu carpeta de assets
        pauseBackground.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        resumeButton = new TextButton("Reanudar", textButtonStyle);
        resumeButton.setPosition(viewport.getWorldWidth() / 2f - resumeButton.getWidth() / 2, this.viewport.getWorldHeight() / 2f + 90);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });

        mainMenuButton = new TextButton("Volver al Menu", textButtonStyle);
        mainMenuButton.setPosition(viewport.getWorldWidth() / 2f - mainMenuButton.getWidth() / 2, this.viewport.getWorldHeight() / 2f - 50);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen((MultiverseWars) game));
                MusicManager.playMusic("bye bye.mp3"); // Reanudar la música principal al volver al menú
            }
        });
    }

    public void handleInput(float touchX, float touchY) {
        if (pauseButtonHitbox.contains(touchX, touchY)) {
            togglePause();
        }
    }

    public void togglePause() {
        isPaused = !isPaused;

        if (isPaused) {
            Timer.instance().stop();
            entityManager.pause();
            MusicManager.pauseMusic();
        } else {
            Timer.instance().start();
            entityManager.resume();
            MusicManager.resumeMusic();
        }
    }

    public boolean getIsPaused() {
        return isPaused;
    }

    public Image getPauseBackground() {
        return pauseBackground;
    }

    public TextButton getMainMenuButton() {
        return mainMenuButton;
    }

    public TextButton getResumeButton() {
        return resumeButton;
    }

    public void dispose() {
        resumeButton.remove();
        pauseBackground.remove();
        mainMenuButton.remove();
    }
}
