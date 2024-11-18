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
import com.milne.mw.maps.PauseMenu;

import java.util.ArrayList;
import java.util.Iterator;

public class RenderManager {
    private static RenderManager instance;
    private ShapeRenderer shapeRenderer;
    private Stage stage;
    private Image backgroundImage;
    private float walkAnimationTime;
    private ArrayList<AttackAnimation> attackAnimations;

    private RenderManager(Texture mapTexture, Stage stage) {
        this.stage = stage;
        this.shapeRenderer = new ShapeRenderer();
        this.backgroundImage = new Image(mapTexture);
        backgroundImage.setSize(stage.getWidth(), stage.getHeight());
        stage.addActor(backgroundImage);
        this.attackAnimations = new ArrayList<>();
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

    public void render(boolean isPaused, EntityManager entityManager, float delta, PauseMenu pauseMenu) {
        if (!isPaused) {
            stage.act(delta);
            entityManager.update(delta);
            updateWalkAnimation(delta, entityManager);
        }
        pauseMenu.checkForEscapeKey();
        stage.draw();

        if (!isPaused && Global.debugMode) {
            drawHitboxes(entityManager);
            drawPlacementZones(entityManager, pauseMenu);
        }

        updateAttackAnimations(delta);
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

    // Método para iniciar la animación del ataque
    public void animateCharacterAttack(Character character, float cooldown) {
        // Crea una nueva animación de ataque y la añade a la lista
        attackAnimations.add(new AttackAnimation(character, cooldown));
    }

    private void updateAttackAnimations(float delta) {
        // Usa un iterador para actualizar y eliminar animaciones finalizadas
        Iterator<AttackAnimation> iterator = attackAnimations.iterator();
        while (iterator.hasNext()) {
            AttackAnimation animation = iterator.next();
            if (animation.update(delta)) {
                iterator.remove(); // Elimina la animación si ha terminado
            }
        }
    }

    private void drawPlacementZones(EntityManager entityManager, PauseMenu pauseMenu) {
        shapeRenderer.setProjectionMatrix(stage.getViewport().getCamera().combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.setColor(Color.GREEN);

        shapeRenderer.circle(pauseMenu.pauseButtonHitbox.x, pauseMenu.pauseButtonHitbox.y, pauseMenu.pauseButtonHitbox.radius);
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
