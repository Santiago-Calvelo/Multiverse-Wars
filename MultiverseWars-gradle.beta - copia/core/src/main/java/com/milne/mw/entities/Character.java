package com.milne.mw.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.Timer.Task;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.Gdx;
import com.milne.mw.renders.RenderManager;

public abstract class Character {
    protected Image image;
    private Rectangle hitbox;
    private int lives;
    private float x;
    private float y;
    private EntityType entityType;
    protected EntityManager entityManager;
    private int speed;
    private Texture walk1Texture;
    private Texture walk2Texture;
    private Texture attack1Texture;
    private Texture attack2Texture;
    private Stage stage;
    private MoveToAction moveAction;
    private boolean isMoving;
    private String type;
    private boolean canAttack;
    private float attackCooldown;

    public Character(Texture texture, float x, float y, int hitboxWidth, int hitboxHeight, int lives, EntityType entityType, EntityManager entityManager, int speed, Texture walk1Texture, Texture walk2Texture, Texture attack1Texture, Texture attack2Texture,  Stage stage, String type, float attackCooldown) {
        this.image = new Image(texture);
        this.image.setPosition(x, y);
        this.image.setSize(50, 50);
        this.hitbox = new Rectangle(x, y, hitboxWidth, hitboxHeight);  // Inicializamos la hitbox
        this.lives = lives;
        this.x = x;
        this.y = y;
        this.entityType = entityType;
        this.entityManager = entityManager;
        this.walk1Texture = walk1Texture;
        this.walk2Texture = walk2Texture;
        this.attack1Texture = attack1Texture;
        this.attack2Texture = attack2Texture;
        this.stage = stage;
        this.type = type;
        this.isMoving = false;
        this.speed = speed;
        this.canAttack = true;
        this.attackCooldown = attackCooldown;
        // Si el personaje puede moverse, iniciamos la animación y el movimiento
        if (speed != 0) {
            startMovement();
        }
    }

    // Método para actualizar la hitbox a la nueva posición del personaje
    private void updateHitbox() {
        hitbox.setPosition(image.getX(), image.getY());  // Actualiza las coordenadas
    }

    // Método para iniciar el movimiento en la dirección X
    public void startMovement() {
        if (!isMoving) {
            animateWalk();
            moveAction = new MoveToAction();

            // Establecemos la posición final en el eje X (salir de la pantalla por el lado izquierdo)
            float targetX = -image.getWidth();

            // Calculamos la distancia que el personaje recorrerá en X
            float distanceX = image.getX() - targetX;

            // La duración del movimiento será inversamente proporcional a la velocidad.
            // Ajustamos la fórmula para que el movimiento sea visible y proporcional al 'speed'.
            float duration = distanceX / speed;  // Duración más directa

            // Ajustamos la acción de movimiento
            moveAction.setPosition(targetX, image.getY());  // Solo movemos en el eje X
            moveAction.setDuration(duration);  // Usamos la duración calculada

            // Añadimos la acción de movimiento al personaje
            image.addAction(moveAction);
            isMoving = true;
        }
    }



    // Método para detener el movimiento y atacar
    public void stopMovementAndAttack() {
        if (isMoving) {
            image.clearActions();  // Detener todas las acciones
            isMoving = false;
        }
        attack();  // Iniciar el ataque
    }

    // Método para reanudar el movimiento si no hay colisiones
    public void resumeMovement() {
        if (!isMoving && speed != 0) {
            startMovement();
        }
    }

    // Método para animar el caminar del personaje
    private void animateWalk() {
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

    public void tryAttack() {
        RenderManager renderManager = RenderManager.getInstance();

        if (canAttack) {
            attack();  // Las subclases implementan la lógica específica del ataque
            renderManager.animateCharacterAttack(this,this.attackCooldown);
            canAttack = false;  // Después de atacar, deshabilitamos el ataque temporalmente

            startAttackCooldown();  // Iniciar el cooldown
        }
    }

    // Método que controla el enfriamiento del ataque
    private void startAttackCooldown() {
        Timer.schedule(new Task() {
            @Override
            public void run() {
                canAttack = true;  // Habilitar el ataque después del cooldown
            }
        }, attackCooldown);  // Ajustamos el enfriamiento con el valor de "attackCooldown"
    }

    // Las subclases deben implementar su propia lógica de ataque
    public abstract void attack();

    public abstract void checkForAttack();

    public void takeDamage(int damage) {
        this.lives -= damage;  // Reducir las vidas
        Gdx.app.log(this.getClass().getSimpleName(), "Hit! Remaining lives: " + lives);

        if (lives <= 0) {
            Gdx.app.log(this.getClass().getSimpleName(), "Died!");
            dispose();
            entityManager.getCharacters().removeValue(this, true);  // Remover de la lista de personajes
        }
    }

    public Image getImage() {
        return image;
    }

    public Rectangle getHitbox() {
        updateHitbox();
        return hitbox;
    }

    public String getType() {
        return type;
    }

    public int getLives() {
        return lives;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public int getSpeed() {
        return speed;
    }

    public Texture getAttack1Texture() {
        return attack1Texture;
    }

    public Texture getAttack2Texture() {
        return attack2Texture;
    }

    public void dispose() {
        // Eliminar todas las acciones
        image.clearActions();
        // Removerlo del escenario
        image.remove();
    }

}

