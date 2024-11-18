package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class FlyCharacter extends Character{
    private final Texture projectileTexture;
    private final int range;

    public FlyCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture walk1Texture,
                        Texture walk2Texture, Texture attack1Texture, Texture attack2Texture,
                        Texture projectileTexture, float x, float y, int lives,
                        int speed, EntityManager entityManager,
                        String type, int range, float attackCooldown, int damage, int energy) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed, walk1Texture, walk2Texture, attack1Texture, attack2Texture, type, attackCooldown, damage, energy);
        this.projectileTexture = projectileTexture;
        this.range = range;
        System.out.println(y);
    }

    public void bombAttack() {
        float bombX = image.getX() + image.getWidth() / 2 + entityManager.getCellWidth();
        float bombY = image.getY() + image.getHeight();
        float targetY = image.getY();
        Bomb bomb = new Bomb(projectileTexture, bombX, bombY, getDamage(), entityManager, targetY);
        entityManager.addBomb(bomb);
    }

    public void callEnemies() {

    }

    @Override
    public void attack() {
        if (this.getType().equalsIgnoreCase("tower")) {
            bombAttack();
        } else {
            callEnemies();
        }
    }

    @Override
    public void checkForAttack(Array<Character> characters) {
        tryAttack();
    }
}
