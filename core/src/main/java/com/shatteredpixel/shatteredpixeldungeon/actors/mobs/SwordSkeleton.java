package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.DelayDamage;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.Longsword;
import com.shatteredpixel.shatteredpixeldungeon.sprites.SwordSkeletonSprite;
import com.watabou.utils.Random;

public class SwordSkeleton extends Skeleton{
    {
        spriteClass = SwordSkeletonSprite.class;

        //see rollToDropLoot
        loot = Generator.Category.WEAPON;
        lootChance = 1f;
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange( 5, 30 );
    }

    @Override
    public int attackProc(Char enemy, int damage ) {
        super.attackProc(enemy,damage);
        Buff.affect(this, DelayDamage.class).setDamage(HT/2);
        return damage;
    }

    @Override
    public Item createLoot() {
        return new Longsword().random();
    }
}
