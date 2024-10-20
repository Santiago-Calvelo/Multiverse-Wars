package com.milne.mw.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.maps.MapSelectionScreen;

public class PlayOptionsScreen implements Screen {
    private Texture menuImage;
    private SpriteBatch batch;
    private Game game;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    public PlayOptionsScreen(Game game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600); // Tamaño base, se ajustará
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        batch = new SpriteBatch();
    }

    @Override
    public void show() {
        menuImage = new Texture(Gdx.files.internal("menu.png")); // Cargar la imagen de fondo
        createMenuButtons();
    }

    private void createMenuButtons() {
        // Botón Un Jugador, que llevará primero a la selección de mapas
        TextButton singlePlayerButton = new TextButton("Un Jugador", skin);
        singlePlayerButton.setPosition(viewport.getWorldWidth() / 2f - singlePlayerButton.getWidth() / 2, viewport.getWorldHeight() / 2f - 100);
        singlePlayerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MapSelectionScreen(game));  // Primero va a la pantalla de selección de mapa
            }
        });

        // Botón Dos Jugadores
        TextButton twoPlayersButton = new TextButton("Dos Jugadores", skin);
        twoPlayersButton.setPosition(viewport.getWorldWidth() / 2f - twoPlayersButton.getWidth() / 2, viewport.getWorldHeight() / 2f - 150);
        twoPlayersButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new TwoPlayerModeScreen(game));  // Redirige al modo de dos jugadores
            }
        });

        // Botón para Volver al Menú
        TextButton exitButton = new TextButton("Volver al Menu", skin);
        exitButton.setPosition(viewport.getWorldWidth() / 2f - exitButton.getWidth() / 2, viewport.getWorldHeight() / 2f - 200);
        exitButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new MainMenuScreen(game));  // Volver al menú principal
            }
        });

        stage.addActor(singlePlayerButton);
        stage.addActor(twoPlayersButton);
        stage.addActor(exitButton);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(menuImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        stage.act();
        stage.draw();
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
        skin.dispose();
        batch.dispose();
        menuImage.dispose();
    }
}
