package com.example.twoknight.strategy;

import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;

public class StandardHeroBrain implements HeroBrain {

    private final int maxShield;

    public StandardHeroBrain(int shield) {
        maxShield = shield;
    }

    @Override
    public int getMaxShield() {
        return maxShield;
    }

    @Override
    public boolean canMerge(MutableHero standardHero, Tile other) {
        boolean exceedArmor = true;
        boolean hasShield = standardHero.getShield() != 0;
        boolean hitsShield = standardHero.getShield() == other.getValue();
        boolean canMerge = exceedArmor && (!hasShield || hitsShield);

        return canMerge;
    }

    @Override
    public int damage(MutableHero standardHero, Tile other) {
        if (standardHero.getShield() != 0) {
            standardHero.setShield(0);
            standardHero.setVulnerable(true);
            return 0;
        } else if (standardHero.isVulnerable()) {
            standardHero.setVulnerable(false);
            return other.getValue() * 2;
        }
        if (other.getValue() <= standardHero.getArmor()) {
            //return 0;
        } else if (other.getValue() > standardHero.getArmor()) {
            return other.getValue() - standardHero.getArmor();
        }
        return other.getValue();
    }
}
