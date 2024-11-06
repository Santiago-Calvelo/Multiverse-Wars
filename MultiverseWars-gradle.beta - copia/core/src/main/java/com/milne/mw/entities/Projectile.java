package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class Projectile {
    private Image image;
    private Rectangle hitbox;
    private int damage = 1;
    private EntityManager entityManager;
    private String type;

    public Projectile(Texture texture, float x, float y, EntityManager entityManager, String type) {
        this.image = new Image(texture);
        this.image.setSize(20, 20);
        this.image.setPosition(x, y);
        this.hitbox = new Rectangle(x, y, 20, 20);
        this.entityManager = entityManager;
        this.type = type;

        moveAction();
    }

    private void moveAction() {
        MoveToAction moveAction = new MoveToAction();
        moveAction.setPosition(image.getX() + 800, image.getY());
        moveAction.setDuration(2);
        image.addAction(moveAction);
    }

    public void update(float delta) {
        updateHitbox();
        checkForCollision();
    }

    private void updateHitbox() {
        hitbox.setPosition(image.getX(), image.getY());
    }

    private void checkForCollision() {
        for (Character character : entityManager.getCharacters()) {
            if (hitbox.overlaps(character.getHitbox()) && !this.type.equalsIgnoreCase(character.getType())) {
                character.takeDamage(damage);
                entityManager.removeProjectile(this);
                break;
            }
        }
    }

    public Image getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public void dispose() {
        image.clearActions();
        image.remove();
    }
}
