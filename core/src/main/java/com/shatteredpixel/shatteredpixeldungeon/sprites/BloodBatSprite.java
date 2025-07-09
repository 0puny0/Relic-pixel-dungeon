package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class BloodBatSprite extends MobSprite{
    public BloodBatSprite() {
        super();

        texture( Assets.Sprites.BAT );

        TextureFilm frames = new TextureFilm( texture, 15, 15 );

        idle = new Animation( 8, true );
        idle.frames( frames, 0+17, 1+17 );

        run = new Animation( 12, true );
        run.frames( frames, 0+17, 1+17 );

        attack = new Animation( 12, false );
        attack.frames( frames, 2+17, 3+17, 0+17, 1+17 );

        die = new Animation( 12, false );
        die.frames( frames, 4+17, 5+17, 6+17 );

        play( idle );
    }
}
