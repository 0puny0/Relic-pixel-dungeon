package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class PoisonousSnakeSprite extends MobSprite{
    public PoisonousSnakeSprite() {
        super();

        texture( Assets.Sprites.SNAKE );

        TextureFilm frames = new TextureFilm( texture, 12, 11 );

        //many frames here as we want the rising/falling to be slow but the tongue to be fast
        idle = new Animation( 10, true );
        idle.frames( frames, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21, 0+21,
                1+21, 1+21, 1+21, 1+21, 1+21, 1+21, 1+21, 1+21, 1+21, 1+21, 2+21, 3+21, 2+21, 1+21, 1+21);

        run = new Animation( 8, true );
        run.frames( frames, 4+21, 5+21, 6+21, 7+21 );

        attack = new Animation( 15, false );
        attack.frames( frames, 8+21, 9+21, 10+21, 9+21, 0+21);

        die = new Animation( 10, false );
        die.frames( frames, 11+21, 12+21, 13+21 );

        play(idle);
    }
}
