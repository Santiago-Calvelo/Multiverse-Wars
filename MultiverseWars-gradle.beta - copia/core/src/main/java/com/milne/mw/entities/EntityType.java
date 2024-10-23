package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public enum EntityType {
    PEASHOOTER("lanza guisantes carta.png", "guisante.png", "guisante3.png", "guisante2.png", "proyectil.png", 30, "tower", 50, 50, 0) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Peashooter no se mueve, pasamos canMove=false
            return new RangedCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getAttack1Texture(), this.getAttack2Texture(), this.getProjectileTexture(), null, null, x, y, this.getLives(), this, entityManager, this.getSpeed(), stage, this.getType());
        }
    },

    SKELETON("skeleton.png","skeleton.png", "skeleton paso.png", "skeleton.png", "skeleton2.png", "skeleton3.png", "rayo.png", 50, "enemy", 50, 50, 100) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Skeleton se mueve, pasamos canMove=true
            return new MeleeCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getAttack1Texture(), this.getAttack2Texture(), this.getWalk1Texture(), this.getWalk2Texture(), x, y, this.getLives(), this, entityManager, this.getSpeed(), stage, this.getType());
        }
    };

    // Definición de atributos
    private Texture cardTexturePath = null;
    private Texture texture;
    private Texture attack1Texture;
    private Texture attack2Texture;
    private Texture projectileTexture;
    private Texture walk1Texture, walk2Texture;
    private int lives;
    private String type;
    private int hitboxWidth;
    private int hitboxHeight;
    private int speed;

    // Constructor para personajes a distancia sin movimiento
    EntityType(String cardTexturePath, String texturePath, String attack1Path, String attack2Path, String projectilePath, int lives, String type, int hitboxWidth, int hitboxHeight, int speed) {
        if (cardTexturePath != null) {
            this.cardTexturePath = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.projectileTexture = new Texture(Gdx.files.internal(projectilePath));
        this.walk1Texture = null;  // No necesita caminar
        this.walk2Texture = null;
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
    }

    // Constructor para personajes cuerpo a cuerpo con movimiento
    EntityType(String cardTexturePath,String texturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, String projectilePath, int lives, String type, int hitboxWidth, int hitboxHeight, int speed) {
        if (cardTexturePath != null) {
            this.cardTexturePath = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.projectileTexture = new Texture(Gdx.files.internal(projectilePath));
        this.walk1Texture = new Texture(Gdx.files.internal(walk1Path));
        this.walk2Texture = new Texture(Gdx.files.internal(walk2Path));
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
    }

    public abstract Character getEntity(float x, float y, Stage stage, EntityManager entityManager);

    public Texture getCardTexture() {
        return cardTexturePath;
    }

    public Texture getTexture() {
        return texture;
    }

    public Texture getAttack1Texture() {
        return attack1Texture;
    }

    public Texture getAttack2Texture() {
        return attack2Texture;
    }

    public Texture getProjectileTexture() {
        return projectileTexture;
    }

    public Texture getWalk1Texture() {
        return walk1Texture;
    }

    public Texture getWalk2Texture() {
        return walk2Texture;
    }

    public int getLives() {
        return lives;
    }

    public String getType() {
        return type;
    }

    public int getHitboxWidth() {
        return hitboxWidth;
    }

    public int getHitboxHeight() {
        return hitboxHeight;
    }

    public int getSpeed() {
        return speed;
    }
}
