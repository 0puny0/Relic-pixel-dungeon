package com.shatteredpixel.shatteredpixeldungeon.items;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.actors.Actor;
import com.shatteredpixel.shatteredpixeldungeon.actors.Char;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Buff;
import com.shatteredpixel.shatteredpixeldungeon.actors.buffs.Corruption;
import com.shatteredpixel.shatteredpixeldungeon.actors.mobs.Wraith;
import com.shatteredpixel.shatteredpixeldungeon.effects.Splash;
import com.shatteredpixel.shatteredpixeldungeon.levels.Terrain;
import com.shatteredpixel.shatteredpixeldungeon.sprites.ItemSpriteSheet;
import com.watabou.noosa.audio.Sample;

public class Soul extends Item{
    {
        image = ItemSpriteSheet.SOUL;
        defaultAction = AC_THROW;
        bones = false;
    }

    @Override
    public boolean isUpgradable() {
        return false;
    }
    @Override
    public boolean isIdentified() {
        return true;
    }
    @Override
    protected void onThrow( int cell ) {
        if (Dungeon.level.map[cell] == Terrain.WELL || Dungeon.level.pit[cell]) {
            super.onThrow( cell );
        } else  {
            Sample.INSTANCE.play( Assets.Sounds.SHATTER );
            if (Dungeon.level.heroFOV[cell]) {
                Char ch = Actor.findChar(cell);
                if (ch != null) {
                    Splash.at(ch.sprite.center(), 0X38ABAB, 3);
                } else {
                    Splash.at(cell, 0X38ABAB, 3);
                }
            }
            Wraith w = Wraith.spawnAt(cell);
            Buff.affect(w, Corruption.class);

        }
    }
}
