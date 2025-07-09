package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.MovieClip;
import com.watabou.noosa.TextureFilm;

public class WardenSprite extends MobSprite{

    public WardenSprite() {
        super();

        texture( Assets.Sprites.GUARD );

        TextureFilm frames = new TextureFilm( texture, 12, 16 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0+21, 0+21, 0+21, 1+21, 0+21, 0+21, 1+21, 1+21 );

        run = new MovieClip.Animation( 15, true );
        run.frames( frames, 2+21, 3+21, 4+21, 5+21, 6+21, 7+21 );

        attack = new MovieClip.Animation( 12, false );
        attack.frames( frames, 8+21, 9+21, 10+21 );

        die = new MovieClip.Animation( 8, false );
        die.frames( frames, 11+21, 12+21, 13+21, 14+21 );

        play( idle );
    }

    @Override
    public void play( Animation anim ) {
        if (anim == die) {
            emitter().burst( ShadowParticle.UP, 4 );
        }
        super.play( anim );
    }
}
