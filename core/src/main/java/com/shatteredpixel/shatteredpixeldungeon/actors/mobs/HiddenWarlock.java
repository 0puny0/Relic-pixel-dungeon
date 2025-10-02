package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Degrade;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Dread;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Terror;
import com.shatteredpixel.shatteredpixeldungeon.items.Generator;
import com.shatteredpixel.shatteredpixeldungeon.items.Item;
import com.shatteredpixel.shatteredpixeldungeon.items.scrolls.exotic.ExoticScroll;
import com.shatteredpixel.shatteredpixeldungeon.sprites.HiddenWarlockSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class HiddenWarlock extends Warlock{
    {
        spriteClass = HiddenWarlockSprite.class;
        lootChance = 0f;
        FLEEING = new Fleeing();
    }

    @Override
    protected void zapProc() {
        Buff.prolong( enemy, Degrade.class, Degrade.DURATION );
        Sample.INSTANCE.play( Assets.Sounds.DEGRADE );
        state=FLEEING;
        baseSpeed=2;
    }

    @Override
    public void rollToDropLoot() {
        super.rollToDropLoot();

        if (Dungeon.hero.lvl > maxLvl + 2) return;
        //测试
        //drops 2 or 3 卷轴
        ArrayList<Item> items = new ArrayList<>();
        items.add(Generator.random(Generator.Category.SCROLL));
        items.add(Generator.random(Generator.Category.SCROLL));
        if (Random.Int(2) == 0) items.add(Generator.random(Generator.Category.SCROLL));

        for (Item item : items){
            int ofs;
            do {
                ofs = PathFinder.NEIGHBOURS9[Random.Int(9)];
            } while (Dungeon.level.solid[pos + ofs] && !Dungeon.level.passable[pos + ofs]);
            Dungeon.level.drop( item, pos + ofs ).sprite.drop( pos );
        }

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
