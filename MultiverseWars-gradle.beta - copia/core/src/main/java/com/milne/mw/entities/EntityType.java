package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;

public enum EntityType {
    PEASHOOTER("peashooter/lanzaguisantes-carta.png", "peashooter/lanzaguisantes.png", "peashooter/lanzaguisantes2.png", "peashooter/lanzaguisantes3.png", "peashooter/lanzaguisantes2.png", "projectile/guisante.png", 30, "tower", 50, 50, 0,5,1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, stage, getType(),
                getRange(), getAttackCooldown()
            );
        }
    },

    COWBOY("cowboy/carta-vaquero.jpg", "cowboy/vaqueroMov1.png", "cowboy/vaqueroMov2.png", "cowboy/vaqueroAtaque1.png", "cowboy/vaqueroMov2.png", "projectile/bala.png", 25, "tower", 50, 50, 0, 7,1.5f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, stage, getType(),
                getRange(), getAttackCooldown()
            );
        }
    },

    SKELETON(null, "skeleton/skeleton.png", "skeleton/skeleton paso.png", "skeleton/skeleton2.png", "skeleton/skeleton3.png", 25, "enemy", 50, 50, 100,1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, stage, getType(), getAttackCooldown()
            );
        }
    },

    STORMTROOPER(null, "stormptrooper/storm01.png", "stormtrooper/storm02.png", "stormtrooper/storm03.png", "stormtrooper/storm02.png", "projectile/laser.png", 50, "enemy", 50, 50, 100, 5, 1f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, stage, getType(),
                getRange(), getAttackCooldown()
            );
        }
    },

    BARBARIAN("barbarian/barbaro01.png", "barbarian/barbaro01.png", "barbarian/barbaro02.png", "barbarian/barbaro03.png", "barbarian/barbaro04.png", 40, "enemy", 50, 50, 50, 0.5f) {
        @Override
        public Character getEntity(float x, float y, Stage stage, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, stage, getType(), getAttackCooldown()
            );
        }
    };

    // Atributos
    private Texture cardTexture;
    private String texturePath;
    private String walk1Path, walk2Path;
    private String attack1Path, attack2Path;
    private String projectilePath;
    private int lives, hitboxWidth, hitboxHeight, speed, range;
    private String type;
    private float attackCooldown;

    // Constructor
    EntityType(String cardTexturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, String projectilePath, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, int range, float attackCooldown) {
        if (cardTexturePath != null) {
            this.cardTexture = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texturePath = walk1Path;
        this.walk1Path = walk1Path;
        this.walk2Path = walk2Path;
        this.attack1Path = attack1Path;
        this.attack2Path = attack2Path;
        this.projectilePath = projectilePath;
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
        this.range = range;
        this.attackCooldown = attackCooldown;
    }

    // Constructor alternativo para entidades sin proyectil
    EntityType(String cardTexturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, float attackCooldown) {
        if (cardTexturePath != null) {
            this.cardTexture = new Texture(Gdx.files.internal(cardTexturePath));
        }
        this.texturePath = walk1Path;
        this.attack1Path = attack1Path;
        this.attack2Path = attack2Path;
        this.walk1Path = walk1Path;
        this.walk2Path = walk2Path;
        this.lives = lives;
        this.type = type;
        this.hitboxWidth = hitboxWidth;
        this.hitboxHeight = hitboxHeight;
        this.speed = speed;
        this.attackCooldown = attackCooldown;
    }
    // Método abstracto para obtener la entidad
    public abstract Character getEntity(float x, float y, Stage stage, EntityManager entityManager);

    // Método para cargar texturas desde archivos
    protected Texture loadTexture(String path) {
        if (path != null) {
            return new Texture(Gdx.files.internal(path));
        }
        return null;
    }

    // Getters
    public Texture getCardTexture() { return cardTexture; }
    public String getTexturePath() { return texturePath; }
    public String getWalk1Path() { return walk1Path; }
    public String getWalk2Path() { return walk2Path; }
    public String getAttack1Path() { return attack1Path; }
    public String getAttack2Path() { return attack2Path; }
    public String getProjectilePath() { return projectilePath; }
    public String getType() { return type; }
    public int getLives() { return lives; }
    public int getHitboxWidth() { return hitboxWidth; }
    public int getHitboxHeight() { return hitboxHeight; }
    public int getSpeed() { return speed; }
    public int getRange() { return range; }
    public float getAttackCooldown() { return attackCooldown; }
}
