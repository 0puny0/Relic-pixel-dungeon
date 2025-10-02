package com.shatteredpixel.shatteredpixeldungeon.actors.buffs;


public class DelayDamage extends Buff{
    int damage=0;
    public void setDamage(int damage){
        this.damage=damage;
    }
    public boolean act() {
        if (target.isAlive()) {
            target.damage( damage, this );
            detach();
        } else {

            detach();

        }

        return true;
    }
}
