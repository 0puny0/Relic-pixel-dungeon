package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.AscensionChallenge;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corrosion;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CorrosionSpinnerSprite;
import com.watabou.utils.Random;

public class CorrosionSpinner extends Spinner{
    {
        spriteClass = CorrosionSpinnerSprite.class;
    }

    @Override
    protected void buffAffect() {
        int duration = 7;
        //我们在这里只使用了一半的提升修正值，因为总伤害不是线性增长的
        duration = Math.round(duration * (AscensionChallenge.statModifier(this)/2f + 0.5f));
        Buff.affect(enemy, Corrosion.class).set(2f,duration);

    }
}
