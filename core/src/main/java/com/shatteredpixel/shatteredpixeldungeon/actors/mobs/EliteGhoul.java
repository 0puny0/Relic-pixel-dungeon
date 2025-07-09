package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.sprites.EliteGhoulSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.GhoulSprite;
import com.watabou.utils.Random;

public class EliteGhoul extends Ghoul{
    {
        spriteClass = EliteGhoulSprite.class;
    }

    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 17, 25 );
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(1, 5);
    }
    @Override
    public int rebirthHP(){
        return Math.round(HT/2f);
    }

    @Override
    protected void crumple() {
        ((EliteGhoulSprite)sprite).crumple();
    }
}
