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
    private float dyingTime;
    private boolean isAnimatingAttack, isDying;
    private Character attackingCharacter, dyingCharacter; // Solo animar este personaje

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
        }
        pauseButton.checkForEscapeKey();
        stage.draw();

        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager);
            drawPlacementZones(entityManager, pauseButton);
        }

        // Llama solo si hay una animación de ataque activa
        if (isAnimatingAttack && !isDying) {
            updateAttackAnimation(delta);
        }

        if (isDying) {
            updateDeathAnimation(delta);
        }
    }

    private void updateWalkAnimation(float delta, EntityManager entityManager) {
        walkAnimationTime += delta;

        // Alterna la textura de caminata cada 0.5 segundos
        if (walkAnimationTime >= 0.5f) {
            for (Character character : entityManager.getCharacters()) {
                if (character.getLives() > 0) {
                    TextureRegionDrawable currentDrawable = (TextureRegionDrawable) character.getImage().getDrawable();
                    TextureRegionDrawable nextDrawable = (currentDrawable.getRegion().getTexture() == character.getWalk1Texture())
                        ? new TextureRegionDrawable(character.getWalk2Texture())
                        : new TextureRegionDrawable(character.getWalk1Texture());

                    character.getImage().setDrawable(nextDrawable);
                }
            }
            walkAnimationTime = 0;
        }
    }

   public void animateCharacterAttack(Character character, float cooldown) {
        if (isDying && dyingCharacter == character) {
            return;
        }
        character.getImage().setDrawable(new TextureRegionDrawable(character.getAttack1Texture()));
        attackAnimationTime = 0;
        isAnimatingAttack = true;
        attackingCharacter = character; // Establece el personaje que está atacando
    }

    private void updateAttackAnimation(float delta) {
        attackAnimationTime += delta;
        if (attackAnimationTime >= 0.5f && attackingCharacter != null) {
            attackingCharacter.getImage().setDrawable(new TextureRegionDrawable(attackingCharacter.getAttack2Texture()));
            isAnimatingAttack = false;
            attackingCharacter = null; // Resetea después de la animación
        }
    }

    public void animateDead(Character character, EntityManager entityManager) {
        if (isAnimatingAttack && attackingCharacter == character) {
            isAnimatingAttack = false;
            attackingCharacter = null;
        }
        isDying = true;
        dyingTime = 0;
        dyingCharacter = character;
    }

    private void updateDeathAnimation(float delta) {
        dyingTime += delta;
        if (dyingTime >= 0.5f && dyingCharacter != null) {
            dyingCharacter.getImage().setDrawable(new TextureRegionDrawable(dyingCharacter.getDeathTexture()));
            isDying = false;
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
