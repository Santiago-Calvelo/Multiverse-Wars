package com.milne.mw.maps;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.screens.DifficultySelectionScreen;

public class MapSelectionScreen implements Screen {
    private Game game;
    private Stage stage;
    private Viewport viewport;

    private Texture backgroundTexture;
    private Image backgroundImage;

    private Texture map1Texture, map2Texture, map3Texture, map4Texture;
    private Image map1Image, map2Image, map3Image, map4Image;

    public MapSelectionScreen(Game game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600);
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);

        // Cargar la imagen de fondo
        backgroundTexture = new Texture(Gdx.files.internal("menu de mapas.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(backgroundImage);

        // Cargar texturas de los mapas
        map1Texture = new Texture(Gdx.files.internal("mapa1.jpg"));
        map2Texture = new Texture(Gdx.files.internal("mapa2.jpg"));
        map3Texture = new Texture(Gdx.files.internal("mapa3.jpg"));
        map4Texture = new Texture(Gdx.files.internal("mapa4.jpg"));

        // Crear las imágenes para los mapas
        map1Image = new Image(map1Texture);
        map2Image = new Image(map2Texture);
        map3Image = new Image(map3Texture);
        map4Image = new Image(map4Texture);

        // Colocar las imágenes de los mapas
        map1Image.setPosition(200, 300);
        map2Image.setPosition(425, 300);
        map3Image.setPosition(200, 150);
        map4Image.setPosition(425, 150);

        // Ajustar el tamaño de los mapas
        map1Image.setSize(170, 90);
        map2Image.setSize(170, 90);
        map3Image.setSize(170, 90);
        map4Image.setSize(170, 90);

        // Añadir las imágenes de los mapas al escenario
        stage.addActor(map1Image);
        stage.addActor(map2Image);
        stage.addActor(map3Image);
        stage.addActor(map4Image);

        // Añadir listeners para seleccionar el mapa
        map1Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapSelected(new Texture(Gdx.files.internal("mapa1.jpg")));
            }
        });

        map2Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapSelected(new Texture(Gdx.files.internal("mapa2.jpg")));
            }
        });

        map3Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapSelected(new Texture(Gdx.files.internal("mapa3.jpg")));
            }
        });

        map4Image.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                mapSelected(new Texture(Gdx.files.internal("mapa4.jpg")));
            }
        });
    }

    private void mapSelected(Texture map) {
        System.out.println("Mapa seleccionado correctamente");
        game.setScreen(new DifficultySelectionScreen(game, map));  // Ir a la pantalla de selección de dificultad
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void show() {}

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        map1Texture.dispose();
        map2Texture.dispose();
        map3Texture.dispose();
        map4Texture.dispose();
        map4Texture.dispose();
        map4Texture.dispose();
        map4Texture.dispose();
    }
}
