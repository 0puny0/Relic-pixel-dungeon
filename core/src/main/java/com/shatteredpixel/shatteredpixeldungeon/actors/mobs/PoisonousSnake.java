package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Poison;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.potions.PotionOfToxicGas;
import com.shatteredpixel.shatteredpixeldungeon.sprites.PoisonousSnakeSprite;
import com.watabou.utils.Random;

public class PoisonousSnake extends Snake{
    {
        spriteClass = PoisonousSnakeSprite.class;
        baseSpeed = 2f;
        loot = PotionOfToxicGas.class;
        lootChance = 1f;
        //see rollToDropLoot

    }
    @Override
    public int attackProc(Char enemy, int damage ) {
       super.attackProc(enemy,damage);
        if (damage > 0 && Random.Int( 2 ) == 0) {
            Buff.affect( enemy, Poison.class ).set( 3 );
        }
        return damage;
    }
}
