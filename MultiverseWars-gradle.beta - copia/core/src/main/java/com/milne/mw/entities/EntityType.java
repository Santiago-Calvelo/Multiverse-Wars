package com.milne.mw.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import static com.milne.mw.Global.loadTexture;

public enum EntityType {
    PEASHOOTER("characters/peashooter/lanzaguisantes-carta.png", "characters/peashooter/lanzaguisantes.png", "characters/peashooter/lanzaguisantes2.png", "characters/peashooter/lanzaguisantes3.png", "characters/peashooter/lanzaguisantes2.png", "characters/projectile/guisante.png",  30, "tower", 50, 50, 0, 5, 1f, 5, 20) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, getType(),
                getRange(), getAttackCooldown(), getDamage(), getEnergy()
            );
        }
    },

    COWBOY("characters/cowboy/carta-vaquero.jpg", "characters/cowboy/vaqueroMov1.png", "characters/cowboy/vaqueroMov2.png", "characters/cowboy/vaqueroAtaque1.png", "characters/cowboy/vaqueroMov2.png", "characters/projectile/bala.png",  25, "tower", 50, 50, 0, 7, 1.5f, 8, 75) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, getType(),
                getRange(), getAttackCooldown(), getDamage(), getEnergy()
            );
        }
    },

    JIRAIYA("characters/jiraiya/carta-jiraiya.jpg", "characters/jiraiya/jiraiya01.png", "characters/jiraiya/jiraiya02.png", "characters/jiraiya/jiraiya04.png", "characters/jiraiya/jiraiya04.png",  1000, "tower", 50, 50, 0, 3f, 0, 25) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, getType(), getAttackCooldown(),
                getDamage(), getEnergy()
            );
        }
    },

    VOLTORB("characters/voltorb/carta-voltorb.jpg", "characters/voltorb/vol01.png", "characters/voltorb/vol01.png", "characters/voltorb/ZAAP!.png",  "characters/voltorb/ZAAP!.png", 1000, "tower", 50, 50, 0, 3f, 0, 25) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, getType(), getAttackCooldown(),
                getDamage(), getEnergy()
            );
        }
    },

    BLOON("characters/bloon/CARTA-GLOBO.jpg", "characters/bloon/globo0.png", "characters/bloon/globo1.png", "characters/bloon/globo0.png", "characters/bloon/globo2.png", "characters/projectile/bomba.png", 10, "tower", 50, 50, 0, 100, 1f, 25, 125) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new FlyCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, getType(),
                getRange(), getAttackCooldown(), getDamage(), getEnergy()
            );
        }
    },

    SKELETON("characters/skeleton/skeleton.png", "characters/skeleton/skeleton.png", "characters/skeleton/skeleton paso.png", "characters/skeleton/skeleton2.png", "characters/skeleton/skeleton3.png",  25, "enemy", 50, 50, 100, 1f, 10, 5) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, getType(), getAttackCooldown(),
                getDamage(), getEnergy()
            );
        }
    },

    STORMTROOPER(null, "characters/stormtrooper/storm01.png", "characters/stormtrooper/storm02.png", "characters/stormtrooper/storm03.png", "characters/stormtrooper/storm02.png", "characters/projectile/laser.png",  35, "enemy", 50, 50, 100, 5, 1f, 5, 10) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, getType(),
                getRange(), getAttackCooldown(), getDamage(), getEnergy()
            );
        }
    },

    BARBARIAN(null, "characters/barbarian/barbaro01.png", "characters/barbarian/barbaro02.png", "characters/barbarian/barbaro03.png", "characters/barbarian/barbaro04.png", 40, "enemy", 50, 50, 50, 0.5f, 15, 15) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new MeleeCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(),
                loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),  loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), x, y, getLives(), getSpeed(), entityManager, getType(), getAttackCooldown(),
                getDamage(), getEnergy()
            );
        }
    },

    SOLDIER(null, "characters/soldier/soldado01.png", "characters/soldier/soldado02.png", "characters/soldier/soldado03.png", "characters/soldier/soldado04.png", "characters/projectile/bala.png",  20, "enemy", 50, 50, 75, 7, 1.5f, 8, 12) {
        @Override
        public Character getEntity(float x, float y, EntityManager entityManager) {
            return new RangedCharacter(
                loadTexture(getTexturePath()), getHitboxWidth(), getHitboxHeight(), loadTexture(getWalk1Path()),
                loadTexture(getWalk2Path()), loadTexture(getAttack1Path()), loadTexture(getAttack2Path()),
                loadTexture(getProjectilePath()), x, y, getLives(), getSpeed(), entityManager, getType(),
                getRange(), getAttackCooldown(), getDamage(), getEnergy()
            );
        }
    };

    // Atributos
    private Texture cardTexture;
    private String texturePath;
    private String walk1Path, walk2Path;
    private String attack1Path, attack2Path;
    private String projectilePath;
    private int lives, hitboxWidth, hitboxHeight, speed, range, damage, energy;
    private String type;
    private float attackCooldown;

    // Constructor
    EntityType(String cardTexturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, String projectilePath, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, int range, float attackCooldown, int damage, int energy) {
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
        this.damage = damage;
        this.energy = energy;
    }

    // Constructor alternativo para entidades sin proyectil
    EntityType(String cardTexturePath, String walk1Path, String walk2Path, String attack1Path, String attack2Path, int lives, String type, int hitboxWidth, int hitboxHeight, int speed, float attackCooldown, int damage, int energy) {
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
        this.damage = damage;
        this.energy = energy;
    }

    // Método abstracto para obtener la entidad
    public abstract Character getEntity(float x, float y, EntityManager entityManager);

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
    public int getDamage() { return damage; }
    public int getEnergy() { return energy; }
}
