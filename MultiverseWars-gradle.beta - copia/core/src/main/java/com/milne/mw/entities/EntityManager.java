package com.milne.mw.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class EntityManager {
    private Stage stage;
    private Array<Character> characters;
    private Viewport viewport;
    private List<Rectangle> placementHitboxes;  // Lista de hitboxes para los puntos de colocación
    private boolean[] isOccupied;
    private final float INITIAL_X = 33;
    private final float INITIAL_Y = 42;
    private final float CELL_WIDTH = 62;
    private final float CELL_HEIGHT = 79;
    private final int COLS = 10;
    private final int ROWS = 5;
    private final float HITBOX_SIZE = 60;  // Tamaño de las hitboxes de colocación

    public EntityManager(Stage stage, Viewport viewport) {
        this.stage = stage;
        this.viewport = viewport;
        characters = new Array<>();
        this.placementHitboxes = new ArrayList<>();
        initPlacementPoints();
    }

    // Inicializar los puntos de colocación y sus hitboxes
    public void initPlacementPoints() {
        placementHitboxes.clear();
        isOccupied = new boolean[COLS * ROWS];
        for (int i = 0; i < COLS; i++) {
            for (int j = 0; j < ROWS; j++) {
                float centerX = INITIAL_X + i * CELL_WIDTH;
                float centerY = INITIAL_Y + j * CELL_HEIGHT;

                // Crear una hitbox alrededor del punto central
                Rectangle hitbox = new Rectangle(
                    centerX - HITBOX_SIZE / 2,  // La esquina inferior izquierda de la hitbox
                    centerY - HITBOX_SIZE / 2,
                    HITBOX_SIZE,  // Ancho de la hitbox
                    HITBOX_SIZE); // Alto de la hitbox

                placementHitboxes.add(hitbox);  // Guardamos la hitbox
            }
        }
    }

    // Método que maneja la colocación de entidades en el mapa
    public void handleEntityPlacement(EntityType entityType, float x, float y, float cardWidth, float cardHeight) {
        boolean placed = false;  // Para saber si la entidad fue colocada
        int i = 0;

        // Creamos una hitbox representando toda la carta (área de la carta)
        Rectangle cardArea = new Rectangle(x, y, cardWidth, cardHeight);

        // Hacemos un do-while para recorrer todos los puntos hasta que se coloque la entidad o se terminen los puntos
        do {
            Rectangle hitbox = placementHitboxes.get(i);

            // Verificamos si la hitbox de colocación se solapa con la hitbox de la carta
            if (!isOccupied[i] && hitbox.overlaps(cardArea)) {
                // Obtener el centro del rectángulo para colocar la entidad centrada
                float centerX = hitbox.x + hitbox.width / 2;
                float centerY = hitbox.y + hitbox.height / 2;

                // Colocar la entidad en ese punto
                spawnEntity(entityType, centerX, centerY);
                isOccupied[i] = true;  // Marcar el punto como ocupado
                placed = true;  // Indicamos que la entidad fue colocada
            }

            i++;  // Pasamos al siguiente punto
        } while (!placed && i < placementHitboxes.size());

        // Si no se encontró un punto válido cercano
        if (!placed) {
            System.out.println("No se encontró un punto válido cerca de x = " + x + ", y = " + y);
        }
    }

    // Colocar la entidad, centrando la hitbox sobre el punto
    public void spawnEntity(EntityType entityType, float x, float y) {
        Character entity = entityType.getEntity(x, y, stage, this);

        // Obtener el tamaño de la hitbox del personaje
        float hitboxWidth = entity.getImage().getWidth();
        float hitboxHeight = entity.getImage().getHeight();

        // Ajustar las coordenadas para centrar la hitbox sobre el punto de colocación
        float adjustedX = x - hitboxWidth / 2;
        float adjustedY = y - hitboxHeight / 2;

        // Colocar el personaje centrado en el punto
        entity.getImage().setPosition(adjustedX, adjustedY);
        stage.addActor(entity.getImage());
        characters.add(entity);
    }

    // Obtener las hitboxes de colocación para poder dibujarlas
    public List<Rectangle> getPlacementHitboxes() {
        return placementHitboxes;
    }

    public void startEnemySpawner(float spawnInterval) {
        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                spawnRandomEnemy();
            }
        }, 0, spawnInterval);  // Spawnear enemigos regularmente
    }

    private void spawnRandomEnemy() {
        Random r = new Random();

        // Elegir una fila aleatoria
        int randomRow = r.nextInt(ROWS);

        // Obtener la coordenada Y del centro de la hitbox en la fila seleccionada
        float spawnY = placementHitboxes.get(randomRow * COLS).y + placementHitboxes.get(randomRow * COLS).height / 2;

        // Configurar la coordenada X fuera de la pantalla
        float spawnX = viewport.getWorldWidth() + 50;  // X fuera de la pantalla, al borde derecho

        // Colocar el enemigo alineado en Y, pero fuera de la pantalla en X
        spawnEntity(EntityType.SKELETON, spawnX, spawnY);
    }


    public void update(float delta) {
        for (Character character : characters) {
            character.checkForAttack();
        }
    }

    public Array<Character> getCharacters() {
        return characters;
    }
}
