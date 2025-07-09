package com.shatteredpixel.shatteredpixeldungeon.sprites;

import com.shatteredpixel.shatteredpixeldungeon.Assets;
import com.shatteredpixel.shatteredpixeldungeon.effects.Speck;
import com.shatteredpixel.shatteredpixeldungeon.effects.particles.ShadowParticle;
import com.watabou.noosa.TextureFilm;
import com.watabou.utils.Random;

public class EliteSuccubusSprite extends MobSprite{
    public EliteSuccubusSprite() {
        super();

        texture( Assets.Sprites.SUCCUBUS );

        TextureFilm frames = new TextureFilm( texture, 12, 15 );
        int num=RandomNum();
        idle = new Animation( 8, true );
        idle.frames( frames, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 0+num, 1+num, 2+num, 2+num, 2+num, 2+num, 1+num );

        run = new Animation( 15, true );
        run.frames( frames, 3+num, 4+num, 5+num, 6+num, 7+num, 8+num );

        attack = new Animation( 12, false );
        attack.frames( frames, 9+num, 10+num, 11+num );

        die = new Animation( 10, false );
        die.frames( frames, 12 +num);

        play( idle );
    }

    @Override
    public void die() {
        super.die();
        emitter().burst( Speck.factory( Speck.HEART ), 6 );
        emitter().burst( ShadowParticle.UP, 8 );
    }
    //返回一个数字，用于随机皮肤
    private int RandomNum(){
        return (1+Random.Int(5))*21;
    }
}
