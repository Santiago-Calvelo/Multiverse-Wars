package com.milne.mw;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.milne.mw.screens.SplashScreen;

public class MultiverseWars extends Game {
    private SpriteBatch batch;

    @Override
    public void create() {
        batch = new SpriteBatch();
        MusicManager.playMusic("bye bye.mp3"); // cargo la música de fondo
        setScreen(new SplashScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
        MusicManager.stopMusic();
    }

    public SpriteBatch getBatch() {
        return batch;
    }
}
