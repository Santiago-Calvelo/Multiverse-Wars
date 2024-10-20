package com.milne.mw.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class ExitConfirmationScreen implements Screen {

    private Game game;
    private Stage stage;
    private Skin skin;
    private Viewport viewport;

    public ExitConfirmationScreen(Game game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600); // Tamaño base, se ajustará
        stage = new Stage(viewport);
        Gdx.input.setInputProcessor(stage);
        skin = new Skin(Gdx.files.internal("uiskin.json"));
    }

    @Override
    public void show() {
        createExitConfirmationDialog();
    }

    private void createExitConfirmationDialog() {
        Dialog dialog = new Dialog("Salir del Juego", skin) {
            @Override
            protected void result(Object object) {
                if ((Boolean) object) {
                    Gdx.app.exit();
                } else {
                    game.setScreen(new MainMenuScreen(game));
                }
            }
        };
        dialog.text("¿Desea salir del juego?");
        dialog.button("Si", true);
        dialog.button("No", false);
        dialog.show(stage);
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
    }
}
