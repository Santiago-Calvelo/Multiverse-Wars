package com.milne.mw.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.MultiverseWars;

public class SplashScreen implements Screen {

    private MultiverseWars game;
    private Texture splashImage;
    private Viewport viewport;

    public SplashScreen(MultiverseWars game) {
        this.game = game;
        this.viewport = new FitViewport(800, 600); // Tamaño base, se ajustará
    }

    @Override
    public void show() {
        splashImage = new Texture(Gdx.files.internal("splash.png")); // Asegúrate de tener esta imagen en tu carpeta assets

        // Temporizador para cambiar a la pantalla del menú después de 3 segundos
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                game.setScreen(new MainMenuScreen(game)); // Cambia a la pantalla del menú principal
            }
        }, 3); // 3 segundos
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        game.getBatch().setProjectionMatrix(viewport.getCamera().combined);
        game.getBatch().begin();
        game.getBatch().draw(splashImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        splashImage.dispose();
    }
}
