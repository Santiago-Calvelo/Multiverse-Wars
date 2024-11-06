package com.milne.mw.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.MultiverseWars;

public class SplashScreen implements Screen {

    private MultiverseWars game;
    private Stage stage;
    private Texture splashImage;
    private Viewport viewport;

    public SplashScreen(MultiverseWars game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600);
        this.stage = new Stage(viewport);
    }

    @Override
    public void show() {
        splashImage = new Texture(Gdx.files.internal("splash.png"));
        Image splash = new Image(splashImage);
        splash.setSize(viewport.getWorldWidth(), viewport.getWorldHeight());
        stage.addActor(splash);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MainMenuScreen(game));
            }
        }, 3);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void pause() {
        
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        dispose();
    }

    @Override
    public void dispose() {
        stage.dispose();
        splashImage.dispose();
    }
}
