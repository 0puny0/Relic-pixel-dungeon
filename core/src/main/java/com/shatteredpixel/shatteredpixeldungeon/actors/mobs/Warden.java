package com.shatteredpixel.shatteredpixeldungeon.actors.mobs;

import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Cripple;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Weakness;
import com.shatteredpixel.shatteredpixeldungeon.scenes.GameScene;
import com.shatteredpixel.shatteredpixeldungeon.sprites.WardenSprite;
import com.watabou.utils.Random;

public class Warden extends Guard{
    {
        spriteClass = WardenSprite.class;

        HP = HT = 40;
        lootChance = 1f; //by default, see lootChance()
    }
    @Override
    public int damageRoll() {
        return Random.NormalIntRange(8, 12);
    }

    @Override
    public int drRoll() {
        return super.drRoll() + Random.NormalIntRange(3, 7);
    }
    @Override
    protected void  pullEnemy(Char enemy, int pullPos) {
        enemy.pos = pullPos;
        enemy.sprite.place(pullPos);
        Dungeon.level.occupyCell(enemy);
        Cripple.prolong(enemy, Cripple.class, 4f);
        Cripple.prolong(enemy, Weakness.class, Weakness.DURATION);
        if (enemy == Dungeon.hero) {
            Dungeon.hero.interrupt();
            Dungeon.observe();
            GameScene.updateFog();
        }
    }
}
