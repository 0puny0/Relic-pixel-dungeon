package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Barrier;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Hex;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.effects.FloatingText;
import com.shatteredpixel.shatteredpixeldungeon.sprites.CharSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.EliteSuccubusSprite;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SuccubusSprite;
import com.watabou.noosa.audio.Sample;

public class EliteSuccubus extends Succubus{
    {
        spriteClass = EliteSuccubusSprite.class;
        lootChance = 1f;
    }

    @Override
    protected void charmAttack(int damage) {
        heal(5+damage,true);
        Buff.prolong(enemy, Weakness.class,5);
        Buff.prolong(enemy, Hex.class,5);
        if (Dungeon.level.heroFOV[pos]) {
            Sample.INSTANCE.play( Assets.Sounds.CHARMS );
        }
    }
}
