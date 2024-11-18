package com.milne.mw.difficulty;

import com.milne.mw.entities.EntityType;
import java.util.ArrayList;
import java.util.List;

public class RoundsManager {
    private ArrayList<Round> rounds;

    public RoundsManager() {
        this.rounds = new ArrayList<>();
        configureRounds();
    }

    private void configureRounds() {
        Round round1 = new Round(EntityType.SKELETON, EntityType.SKELETON, EntityType.SKELETON, EntityType.SKELETON);
        Round round2 = new Round(EntityType.SKELETON, EntityType.SKELETON, EntityType.STORMTROOPER, EntityType.SKELETON, EntityType.STORMTROOPER);
        Round round3 = new Round(EntityType.STORMTROOPER, EntityType.SKELETON, EntityType.SKELETON, EntityType.STORMTROOPER, EntityType.STORMTROOPER);
        Round round4 = new Round(EntityType.SOLDIER, EntityType.BARBARIAN, EntityType.BARBARIAN, EntityType.SOLDIER, EntityType.SKELETON);

        rounds.add(round1);
        rounds.add(round2);
        rounds.add(round3);
        rounds.add(round4);
    }

    public Round getRound(int index) {
        if (index < rounds.size()) {
            return rounds.get(index);
        }
        return null; // O lanza una excepción si deseas manejar fuera de rango
    }

    public int getTotalRounds() {
        return rounds.size();
    }
}
