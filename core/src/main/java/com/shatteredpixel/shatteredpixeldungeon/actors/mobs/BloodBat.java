package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Bleeding;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Clotting;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Healing;
import com.shatteredpixel.shatteredpixeldungeon.sprites.BloodBatSprite;
import com.watabou.utils.Random;

public class BloodBat extends Bat{
    {
        spriteClass = BloodBatSprite.class;
        lootChance = 1f;
    }

    @Override
    public int attackSkill( Char target ) {
        return 20;
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 8, 18 );
    }

    @Override
    public int attackProc(Char enemy, int damage ) {
       super.attackProc(enemy,damage);
        if (Random.Int( 2 ) == 0) {
           Buff.prolong(enemy,Clotting.class,Clotting.DURATION);
        }
        return damage;
    }
}
