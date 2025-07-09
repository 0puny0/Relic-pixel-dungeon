package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HiddenWarlockSprite;
import com.watabou.noosa.audio.Sample;

public class HiddenWarlock extends Warlock{
    {
        spriteClass = HiddenWarlockSprite.class;
        lootChance = 1f;
        FLEEING = new Fleeing();
    }

    @Override
    protected void zapProc() {
        Buff.prolong( enemy, Degrade.class, Degrade.DURATION );
        Sample.INSTANCE.play( Assets.Sounds.DEGRADE );
        state=FLEEING;
        baseSpeed=2;
    }


    private class Fleeing extends Mob.Fleeing {

        @Override
        public boolean act(boolean enemyInFOV, boolean justAlerted) {
            if (buff( Terror.class ) == null && buff( Dread.class ) == null &&
                    enemyInFOV && enemy.buff( Degrade.class ) == null){
                state = HUNTING;
                baseSpeed=1;
                return true;
            }
            return super.act(enemyInFOV, justAlerted);
        }

    }
}
