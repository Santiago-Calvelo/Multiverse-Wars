package com.milne.mw.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.MultiverseWars;
import com.milne.mw.MusicManager;
import com.milne.mw.screens.MainMenuScreen;

public class PauseButton {
    private TextButton resumeButton;
    private TextButton mainMenuButton;
    private boolean isPaused;
    private Viewport viewport;
    private Stage stage;
    private Game game;
    private Image pauseBackground;

    public PauseButton(Viewport viewport, Stage stage, Game game) {
        this.viewport = viewport;
        this.stage = stage;
        this.game = game;
        createPauseMenuButtons();
    }

    private void createPauseMenuButtons() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = new BitmapFont();  // Fuente por defecto
        textButtonStyle.fontColor = Color.WHITE;  // Color de fuente

        pauseBackground = new Image(new Texture("ESENA PAUSA.png")); // Asegúrate de tener esta imagen en tu carpeta de assets
        pauseBackground.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        pauseBackground.setVisible(false);  // Oculta el fondo hasta que se active la pausa
        this.stage.addActor(pauseBackground);

        TextButton resumeButton = new TextButton("Reanudar", textButtonStyle);
        resumeButton.setPosition(viewport.getWorldWidth() / 2f - resumeButton.getWidth() / 2, this.viewport.getWorldHeight() / 2f + 90);
        resumeButton.setVisible(false);
        resumeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                togglePause();
            }
        });
        this.stage.addActor(resumeButton);

        mainMenuButton = new TextButton("Volver al Menu", textButtonStyle);
        mainMenuButton.setPosition(viewport.getWorldWidth() / 2f - mainMenuButton.getWidth() / 2, this.viewport.getWorldHeight() / 2f - 50);
        mainMenuButton.setVisible(false);
        mainMenuButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen((MultiverseWars) game));
                MusicManager.playMusic("bye bye.mp3"); // Reanudar la música principal al volver al menú
            }
        });
        this.stage.addActor(mainMenuButton);
    }

    public void togglePause() {
        isPaused = !isPaused;
        pauseBackground.setVisible(isPaused);
        resumeButton.setVisible(isPaused);
        mainMenuButton.setVisible(isPaused);

        if (isPaused) {
            Timer.instance().stop();
            MusicManager.pauseMusic();
        } else {
            Timer.instance().start();
            MusicManager.resumeMusic();
        }
    }

    public void dispose() {
        resumeButton.remove();
        pauseBackground.remove();
        mainMenuButton.remove();
    }
}
