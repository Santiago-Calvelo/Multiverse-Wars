package com.milne.mw.entities.boss;

import com.milne.mw.entities.Character;
import com.milne.mw.entities.EntityManager;
import com.milne.mw.renders.BossAnimator;

public class DashAttack implements BossAttack{

    @Override
    public void execute(BossCharacter boss, EntityManager entityManager, BossAnimator animator, int damage) {
        for (int i = 0; i < entityManager.getCharacters().size; i++) {
            Character character = entityManager.getCharacters().get(i);
            if (boss.getHitbox().contains(character.getHitbox())) {
                character.takeDamage(5);
            }
        }
    }

    private void incrementSpeed(BossCharacter boss) {
        int tempSpeed = boss.getSpeed();
        boss.setSpeed(tempSpeed*15);
        boss.startMovement();
    }

    @Override
    public float getDuration() {
        return 0;
    }
}
