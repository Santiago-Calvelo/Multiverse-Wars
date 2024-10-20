package com.milne.mw.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class TwoPlayerModeScreen implements Screen {

    private Game game;
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Viewport viewport;

    public TwoPlayerModeScreen(Game game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600); // Tamaño base, se ajustará
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Cargar la textura de la imagen de fondo
        backgroundTexture = new Texture(Gdx.files.internal("DIFICULTAD DOS JUGADORES.jpg"));
        backgroundImage = new Image(backgroundTexture);

        // Configurar el tamaño de la imagen de fondo
        backgroundImage.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());

        // Agregar la imagen de fondo al stage
        stage.addActor(backgroundImage);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        // Detectar la tecla Esc para volver al menú principal
        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
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
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
    }
}
