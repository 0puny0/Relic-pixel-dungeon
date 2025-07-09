package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WarGolemSprite;
import com.watabou.utils.Random;

public class WarGolem extends Golem {
    {
        spriteClass = WarGolemSprite.class;
        HP = HT = 150;
        lootChance = 1f;
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 30, 40 );
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(8, 14);
    }

    @Override
    protected void showParticles(boolean value) {
        ((WarGolemSprite)sprite).teleParticles(value);
    }
}
