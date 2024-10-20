package com.milne.mw.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.milne.mw.MultiverseWars;
import com.milne.mw.maps.MapScreen;

public class DifficultySelectionScreen implements Screen {

    private Game game;
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Viewport viewport;
    private Skin skin;
    private Texture map;

    public DifficultySelectionScreen(Game game, Texture map) {
        this.game = game;
        this.map = map;
        this.viewport = new FitViewport(800, 600);
        this.stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json")); // Asegúrate de tener tu skin

        // Cargar la imagen de fondo
        this.backgroundTexture = new Texture(Gdx.files.internal("DIFICULTAD UN JUGADOR.jpg")); // Usa el nombre correcto de tu imagen
        this.backgroundImage = new Image(backgroundTexture);
        this.backgroundImage.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(backgroundImage);

        // Crear botones para las opciones de dificultad
        createDifficultyButtons();
    }

    private void createDifficultyButtons() {
        // Estilo de los botones con mayor tamaño de fuente
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.font.getData().setScale(2.5f); // Mantener el tamaño de la fuente
        textButtonStyle.fontColor = Color.YELLOW;
        textButtonStyle.up = null;
        textButtonStyle.down = null;

        // Botón Fácil - Mover un poquito a la izquierda
        TextButton easyButton = new TextButton("     ", textButtonStyle);
        easyButton.setPosition(viewport.getWorldWidth() / 6f - easyButton.getWidth() / 2 + 20, viewport.getWorldHeight() / 2f - 20); // Mover a la izquierda ligeramente
        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(1); // 1 = Fácil
            }
        });

        // Botón Media - Posición ajustada y centrada con el texto grande
        TextButton mediumButton = new TextButton("     ", textButtonStyle);
        mediumButton.setPosition(viewport.getWorldWidth() / 2f - mediumButton.getWidth() / 2, viewport.getWorldHeight() / 2f - 20); // Centrado
        mediumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(2); // 2 = Media
            }
        });

        // Botón Difícil - Posición ajustada
        TextButton hardButton = new TextButton("       ", textButtonStyle);
        hardButton.setPosition(viewport.getWorldWidth() * 5/6f - hardButton.getWidth() / 2 - 30, viewport.getWorldHeight() / 2f - 20); // Mantener la posición actual
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(3); // 3 = Difícil
            }
        });

        // Agregar los botones al stage
        stage.addActor(easyButton);
        stage.addActor(mediumButton);
        stage.addActor(hardButton);
    }


    private void startGameWithDifficulty(int difficultyLevel) {
        // Lógica para comenzar el juego con la dificultad seleccionada
        MapScreen mapScreen = new MapScreen((MultiverseWars) game, map);
        mapScreen.setDifficulty(difficultyLevel);
        game.setScreen(mapScreen); // Cambiar a la pantalla del juego
    }

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
        skin.dispose();
    }
}
