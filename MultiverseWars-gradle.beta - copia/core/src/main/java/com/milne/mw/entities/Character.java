package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;

public abstract class Character {
    protected Image image;
    private Rectangle hitbox;
    protected int lives;
    private float x;
    private float y;
    private EntityType entityType;
    protected EntityManager entityManager;
    private boolean canMove;  // Ahora se decide si puede moverse
    private Texture walk1Texture;
    private Texture walk2Texture;
    protected Stage stage;

    public Character(Texture texture, float x, float y, int lives, EntityType entityType, EntityManager entityManager, boolean canMove, Texture walk1Texture, Texture walk2Texture, Stage stage) {
        this.image = new Image(texture);
        this.image.setPosition(x, y);
        this.image.setSize(50, 50);
        this.hitbox = new Rectangle(x, y, 50, 50);
        this.lives = lives;
        this.x = x;
        this.y = y;
        this.entityType = entityType;
        this.entityManager = entityManager;
        this.canMove = canMove;
        this.walk1Texture = walk1Texture;
        this.walk2Texture = walk2Texture;
        this.stage = stage;

        // Si el personaje puede moverse, iniciamos la animación y el movimiento
        if (canMove) {
            moveToAction();
        }
    }

    // Método para mover al personaje y animar caminar si tiene texturas
    private void moveToAction() {
        animateWalk();  // Solo animar caminar si tiene las texturas

        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(0, y);  // Movimiento hacia la izquierda
        moveAction.setDuration(10);  // Duración del movimiento
        image.addAction(moveAction);
    }

    // Método para animar el caminar del personaje
    protected void animateWalk() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                image.setDrawable(new TextureRegionDrawable(new TextureRegion(walk1Texture)));
                Timer.schedule(new Task() {
                    @Override
                    public void run() {
                        image.setDrawable(new TextureRegionDrawable(new TextureRegion(walk2Texture)));
                    }
                }, 0.5f);
            }
        }, 0, 1);  // Cambiar texturas de caminar cada 0.5 segundos
    }

    public abstract void attack();

    public void takeDamage(int damage) {
        this.lives -= damage;  // Reducir las vidas
        Gdx.app.log(this.getClass().getSimpleName(), "Hit! Remaining lives: " + lives);

        if (lives <= 0) {
            Gdx.app.log(this.getClass().getSimpleName(), "Died!");
            image.remove();  // Eliminar del escenario
            entityManager.getCharacters().removeValue(this, true);  // Remover de la lista de personajes
        }
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public int getLives() {
        return lives;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean canMove() {
        return canMove;
    }
}
