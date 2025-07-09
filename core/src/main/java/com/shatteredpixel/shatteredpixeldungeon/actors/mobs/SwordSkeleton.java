package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.ChampionEnemy;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.weapon.melee.BattleAxe;
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
        return Random.NormalIntRange( 3, 20 );
    }
    @Override
    public int attackProc(Char enemy, int damage ) {
        super.attackProc(enemy,damage);
        damage(HT/2,this);
        return damage;
    }

    @Override
    public Item createLoot() {
        return new BattleAxe().random();
    }
}
