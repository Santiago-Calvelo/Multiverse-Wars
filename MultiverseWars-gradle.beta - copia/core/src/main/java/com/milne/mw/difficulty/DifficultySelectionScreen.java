package com.milne.mw.difficulty;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.milne.mw.MultiverseWars;
import com.milne.mw.maps.MapScreen;
import com.milne.mw.screens.MainMenuScreen;

public class DifficultySelectionScreen implements Screen {

    private Game game;
    private Stage stage;
    private Texture backgroundTexture;
    private Image backgroundImage;
    private Skin skin;
    private Texture map;

    public DifficultySelectionScreen(Game game, Texture map) {
        this.game = game;
        this.map = map;
        this.stage = new Stage(new FitViewport(800, 600));
        Gdx.input.setInputProcessor(stage);
        this.skin = new Skin(Gdx.files.internal("uiskin.json"));

        backgroundTexture = new Texture(Gdx.files.internal("DIFICULTAD UN JUGADOR.jpg"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        stage.addActor(backgroundImage);

        createDifficultyButtons();
    }

    private void createDifficultyButtons() {
        TextButton.TextButtonStyle textButtonStyle = new TextButton.TextButtonStyle();
        textButtonStyle.font = skin.getFont("default-font");
        textButtonStyle.font.getData().setScale(2.5f);
        textButtonStyle.fontColor = Color.YELLOW;
        textButtonStyle.up = null;
        textButtonStyle.down = null;

        TextButton easyButton = new TextButton("     ", textButtonStyle);
        easyButton.setPosition(stage.getViewport().getWorldWidth() / 6f - easyButton.getWidth() / 2 + 20, stage.getViewport().getWorldHeight() / 2f - 20);
        easyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(1);
            }
        });

        TextButton mediumButton = new TextButton("     ", textButtonStyle);
        mediumButton.setPosition(stage.getViewport().getWorldWidth() / 2f - mediumButton.getWidth() / 2, stage.getViewport().getWorldHeight() / 2f - 20);
        mediumButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(2);
            }
        });

        TextButton hardButton = new TextButton("       ", textButtonStyle);
        hardButton.setPosition(stage.getViewport().getWorldWidth() * 5 / 6f - hardButton.getWidth() / 2 - 30, stage.getViewport().getWorldHeight() / 2f - 20);
        hardButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGameWithDifficulty(3);
            }
        });

        stage.addActor(easyButton);
        stage.addActor(mediumButton);
        stage.addActor(hardButton);
    }

    private void startGameWithDifficulty(int difficultyLevel) {
        MapScreen mapScreen = new MapScreen((MultiverseWars) game, map);
        mapScreen.setDifficulty(difficultyLevel);
        game.setScreen(mapScreen);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act();
        stage.draw();

        if (Gdx.input.isKeyPressed(com.badlogic.gdx.Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void dispose() {
        stage.dispose();
        backgroundTexture.dispose();
        skin.dispose();
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
}
