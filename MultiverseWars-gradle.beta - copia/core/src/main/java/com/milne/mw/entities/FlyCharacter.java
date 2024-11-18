package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Array;

public class FlyCharacter extends Character {
    private int enemiesSpawned = 0;

    public FlyCharacter(Texture texture, int hitboxWidth, int hitboxHeight, Texture walkTexture,
                        Texture attackTexture, float x, float y, int lives, int speed,
                        EntityManager entityManager, String type, float attackCooldown,
                        int damage, int energy) {
        super(texture, x, y, hitboxWidth, hitboxHeight, lives, entityManager, speed, walkTexture, walkTexture, attackTexture, attackTexture, type, attackCooldown, damage, energy);
        System.out.println(y);
    }

    public void bombAttack() {
        float bombX = image.getX() + image.getWidth() / 2 + entityManager.getCellWidth();
        float bombY = image.getY() + image.getHeight();
        float targetY = image.getY();
        Bomb bomb = new Bomb(bombX, bombY, getDamage(), entityManager, targetY);
        entityManager.addBomb(bomb);
    }

    public void callEnemies() {
        final float MIDDLE_X = entityManager.getCellWidth() * 3;
        final int maxEnemies = 3;

        if (image.getX() >= MIDDLE_X && enemiesSpawned < maxEnemies) {


            EntityType[] enemyTypes = EntityType.values();
            Array<EntityType> enemyList = new Array<>();
            for (EntityType type : enemyTypes) {
                if (type.getType().equalsIgnoreCase("enemy") && type.getCanBeSpawned()) {
                    enemyList.add(type);
                }
            }

            if (!enemyList.isEmpty()) {
                int randomIndex = (int) (Math.random() * enemyList.size);
                EntityType randomEnemy = enemyList.get(randomIndex);

                float spawnX = image.getX() + (float) randomEnemy.getHitboxWidth() / 2;
                float spawnY = image.getY() + (float) randomEnemy.getHitboxHeight() / 2;
                entityManager.spawnEntity(randomEnemy, spawnX, spawnY);
                entityManager.setEnemiesInGame(1);
                enemiesSpawned++;
            }
        }
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
