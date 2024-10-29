package com.milne.mw.renders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.milne.mw.entities.Character;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.Global;
import com.milne.mw.maps.PauseButton;

public class RenderManager {
    private static RenderManager instance;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Image backgroundImage;
    private float attackAnimationTime;
    private boolean isAnimatingAttack;

    private RenderManager(Texture mapTexture, Stage stage) {
        this.stage = stage;
        this.shapeRenderer = new ShapeRenderer();
        this.backgroundImage = new Image(mapTexture);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);
    }

    public static RenderManager getInstance(Texture mapTexture, Stage stage) {
        if (instance == null) {
            instance = new RenderManager(mapTexture, stage);
        }
        return instance;
    }

    public static void resetInstance() {
        if (instance != null) {
            instance.dispose();
            instance = null;
        }
    }

    public static RenderManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("RenderManager no ha sido inicializado. Llama a getInstance() con parámetros primero.");
        }
        return instance;
    }

    public void render(Viewport viewport, boolean isPaused, EntityManager entityManager, float delta, PauseButton pauseButton) {
        if (!isPaused) {
            stage.act(delta);
            entityManager.update(delta);
        }
        pauseButton.checkForEscapeKey();
        stage.draw();

        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager, viewport);
            drawPlacementZones(viewport, entityManager, pauseButton);
        }

        if (isAnimatingAttack) {
            updateAttackAnimation(delta,entityManager);
        }
    }

    public void animateCharacterAttack(Character character, float cooldown) {
        character.getImage().setDrawable(new TextureRegionDrawable(character.getAttack1Texture()));
        attackAnimationTime = 0;
        isAnimatingAttack = true;
    }

    private void updateAttackAnimation(float delta, EntityManager entityManager) {
        attackAnimationTime += delta;
        if (attackAnimationTime >= 0.5f) {
            for (Character character : entityManager.getCharacters()) {
                character.getImage().setDrawable(new TextureRegionDrawable(character.getAttack2Texture()));
            }
            isAnimatingAttack = false;
        }
    }

    private void drawPlacementZones(Viewport viewport, EntityManager entityManager, PauseButton pauseButton) {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.circle(pauseButton.pauseButtonHitbox.x, pauseButton.pauseButtonHitbox.y, pauseButton.pauseButtonHitbox.radius);
        for (Rectangle hitbox : entityManager.getPlacementHitboxes()) {
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        shapeRenderer.end();
    }

    private void drawHitboxes(EntityManager entityManager, Viewport viewport) {
        shapeRenderer.setProjectionMatrix(viewport.getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.RED);

        for (Character character : entityManager.getCharacters()) {
            Rectangle hitbox = character.getHitbox();
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        shapeRenderer.end();
    }

    public void dispose() {
        shapeRenderer.dispose();
        backgroundImage.remove();
    }
}
