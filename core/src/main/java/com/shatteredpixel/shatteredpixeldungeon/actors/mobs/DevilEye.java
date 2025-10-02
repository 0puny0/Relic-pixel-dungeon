package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;


import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Blindness;
import com.shatteredpixel.shatteredpixeldungeon.sprites.DevilEyeSprite;
import com.watabou.utils.Random;

public class DevilEye extends Eye {
    {
        spriteClass = DevilEyeSprite.class;
        immunities.add(Blindness.class);
    }


    @Override
    public int attackSkill( Char target ) {
        return Char.INFINITE_ACCURACY;
    }
    protected void beamCharge() {
        ((DevilEyeSprite)sprite).charge( enemy.pos );
        spend( attackDelay()*2f );
        beamCharged = true;
    }

    @Override
    protected int deathGazeDamge() {
        int dmg = Random.NormalIntRange( 30, 80 );
        dmg = Math.round(dmg * AscensionChallenge.statModifier(this));
        return dmg;
    }
}
