package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public enum EntityType {
    PEASHOOTER("lanza guisantes carta.png", "guisante.png", "guisante3.png", "guisante2.png", "proyectil.png", 30, "tower") {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Peashooter no se mueve, pasamos canMove=false
            return new RangedCharacter(this.getTexture(), this.getAttack1Texture(), this.getAttack2Texture(), this.getProjectileTexture(), null, null, x, y, this.getLives(), this, entityManager, false, stage, this.getType());
        }

        @Override
        public Texture getCardTexture() {
            return new Texture(Gdx.files.internal("lanza guisantes carta.png"));
        }
    },

    SKELETON("skeleton.png", "skeleton paso.png", "skeleton.png", "skeleton2.png", "skeleton3.png", "rayo.png", 50, "enemy") {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Skeleton se mueve, pasamos canMove=true
            return new MeleeCharacter(this.getTexture(), this.getAttack1Texture(), this.getAttack2Texture(), this.getWalk1Texture(), this.getWalk2Texture(), x, y, this.getLives(), this, entityManager, true, stage, this.getType());
        }
    };

    // Definición de atributos
    private Texture texture;
    private Texture attack1Texture;
    private Texture attack2Texture;
    private Texture projectileTexture;
    private Texture walk1Texture, walk2Texture;
    private int lives;
    private String type;

    // Constructor para personajes a distancia sin movimiento
    EntityType(String cardTexturePath, String texturePath, String attack1Path, String attack2Path, String projectilePath, int lives, String type) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.projectileTexture = new Texture(Gdx.files.internal(projectilePath));
        this.walk1Texture = null;  // No necesita caminar
        this.walk2Texture = null;
        this.lives = lives;
        this.type = type;
    }

    // Constructor para personajes cuerpo a cuerpo con movimiento
    EntityType(String texturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, String projectilePath, int lives, String type) {
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.projectileTexture = new Texture(Gdx.files.internal(projectilePath));
        this.walk1Texture = new Texture(Gdx.files.internal(walk1Path));
        this.walk2Texture = new Texture(Gdx.files.internal(walk2Path));
        this.lives = lives;
        this.type = type;
    }

    public abstract Character getEntity(float x, float y, Stage stage, EntityManager entityManager);

    public Texture getCardTexture() {
        return null;
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
}
