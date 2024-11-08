package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public enum EntityType {
    PEASHOOTER("lanza guisantes carta.png", "guisante.png", "guisante2.png", "guisante3.png", "guisante2.png", "proyectil.png", 30, "tower", 50, 50, 0,5,1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Peashooter no se mueve, pasamos canMove=false
            return new RangedCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getWalk1Texture(), this.getWalk2Texture(), this.getAttack1Texture(), this.getAttack2Texture(), this.getProjectileTexture(), x, y, this.getLives(), this.getSpeed(), entityManager, stage, this.getType(), this.getRange(), this.getAttackCooldown());
        }
    },

    COWBOY("CARTA VAQUERO.jpg", "vaqwalk1.png", "vaqwalk2.png", "vaqattack1.png", "vaqwalk2.png", "bala.png", 25, "tower", 50, 50, 0, 7,1.5f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new RangedCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getWalk1Texture(), this.getWalk2Texture(), this.getAttack1Texture(), this.getAttack2Texture(), this.getProjectileTexture(), x, y, this.getLives(), this.getSpeed(), entityManager, stage, this.getType(), this.getRange(), this.getAttackCooldown());
        }
    },

    SKELETON("skeleton.png","skeleton.png", "skeleton paso.png", "skeleton.png", "skeleton2.png", "skeleton3.png", 25, "enemy", 50, 50, 100,1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            // Skeleton se mueve, pasamos canMove=true
            return new MeleeCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getAttack1Texture(), this.getAttack2Texture(), this.getWalk1Texture(), this.getWalk2Texture(), x, y, this.getLives(), this.getSpeed(), entityManager, stage, this.getType(), this.getAttackCooldown());
        }
    },

   STORMTROOPER("storm01.png", "storm01.png", "storm02.png", "storm03.png", "storm02.png", "laser.png", 50, "enemy", 50, 50, 100, 5, 1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new RangedCharacter(this.getTexture(), this.getHitboxWidth(), this.getHitboxHeight(), this.getWalk1Texture(), this.getWalk2Texture(), this.getAttack1Texture(), this.getAttack2Texture(), this.getProjectileTexture(), x, y, this.getLives(), this.getSpeed(), entityManager, stage, this.getType(), this.getRange(), this.getAttackCooldown());
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
    private int range;
    private float attackCooldown;

    // Constructor para personajes a distancia sin movimiento
    EntityType(String cardTexturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, String projectilePath, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, int range, float attackCooldown) {
        if (cardTexturePath != null) {
            this.cardTexturePath = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texture = new Texture(Gdx.files.internal(walk1Path));
        this.walk1Texture = new Texture(Gdx.files.internal(walk1Path));
        this.walk2Texture = new Texture(Gdx.files.internal(walk2Path));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.projectileTexture = new Texture(Gdx.files.internal(projectilePath));
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
        this.range = range;
        this.attackCooldown = attackCooldown;
    }

    EntityType(String cardTexturePath,String texturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, float attackCooldown) {
        if (cardTexturePath != null) {
            this.cardTexturePath = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texture = new Texture(Gdx.files.internal(texturePath));
        this.attack1Texture = new Texture(Gdx.files.internal(attack1Path));
        this.attack2Texture = new Texture(Gdx.files.internal(attack2Path));
        this.walk1Texture = new Texture(Gdx.files.internal(walk1Path));
        this.walk2Texture = new Texture(Gdx.files.internal(walk2Path));
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
        this.attackCooldown = attackCooldown;
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

    public int getRange() {
        return range;
    }

    public float getAttackCooldown() {
        return attackCooldown;
    }
}
