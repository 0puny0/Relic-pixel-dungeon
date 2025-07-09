package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.Dungeon;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.watabou.noosa.TextureFilm;

public class SwordSkeletonSprite extends MobSprite {

    public SwordSkeletonSprite() {
        super();

        texture( Assets.Sprites.SKELETON );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );

        idle = new Animation( 12, true );
        idle.frames( frames, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21, 21,21, 22, 23, 24 );

        run = new Animation( 15, true );
        run.frames( frames, 25, 26, 27, 7+21, 8+21, 9+21 );

        attack = new Animation( 15, false );
        attack.frames( frames, 14+21, 15+21, 16+21 );

        die = new Animation( 12, false );
        die.frames( frames, 10+21, 11+21, 12+21, 13+21 );

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        if (Dungeon.level.heroFOV[ch.pos]) {
            emitter().burst( Speck.factory( Speck.BONE ), 6 );
        }
    }

    @Override
    public int blood() {
        return 0xFFcccccc;
    }
}
