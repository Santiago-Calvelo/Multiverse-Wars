package com.milne.mw.entities.boss;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.RunnableAction;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.renders.BossAnimator;

public class ForceSmashAttack implements BossAttack {
    private final Texture texture; // Textura específica para el ataque
    private final float duration = 3f; // Duración del ataque

    public ForceSmashAttack(Texture texture) {
        this.texture = texture;
    }

    @Override
    public void execute(BossCharacter boss, EntityManager entityManager, BossAnimator animator) {
        System.out.println("Force Smash activado!");

        RunnableAction applyDamage = new RunnableAction();
        applyDamage.setRunnable(() -> {
            Circle forceSmashRange = boss.getRange();
            animator.playForceSmashAnimation(texture, boss, forceSmashRange, duration);

            entityManager.getCharacters().forEach(enemy -> {
                float characterX = enemy.getImage().getX() + enemy.getImage().getWidth() / 2;
                float characterY = enemy.getImage().getY() + enemy.getImage().getHeight() / 2;

                if (enemy.getType().equalsIgnoreCase("tower") && forceSmashRange.contains(characterX, characterY)) {
                    enemy.takeDamage(30);
                }
            });
        });

        boss.getImage().addAction(Actions.sequence(
            Actions.repeat((int) (duration / 0.5f), Actions.sequence(
                applyDamage,
                Actions.delay(0.5f) // Intervalo entre daños
            )),
            Actions.run(boss::finishSpecial)
        ));
    }

    @Override
    public float getDuration() {
        return duration;
    }
}
