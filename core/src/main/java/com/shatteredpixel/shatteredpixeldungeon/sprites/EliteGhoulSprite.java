package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.watabou.noosa.TextureFilm;

public class EliteGhoulSprite extends MobSprite{
    private Animation crumple;

    public EliteGhoulSprite() {
        super();

        texture( Assets.Sprites.GHOUL );

        TextureFilm frames = new TextureFilm( texture, 12, 14 );

        idle = new Animation( 2, true );
        idle.frames( frames, 0+21, 0+21, 0+21, 1+21 );

        run = new Animation( 12, true );
        run.frames( frames, 2+21, 3+21, 4+21, 5+21, 6+21, 7+21 );

        attack = new Animation( 12, false );
        attack.frames( frames, 0+21, 8+21, 9+21 );

        crumple = new Animation( 15, false);
        crumple.frames( frames, 0+21, 10+21, 11+21, 12+21 );

        die = new Animation( 15, false );
        die.frames( frames, 0+21, 10+21, 11+21, 12+21, 13+21 );

        play( idle );
    }

    public void crumple(){
        hideEmo();
        processStateRemoval(State.PARALYSED);
        play(crumple);
    }

    @Override
    public void die() {
        if (curAnim == crumple){
            //causes the sprite to not rise then fall again when dieing.
            die.frames[0] = die.frames[1] = die.frames[2] = die.frames[3];
        }
        super.die();
    }
}
