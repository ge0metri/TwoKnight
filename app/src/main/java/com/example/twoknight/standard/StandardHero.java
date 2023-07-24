package com.example.twoknight.standard;

import com.example.twoknight.framework.MutableHero;
import com.example.twoknight.framework.Tile;
import com.example.twoknight.strategy.HeroBrain;
import com.example.twoknight.strategy.StandardHeroBrain;

public class StandardHero implements MutableHero {
    private final String heroType;
    private final int maxHealth;
    private int health;
    private boolean merged;

    public HeroBrain heroBrain;
    private int armor;
    private int shield;
    private boolean vulnerable = false;

    public StandardHero(String heroType, int heroMaxHealth, int armor, int shield) {
        this.heroType = heroType;
        this.health = heroMaxHealth;
        this.armor = armor;
        this.shield = shield;
        heroBrain = new StandardHeroBrain(shield);
        this.maxHealth = heroMaxHealth;
    }

    @Override
    public int getHealth() {
        return health;
    }

    @Override
    public String getType() {
        return heroType;
    }

    @Override
    public int getArmor() {
        return armor;
    }

    @Override
    public int getShield() {
        return shield;
    }

    @Override
    public boolean isVulnerable() {
        return vulnerable;
    }

    @Override
    public void setShield(int Value) {
        shield = Value;
    }

    @Override
    public int getValue() {
        return maxHealth;
    }

    @Override
    public void setMerged(boolean m) {
        merged = m;
    }

    @Override
    public boolean canMergeWith(Tile other) {
        return !merged && other != null && !other.getMerged() && heroBrain.canMerge(this, other);
    }

    @Override
    public int mergeWith(Tile other) {
        if (canMergeWith(other)) {
            health -= heroBrain.damage(this, other);
            merged = true;
            return health;
        }
        return -1;
    }

    @Override
    public boolean getMerged() {
        return merged;
    }

    @Override
    public void setVulnerable(boolean vulnerable) {
        this.vulnerable = vulnerable;
    }
}
