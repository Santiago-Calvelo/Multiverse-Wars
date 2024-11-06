package com.milne.mw.renders;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.milne.mw.entities.Character;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.Global;
import com.milne.mw.maps.PauseButton;

public class RenderManager {
    private static RenderManager instance;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Image backgroundImage;
    private float walkAnimationTime;
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

    public void render(boolean isPaused, EntityManager entityManager, float delta, PauseButton pauseButton) {
        if (!isPaused) {
            stage.act(delta);
            entityManager.update(delta);
            updateWalkAnimation(delta, entityManager);
            updateAttackAnimation(delta, entityManager);
        }
        pauseButton.checkForEscapeKey();
        stage.draw();

        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager);
            drawPlacementZones(entityManager, pauseButton);
        }
    }

    private void updateWalkAnimation(float delta, EntityManager entityManager) {
        walkAnimationTime += delta;

        // Alterna la textura de caminata cada 0.5 segundos
        if (walkAnimationTime >= 0.5f) {
            for (Character character : entityManager.getCharacters()) {
                TextureRegionDrawable currentDrawable = (TextureRegionDrawable) character.getImage().getDrawable();
                TextureRegionDrawable nextDrawable = (currentDrawable.getRegion().getTexture() == character.getWalk1Texture())
                    ? new TextureRegionDrawable(character.getWalk2Texture())
                    : new TextureRegionDrawable(character.getWalk1Texture());

                character.getImage().setDrawable(nextDrawable);
            }
            walkAnimationTime = 0;
        }
    }

    public void animateCharacterAttack(Character character, float cooldown) {
        character.getImage().setDrawable(new TextureRegionDrawable(character.getAttack1Texture()));
        attackAnimationTime = 0;
        isAnimatingAttack = true;
    }

    private void updateAttackAnimation(float delta, EntityManager entityManager) {
        if (!isAnimatingAttack) return;

        attackAnimationTime += delta;
        if (attackAnimationTime >= 0.5f) {
            for (Character character : entityManager.getCharacters()) {
                character.getImage().setDrawable(new TextureRegionDrawable(character.getAttack2Texture()));
            }
            isAnimatingAttack = false;
        }
    }

    private void drawPlacementZones(EntityManager entityManager, PauseButton pauseButton) {
        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.circle(pauseButton.pauseButtonHitbox.x, pauseButton.pauseButtonHitbox.y, pauseButton.pauseButtonHitbox.radius);
        for (Rectangle hitbox : entityManager.getPlacementHitboxes()) {
            shapeRenderer.rect(hitbox.x, hitbox.y, hitbox.width, hitbox.height);
        }

        shapeRenderer.end();
    }

    private void drawHitboxes(EntityManager entityManager) {
        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
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
