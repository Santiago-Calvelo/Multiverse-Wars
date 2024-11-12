package com.milne.mw;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Global {
    public static boolean debugMode = true;

    public static Texture loadTexture(String path) {
        if (path != null) {
            return new Texture(Gdx.files.internal(path));
        }
        return null;
    }
}
